import java.util.Arrays;

// ----------------------------------------------------------
/**
 * The MemPool (short for memory pool) has two main components. The first is the
 * actual memory pool represented by a byte array which stores Strings for
 * artists/songs. Byte arrays (which represent Strings to be inserted) are
 * preceded by two bytes that represent the length of the inserted byte array.
 * The pool is just a collection of these byte arrays and free blocks. The
 * second is an ordered doubly-linked list which keeps track of the free blocks
 * of memory within the pool. The list contains Blocks, which hold two integers
 * that represent the position and length of each free block. The list is
 * ordered by position of the free blocks in the memory pool for efficient
 * access.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 27, 2014
 */
public class MemPool
{
    private byte[]                  pool;
    private int                     initCapacity;
    private DoublyLinkedList<Block> fBList;
    private DoublyLinkedList<Block> trash;
    private static final int        LEN = 2;


    // ----------------------------------------------------------
    /**
     * Initializes the pool (byte array), the free-block list as a
     * DoublyLinkedList<Block> with an initial Block with an initial position of
     * 0 and length size, and garbage Block storage called trash as an empty
     * DoublyLinkedList<Block>.
     *
     * @param size
     *            initial pool (byte array) size
     */
    public MemPool(int size)
    {
        pool = new byte[initCapacity = size];

        fBList = new DoublyLinkedList<Block>();
        fBList.add(new Block(0, size));

        trash = new DoublyLinkedList<Block>();
    }


    // ----------------------------------------------------------
    /**
     * Checks the free-block list to find the best position to insert space into
     * the pool. Expands the pool if there is not enough memory to insert. After
     * inserting the byte array into the pool (using a helper method), the
     * free-block list is updated accordingly.
     *
     * @param space
     *            byte array to insert
     * @return the position of the byte array in the pool
     */
    public int insert(byte[] space)
    {
        int pos = -1;
        int diff = Integer.MAX_VALUE;
        fBList.moveToFront();
        for (int i = 0; i < fBList.size(); i++)
        {
            // if a free block matches the exact size of the byte array (+ LEN),
            // it is inserted at that spot with the helped method
            if (fBList.get().getLen() == space.length + LEN)
                return add(space);

            // if a free block is bigger than the size of the byte array
            // (+ LEN), then the absolute difference between the two and the
            // position of the free block is saved for later reference when
            // comparing for the tightest fit
            else if ((fBList.get().getLen() > space.length + LEN)
                && (fBList.get().getLen() - (space.length + LEN)) < diff)
            {
                pos = i;
                diff = fBList.get().getLen() - (space.length + LEN);
            }

            fBList.next();
        }

        // if there is no free block in the pool that space can be inserted
        // into, then the size of the pool is incremented by its initial size,
        // and the method is called recursively until there is enough memory to
        // accommodate the insertion of the byte array; the free-block list is
        // updated accordingly
        if (pos == -1)
        {
            fBList.moveToRear();

            // if there is a free block at the end of the pool, then it is
            // merged with newly added free block (of size initCapacity), or
            // in actuality, the last free block in the list is updated to
            // reflect the change
            if (fBList.size() > 0
                && fBList.get().getPos() + fBList.get().getLen() == pool.length)
                fBList.get().set(
                    fBList.get().getPos(),
                    fBList.get().getLen() + initCapacity);

            // if there isn't a free block at the end of pool, then one is added
            // to the free-block list to reflect the expansion of the memory
            // pool
            else
            {
                fBList.append((trash.size() != 0)
                    ? trash.remove().get().set(pool.length, initCapacity)
                    : new Block(pool.length,initCapacity));
                fBList.next();
            }

            pool = Arrays.copyOf(pool, pool.length + initCapacity);
            return insert(space);
        }

        // if there is a free block that can accommodate the insertion of the
        // byte array (tightest, but not perfect, fit), then the list is
        // traversed to reach that Block for the helper method to update the
        // list
        else
        {
            fBList.moveToFront();
            for (int j = 0; j < pos; j++)
                fBList.next();
        }
        return add(space);
    }


    // ----------------------------------------------------------
    /**
     * "Removes" the byte array that corresponds to the position specified by
     * Handle h from the pool by adding a new free block to fBList with the same
     * position and length.
     *
     * @param h
     *            Handle that corresponds to the byte array to be removed
     */
    public void remove(Handle h)
    {
        fBList.moveToFront();

        int pos;

        // the ordered free-block list is traversed until the proper position to
        // place the new free block is found
        for (pos = 0; pos < fBList.size() &&
            fBList.get().getPos() < h.get(); pos++)
            fBList.next();

        int len = ((pool[h.get()] << 8) + pool[h.get() + 1]) + LEN;

        // if trash is not empty, then a garbage Node is removed, set to the
        // position of the new free block (with respective length) and added to
        // the free block list
        if (pos != fBList.size())
            fBList.add((trash.size() != 0)
                ? trash.remove().get().set(h.get(), len)
                : new Block(h.get(), len));
        else
            fBList.append((trash.size() != 0)
                ? trash.remove().get().set(h.get(), len)
                : new Block(h.get(), len));

        // if the new free block is not at the beginning of the list, then it
        // can be checked for possible merging with the preceding free block
        if (!fBList.isFirst())
        {
            fBList.previous();
            merge();
        }

        // if the new free block is not at the end of the list, then it
        // can be checked for possible merging with the following free block
        if (!fBList.isLast())
            merge();
    }


    // ----------------------------------------------------------
    /**
     * Gets the byte array from the pool which corresponds to Handle h.
     *
     * @param h
     *            Handle which corresponds to the byte array
     * @return byte array in the pool that corresponds to Handle h
     */
    public byte[] getBytes(Handle h)
    {
        return Arrays.copyOfRange(pool, h.get() + LEN,
            (h.get() + LEN) + ((pool[h.get()] << 8) + pool[h.get() + 1]));
    }


    // ----------------------------------------------------------
    /**
     * @return String representation of the free-block list
     */
    public String dump()
    {
        return fBList.toString();
    }


    // ----------------------------------------------------------
    /**
     * @return the capacity (overall length) of the pool
     */
    public int capacity()
    {
        return pool.length;
    }


    private int add(byte[] space)
    {
        int pos = fBList.get().getPos();

        // the length (guaranteed to be no bigger than 16 bits) is split into
        // two bytes and inserted into the pool
        pool[pos] = (byte) (space.length >> 8);
        pool[pos + 1] = (byte) space.length;

        // the actual insertion of the byte array into the pool
        for (int j = 0; j < space.length; j++)
            pool[(pos + LEN) + j] = space[j];

        // if the size of the inserted byte array (+ LEN) is not a perfect fit
        // for the free block, then the block length is updated to reflect the
        // change
        if (fBList.get().getLen() != space.length + LEN)
            fBList.get().set(
                fBList.get().getPos() + (space.length + LEN),
                fBList.get().getLen() - (space.length + LEN));

        // the free block is removed if the size of the free block and the
        // size of the byte array (+ LEN) are the same
        else
            trash.add(fBList.remove().get());

        return pos;
    }


    private void merge()
    {
        int end = fBList.get().getPos() + fBList.get().getLen();

        fBList.next();

        // if two free blocks are adjacent (hence, the (position + length) of
        // the preceding Block is equal to the position of the one following),
        // then one is removed and the other is updated to reflect the merge
        if (end == fBList.get().getPos())
        {
            int remSize = fBList.remove().get().getLen();
            fBList.get().set(
                fBList.get().getPos(),
                fBList.get().getLen() + remSize);
        }
    }
}

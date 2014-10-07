// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Oct 4, 2014
 */
public class Two3PlusTree
{
    private TreeNode                   root;
    private int                        size;
    private DoublyLinkedList<KVPair>   prTrash;
    private DoublyLinkedList<Internal> inTrash;
    private DoublyLinkedList<Leaf>     lfTrash;


    // ----------------------------------------------------------
    /**
     * Initializes the Two3PlusTree (2-3+ tree) with a size = 0, and initializes
     * trash DoublyLinkedLists for KVPairs, Internal Nodes, and Leaf Nodes.
     */
    public Two3PlusTree()
    {
        size = 0;
        prTrash = new DoublyLinkedList<KVPair>();
        inTrash = new DoublyLinkedList<Internal>();
        lfTrash = new DoublyLinkedList<Leaf>();
    }


    // ----------------------------------------------------------
    /**
     * Calls the helper method recInsert with this.root as the starting point,
     * to insert a KVPair with corresponding key and value.
     * @param key key for KVPair to be inserted
     * @param value value for KVPair to be inserted
     * @return if a KVPair corresponding to key and value was inserted
     * successfully
     */
    public boolean insert(Handle key, Handle value)
    {
        return recInsert(this.root, key, value);
    }


    // ----------------------------------------------------------
    /**
     * @return String representation of the tree using a helper traversal method
     */
    public String print()
    {
        return "Printing 2-3 tree:" + traverse(this.root, 0);
    }


    // ----------------------------------------------------------
    /**
     * @return the size of the Two3PlusTree (number of TreeNodes in the tree)
     */
    public int size()
    {
        return size;
    }


    @SuppressWarnings("hiding")
    private boolean recInsert(TreeNode root, Handle key, Handle value)
    {
        // creates a KVPair corresponding to key and value
        KVPair pair =
            (prTrash.size() != 0) ? prTrash.get().set(key, value)
                : new KVPair(key, value);

        if (size == 0)
            this.root = ((lfTrash.size() != 0)
                ? lfTrash.get().compSet(pair, null) : new Leaf(pair));

        // if root contains a KVPair equal to pair, that means there is a
        // duplicate, and the method returns false
        else if (root.containsEqual(pair))
            return false;
        else
        {
            // if root is an Internal, then the method will search for the
            // proper location to insert pair
            if (root instanceof Internal)
            {
                if (pair.compareTo(((Internal)root).left()) == -1)
                    recInsert(((Internal)root).low(), key, value);
                else if (pair.compareTo(((Internal)root).left()) > -1)
                {
                    if (((Internal)root).right() == null
                        || pair.compareTo(((Internal)root).right()) == -1)
                        recInsert(((Internal)root).mid(), key, value);
                    else
                        recInsert(((Internal)root).high(), key, value);
                }
            }
            else if (root instanceof Leaf)
            {
                // if the Leaf where the method wants to insert pair is not
                // full, then it is inserted and sorted
                if (root.right() == null)
                    root.compSet(root.left(), pair);
                else
                {
                    // if the Leaf where the method wants to insert is full,
                    // then it must create a new split Leaf (and update
                    // pointers accordingly) the split Leaf will contain two
                    // KVPairs, and root will contain one; the original KVPair
                    // is less than the ones in the new split Leaf (which
                    // contains the other two KVPairs, which are ordered)
                    // the split Leaf will acquire root.right() by default, and
                    // it must also acquire the maximum of pair and root.left(),
                    // and the minimum will be in root
                    KVPair move = (root.left().compareTo(pair) == 1)
                        ? root.left() : pair;

                    Leaf split = ((lfTrash.size() != 0)
                        ? (Leaf)lfTrash.get().compSet(move, root.right())
                            : new Leaf(move, root.right()));

                    // root.left() is set to the minimum of root.left() and pair
                    root.compSet(((root.left().compareTo(pair) == 1)
                        ? pair : root.left()), null);

                    // Leaf pointers are updated
                    split.setNext(((Leaf)root).next());
                    ((Leaf)root).setNext(split);

                    // splitting Leaves requires new parents, so the proper
                    // values are promoted using a helper method
                    promote(this.root, root, split, split.left());
                }
            }
        }
        size++;
        return true;
    }


    @SuppressWarnings("hiding")
    private void promote(TreeNode root, TreeNode orig, TreeNode split,
        KVPair promo)
    {
        Internal inRoot;
        if (root == orig)
            this.root = ((inTrash.size() != 0)
                ? inTrash.get().set(promo, null, orig, split)
                    : new Internal(promo, orig, split));
        else if (promo.compareTo((inRoot = ((Internal)root)).left()) == -1)
        {
            if (inRoot.low() == orig)
            {
                if (inRoot.right() == null)
                {
                    inRoot.setHigh(inRoot.mid());
                    inRoot.setMid(split);
                    inRoot.compSet(inRoot.left(), promo);
                }
                else
                {
                    Internal parent = ((inTrash.size() != 0)
                        ? inTrash.get().set(inRoot.high().left(), null,
                            inRoot.mid(), inRoot.high())
                                : new Internal(inRoot.high().left(),
                                    inRoot.mid(), inRoot.high()));

                    KVPair newPromo = inRoot.left();
                    inRoot.setMid(split);
                    inRoot.setLeft(promo);

                    inRoot.setRight(null);
                    inRoot.setHigh(null);

                    promote(this.root, inRoot, parent, newPromo);
                }
            }
            else
                promote(inRoot.low(), orig, split, promo);
        }
        else if (promo.compareTo(inRoot.left()) >= 0 &&
            (inRoot.right() == null || promo.compareTo(inRoot.right()) == -1))
        {
            if (inRoot.mid() == orig)
            {
                if (inRoot.right() == null)
                {
                    inRoot.setHigh(split);
                    inRoot.compSet(inRoot.left(), promo);
                }
                else
                {
                    Internal parent = ((inTrash.size() != 0)
                        ? inTrash.get().set(inRoot.high().left(), null,
                            split, inRoot.high())
                               : new Internal(inRoot.high().left(),
                                   split, inRoot.high()));

                    KVPair newPromo = split.left();
                    inRoot.setRight(null);
                    inRoot.setHigh(null);

                    promote(this.root, inRoot, parent, newPromo);
                }
            }
            else
                promote(inRoot.mid(), orig, split, promo);
        }
        else if (promo.compareTo(inRoot.right()) >= 0)
        {
            if (inRoot.high() == orig)
            {
                Internal parent = ((inTrash.size() != 0)
                    ? inTrash.get().set(promo, null, inRoot.high(), split)
                        : new Internal(promo, inRoot.high(), split));

                KVPair newPromo = inRoot.right();
                inRoot.setRight(null);
                inRoot.setHigh(null);

                promote(this.root, inRoot, parent, newPromo);
            }
            else
                promote(inRoot.high(), orig, split, promo);
        }
    }


    @SuppressWarnings("hiding")
    private String traverse(TreeNode root, int depth)
    {
        String traversal = "";
        if (root == null)
            return "";
        else
        {
            traversal += "\n";

            for (int pos = 0; pos < depth; pos++)
                traversal += "  ";

            traversal += root.toString();

            if (root instanceof Internal)
            {
                traversal += traverse(((Internal)root).low(), depth + 1);
                traversal += traverse(((Internal)root).mid(), depth + 1);
                traversal += traverse(((Internal)root).high(), depth + 1);
            }
        }
        return traversal;
    }
}

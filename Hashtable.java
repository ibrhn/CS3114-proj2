import java.util.Arrays;

// ----------------------------------------------------------
/**
 * The Hashtable class contains half of the actual Hashtable data structure. It
 * contains a Handle array (where the Handles are references, or keys, that
 * indicate the location of the corresponding byte array in the memory pool).
 *
 * The Handle array, or map, uses a hashing function to find proper positions
 * to insert Handles into the map. If a String hashes to a location for a
 * corresponding Handle in the same spot as another Handle (assuming they are
 * different), then the map is quadratically probed to find another position.
 * This ensures each Handle has a unique position in the map, and that finding
 * and retrieving the position for hashed String Handle is much more efficient
 * than any linear search.
 * Removing Handles require the use of tombstones. Tombstones indicate that a
 * Handle has been removed from a position. This technique is used to avoid
 * accidental insertions of duplicate Strings.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 14, 2014
 */
public class Hashtable
{
    private Handle[]   map;
    private int        usage;
    private Controller ctrl;


    // ----------------------------------------------------------
    /**
     * Initializes the Hashtable using the initial hashSz input from the
     * controller.
     *
     * @param ctrl
     *            Controller that manages the Hashtable and MemPool
     */
    public Hashtable(Controller ctrl)
    {
        map = new Handle[(this.ctrl = ctrl).initHashSz()];
        usage = 0;
    }


    // ----------------------------------------------------------
    /**
     * Adds a corresponding Handle to the HashTable and inserts the String into
     * the memory manager through access to the Controller.
     *
     * @param key
     *            String to be added to the pool
     * @return if a Handle was inserted into the map (and if the corresponding
     *         String was inserted into the pool)
     * @throws Exception
     */
    public boolean put(String key)
        throws Exception
    {
        int pos;

        // returns false if there is no viable position to place a Handle
        // (corresponding to key) in the map (usually only if there is a
        // duplicate String already in the pool)
        if ((pos = findPos(key)) != -1)
        {
            // if the usage before the Handle is inserted into the map is
            // greater than half its capacity, then its capacity is doubled and
            // all its contents are rehashed before inserting the Handle
            if (++usage > (map.length / 2))
            {
                Handle[] copy = Arrays.copyOf(map, map.length);

                map = new Handle[map.length * 2];

                for (int i = 0; i < copy.length; i++)
                    if (ctrl.pool().getString(copy[i]) != null)
                        map[findPos(ctrl.pool().getString(copy[i]))] = copy[i];

                pos = findPos(key);
            }

            // if there is already a Handle in the map at pos, then its data
            // is adjusted to accommodate the Handle insertion; otherwise, a
            // new Handle is substantiated at pos
            if (map[pos] != null)
                map[pos].set(ctrl.pool().insert(key.getBytes()));
            else
                map[pos] = new Handle(ctrl.pool().insert(key.getBytes()));

            return true;
        }
        return false;
    }


    // ----------------------------------------------------------
    /**
     * Removes key from the memory pool and sets the corresponding Handle to a
     * tombstone (an indicator that a (non-tombstone) Handle had been at that
     * location, but was removed).
     *
     * The tombstone is necessary to prevent duplicate insertions. For example,
     * a String str hashes to position 0 and is inserted there. Then a String
     * str2 hashes to the same position, but is then quadratically probed to
     * position 1 and inserted there. Then the original str is removed, and
     * there is an attempt to insert str2 (although, it is already in the map).
     * It will hash to position 0 (which is no longer occupied), and this
     * duplicate will be inserted there.
     * To avoid this, we have an indicator at any position where a Handle was
     * removed (a tombstone). This indicator forces any insertion to check
     * every other possible location for duplicates before returning to this
     * first tombstone location. If the check shows there are no duplicates,
     * then a new Handle (corresponding to an actual String) can replace the
     * tombstone (by updating the tombstone with the new position).
     *
     * @param key
     *            String to be removed from the memory pool
     * @return if key exists and was removed
     * @throws Exception
     */
    public boolean remove(String key)
        throws Exception
    {
        int pos = getPos(key);
        boolean chk = (pos != -1);
        if (chk)
        {
            ctrl.pool().remove(map[pos]);
            map[pos].set(-1);
            usage--;
        }
        return chk;
    }


    // ----------------------------------------------------------
    /**
     * Prints a line-by-line list of each element in the map by position with
     * its corresponding String, like this:
     *
     * <pre>
     * |Hello World| 0
     * |Greetings Planet| 4
     * |Salutations Ecosystem| 7
     * </pre>
     * <p>
     * An empty list is simply blank.
     * </p>
     *
     * @return a String representation of the map
     * @throws Exception
     */
    public String print()
        throws Exception
    {
        String hndl;
        String str = "";
        for (int i = 0; i < map.length; i++)
            if ((hndl = ctrl.pool().getString(map[i])) != null
            && map[i] != null && map[i].get() != -1)
                str += "|" + hndl + "| " + i + "\n";
        return str;
    }


    // ----------------------------------------------------------
    /**
     * @return the capacity of the map
     */
    public int capacity()
    {
        return map.length;
    }


    // ----------------------------------------------------------
    /**
     * @return the usage of the map
     */
    public int usage()
    {
        return usage;
    }


    // ----------------------------------------------------------
    /**
     * @param key String to be searched
     * @return the Handle in the HashTable corresponding to key
     * @throws Exception
     */
    public Handle getHandle(String key) throws Exception
    {
        int pos = getPos(key);
        return (pos != -1) ? map[pos] : null;
    }


    private int findPos(String key)
        throws Exception
    {
        int start = (int)hash(key);
        int pos = start;
        int tmbstn = -1;

        // starting at the initial hash position, this loop checks for the best
        // position to place a Handle corresponding to key by quadratically
        // probing to the next position as to check each probed position where
        // pos != null
        for (int i = 0; map[(pos = (start + (i * i)) % map.length)] != null;
            i++)
        {
            // saves the location of the first encountered tombstone for
            // reference; if the loop breaks and tmbstn != -1, then the initial
            // tombstone location is the best position to place the Handle
            if (map[pos].get() == -1 && tmbstn == -1)
                tmbstn = pos;

            // returns -1 if there is a duplicate String already in the pool
            else if (map[pos].get() != -1
                && ctrl.pool().getString(map[pos]).equals(key))
                    return -1;
        }

        // if a tombstone was encountered before the null position, that means
        // there are no duplicates in the list, and that is the best position
        // to place the Handle; otherwise, pos is the best position
        return (tmbstn != -1) ? tmbstn : pos;
    }


    private int getPos(String key)
        throws Exception
    {
        int start = (int)hash(key);
        int pos = start;

        // quadratically probes after the initial hash position to locate a
        // String in the map (returns -1, if the loop gets back to the starting
        // position without finding the location of a Handle corresponding to
        // key)
        for (int i = 0; (pos = (start + (i * i)) % map.length) != start
            || i == 0; i++)
        {
            // if the function probes to an empty position (void of actual
            // Handles and tombstones), this means key is not in the pool and
            // there is no corresponding Handle in the map either
            if (map[pos] == null)
                return -1;

            // returns pos if a Handle corresponding to key is found
            else if (map[pos].get() != -1
                && ctrl.pool().getString(map[pos]).equals(key))
                    return pos;
        }
        return -1;
    }


    private long hash(String key)
    {
        String keyString = key;

        int intLength = keyString.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++)
        {
            char[] c = keyString.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++)
            {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = keyString.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++)
        {
            sum += c[k] * mult;
            mult *= 256;
        }

        return (Math.abs(sum) % map.length);
    }
}

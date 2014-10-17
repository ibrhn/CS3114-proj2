import java.util.ArrayList;

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
    private final static KVPair DUP =
                                        new KVPair(new Handle(-1), new Handle(
                                            -1));
    private Controller          ctrl;
    private TreeNode            root;


    // ----------------------------------------------------------
    /**
     * Initializes the Two3PlusTree (2-3+ tree) with a size = 0, and initializes
     * trash DoublyLinkedLists for KVPairs, Internal Nodes, and Leaf Nodes.
     *
     * @param ctrl
     *            Controller that manages the Hashtable and MemPool
     */
    public Two3PlusTree(Controller ctrl)
    {
        this.ctrl = ctrl;
    }


    // ----------------------------------------------------------
    /**
     * Calls the helper method recInsert with root as the starting point, to
     * insert a KVPair with the corresponding key and value
     *
     * @param key
     *            key for KVPair to be inserted
     * @param value
     *            value for KVPair to be inserted
     * @return if a KVPair corresponding to key and value was inserted
     *         successfully (if there are no duplicates)
     */
    public boolean insert(Handle key, Handle value)
    {
        return recInsert(root, new KVPair(key, value));
    }


    // ----------------------------------------------------------
    /**
     * Calls the helper method recDelete with root as the starting point, to
     * delete a KVPair with the corresponding key and value
     *
     * @param key
     *            key of KVPair to be deleted
     * @param value
     *            value of KVPair to be deleted
     * @return if a KVPair corresponding to key and value was deleted
     *         successfully (if a corresponding KVPair is in the tree)
     */
    public boolean delete(Handle key, Handle value)
    {
        return (recDelete(root, new KVPair(key, value)) != DUP);
    }


    // ----------------------------------------------------------
    /**
     * Removes all KVPairs that have the specified key.
     *
     * @param key
     *            key to be searched
     * @return true if it was found false otherwise
     */
    public ArrayList<KVPair> remove(Handle key)
    {
        ArrayList<KVPair> list = new ArrayList<KVPair>();
        Leaf leaf;
        KVPair pair;
        leaf = find(root, key);
        if (key == null || leaf == null)
        {
            return list;
        }

        while (leaf != null)
        {
            leaf = find(root, key);
            if (leaf == null)
            {
                return list;
            }

            if (leaf.left().key().equals(key))
            {
                pair = leaf.left();
                list.add(pair);
                delete(pair.key(), pair.value());
                delete(pair.value(), pair.key());
            }

            leaf = find(root, key);
            if (leaf == null)
            {
                return list;
            }

            if (leaf.right() != null && leaf.right().key().equals(key))
            {
                pair = leaf.right();
                list.add(pair);
                delete(pair.key(), pair.value());
                delete(pair.value(), pair.key());
            }
        }
        return list;
    }


    // ----------------------------------------------------------
    /**
     * @param key
     *            the key of the KVPairs to append
     * @return a String representation of a list of all KVPairs with key
     * @throws Exception
     */
    public ArrayList<String> list(Handle key)
        throws Exception
    {
        ArrayList<String> list = new ArrayList<String>();
        Leaf leaf;
        KVPair pair;

        if (key == null)
        {
            return list;
        }
        leaf = search(key);

        if (key == null || leaf == null)
        {
            return list;
        }

        while (leaf != null
            && (leaf.left().key().equals(key) || (leaf.right() != null && leaf
                .right().key().equals(key))))
        {

            if (leaf.left().key().equals(key))
            {
                pair = leaf.left();
                list.add(ctrl.pool().getString(pair.value()));
            }

            if (leaf.right() != null && leaf.right().key().equals(key))
            {
                pair = leaf.right();
                list.add(ctrl.pool().getString(pair.value()));
            }

            leaf = leaf.next();
        }

        return list;
    }


    // ----------------------------------------------------------
    /**
     * Calls the helper method find with root as the starting point, to locate a
     * Leaf with the lowest KVPair containing key
     *
     * @param key
     *            key of lowest KVPair to be located
     * @return the Leaf that contains the lowest KVPair with key (null, if not
     *         found)
     */
    public Leaf search(Handle key)
    {
        return find(root, key);
    }


    // ----------------------------------------------------------
    /**
     * @return String representation of the tree using a helper traversal method
     */
    public String print()
    {
        return "Printing 2-3 tree:\n" + traverse(root, 0);
    }


    // ----------------------------------------------------------
    /**
     * Finds the lowest leaf with the given key
     *
     * @param subRoot
     *            the root to start from
     * @param key
     *            the key we're looking for
     */
    private Leaf find(TreeNode subRoot, Handle key)
    {
        if (root != null)
        {
            if (subRoot instanceof Internal)
            {
                // if key is less than or equal to root.left().key()
                if (key.compareTo(subRoot.left().key()) <= 0)
                {
                    return find(((Internal)subRoot).low(), key);
                }
                // if key is greater than or equal to root.left().key(), and
                // either root is not full or key is less than
                // root.right().key()
                else if (!subRoot.isFull()
                    || key.compareTo(subRoot.right().key()) <= 0)
                {
                    return find(((Internal)subRoot).mid(), key);
                }
                // if key is greater than or equal to root.right().key()
                else
                {
                    return find(((Internal)subRoot).high(), key);
                }
            }
            else if (subRoot instanceof Leaf)
            {
                // if root contains the lowest KVPair with key
                if (subRoot.left().key().equals(key)
                    || (subRoot.isFull() && subRoot.right().key().equals(key)))
                {
                    return (Leaf)subRoot;
                }

                // if root.next() contains the lowest KVPair with key
                else if (((Leaf)subRoot).next() != null
                    && ((Leaf)subRoot).next().left().key().equals(key)
                    || ((Leaf)subRoot).next() != null
                    && ((Leaf)subRoot).next().isFull()
                    && ((Leaf)subRoot).next().right().key().equals(key))
                {
                    return ((Leaf)subRoot).next();
                }
            }
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * inserts the kvpair into the tree at the correct position
     *
     * @param subRoot
     *            the root of the tree
     * @param ins
     *            the kvpair to insert
     * @return true if pair was inserted false otherwise
     */
    public boolean recInsert(TreeNode subRoot, KVPair ins)
    {

        if (root == null)
        {
            root = new Leaf(ins);
        }
        else if (ins == null || ins.key() == null || ins.value() == null
            || subRoot.containsEqual(ins))
        {
            return false;
        }
        else if (subRoot instanceof Internal)
        {
            Internal inRoot = (Internal)subRoot;
            if (ins.compareTo(inRoot.left()) == -1)
            {
                if (!recInsert(((Internal)subRoot).low(), ins))
                {
                    return false;
                }

                if (inRoot.low().overflow())
                {
                    inRoot.insert(inRoot.low().ovr());

                    if (inRoot.overflow())
                    {
                        inRoot.setOvrPtr(inRoot.mid());
                    }
                    else
                    {
                        inRoot.setHigh(inRoot.mid());
                    }
                    inRoot.setMid(inRoot.low().split());
                }
            }
            else if (subRoot.right() == null
                || ins.compareTo(subRoot.right()) == -1)
            {
                if (!recInsert(((Internal)subRoot).mid(), ins))
                {
                    return false;
                }

                if (inRoot.mid().overflow())
                {
                    inRoot.insert(inRoot.mid().ovr());

                    if (inRoot.overflow())
                    {
                        inRoot.setOvrPtr(inRoot.mid().split());
                    }
                    else
                    {
                        inRoot.setHigh(inRoot.mid().split());
                    }
                }
            }
            else
            {
                if (!recInsert(((Internal)subRoot).high(), ins))
                {
                    return false;
                }

                if (inRoot.high().overflow())
                {
                    inRoot.insert(inRoot.high().ovr());
                    inRoot.setOvrPtr(inRoot.high());
                    inRoot.setHigh(inRoot.ovrPtr().split());
                }
            }

        }
        else
        {
            subRoot.insert(ins);
        }

        if (subRoot == root && subRoot.overflow())
        {
            root = new Internal(subRoot.ovr(), subRoot, subRoot.split());
        }
        return true;

    }


    // ----------------------------------------------------------
    /**
     * Clears the tree
     */
    public void clear()
    {
        root = null;
    }


    /**
     * Traverses the tree
     *
     * @param subRoot
     *            the root to traverse from
     * @param depth
     *            the depth you are at in the tree
     * @return the string representation
     */
    private String traverse(TreeNode subRoot, int depth)
    {
        String traversal = "";
        if (subRoot != null)
        {
            // appends double-spaces for the TreeNode level (depth)
            for (int pos = 0; pos < depth; pos++)
            {
                traversal += "  ";
            }

            traversal += subRoot + "\n";

            // if root is an Internal Node, then its children are appended
            // after itself
            if (subRoot instanceof Internal)
            {
                traversal += traverse(((Internal)subRoot).low(), depth + 1);
                traversal += traverse(((Internal)subRoot).mid(), depth + 1);
                traversal += traverse(((Internal)subRoot).high(), depth + 1);
            }
        }
        return traversal;
    }


    /**
     * recursively deletes the KVPair
     *
     * @param subRoot
     *            the root of the tree
     * @param rem
     *            the pair to remove
     * @return a kvpair that replaced the pair we removed
     */
    private KVPair recDelete(TreeNode subRoot, KVPair rem)
    {
        KVPair rep = null;
        if (rem == null || (rem.key() == null || rem.value() == null))
        {
            return DUP;
        }

        if (subRoot instanceof Internal)
        {
            Internal inRoot = (Internal)subRoot;
            if (rem.compareTo(inRoot.left()) == -1)
            {
                rep = recDelete(inRoot.low(), rem);
                if (rep != null && rep.equals(DUP))
                {
                    return DUP;
                }

                if (inRoot.low() instanceof Leaf)
                {
                    if (!inRoot.low().underflow())
                    {
                        if (inRoot.low().left() == null)
                        {
                            inRoot.low().setLeft(inRoot.low().right());
                            inRoot.low().setRight(null);
                        }
                    }
                    else
                    {
                        if (inRoot.mid().isFull())
                        {
                            inRoot.low().setLeft(inRoot.mid().left());
                            inRoot.mid().setLeft(inRoot.mid().right());
                            inRoot.mid().setRight(null);

                            inRoot.setLeft(inRoot.mid().left());
                        }
                        else if (inRoot.isFull())
                        {
                            inRoot.low().setLeft(inRoot.mid().left());
                            inRoot.low().setRight(inRoot.mid().right());
                            ((Leaf)inRoot.low()).setNext(((Leaf)inRoot.mid())
                                .next());

                            inRoot.setMid(inRoot.high());
                            inRoot.setLeft(inRoot.mid().left());
                            inRoot.setRight(null);
                        }
                        else
                        {
                            inRoot.low().setLeft(inRoot.mid().left());
                            ((Leaf)inRoot.low()).setNext(((Leaf)inRoot.mid())
                                .next());

                            inRoot.setMid(null);
                        }
                    }
                    if (subRoot != root)
                    {
                        return inRoot.low().left();
                    }
                }
                else
                {
                    if (inRoot.low().underflow())
                    {
                        if (inRoot.mid().isFull())
                        {
                            ((Internal)inRoot.low()).setMid(((Internal)inRoot
                                .mid()).low());
                            inRoot.low().setLeft(inRoot.left());

                            inRoot.setLeft(inRoot.mid().left());

                            ((Internal)inRoot.mid()).setLow(((Internal)inRoot
                                .mid()).mid());
                            ((Internal)inRoot.mid()).setMid(((Internal)inRoot
                                .mid()).high());
                            inRoot.mid().setLeft(inRoot.mid().right());
                            inRoot.mid().setRight(null);
                        }
                        else
                        {
                            inRoot.low().setLeft(inRoot.left());
                            inRoot.setLeft(inRoot.right());

                            ((Internal)inRoot.low()).setMid(((Internal)inRoot
                                .mid()).low());
                            ((Internal)inRoot.low()).setHigh(((Internal)inRoot
                                .mid()).mid());
                            inRoot.low().setRight(inRoot.mid().left());

                            inRoot.setMid(inRoot.high());
                            inRoot.setRight(null);
                        }
                    }
                }
            }
            else if (!inRoot.isFull() || rem.compareTo(inRoot.right()) == -1)
            {
                rep = recDelete(inRoot.mid(), rem);
                if (rep != null && rep.equals(DUP))
                {
                    return DUP;

                }

                if (inRoot.mid() instanceof Leaf)
                {
                    if (!inRoot.mid().underflow())
                    {
                        if (inRoot.mid().left() == null)
                        {
                            inRoot.mid().setLeft(inRoot.mid().right());
                            inRoot.mid().setRight(null);
                            inRoot.setLeft(inRoot.mid().left());
                        }
                    }
                    else
                    {
                        if (inRoot.low().isFull())
                        {
                            inRoot.mid().setLeft(inRoot.low().right());
                            inRoot.low().setRight(null);
                            inRoot.setLeft(inRoot.mid().left());
                        }
                        else if (inRoot.isFull())
                        {
                            if (inRoot.high().isFull())
                            {
                                inRoot.mid().setLeft(inRoot.high().left());
                                inRoot.high().setLeft(inRoot.high().right());
                                inRoot.high().setRight(null);

                                inRoot.setLeft(inRoot.mid().left());
                                inRoot.setRight(inRoot.high().left());
                            }
                            else
                            {
                                inRoot.setMid(inRoot.high());
                                ((Leaf)inRoot.low())
                                    .setNext((Leaf)inRoot.mid());
                                inRoot.setRight(null);

                                inRoot.setLeft(inRoot.mid().left());
                            }
                        }
                        else
                        {
                            ((Leaf)inRoot.low()).setNext(((Leaf)inRoot.mid())
                                .next());
                            inRoot.setMid(null);
                        }
                    }
                }
                else
                {
                    if (rep != null && inRoot.left().equals(rem))
                    {
                        inRoot.setLeft(rep);
                    }

                    if (inRoot.mid().underflow())
                    {
                        if (inRoot.low().isFull())
                        {
                            ((Internal)inRoot.mid()).setMid(((Internal)inRoot
                                .mid()).low());
                            ((Internal)inRoot.mid()).setLow(((Internal)inRoot
                                .low()).high());

                            inRoot.mid().setLeft(inRoot.left());
                            inRoot.setLeft(inRoot.low().right());
                            inRoot.low().setRight(null);
                        }
                        else if (inRoot.isFull() && inRoot.high().isFull())
                        {
                            ((Internal)inRoot.mid()).setMid(((Internal)inRoot
                                .high()).low());
                            ((Internal)inRoot.high()).setLow(((Internal)inRoot
                                .high()).mid());
                            ((Internal)inRoot.high()).setMid(((Internal)inRoot
                                .high()).high());

                            inRoot.mid().setLeft(inRoot.right());
                            inRoot.setRight(inRoot.high().left());
                            inRoot.high().setLeft(inRoot.high().right());
                            inRoot.high().setRight(null);
                        }
                        else
                        {
                            ((Internal)inRoot.low()).setHigh(((Internal)inRoot
                                .mid()).low());
                            inRoot.low().setRight(inRoot.left());
                            inRoot.setMid(inRoot.high());
                            inRoot.setLeft(inRoot.right());
                            inRoot.setRight(null);
                        }
                    }
                }
            }
            else
            {
                rep = recDelete(inRoot.high(), rem);
                if (rep != null && rep.equals(DUP))
                {
                    return DUP;
                }

                if (inRoot.high() instanceof Leaf)
                {
                    if (!inRoot.high().underflow())
                    {
                        if (inRoot.high().left() == null)
                        {
                            inRoot.high().setLeft(inRoot.high().right());
                            inRoot.high().setRight(null);
                            inRoot.setRight(inRoot.high().left());
                        }
                    }
                    else
                    {
                        if (inRoot.mid().isFull())
                        {
                            inRoot.high().setLeft(inRoot.mid().right());
                            inRoot.mid().setRight(null);
                            inRoot.setRight(inRoot.high().left());
                        }
                        else
                        {
                            ((Leaf)inRoot.mid()).setNext(((Leaf)inRoot.high())
                                .next());
                            inRoot.setRight(null);
                        }
                    }
                }
                else
                {
                    if (rep != null && inRoot.right().equals(rem)) {
                        inRoot.setRight(rep);
                    }

                    if (inRoot.high().underflow())
                    {
                        if (inRoot.mid().isFull())
                        {
                            ((Internal)inRoot.high()).setMid(((Internal)inRoot
                                .high()).low());
                            ((Internal)inRoot.high()).setLow(((Internal)inRoot
                                .mid()).high());

                            inRoot.high().setLeft(inRoot.right());
                            inRoot.setRight(inRoot.mid().right());
                            inRoot.mid().setRight(null);
                        }
                        else
                        {
                            ((Internal)inRoot.mid()).setHigh(((Internal)inRoot
                                .high()).low());
                            inRoot.mid().setRight(inRoot.right());
                            inRoot.setRight(null);
                        }
                    }
                }
            }
        }
        else
        {
            if (subRoot != null)
            {
                if (!subRoot.containsEqual(rem))
                {
                    return DUP;
                }
                else {
                    subRoot.remove(rem);
                }
            }
        }
        if (root != null && root == subRoot)
        {
            if (subRoot.underflow())
            {
                if (subRoot instanceof Leaf) {
                    root = null;
                }
                else {
                    root = ((Internal)subRoot).low();
                }
            }

            if (subRoot.left() == null)
            {
                subRoot.setLeft(subRoot.right());
                subRoot.setRight(null);
            }
        }

        return rep;
    }
}

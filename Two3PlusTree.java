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
    private TreeNode            root;
    private int                 size;
    private ArrayList<KVPair>   prTrash;
    private ArrayList<Internal> inTrash;
    private ArrayList<Leaf>     lfTrash;


    // ----------------------------------------------------------
    /**
     * Initializes the Two3PlusTree (2-3+ tree) with a size = 0, and initializes
     * trash DoublyLinkedLists for KVPairs, Internal Nodes, and Leaf Nodes.
     */
    public Two3PlusTree()
    {
        size = 0;
        prTrash = new ArrayList<KVPair>();
        inTrash = new ArrayList<Internal>();
        lfTrash = new ArrayList<Leaf>();
    }


    // ----------------------------------------------------------
    /**
     * Calls the helper method recInsert with this.root as the starting point,
     * to insert a KVPair with the corresponding key and value
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
        return recInsert(this.root, key, value);
    }


    // ----------------------------------------------------------
    /**
     * Calls the helper method recRemove with this.root as the starting point,
     * to remove a KVPair with the corresponding key and value
     *
     * @param key
     *            key of KVPair to be removed
     * @param value
     *            value of KVPair to be removed
     * @return if a KVPair corresponding to key and value was removed
     *         successfully (if a corresponding KVPair is in the tree)
     */
    public boolean remove(Handle key, Handle value)
    {
        KVPair pair = new KVPair(key, value);
        return recRemove(this.root, pair);
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


    public Leaf search(Handle key)
    {
        return search(root, key);
    }


    @SuppressWarnings("hiding")
    private Leaf search(TreeNode root, Handle key)
    {
        if (size != 0)
        {
            if (root instanceof Internal)
            {
                // if key is less than root.left().key()
                if (key.compareTo(root.left().key()) <= 0)
                    return search(((Internal)root).low(), key);
                // if key is greater than or equal to root.left().key(), and
                // either root.right() is null or key is less than
                // root.right().key()
                else if (root.right() == null
                    || key.compareTo(root.right().key()) == -1)
                    return search(((Internal)root).mid(), key);
                // if key is greater than or equal to root.right().key()
                else
                    return search(((Internal)root).high(), key);
            }
            else if (root instanceof Leaf)

                if ((root.left().key()).equals(key)
                    || (root.right() != null && root.right().key().equals(key)))
                    return (Leaf)root;
                else if (((Leaf)root).next() != null)
                {
                    root = ((Leaf)root).next();
                    if ((root.left().key()).equals(key)
                        || (root.right() != null && root.right().key()
                            .equals(key)))
                    {
                        return (Leaf)root;
                    }
                }
        }
        return null;
    }


    @SuppressWarnings("hiding")
    private boolean recInsert(TreeNode root, Handle key, Handle value)
    {
        KVPair pair =
            (!prTrash.isEmpty()) ? prTrash.remove(prTrash.size() - 1).set(
                key,
                value) : new KVPair(key, value);

        if (size == 0)
            this.root =
                ((lfTrash.size() != 0) ? lfTrash.remove(lfTrash.size() - 1)
                    .compSet(pair, null) : new Leaf(pair));

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
                else
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
                    KVPair move =
                        (root.left().compareTo(pair) == 1) ? root.left() : pair;

                    Leaf split =
                        ((lfTrash.size() != 0)
                            ? (Leaf)lfTrash.remove(lfTrash.size() - 1).compSet(
                                move,
                                root.right())
                            : new Leaf(move, root.right()));

                    // root.left() is set to the minimum of root.left() and pair
                    root.compSet(((root.left().compareTo(pair) == 1)
                        ? pair
                        : root.left()), null);

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
    private boolean recRemove(TreeNode root, KVPair pair)
    {
        Internal inRoot;
        if (size == 0)
        {
            return false;
        }
        // if root is an Internal, then the method will search for the
        // proper location to insert pair
        if (root instanceof Internal)
        {
            if ((inRoot = (Internal)root).low() instanceof Internal)
            {
                if (pair.compareTo(inRoot.left()) == -1)
                    recRemove(inRoot.low(), pair);
                else
                {
                    if (inRoot.right() == null
                        || pair.compareTo(inRoot.right()) == -1)
                        recRemove(inRoot.mid(), pair);
                    else
                        recRemove(inRoot.high(), pair);
                }
            }
            else if (inRoot.low() instanceof Leaf)
            {

                Leaf lfRoot;

                if (inRoot.low().containsEqual(pair))
                {

                    lfRoot = ((Leaf)inRoot.low());

                    if (lfRoot.left().equals(pair) && lfRoot.right() != null)
                    {

                        lfRoot.compSet(lfRoot.right(), null);
                        update(inRoot, root, pair, -1);
                    }
                }
                else if (inRoot.mid().contains(pair))
                {
                    lfRoot = ((Leaf)inRoot.mid());

                }
                else if (inRoot.high().containsEqual(pair))
                {
                    lfRoot = ((Leaf)inRoot.high());

                }
                else
                {
                    return false;
                }
            }
            return true;
        }
        else if (root instanceof Leaf)
        {
            // if the leaf does not contain the pair
            if (!root.containsEqual(pair))
            {
                return false;
            }
            // the right kvpair has the data we're looking for
            else if (root.right() != null && root.right().compareTo(pair) == 0)
            {
                prTrash.add(root.right());
                root.right().set(null);
                return true;
            }
            else if (root.left().compareTo(pair) == 0)
            {

            }
            else
            {

                // TODO right is null, so need to demote then remove the leaf.
            }
        }
        return false;
    }


    private void update(Internal parentRoot, TreeNode origRoot, KVPair update, int place)
    {
        if (place == -1) {
            if (parentRoot.low().right() != null) {
                parentRoot.setLeft(update);
            }
        }
        else if (place == 0) {

        }
        else if (place == 1) {

        }
    }


    @SuppressWarnings("hiding")
    private void promote(
        TreeNode root,
        TreeNode orig,
        TreeNode split,
        KVPair promo)
    {
        Internal inRoot;

        // if root is orig, that means the method has promoted up to where it
        // needs a new this.root, which would be the parent of orig and split
        if (root == orig)
            this.root =
                ((inTrash.size() != 0) ? inTrash.remove(inTrash.size() - 1)
                    .set(promo, null, orig, split) : new Internal(
                    promo,
                    orig,
                    split));

        // if the promotion is less than the left KVPair of root, then the
        // method checks root.low()
        else if (promo.compareTo((inRoot = ((Internal)root)).left()) == -1)
        {
            if (inRoot.low() == orig)
            {
                // if the right KVPair of root is null, then pointers are
                // adjusted to accommodate the insertion of the split Node
                if (inRoot.right() == null)
                {
                    inRoot.setHigh(inRoot.mid());
                    inRoot.setMid(split);
                    inRoot.compSet(inRoot.left(), promo);
                }

                // if the right KVPair of root is not null, then another
                // Internal parent needs to be created; the appropriate pointers
                // get adjusted and the center value of the parents is saved
                // (before being set to null) for further promotion and promote
                // is called again
                else
                {
                    Internal parent =
                        ((inTrash.size() != 0) ? inTrash.remove(
                            inTrash.size() - 1).set(
                            inRoot.high().left(),
                            null,
                            inRoot.mid(),
                            inRoot.high()) : new Internal(
                            inRoot.high().left(),
                            inRoot.mid(),
                            inRoot.high()));

                    KVPair newPromo = inRoot.left();
                    inRoot.setMid(split);
                    inRoot.setLeft(promo);

                    // prTrash.add(inRoot.right());
                    inRoot.setRight(null);
                    inRoot.setHigh(null);

                    promote(this.root, inRoot, parent, newPromo);
                }
            }

            // if root.low() is not orig, then the method continues to search in
            // that direction
            else
                promote(inRoot.low(), orig, split, promo);
        }

        // if the promotion is greater than or equal to the left KVPair of root,
        // and either the right KVPair of root is null, or the promotion is less
        // than the right KVPair of root, then the method checks root.mid()
        else if (promo.compareTo(inRoot.left()) >= 0
            && (inRoot.right() == null || promo.compareTo(inRoot.right()) == -1))
        {
            if (inRoot.mid() == orig)
            {
                // if the right KVPair of root is null, then pointers are
                // adjusted to accommodate the insertion of the split Node
                if (inRoot.right() == null)
                {
                    inRoot.setHigh(split);
                    inRoot.compSet(inRoot.left(), promo);
                }

                // if the right KVPair of root is not null, then another
                // Internal parent needs to be created; the appropriate pointers
                // get adjusted and the center value of the parents is saved
                // (before being set to null) for further promotion and promote
                // is called again
                else
                {
                    Internal parent =
                        ((inTrash.size() != 0) ? inTrash.remove(
                            inTrash.size() - 1).set(
                            inRoot.high().left(),
                            null,
                            split,
                            inRoot.high()) : new Internal(
                            inRoot.high().left(),
                            split,
                            inRoot.high()));

                    KVPair newPromo = split.left();
                    // prTrash.add(inRoot.right());
                    inRoot.setRight(null);
                    inRoot.setHigh(null);

                    promote(this.root, inRoot, parent, newPromo);
                }
            }

            // if root.mid() is not orig, then the method continues to search in
            // that direction
            else
                promote(inRoot.mid(), orig, split, promo);
        }

        // if the promotion is greater than or equal to the right KVPair of
        // root then the method checks root.high()
        else if (promo.compareTo(inRoot.right()) >= 0)
        {
            // an Internal parent needs to be created; the appropriate pointers
            // get adjusted and the center value of the parents is saved
            // (before being set to null) for further promotion and promote
            // is called again
            if (inRoot.high() == orig)
            {
                Internal parent =
                    ((inTrash.size() != 0) ? inTrash.remove(inTrash.size() - 1)
                        .set(promo, null, inRoot.high(), split) : new Internal(
                        promo,
                        inRoot.high(),
                        split));

                KVPair newPromo = inRoot.right();
                inRoot.setRight(null);
                inRoot.setHigh(null);

                promote(this.root, inRoot, parent, newPromo);
            }

            // if root.high() is not orig, then the method continues to search
            // in that direction
            else
                promote(inRoot.high(), orig, split, promo);
        }
    }


    @SuppressWarnings("hiding")
    private String traverse(TreeNode root, int depth)
    {
        String traversal = "";
        if (root != null)
        {
            traversal += "\n";

            // appends double-spaces for the TreeNode level (depth)
            for (int pos = 0; pos < depth; pos++)
                traversal += "  ";

            traversal += root;

            // if root is an Internal Node, then its children are appended
            // after itself
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

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
    private Controller          ctrl;
    private TreeNode            root;
    private int                 size;
    private ArrayList<KVPair>   prTrash;
    private ArrayList<Internal> inTrash;
    private ArrayList<Leaf>     lfTrash;


    // ----------------------------------------------------------
    /**
     * Initializes the Two3PlusTree (2-3+ tree) with a size = 0, and initializes
     * trash DoublyLinkedLists for KVPairs, Internal Nodes, and Leaf Nodes.
     * @param ctrl
     *            Controller that manages the Hashtable and MemPool
     */
    public Two3PlusTree(Controller ctrl)
    {
        this.ctrl = ctrl;
        size = 0;
        prTrash = new ArrayList<KVPair>();
        inTrash = new ArrayList<Internal>();
        lfTrash = new ArrayList<Leaf>();
    }


    // ----------------------------------------------------------
    /**
     * Calls the helper method recInsert with this.root as the starting point,
     * to insert a KVPair with the corresponding key and value
     * @param key key for KVPair to be inserted
     * @param value value for KVPair to be inserted
     * @return if a KVPair corresponding to key and value was inserted
     *         successfully (if there are no duplicates)
     */
    public boolean insert(Handle key, Handle value)
    {
        return recInsert(this.root, key, value);
    }


 // ----------------------------------------------------------
    /**
     * Calls the helper method recDelete with this.root as the starting point,
     * to delete a KVPair with the corresponding key and value
     * @param key key of KVPair to be deleted
     * @param value value of KVPair to be deleted
     * @return if a KVPair corresponding to key and value was deleted
     *         successfully (if a corresponding KVPair is in the tree)
     */
    public boolean delete(Handle key, Handle value)
    {
        return recDelete(this.root, (!prTrash.isEmpty())
            ? prTrash.remove(prTrash.size() - 1).set(key, value)
                : new KVPair(key, value));
    }


    // ----------------------------------------------------------
    /**
     * @param key the key of the KVPairs to append
     * @return a String representation of a list of all KVPairs with key
     * @throws Exception
     */
    public String list(Handle key) throws Exception
    {
        String list = "";
        Leaf leaf;
        if ((leaf = search(key)) != null)
        {
            KVPair pos;
            do
            {
                pos = (leaf.left().key().equals(key)) ? leaf.left()
                    : leaf.right();
                list += "|" + ctrl.pool().getString(pos.value()) + "|\n";

                if (pos != leaf.right() && leaf.isFull()
                    && leaf.right().key().equals(key))
                {
                    list += "|" + ctrl.pool().getString(leaf.right().value())
                        + "|\n";
                }
            } while ((leaf = leaf.next()) != null
                && (pos = leaf.left()).key().equals(key));
        }
        return list;
    }


    // ----------------------------------------------------------
    /**
     * Calls the helper method find with this.root as the starting point,
     * to locate a Leaf with the lowest KVPair containing key
     * @param key key of lowest KVPair to be located
     * @return the Leaf that contains the lowest KVPair with key (null, if not
     *         found)
     */
    public Leaf search(Handle key)
    {
        return find(this.root, key);
    }


    // ----------------------------------------------------------
    /**
     * @return String representation of the tree using a helper traversal method
     */
    public String print()
    {
        return "Printing 2-3 tree:\n" + traverse(this.root, 0);
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
    private Leaf find(TreeNode root, Handle key)
    {
        if (size != 0)
        {
            if (root instanceof Internal)
            {
                // if key is less than or equal to root.left().key()
                if (key.compareTo(root.left().key()) <= 0)
                    return find(((Internal)root).low(), key);
                // if key is greater than or equal to root.left().key(), and
                // either root is not full or key is less than
                // root.right().key()
                else if (!root.isFull()
                    || key.compareTo(root.right().key()) == -1)
                        return find(((Internal)root).mid(), key);
                // if key is greater than or equal to root.right().key()
                else
                    return find(((Internal)root).high(), key);
            }
            else if (root instanceof Leaf)
            {
                // if root contains the lowest KVPair with key
                if (root.left().key().equals(key)
                || (root.isFull() && root.right().key().equals(key)))
                    return (Leaf)root;

                // if root.next() contains the lowest KVPair with key
                else if (((Leaf)root).next() != null
                    && ((Leaf)root).next().left().key().equals(key)
                    || ((Leaf)root).isFull()
                    && ((Leaf)root).right().key().equals(key))
                        return ((Leaf)root).next();
            }
        }
        return null;
    }


    @SuppressWarnings("hiding")
    private boolean recInsert(TreeNode root, Handle key, Handle value)
    {
        KVPair pair = (!prTrash.isEmpty())
            ? prTrash.remove(prTrash.size() - 1).set(key, value)
                : new KVPair(key, value);

        if (size == 0)
            this.root = ((!lfTrash.isEmpty())
                ? lfTrash.remove(lfTrash.size() - 1).compSet(pair, null)
                    : new Leaf(pair));

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
                else if (!((Internal)root).isFull()
                    || pair.compareTo(((Internal)root).right()) == -1)
                        recInsert(((Internal)root).mid(), key, value);
                else
                    recInsert(((Internal)root).high(), key, value);
            }
            else if (root instanceof Leaf)
            {
                // if the Leaf where the method wants to insert pair is not
                // full, then it is inserted and sorted
                if (!root.isFull())
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

                    Leaf split = ((!lfTrash.isEmpty())
                        ? (Leaf)lfTrash.remove(lfTrash.size() - 1).compSet(move,
                            root.right()) : new Leaf(move, root.right()));

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
    private boolean recDelete(TreeNode root, KVPair pair)
    {
        Internal inRoot;
        Leaf loc;
        if (size == 0)
            return false;

        // if there is only one Leaf in the tree (and it is the root of the
        // tree)
        else if (root instanceof Leaf)
        {
            // if the Leaf does not contain pair
            if (!root.containsEqual(pair))
                return false;
            else if (root.isFull() && root.right().equals(pair))
            {
                prTrash.add(root.right());
                root.setRight(null);
            }

            // if there only one KVPair in the Leaf, then the Leaf is deleted
            // and the size of the tree is 0
            else
            {
                prTrash.add(root.left());
                lfTrash.add((Leaf)root);
            }
        }
        else if (root instanceof Internal)
        {
            // if root is not at a level right above the Leaves, then the method
            // is recursively called until root is at that level
            if ((inRoot = (Internal)root).low() instanceof Internal)
            {
                if (pair.compareTo(inRoot.left()) == -1)
                    recDelete(inRoot.low(), pair);
                else if (!inRoot.isFull()
                    || pair.compareTo(inRoot.right()) == -1)
                        recDelete(inRoot.mid(), pair);
                else
                    recDelete(inRoot.high(), pair);
            }

            // if root is at a level right above the Leaves, then the proper
            // adjustments are made from the Internal parent level to the
            // Leaves for deletion
            else if (inRoot.low() instanceof Leaf)
            {
                if (pair.compareTo(inRoot.left()) == -1)
                    loc = (Leaf)inRoot.low();
                else if (!inRoot.isFull()
                    || pair.compareTo(inRoot.right()) == -1)
                    loc = (Leaf)inRoot.mid();
                else
                    loc = (Leaf)inRoot.high();

                if (!loc.containsEqual(pair))
                    return false;
                else if (loc.isFull() && loc.right().equals(pair))
                {
                    prTrash.add(loc.right());
                    loc.setRight(null);
                }
                else
                    // the helper method is called whenever the left KVPair of
                    // a Leaf needs to be deleted in order to deal with
                    // pointer reassignment and promotions
                    delUpdate(inRoot, loc.left());
            }
        }
        size--;
        return true;
    }


    private void delUpdate(Internal parent, KVPair pair)
    {
        // most cases (parent.{low, mid, high}) are very similar in their
        // implementation of pointer and KVPair assignment, however, for
        // for every case for parent.low() a promotion in higher levels is
        // necessary to balance the tree; but, for parent.{mid, high}, only
        // the parent receives the promotion and the method does not need to
        // change any higher level Internal Nodes
        if (parent.low().containsEqual(pair))
        {
            // if the Leaf child is full, then it only requires a simple
            // transfer from the right KVPair to the left
            if (parent.low().isFull())
            {
                parent.low().compSet(parent.low().right(), null);

                promote(this.root, parent, null, parent.low().left());
            }

            // however, if the Leaf child is not full, but the adjacent Leaf
            // child (to the right and under the same parent) is full, then it
            // requires two transfers: first, from the left KVPair of the middle
            // child to the left KVPair of the low; second, within the adjacent
            // Leaf child, from the right KVPair to the left
            else if (parent.mid().isFull())
            {
                parent.low().setLeft(parent.mid().left());
                parent.mid().compSet(parent.mid().right(), null);

                parent.setLeft(parent.mid().left());

                promote(this.root, parent, null, parent.low().left());
            }

            // if both the Leaf child and the adjacent Leaf child are not full,
            // then if the highest Leaf child is not null, the current Leaf must
            // be deleted and pointers readjusted
            else if (parent.isFull())
            {
                ((Leaf)parent.low()).previous().link((Leaf)parent.mid());

                prTrash.add(parent.low().left());
                lfTrash.add((Leaf)parent.low());

                parent.setLow(parent.mid());
                parent.setMid(parent.high());
                parent.compSet(parent.mid().left(), null);

                promote(this.root, parent, null, parent.low().left());
            }

            // if none of the above conditions can be met, then there are not
            // enough resources within this parent/Leaves structure to keep the
            // tree ordered and a transfer between parent siblings is required
            else
                transfer((Internal)this.root, parent, (Leaf)parent.low());
        }
        else if (parent.mid().containsEqual(pair))
        {
            if (parent.mid().isFull())
            {
                parent.mid().compSet(parent.mid().right(), null);
                parent.setLeft(parent.mid().left());
            }
            else if (parent.isFull())
            {
                if (parent.high().isFull())
                {
                    parent.mid().setLeft(parent.high().left());
                    parent.high().compSet(parent.high().right(), null);

                    parent.compSet(parent.mid().left(),
                        parent.high().left());
                }
                else
                {
                    ((Leaf)parent.low()).link((Leaf)parent.high());

                    prTrash.add(parent.mid().left());
                    lfTrash.add((Leaf)parent.mid());

                    parent.setMid(((Leaf)parent.low()).link(
                        (Leaf)parent.high()));
                    parent.compSet(parent.mid().left(), null);
                }
            }
            else
                transfer((Internal)this.root, parent, (Leaf)parent.mid());
        }
        else if (parent.high().containsEqual(pair))
        {
            if (parent.high().isFull())
            {
                parent.high().compSet(parent.high().right(), null);
                parent.setRight(parent.high().left());
            }
            else
            {
                ((Leaf)parent.mid()).link(((Leaf)parent.high()).next());

                prTrash.add(parent.high().left());
                lfTrash.add((Leaf)parent.high());
                parent.setHigh(null);
            }
        }
    }


    @SuppressWarnings("hiding")
    private void promote(TreeNode root, TreeNode original, TreeNode split,
        KVPair promo)
    {
        Internal inRoot;

        // if root is orig, that means the method has promoted up to where it
        // needs a new this.root, which would be the parent of orig and split;
        // however, if split is null, then the original promo KVPair resides in
        // the bottom left of the tree and is trying to promote itself, but
        // should not be allowed to
        if (root == original && split != null)
            this.root = ((!inTrash.isEmpty())
                ? inTrash.remove(inTrash.size() - 1).set(promo, null, original,
                    split) : new Internal(promo, original, split));

        // if the promotion is less than the left KVPair of root, then the
        // method checks root.low()
        else if (promo.compareTo((inRoot = ((Internal)root)).left()) == -1)
        {
            if (inRoot.low() == original)
            {
                // if the right KVPair of root is null, then pointers are
                // adjusted to accommodate the insertion of the split Node
                if (!inRoot.isFull())
                {
                    inRoot.setHigh(inRoot.mid());
                    inRoot.setMid(split);
                    inRoot.compSet(inRoot.left(), promo);
                }

                // if root is full, then another Internal parent needs to be
                // created; the appropriate pointers get adjusted and the center
                // value of the parents is saved (before being set to null) for
                // further promotion and promote is called again
                else
                {
                    // if split is null, then there needs to be a promotion due
                    // to deletion, but because original is the low pointer, the
                    // promotion must continue up the tree
                    if (split == null)
                        promote(this.root, inRoot, null, promo);
                    else
                    {
                        Internal parent = ((!inTrash.isEmpty())
                            ? inTrash.remove(inTrash.size() - 1).set(
                                inRoot.high().left(), null, inRoot.mid(),
                                    inRoot.high())
                            : new Internal(inRoot.high().left(),
                                inRoot.mid(), inRoot.high()));

                        KVPair newPromo = inRoot.left();
                        inRoot.setMid(split);

                        inRoot.compSet(promo, null);
                        inRoot.setHigh(null);

                        promote(this.root, inRoot, parent, newPromo);
                    }
                }
            }

            // if root.low() is not orig, then the method continues to search in
            // that direction
            else
                promote(inRoot.low(), original, split, promo);
        }

        // if the promotion is greater than or equal to the left KVPair of root,
        // and either the root is not full, or the promotion is less than the
        // right KVPair of root, then the method checks root.mid()
        else if (promo.compareTo(inRoot.left()) >= 0 &&
            (!inRoot.isFull() || promo.compareTo(inRoot.right()) == -1))
        {
            if (inRoot.mid() == original)
            {
                // if root is not full, then pointers are adjusted to
                // accommodate the insertion of the split Node
                if (!inRoot.isFull())
                {
                    inRoot.setHigh(split);
                    inRoot.compSet(inRoot.left(), promo);
                }

                // if root is full, then another Internal parent needs to be
                // created; the appropriate pointers get adjusted and the center
                // value of the parents is saved (before being set to null) for
                // further promotion and promote is called again
                else
                {
                    // if split is null, then there needs to be a promotion due
                    // to deletion
                    if (split == null)
                        inRoot.setLeft(promo);
                    else
                    {
                        Internal parent = ((!inTrash.isEmpty())
                            ? inTrash.remove(inTrash.size() - 1).set(
                                inRoot.high().left(), null, split,
                                inRoot.high())
                            : new Internal(inRoot.high().left(),
                                split, inRoot.high()));

                        KVPair newPromo = split.left();
                        inRoot.setHigh(null);

                        promote(this.root, inRoot, parent, newPromo);
                    }
                }
            }

            // if root.mid() is not orig, then the method continues to search in
            // that direction
            else
                promote(inRoot.mid(), original, split, promo);
        }

        // if the promotion is greater than or equal to the right KVPair of
        // root then the method checks root.high()
        else if (promo.compareTo(inRoot.right()) >= 0)
        {
            // an Internal parent needs to be created; the appropriate pointers
            // get adjusted and the center value of the parents is saved
            // (before being set to null) for further promotion and promote
            // is called again
            if (inRoot.high() == original)
            {
             // if split is null, then there needs to be a promotion due
                // to deletion
                if (split == null)
                    inRoot.setRight(promo);
                else
                {
                    Internal parent = ((!inTrash.isEmpty())
                        ? inTrash.remove(inTrash.size() - 1).set(promo, null,
                            inRoot.high(), split)
                        : new Internal(promo, inRoot.high(), split));

                    KVPair newPromo = inRoot.right();
                    inRoot.setHigh(null);

                    promote(this.root, inRoot, parent, newPromo);
                }
            }

            // if root.high() is not orig, then the method continues to search
            // in that direction
            else
                promote(inRoot.high(), original, split, promo);
        }
    }


    @SuppressWarnings("hiding")
    private void transfer(Internal root, Internal parent, Leaf rem)
    {
        Internal adj;
        if (parent.left().compareTo(root.left()) == -1)
        {
            if (root.low() == parent)
            {
                if ((adj = (Internal)root.mid()).isFull())
                {
                    (((Leaf)((parent.low() == rem)
                        ? ((Leaf)parent.low()).previous()
                            : parent.low()))).link((Leaf)((parent.low() == rem)
                                ? parent.mid() : adj.low()));

                    prTrash.add((parent.low() == rem)
                        ? parent.low().left() : parent.mid().left());
                    lfTrash.add((Leaf)((parent.low() == rem)
                        ? parent.low() : parent.mid()));

                    if (parent.low() == rem)
                    {
                        parent.setLow(parent.mid());
                        promote(this.root, parent, null, parent.low().left());
                    }
                    leftTransfer(root, parent, adj);
                    root.setLeft(((Internal)root.mid()).low().left());
                }
                else if (root.isFull())
                {
                    adj.setHigh(adj.mid());
                    adj.setRight(adj.high().left());

                    adj.setMid(adj.low());
                    adj.setLeft(adj.mid().left());

                    adj.setLow((parent.low() == rem)
                        ? parent.mid() : parent.low());

                    (((Leaf)((parent.low() == rem)
                        ? ((Leaf)parent.low()).previous()
                            : parent.low()))).link((Leaf)((parent.low() == rem)
                                ? parent.mid() : adj.low()));

                    prTrash.add((parent.low() == rem)
                        ? parent.low().left() : parent.mid().left());
                    lfTrash.add((Leaf)((parent.low() == rem)
                        ? parent.low() : parent.mid()));
                    inTrash.add(parent);

                    root.setLow(root.mid());
                    root.setMid(root.high());
                    root.setHigh(null);
                    root.compSet(((Internal)root.mid()).low().left(), null);

                    promote(this.root, root.low(), null,
                        ((Internal)root.low()).low().left());
                }
                else
                {

                }
            }
            else
                transfer((Internal)root.low(), parent, rem);
        }
        else if (parent.left().compareTo(root.left()) >= 0
            && (!root.isFull() || parent.left().compareTo(root.right()) == -1))
        {
            if (root.mid() == parent)
            {
                if ((adj = (Internal)root.low()).isFull())
                {
                    ((Leaf)((parent.mid() == rem)
                        ? (parent.low())
                            : adj.high())).link((Leaf)((parent.mid() == rem)
                                ? ((Leaf)parent.mid()).next() : parent.mid()));

                    prTrash.add((parent.mid() == rem)
                        ? parent.mid().left() : parent.low().left());
                    lfTrash.add((Leaf)((parent.mid() == rem)
                        ? parent.mid() : parent.low()));

                    if (parent.mid() == rem)
                    {
                        parent.setMid(parent.low());
                        parent.setLeft(parent.mid().left());
                    }
                    rightTransfer(root, parent, adj);
                    root.setLeft(((Internal)root.mid()).low().left());
                }
                else if ((adj = (Internal)root.high()).isFull())
                {
                    (((Leaf)((parent.low() == rem)
                        ? ((Leaf)parent.low()).previous()
                            : parent.low()))).link((Leaf)((parent.low() == rem)
                                ? parent.mid() : adj.low()));

                    prTrash.add((parent.low() == rem)
                        ? parent.low().left() : parent.mid().left());
                    lfTrash.add((Leaf)((parent.low() == rem)
                        ? parent.low() : parent.mid()));

                    if (parent.low() == rem)
                    {
                        parent.setLow(parent.mid());
                        root.setLeft(parent.low().left());
                    }
                    leftTransfer(root, parent, adj);
                    root.setRight(((Internal)root.high()).low().left());
                }
                else if (root.isFull())
                {
                    ((Leaf)((parent.low() == rem)
                        ? ((Leaf)parent.low()).previous()
                            : parent.low())).link((Leaf)((parent.low() == rem)
                                ? parent.mid() : ((Leaf)parent.mid()).next()));

                    (adj = (Internal)root.low()).setHigh((parent.low() == rem)
                        ? parent.mid() : parent.low());
                    adj.setRight(adj.high().left());

                    prTrash.add((parent.low() == rem)
                        ? parent.low().left() : parent.mid().left());
                    lfTrash.add((Leaf)((parent.low() == rem)
                        ? parent.low() : parent.mid()));
                    inTrash.add(parent);

                    root.setMid(root.high());
                    root.setLeft(((Internal)root.mid()).low().left());

                    root.setHigh(null);
                }
                else
                {

                }
            }
            else
                transfer((Internal)root.mid(), parent, rem);
        }
        else if (parent.left().compareTo(root.right()) >= 0)
        {
            if (root.high() == parent)
            {
                if ((adj = (Internal)root.mid()).isFull())
                {
                    ((Leaf)((parent.mid() == rem)
                        ? parent.low()
                            : ((Leaf)parent.low()).previous())).link(
                                (Leaf)((parent.mid() == rem)
                                    ? ((Leaf)parent.mid()).next()
                                        : parent.mid()));

                    prTrash.add((parent.mid() == rem)
                        ? parent.mid().left() : parent.low().left());
                    lfTrash.add((Leaf)((parent.mid() == rem)
                        ? parent.mid() : parent.low()));

                    if (parent.mid() == rem)
                    {
                        parent.setMid(parent.low());
                        parent.setLeft(parent.mid().left());
                    }
                    rightTransfer(root, parent, adj);
                    root.setRight(((Internal)root.high()).low().left());
                }
                else
                {
                    ((Leaf)((parent.low() == rem)
                        ? ((Leaf)parent.low()).previous()
                            : parent.low())).link((Leaf)((parent.low() == rem)
                                ? parent.mid() : ((Leaf)parent.mid()).next()));

                    adj.setHigh((parent.low() == rem)
                        ? parent.mid() : parent.low());
                    adj.setRight(adj.high().left());

                    prTrash.add((parent.low() == rem)
                        ? parent.low().left() : parent.mid().left());
                    lfTrash.add((Leaf)((parent.low() == rem)
                        ? parent.low() : parent.mid()));
                    inTrash.add(parent);

                    root.setHigh(null);
                }
            }
            else
                transfer((Internal)root.high(), parent, rem);
        }
    }


    @SuppressWarnings("hiding")
    private void leftTransfer(Internal root, Internal parent, Internal rightAdj)
    {
        parent.setMid(rightAdj.low());
        parent.setLeft(parent.mid().left());

        rightAdj.setLow(rightAdj.mid());
        rightAdj.setMid(rightAdj.high());
        rightAdj.setLeft(rightAdj.mid().left());
        rightAdj.setHigh(null);
    }


    @SuppressWarnings("hiding")
    private void rightTransfer(Internal root, Internal parent, Internal leftAdj)
    {
        parent.setLow(leftAdj.high());
        leftAdj.setHigh(null);
    }


    @SuppressWarnings("hiding")
    private String traverse(TreeNode root, int depth)
    {
        String traversal = "";
        if (root != null)
        {
            // appends double-spaces for the TreeNode level (depth)
            for (int pos = 0; pos < depth; pos++)
                traversal += "  ";

            traversal += root + "\n";

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

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
    private ArrayList<KVPair>   prTrash;
    private ArrayList<Internal> inTrash;
    private ArrayList<Leaf>     lfTrash;


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
        prTrash = new ArrayList<KVPair>();
        inTrash = new ArrayList<Internal>();
        lfTrash = new ArrayList<Leaf>();
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
        return recInsert(root, key, value);
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
        return recDelete(
            root,
            (!prTrash.isEmpty()) ? prTrash.remove(prTrash.size() - 1).set(
                key,
                value) : new KVPair(key, value));
    }


    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     *
     * @param key
     * @return true if it was found false otherwise
     */
    public ArrayList<KVPair> remove(Handle key)
    {

        ArrayList<KVPair> list = new ArrayList<KVPair>();
        Leaf leaf;
        KVPair pair;
        if (key == null || (leaf = find(root, key)) == null)
            return list;

        while ( leaf != null
            && ((pair = leaf.left()).key().equals(key) || (leaf.right() != null && leaf
                .right().key().equals(key))))
        {
            pair = (leaf.left().key().equals(key)) ? leaf.left() : leaf.right();

            list.add(pair);
            delete(pair.key(), pair.value());
            leaf = leaf.next();
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
    public String list(Handle key)
        throws Exception
    {
        String list = "";
        Leaf leaf;
        if ((leaf = search(key)) != null)
        {
            KVPair pos;
            while ((leaf = leaf.next()) != null
                && (pos = leaf.left()).key().equals(key))
            {
                pos =
                    (leaf.left().key().equals(key)) ? leaf.left() : leaf
                        .right();
                list += "|" + ctrl.pool().getString(pos.value()) + "|\n";

                if (pos != leaf.right() && leaf.isFull()
                    && leaf.right().key().equals(key))
                {
                    list +=
                        "|" + ctrl.pool().getString(leaf.right().value())
                            + "|\n";
                }
            }
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


    private Leaf find(TreeNode subRoot, Handle key)
    {
        if (root != null)
        {
            if (subRoot instanceof Internal)
            {
                // if key is less than or equal to root.left().key()
                if (key.compareTo(subRoot.left().key()) <= 0)
                    return find(((Internal)subRoot).low(), key);
                // if key is greater than or equal to root.left().key(), and
                // either root is not full or key is less than
                // root.right().key()
                else if (!subRoot.isFull()
                    || key.compareTo(subRoot.right().key()) == -1)
                    return find(((Internal)subRoot).mid(), key);
                // if key is greater than or equal to root.right().key()
                else
                    return find(((Internal)subRoot).high(), key);
            }
            else if (subRoot instanceof Leaf)
            {
                // if root contains the lowest KVPair with key
                if (subRoot.left().key().equals(key)
                    || (subRoot.isFull() && subRoot.right().key().equals(key)))
                    return (Leaf)subRoot;

                // if root.next() contains the lowest KVPair with key
                else if (((Leaf)subRoot).next() != null
                    && ((Leaf)subRoot).next().left().key().equals(key)
                    || ((Leaf)subRoot).isFull()
                    && ((Leaf)subRoot).right().key().equals(key))
                    return ((Leaf)subRoot).next();
            }
        }
        return null;
    }


    private boolean recInsert(TreeNode subRoot, Handle key, Handle value)
    {
        KVPair pair =
            (!prTrash.isEmpty()) ? prTrash.remove(prTrash.size() - 1).set(
                key,
                value) : new KVPair(key, value);

        if (root == null)
            root =
                ((!lfTrash.isEmpty()) ? lfTrash.remove(lfTrash.size() - 1)
                    .compSet(pair, null) : new Leaf(pair));

        // if root contains a KVPair equal to pair, that means there is a
        // duplicate, and the method returns false
        else if (subRoot.containsEqual(pair))
            return false;
        else
        {
            // if root is an Internal, then the method will search for the
            // proper location to insert pair
            if (subRoot instanceof Internal)
            {
                if (pair.compareTo(((Internal)subRoot).left()) == -1)
                    recInsert(((Internal)subRoot).low(), key, value);
                else if (!((Internal)subRoot).isFull()
                    || pair.compareTo(((Internal)subRoot).right()) == -1)
                    recInsert(((Internal)subRoot).mid(), key, value);
                else
                    recInsert(((Internal)subRoot).high(), key, value);
            }
            else if (subRoot instanceof Leaf)
            {
                // if the Leaf where the method wants to insert pair is not
                // full, then it is inserted and sorted
                if (!subRoot.isFull())
                    subRoot.compSet(subRoot.left(), pair);
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
                        (subRoot.left().compareTo(pair) == 1)
                            ? subRoot.left()
                            : pair;

                    Leaf split =
                        ((!lfTrash.isEmpty())
                            ? (Leaf)lfTrash.remove(lfTrash.size() - 1).compSet(
                                move,
                                subRoot.right())
                            : new Leaf(move, subRoot.right()));

                    // root.left() is set to the minimum of root.left() and pair
                    subRoot.compSet(((subRoot.left().compareTo(pair) == 1)
                        ? pair
                        : subRoot.left()), null);

                    // Leaf pointers are updated
                    split.link(((Leaf)subRoot).next());
                    ((Leaf)subRoot).link(split);

                    // splitting Leaves requires new parents, so the proper
                    // values are promoted using a helper method
                    promote(root, subRoot, split, split.left());
                }
            }
        }
        return true;
    }


    private boolean recDelete(TreeNode subRoot, KVPair pair)
    {
        Internal inRoot;
        Leaf loc;
        if (root == null)
            return false;

        // if there is only one Leaf in the tree (and it is the root of the
        // tree)
        else if (subRoot instanceof Leaf)
        {
            // if the Leaf does not contain pair
            if (!subRoot.containsEqual(pair))
                return false;
            else if (subRoot.isFull())
            {
                if (subRoot.left().equals(pair))
                    subRoot.compSet(subRoot.right(), null);
                else
                    // prTrash.add(subRoot.right());
                    subRoot.setRight(null);
            }

            // if there only one KVPair in the Leaf, then the Leaf is deleted
            // and the size of the tree is 0
            else
            {
                // prTrash.add(subRoot.left());
                // lfTrash.add((Leaf)subRoot);
                root = null;
            }
        }
        else if (subRoot instanceof Internal)
        {
            // if root is not at a level right above the Leaves, then the method
            // is recursively called until root is at that level
            if ((inRoot = (Internal)subRoot).low() instanceof Internal)
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
                    // prTrash.add(loc.right());
                    loc.setRight(null);
                }
                else
                    // the helper method is called whenever the left KVPair of
                    // a Leaf needs to be deleted in order to deal with
                    // pointer reassignment and promotions
                    delUpdate(inRoot, loc.left());
            }
        }
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

                promote(root, parent, null, parent.low().left());
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

                promote(root, parent, null, parent.low().left());
            }

            // if both the Leaf child and the adjacent Leaf child are not full,
            // then if the highest Leaf child is not null, the current Leaf must
            // be deleted and pointers readjusted
            else if (parent.isFull())
            {
                if (((Leaf)parent.low()).previous() != null)
                    ((Leaf)parent.low()).previous().link((Leaf)parent.mid());

// prTrash.add(parent.low().left());
// lfTrash.add((Leaf)parent.low());

                parent.setLow(parent.mid());
                parent.setMid(parent.high());
                parent.setLeft(parent.mid().left());
                parent.setHigh(null);

                promote(root, parent, null, parent.low().left());
            }

            // if none of the above conditions can be met, then there are not
            // enough resources within this parent/Leaves structure to keep the
            // tree ordered and a transfer between parent siblings is required
            else
                transfer((Internal)root, parent, (Leaf)parent.low());
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

                    parent.compSet(parent.mid().left(), parent.high().left());
                }
                else
                {
                    ((Leaf)parent.low()).link((Leaf)parent.high());
                    // ((Leaf)parent.low()).link((Leaf)parent.high());
// prTrash.add(parent.mid().left());
// lfTrash.add((Leaf)parent.mid());

                    parent.setMid(parent.high());
                    parent.setLeft(parent.mid().left());
                    parent.setHigh(null);
                }
            }
            else
                transfer((Internal)root, parent, (Leaf)parent.mid());
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

// prTrash.add(parent.high().left());
// lfTrash.add((Leaf)parent.high());
                parent.setHigh(null);
            }
        }
    }


    private void promote(
        TreeNode subRoot,
        TreeNode original,
        TreeNode split,
        KVPair promo)
    {
        Internal inRoot;

        // if root is orig, that means the method has promoted up to where it
        // needs a new root, which would be the parent of orig and split;
        // however, if split is null, then the original promo KVPair resides in
        // the bottom left of the tree and is trying to promote itself, but
        // should not be allowed to
        if (subRoot == original)
        {
            if (split != null)
                root =
                    ((!inTrash.isEmpty()) ? inTrash.remove(inTrash.size() - 1)
                        .set(promo, original, split) : new Internal(
                        promo,
                        original,
                        split));
        }

        // if the promotion is less than the left KVPair of root, then the
        // method checks root.low()
        else if (promo.compareTo((inRoot = ((Internal)subRoot)).left()) == -1)
        {
            if (inRoot.low() == original)
            {
                // if split is null, then there needs to be a promotion due
                // to deletion, but because original is the low pointer, the
                // promotion must continue up the tree
                if (split == null)
                    promote(root, inRoot, null, promo);

                // if the root is not full, then pointers are adjusted to
                // accommodate the insertion of the split Node
                else if (!inRoot.isFull())
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
                    Internal parent =
                        ((!inTrash.isEmpty()) ? inTrash.remove(
                            inTrash.size() - 1).set(
                            inRoot.high().left(),
                            inRoot.mid(),
                            inRoot.high()) : new Internal(
                            inRoot.high().left(),
                            inRoot.mid(),
                            inRoot.high()));

                    KVPair newPromo = inRoot.left();
                    inRoot.setMid(split);

                    inRoot.compSet(promo, null);
                    inRoot.setHigh(null);

                    promote(root, inRoot, parent, newPromo);
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
        else if (promo.compareTo(inRoot.left()) >= 0
            && (!inRoot.isFull() || promo.compareTo(inRoot.right()) == -1))
        {
            if (inRoot.mid() == original)
            {
                // if split is null, then there needs to be a promotion due
                // to deletion
                if (split == null)
                    inRoot.setLeft(promo);

                // if root is not full, then pointers are adjusted to
                // accommodate the insertion of the split Node

                else if (!inRoot.isFull())
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
                    Internal parent =
                        ((!inTrash.isEmpty()) ? inTrash.remove(
                            inTrash.size() - 1).set(
                            inRoot.high().left(),
                            split,
                            inRoot.high()) : new Internal(
                            inRoot.high().left(),
                            split,
                            inRoot.high()));

                    KVPair newPromo = split.left();
                    inRoot.setHigh(null);

                    promote(root, inRoot, parent, newPromo);
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
                    Internal parent =
                        ((!inTrash.isEmpty()) ? inTrash.remove(
                            inTrash.size() - 1)
                            .set(promo, inRoot.high(), split) : new Internal(
                            promo,
                            inRoot.high(),
                            split));

                    KVPair newPromo = inRoot.right();
                    inRoot.setHigh(null);

                    promote(root, inRoot, parent, newPromo);
                }
            }

            // if root.high() is not orig, then the method continues to search
            // in that direction
            else
                promote(inRoot.high(), original, split, promo);
        }
    }


    private void transfer(Internal subRoot, Internal parent, Leaf rem)
    {
        Internal adj;
        if (root == parent)
            root = shrink(parent, rem.left());
        else if (parent.left().compareTo(subRoot.left()) == -1)
        {
            if (subRoot.low() == parent)
            {
                if ((adj = (Internal)subRoot.mid()).isFull())
                {
// prTrash.add((parent.low() == rem)
// ? parent.low().left() : parent.mid().left());
// lfTrash.add((Leaf)((parent.low() == rem)
// ? parent.low() : parent.mid()));

                    if (parent.low() == rem)
                    {
                        if (((Leaf)parent.low()).previous() != null)
                            ((Leaf)parent.low()).previous().link(
                                (Leaf)parent.mid());
                        parent.setLow(parent.mid());
                        promote(root, parent, null, parent.low().left());
                    }
                    else
                    {
                        ((Leaf)parent.low()).link((Leaf)adj.low());
                    }
                    leftTransfer(parent, adj);
                    subRoot.setLeft(((Internal)subRoot.mid()).low().left());
                }
                else if (subRoot.isFull())
                {
                    if (parent.low() == rem)
                    {
                        if (((Leaf)parent.low()).previous() != null)
                            ((Leaf)parent.low()).previous().link(
                                (Leaf)parent.mid());
                    }
                    else
                        ((Leaf)parent.low()).link((Leaf)adj.low());

                    adj.setHigh(adj.mid());
                    adj.setRight(adj.high().left());

                    adj.setMid(adj.low());
                    adj.setLeft(adj.mid().left());

                    adj.setLow((parent.low() == rem) ? parent.mid() : parent
                        .low());

// prTrash.add((parent.low() == rem)
// ? parent.low().left() : parent.mid().left());
// lfTrash.add((Leaf)((parent.low() == rem)
// ? parent.low() : parent.mid()));
// inTrash.add(parent);

                    subRoot.setLow(subRoot.mid());
                    subRoot.setMid(subRoot.high());
                    subRoot.setHigh(null);
                    subRoot.compSet(
                        ((Internal)subRoot.mid()).low().left(),
                        null);

                    promote(
                        root,
                        subRoot.low(),
                        null,
                        ((Internal)subRoot.low()).low().left());
                }
                else
                    shrink((Internal)this.root, rem.left());
            }
            else
                transfer((Internal)subRoot.low(), parent, rem);
        }
        else if (parent.left().compareTo(subRoot.left()) >= 0
            && (!subRoot.isFull() || parent.left().compareTo(subRoot.right()) == -1))
        {
            if (subRoot.mid() == parent)
            {
                if ((adj = (Internal)subRoot.low()).isFull())
                {
                    ((Leaf)((parent.mid() == rem) ? (parent.low()) : adj.high()))
                        .link((Leaf)((parent.mid() == rem) ? ((Leaf)parent
                            .mid()).next() : parent.mid()));

// prTrash.add((parent.mid() == rem)
// ? parent.mid().left() : parent.low().left());
// lfTrash.add((Leaf)((parent.mid() == rem)
// ? parent.mid() : parent.low()));

                    if (parent.mid() == rem)
                    {
                        if (((Leaf)parent.mid()).next() != null)
                            ((Leaf)parent.low()).link(((Leaf)parent.mid())
                                .next());
                        parent.setMid(parent.low());
                        parent.setLeft(parent.mid().left());
                    }
                    else
                    {
                        ((Leaf)adj.high()).link((Leaf)parent.mid());
                    }
                    rightTransfer(parent, adj);
                    subRoot.setLeft(((Internal)subRoot.mid()).low().left());
                }
                else if (subRoot.high() != null
                    && (adj = (Internal)subRoot.high()).isFull())
                {
// prTrash.add((parent.low() == rem)
// ? parent.low().left() : parent.mid().left());
// lfTrash.add((Leaf)((parent.low() == rem)
// ? parent.low() : parent.mid()));

                    if (parent.low() == rem)
                    {
                        if (((Leaf)parent.low()).previous() != null)
                            ((Leaf)parent.low()).previous().link(
                                (Leaf)parent.mid());
                        parent.setLow(parent.mid());
                        subRoot.setLeft(parent.low().left());
                    }
                    else
                        ((Leaf)parent.low()).link((Leaf)adj.low());
                    leftTransfer(parent, adj);
                    subRoot.setRight(((Internal)subRoot.high()).low().left());
                }
                else if (subRoot.isFull())
                {
                    if (parent.low() == rem)
                    {
                        if (((Leaf)parent.low()).previous() != null)
                            ((Leaf)parent.low()).previous().link(
                                (Leaf)parent.mid());
                    }
                    else
                    {
                        if (((Leaf)parent.mid()).next() != null)
                            ((Leaf)parent.low()).link(((Leaf)parent.mid())
                                .next());
                    }

                    (adj = (Internal)subRoot.low())
                        .setHigh((parent.low() == rem) ? parent.mid() : parent
                            .low());
                    adj.setRight(adj.high().left());

// prTrash.add((parent.low() == rem)
// ? parent.low().left() : parent.mid().left());
// lfTrash.add((Leaf)((parent.low() == rem)
// ? parent.low() : parent.mid()));
// inTrash.add(parent);

                    subRoot.setMid(subRoot.high());
                    subRoot.setLeft(((Internal)subRoot.mid()).low().left());

                    subRoot.setHigh(null);
                }
                else
                    shrink((Internal)this.root, rem.left());
            }
            else
                transfer((Internal)subRoot.mid(), parent, rem);
        }
        else if (parent.left().compareTo(subRoot.right()) >= 0)
        {
            if (subRoot.high() == parent)
            {
                if ((adj = (Internal)subRoot.mid()).isFull())
                {
                    ((Leaf)((parent.mid() == rem)
                        ? parent.low()
                        : ((Leaf)parent.low()).previous())).link((Leaf)((parent
                        .mid() == rem) ? ((Leaf)parent.mid()).next() : parent
                        .mid()));

// prTrash.add((parent.mid() == rem)
// ? parent.mid().left() : parent.low().left());
// lfTrash.add((Leaf)((parent.mid() == rem)
// ? parent.mid() : parent.low()));

                    if (parent.mid() == rem)
                    {
                        if (((Leaf)parent.mid()).next() != null)
                            ((Leaf)parent.low()).link(((Leaf)parent.mid())
                                .next());

                        parent.setMid(parent.low());
                        parent.setLeft(parent.mid().left());
                    }
                    else
                    {
                        if (((Leaf)parent.low()).previous() != null)
                            ((Leaf)parent.low()).previous().link(
                                (Leaf)parent.mid());
                    }
                    rightTransfer(parent, adj);
                    subRoot.setRight(((Internal)subRoot.high()).low().left());
                }
                else
                {
                    if (parent.low() == rem)
                    {
                        if (((Leaf)parent.low()).previous() != null)
                            ((Leaf)parent.low()).previous().link(
                                (Leaf)parent.mid());
                    }
                    else
                    {
                        if (((Leaf)parent.mid()).next() != null)
                            ((Leaf)parent.low()).link(((Leaf)parent.mid())
                                .next());
                    }

                    adj.setHigh((parent.low() == rem) ? parent.mid() : parent
                        .low());
                    adj.setRight(adj.high().left());

// prTrash.add((parent.low() == rem)
// ? parent.low().left() : parent.mid().left());
// lfTrash.add((Leaf)((parent.low() == rem)
// ? parent.low() : parent.mid()));
// inTrash.add(parent);

                    subRoot.setHigh(null);
                }
            }
            else
                transfer((Internal)subRoot.high(), parent, rem);
        }
    }


    private void leftTransfer(Internal parent, Internal rightAdj)
    {
        parent.setMid(rightAdj.low());
        parent.setLeft(parent.mid().left());

        rightAdj.setLow(rightAdj.mid());
        rightAdj.setMid(rightAdj.high());
        rightAdj.setLeft(rightAdj.mid().left());
        rightAdj.setHigh(null);
    }


    private void rightTransfer(Internal parent, Internal leftAdj)
    {
        parent.setLow(leftAdj.high());
        leftAdj.setHigh(null);
    }


    private TreeNode shrink(Internal subRoot, KVPair rem)
    {
        if (rem.compareTo(subRoot.left()) == -1)
        {
            if (subRoot.low() instanceof Leaf)
            {
// prTrash.add(rem);
// lfTrash.add((Leaf)subRoot.low());
                if (((Leaf)subRoot.low()).previous() != null)
                    ((Leaf)subRoot.low()).previous().link((Leaf)subRoot.mid());
                return subRoot.mid();
            }
            subRoot.compSet(subRoot.left(), subRoot.mid().left());
            subRoot.setLow(shrink((Internal)subRoot.low(), rem));
            subRoot.setHigh(((Internal)subRoot.mid()).mid());
            subRoot.setMid(((Internal)subRoot.mid()).low());
// inTrash.add(replace);
        }
        else if (rem.compareTo(subRoot.left()) >= 0
            && (!subRoot.isFull() || rem.compareTo(subRoot.right()) == -1))
        {
            if (subRoot.mid() instanceof Leaf)
            {
// prTrash.add(rem);
// lfTrash.add((Leaf)subRoot.mid());
                if (((Leaf)subRoot.mid()).next() != null)
                    ((Leaf)subRoot.low()).link(((Leaf)subRoot.mid()).next());
                return subRoot.low();
            }
            subRoot.compSet(subRoot.low().left(), subRoot.left());
            subRoot.setHigh(shrink((Internal)subRoot.mid(), rem));
            subRoot.setMid(((Internal)subRoot.low()).mid());
            subRoot.setLow(((Internal)subRoot.low()).low());

            if (subRoot.low() instanceof Leaf)
                subRoot.compSet(subRoot.left(), subRoot.high().left());
// inTrash.add(replace);
        }
        else if (rem.compareTo(subRoot.right()) >= 0)
        {
            subRoot.setHigh(shrink((Internal)subRoot.high(), rem));
// inTrash.add(replace);
        }
        return subRoot;
    }


    private String traverse(TreeNode subRoot, int depth)
    {
        String traversal = "";
        if (subRoot != null)
        {
            // appends double-spaces for the TreeNode level (depth)
            for (int pos = 0; pos < depth; pos++)
                traversal += "  ";

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
}

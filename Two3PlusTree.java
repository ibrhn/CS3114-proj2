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


    public Two3PlusTree()
    {
        size = 0;
        prTrash = new DoublyLinkedList<KVPair>();
        inTrash = new DoublyLinkedList<Internal>();
        lfTrash = new DoublyLinkedList<Leaf>();
    }


    public boolean insert(Handle key, Handle value)
    {
        return recInsert(this.root, key, value);
    }


    public String print()
    {
        return "Printing 2-3 tree:" + traverse(this.root, 0);
    }


    // ----------------------------------------------------------
    /**
     * @return the root TreeNode
     */
    public TreeNode root()
    {
        return root;
    }


    // ----------------------------------------------------------
    /**
     * @return the size of the Two3PlusTree (number of TreeNodes in the tree)
     */
    public int size()
    {
        return size;
    }


    private boolean recInsert(TreeNode root, Handle key, Handle value)
    {
        KVPair pair =
            (prTrash.size() != 0) ? prTrash.get().set(key, value)
                : new KVPair(key, value);

            if (size == 0)
                this.root = new Leaf(pair);
            else if (root.containsEqual(pair))
                return false;
            else
            {
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
                    if (root.right() == null)
                        root.compSet(root.left(), pair);
                    else
                    {
                        KVPair move = (root.left().compareTo(pair) > 0)
                            ? root.left() : pair;

                            Leaf split = (Leaf)((lfTrash.size() != 0)
                                ? lfTrash.get().compSet(move, root.right())
                                    : new Leaf(move, root.right()));

                            root.compSet(((root.left().compareTo(pair) > 0)
                                ? pair : root.left()), null);

                            split.setNext(((Leaf)root).next());
                            ((Leaf)root).setNext(split);

                            promote(this.root, root, split, split.left());
                    }
                }
            }
            size++;
            return true;
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
                    inRoot.setRight(inRoot.high().left());

                    inRoot.setMid(split);
                    inRoot.setLeft(promo);
                }
                else
                {
                    Internal parent = ((inTrash.size() != 0)
                        ? inTrash.get().set(inRoot.high().left(), null,
                            inRoot.mid(), inRoot.high())
                                : new Internal(inRoot.high().left(),
                                    inRoot.mid(), inRoot.high()));

                    KVPair newPromo = inRoot.right();
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
                    inRoot.setRight(promo);
                }
                else
                {
                    Internal parent = ((inTrash.size() != 0)
                        ? inTrash.get().set(inRoot.high().left(), null,
                            split, inRoot.high())
                               : new Internal(inRoot.high().left(),
                                   split, inRoot.high()));

                    KVPair newPromo = inRoot.right();
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
}

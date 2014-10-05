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
    private DoublyLinkedList<KVPair>   pairTrash;
    private DoublyLinkedList<Internal> InternalTrash;
    private DoublyLinkedList<Leaf>     LeafTrash;


    public Two3PlusTree()
    {
        size = 0;
        pairTrash = new DoublyLinkedList<KVPair>();
        InternalTrash = new DoublyLinkedList<Internal>();
        LeafTrash = new DoublyLinkedList<Leaf>();
    }


    public boolean insert(TreeNode root, Handle left, Handle right)
    {
        KVPair pair =
            (pairTrash.size() != 0)
                ? pairTrash.get().set(left, right)
                : new KVPair(left, right);

        if (size == 0)
            root.setLeft(pair);
        else
        {
            if (root instanceof Internal)
            {
                if (root.contains(pair))
                    return false;
                else if (pair.compareTo(((Internal)root).left()) == -1)
                    insert(((Internal)root).low(), left, right);
                else if (pair.compareTo(((Internal)root).left()) > -1)
                {
                    if (((Internal)root).right() == null
                        || pair.compareTo(((Internal)root).right()) == -1)
                        insert(((Internal)root).mid(), left, right);
                    else
                        insert(((Internal)root).high(), left, right);
                }
            }
            else if (root instanceof Leaf)
            {
                if ((root).right() == null)
                    root.set(root.left(), pair);
                else
                {
                    KVPair move = (root.left().compareTo(pair) > 0)
                        ? root.left() : pair;

                    Leaf split = ((LeafTrash.size() != 0)
                            ? (Leaf)LeafTrash.get().set(move, root.right())
                                : new Leaf(move, root.right()));

                    root.set(((root.left().compareTo(pair) > 0)
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


    // ----------------------------------------------------------
    /**
     * @return the root TreeNode
     */
    public TreeNode root()
    {
        return root;
    }


    private void promote(TreeNode root, TreeNode orig, TreeNode split,
        KVPair promo)
    {
        if (root == orig)
            this.root = ((InternalTrash.size() != 0)
                ? InternalTrash.get().set(promo, null, orig, split)
                    : new Internal(promo, orig, split));
        else if (promo.compareTo(((Internal)root).left()) == -1)
        {
            if (((Internal)root).low() == orig)
            {
                if (root.right() == null)
                {
                    ((Internal)root).setHigh(((Internal)root).mid());
                    root.setRight(((Internal)root).high().left());

                    ((Internal)root).setMid(split);
                    root.setLeft(((Internal)root).mid().left());
                }
                else
                {
                    Internal parent = ((InternalTrash.size() != 0)
                        ? InternalTrash.get().set(root.right(), null,
                            ((Internal)root).mid(), ((Internal)root).high())
                                : new Internal(root.right(),
                                    ((Internal)root).mid(),
                                    ((Internal)root).high()));

                    ((Internal)root).setLeft(promo);

                    KVPair newPromo = root.right();
                    ((Internal)root).setRight(null);

                    promote(this.root, root, parent, newPromo);
                }
            }
            else
                promote(((Internal)root).low(), orig, split, promo);
        }
        else if (promo.compareTo(((Internal)root).left()) > -1)
        {
            if (((Internal)root).mid() == orig)
            {
                if (root.right() == null)
                {
                    ((Internal)root).setHigh(split);
                    root.setRight(promo);

                    ((Internal)root).setMid(split);
                }
                else
                {
                    Internal parent = ((InternalTrash.size() != 0)
                        ? InternalTrash.get().set(root.right(), null,
                            ((Internal)root).mid(), ((Internal)root).high())
                            : new Internal(root.right(),
                                ((Internal)root).mid(),
                                ((Internal)root).high()));

                    ((Internal)root).setLeft(promo);

                    KVPair newPromo = root.right();
                    ((Internal)root).setRight(null);

                    promote(this.root, root, parent, newPromo);
                }
            }
            else
                promote(((Internal)root).low(), orig, split, promo);
        }
    }
}

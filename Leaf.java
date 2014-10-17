// -------------------------------------------------------------------------
/**
 *  The Leaf class represents a Leaf Node within a 2-3+ tree. It
 *  contains two KVPairs (left and right) and a pointer to a Leaf TreeNode
 *  (next).
 *
 *  @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 *  @version Oct 4, 2014
 */
public class Leaf
    extends TreeNode
{
    private Leaf next;


    // -------------------------------------------------------------------------
    /**
     * Initializes the Leaf TreeNode by setting the left KVPair.
     * @param left left KVPair
     */
    public Leaf(KVPair left)
    {
        super(left);
    }


    // -------------------------------------------------------------------------
    /**
     * Initializes the Leaf TreeNode by setting the left and right KVPairs.
     * @param left left KVPair
     * @param right right KVPair
     */
    public Leaf(KVPair left, KVPair right)
    {
        super(left, right);
    }


    // -------------------------------------------------------------------------
    /**
     * @return the next Leaf
     */
    public Leaf next()
    {
        return next;
    }


    // -------------------------------------------------------------------------
    /**
     * Sets the next Leaf.
     * @param nxt the new next Leaf
     * @return next
     */
    public Leaf setNext(Leaf nxt)
    {
        this.next = nxt;
        return nxt;
    }


    // -------------------------------------------------------------------------
    @Override
    public Leaf split()
    {
        Leaf split = new Leaf(ovr(), right());

        setOvr(null);
        setRight(null);

        split.setNext(next);
        return setNext(split);
    }

    // -------------------------------------------------------------------------
    @Override
    public boolean underflow()
    {
        return (left() == null) && (right() == null);
    }
}

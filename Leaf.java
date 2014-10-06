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
     * @param next the new next Leaf
     */
    public void setNext(Leaf next)
    {
        this.next = next;
    }
}

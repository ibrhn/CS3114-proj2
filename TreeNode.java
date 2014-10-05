// -------------------------------------------------------------------------
/**
 *  The TreeNode class contains two KVPairs (left and right).
 *
 *  @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 *  @version Oct 3, 2014
 */
public class TreeNode
{
    private KVPair left;
    private KVPair right;


    // ----------------------------------------------------------
    /**
     * Initializes a TreeNode by setting the left KVPair.
     * @param left left KVPair
     */
    public TreeNode(KVPair left)
    {
        setLeft(left);
    }


    // ----------------------------------------------------------
    /**
     * Initializes a TreeNode by setting the left and right KVPairs.
     * @param left left KVPair
     * @param right right KVPair
     */
    public TreeNode(KVPair left, KVPair right)
    {
        set(left, right);
    }


    // ----------------------------------------------------------
    /**
     * @return the left KVPair
     */
    public KVPair left()
    {
        return left;
    }


    // ----------------------------------------------------------
    /**
     * @return the right KVPair
     */
    public KVPair right()
    {
        return right;
    }


    /**
     * Setter for the entire TreeNode.
     * @param left the new left KVPair
     * @param right the new right KVPair
     * @return this
     */
    public TreeNode set(KVPair left, KVPair right)
    {
        setLeft((left.compareTo(right) <= 0) ? left : right);
        setRight((right.compareTo(left) >= 0) ? right : left);
        return this;
    }


    /**
     * Setting for the left KVPair.
     * @param left the new left KVPair
     */
    public void setLeft(KVPair left)
    {
        this.left.set(left);
    }


    /**
     * Setting for the right KVPair.
     * @param right the new right KVPair
     */
    public void setRight(KVPair right)
    {
        this.right.set(right);
    }


    // ----------------------------------------------------------
    /**
     * @param pair KVPair to compare to
     * @return if this contains the same pair
     */
    public boolean contains(KVPair pair)
    {
        return (left == pair) || (right == pair);
    }
}

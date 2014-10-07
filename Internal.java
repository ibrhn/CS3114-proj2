// -------------------------------------------------------------------------
/**
 *  The Internal class represents an internal Node within a 2-3+ tree. It
 *  contains two KVPairs (left and right) and three pointers to TreeNodes
 *  (low, mid, high).
 *
 *  @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 *  @version Oct 4, 2014
 */
public class Internal
    extends TreeNode
{
    private TreeNode low;
    private TreeNode mid;
    private TreeNode high;


    // -------------------------------------------------------------------------
    /**
     * Initializes the Internal TreeNode by setting the left KVPair.
     * @param left left KVPair
     * @param low low TreeNode
     * @param mid middle TreeNode
     */
    public Internal(KVPair left, TreeNode low, TreeNode mid)
    {
        super(left);
        setLow(low);
        setMid(mid);
    }


    // -------------------------------------------------------------------------
    /**
     * @return the low TreeNode
     */
    public TreeNode low()
    {
        return low;
    }


    // -------------------------------------------------------------------------
    /**
     * @return the mid TreeNode
     */
    public TreeNode mid()
    {
        return mid;
    }


    // -------------------------------------------------------------------------
    /**
     * @return the high TreeNode
     */
    public TreeNode high()
    {
        return high;
    }


    // ----------------------------------------------------------
    /**
     * Overloads the set method in the TreeNode class with pointers to low and
     * mid, and returns this.
     * @param left new left KVPair
     * @param right new right KVPair
     * @param low new low TreeNode
     * @param mid new mid TreeNode
     * @return this
     */
    public Internal set(KVPair left, KVPair right, TreeNode low, TreeNode mid)
    {
        setLeft(left);
        setRight(right);

        setLow(low);
        setMid(mid);

        return this;
    }


    // -------------------------------------------------------------------------
    /**
     * Sets the low TreeNode.
     * @param low new low TreeNode
     */
    public void setLow(TreeNode low)
    {
        this.low = low;
    }


    // -------------------------------------------------------------------------
    /**
     * Sets the mid TreeNode.
     * @param mid new mid TreeNode
     */
    public void setMid(TreeNode mid)
    {
        this.mid = mid;
    }


    // -------------------------------------------------------------------------
    /**
     * Sets the high TreeNode.
     * @param high new high TreeNode
     */
    public void setHigh(TreeNode high)
    {
        this.high = high;
    }
}

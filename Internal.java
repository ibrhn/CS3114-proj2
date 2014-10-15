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
    private TreeNode ovrPtr;
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
     * @return the overflowing middle TreeNode
     */
    public TreeNode ovrPtr()
    {
        return ovrPtr;
    }


    // ----------------------------------------------------------
    /**
     * Overloads the set method in the TreeNode class with pointers to low and
     * mid, and returns this.
     * @param left new left KVPair
     * @param low new low TreeNode
     * @param mid new mid TreeNode
     * @return this
     */
    public Internal set(KVPair left, TreeNode low, TreeNode mid)
    {
        setLeft(left);
        setLow(low);
        setMid(mid);
        setOvrPtr(null);
        setHigh(null);
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
        if (high == null)
            setRight(null);
        this.high = high;
    }


    // ----------------------------------------------------------
    /**
     * Sets the overflowing TreeNode.
     * @param ovr new overflowing TreeNode
     */
    public void setOvrPtr(TreeNode ovr)
    {
        if (ovr == null)
            setOvr(null);
        this.ovrPtr = ovr;
    }


    // -------------------------------------------------------------------------
    @Override
    public Internal split()
    {
        Internal split = new Internal(right(), ovrPtr, high);
        set(left(), low, mid);
        return split;
    }
}

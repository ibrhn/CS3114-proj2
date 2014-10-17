// -------------------------------------------------------------------------
/**
 * The abstract TreeNode class contains two KVPairs (left and right).
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Oct 3, 2014
 */
public abstract class TreeNode
{
    private KVPair left;
    private KVPair right;
    private KVPair ovrMid;


    // ----------------------------------------------------------
    /**
     * Splits the TreeNode (sorts both) and returns the split.
     *
     * @return the split TreeNode
     */
    abstract TreeNode split();


    // ----------------------------------------------------------
    /**
     * @return if there is overflow
     */
    abstract boolean underflow();


    // ----------------------------------------------------------
    /**
     * Initializes a TreeNode by setting the left KVPair.
     *
     * @param left
     *            left KVPair
     */
    public TreeNode(KVPair left)
    {
        setLeft(left);
    }


    // ----------------------------------------------------------
    /**
     * Initializes a TreeNode by setting the left and right KVPairs.
     *
     * @param left
     *            left KVPair
     * @param right
     *            right KVPair
     */
    public TreeNode(KVPair left, KVPair right)
    {
        this(left);
        insert(right);
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


    // ----------------------------------------------------------
    /**
     * @return the overflowing middle KVPair to be promoted
     */
    public KVPair ovr()
    {
        return ovrMid;
    }


    /**
     * Setter for the entire TreeNode.
     *
     * @param ins
     *            KVPair to be inserted
     */
    public void insert(KVPair ins)
    {
        if (ins != null && !containsEqual(ins))
        {
            if (right == null)
            {
                setRight((ins.compareTo(left) >= 0) ? ins : left);
                setLeft((ins.compareTo(left) >= 0) ? left : ins);
            }
            else if (ins.compareTo(left) > 0)
            {
                if (ins.compareTo(right) < 0)
                {
                    setOvr(ins);
                }
                else
                {
                    setOvr(right);
                    setRight(ins);
                }
            }
            else
            {
                setOvr(left);
                setLeft(ins);
            }
        }
    }


    /**
     * Setting for the left KVPair.
     *
     * @param left
     *            the new left KVPair
     */
    public void setLeft(KVPair left)
    {
        this.left = left;
    }


    /**
     * Setting for the right KVPair.
     *
     * @param right
     *            the new right KVPair
     */
    public void setRight(KVPair right)
    {
        this.right = right;
    }


    // ----------------------------------------------------------
    /**
     * Setter for the overflowing middle KVPair.
     *
     * @param mid
     *            the overflowing KVPair
     */
    public void setOvr(KVPair mid)
    {
        this.ovrMid = mid;
    }


    // ----------------------------------------------------------
    /**
     * @return if this is full
     */
    public boolean isFull()
    {
        return right != null;
    }


    // ----------------------------------------------------------
    /**
     * removes the kv pair
     *
     * @param rem
     *            the value to remove
     */
    public void remove(KVPair rem)
    {
        if (rem != null)
        {
            if (left.equals(rem))
            {
                setLeft(null);
            }
            else if (right.equals(rem))
            {
                setRight(null);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * @return if there is overflow
     */
    public boolean overflow()
    {
        return ovrMid != null;
    }


    // ----------------------------------------------------------
    /**
     * @param pair
     *            KVPair to compare to
     * @return if this contains the same pair
     */
    public boolean contains(KVPair pair)
    {
        return (left == pair) || (right == pair);
    }


    // ----------------------------------------------------------
    /**
     * @param pair
     *            KVPair to compare to
     * @return if this contains something equivalent to pair, but not pair
     */
    public boolean containsEqual(KVPair pair)
    {
        return ((left != null) ? left.equals(pair) : (pair == null))
            || ((right != null) ? right.equals(pair) : (pair == null));
    }


    // ----------------------------------------------------------
    @Override
    public String toString()
    {
        return ((left != null) ? left : "")
            + ((right != null) ? " " + right : "");
    }
}

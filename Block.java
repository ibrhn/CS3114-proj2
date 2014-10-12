// ----------------------------------------------------------
/**
 * A Block stores two integers. The first represents a starting position in
 * one-dimension, and the second represents its length.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 14, 2014
 */
public class Block
{
    private int pos;
    private int len;


    // ----------------------------------------------------------
    /**
     * Initializes the Block with a position and length.
     *
     * @param pos
     *            position
     * @param len
     *            length
     */
    public Block(int pos, int len)
    {
        this.pos = pos;
        this.len = len;
    }


    // ----------------------------------------------------------
    /**
     * @return start position of the Block
     */
    public int getPos()
    {
        return pos;
    }


    // ----------------------------------------------------------
    /**
     * @return length of the Block
     */
    public int getLen()
    {
        return len;
    }


    // ----------------------------------------------------------
    /**
     * Sets Block position and length to pos and len respectively.
     *
     * @param pos
     *            new position
     * @param len
     *            new length
     * @return this
     */
    public Block set(int pos, int len)
    {
        this.pos = pos;
        this.len = len;
        return this;
    }


    // ----------------------------------------------------------
    /**
     * Sets Block position to pos.
     *
     * @param pos
     *            new position
     */
    public void setPos(int pos)
    {
        this.pos = pos;
    }


    // ----------------------------------------------------------
    /**
     * Sets Block length to len.
     *
     * @param len
     *            new length
     */
    public void setLen(int len)
    {
        this.len = len;
    }


    // ----------------------------------------------------------
    /**
     * @param block
     *            Block to be compared to this
     * @return if two Blocks are equal
     */
    public boolean equals(Object block)
    {
        return (block instanceof Block) &&
            this.pos == ((Block) block).getPos() &&
            this.len == ((Block) block).getLen();
    }


    // ----------------------------------------------------------
    /**
     * @return a String representation of the Block
     */
    public String toString()
    {
        return "(" + pos + "," + len + ")";
    }
}

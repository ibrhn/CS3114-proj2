// ----------------------------------------------------------
/**
 * A Handle stores a single integer.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 14, 2014
 */
public class Handle implements Comparable<Handle>
{
    private int num;


    // ----------------------------------------------------------
    /**
     * Initializes the Handle with a number.
     *
     * @param num
     *            number to be stored in the Handle
     */
    public Handle(int num)
    {
        this.num = num;
    }


    // ----------------------------------------------------------
    /**
     * @return num
     */
    public int get()
    {
        return num;
    }


    // ----------------------------------------------------------
    /**
     * Sets num to new value.
     *
     * @param num
     *            new value to set equal to this.num
     */
    public void set(int num)
    {
        this.num = num;
    }


    // ----------------------------------------------------------
    @Override
    public int compareTo(Handle h)
    {
        return Integer.signum(num - h.get());
    }


    // ----------------------------------------------------------
    @Override
    public boolean equals(Object obj)
    {
        return (obj != null) && (obj instanceof Handle) &&
            compareTo((Handle)obj) == 0;
    }


    // ----------------------------------------------------------
    @Override
    public String toString()
    {
        return String.valueOf(num);
    }
}

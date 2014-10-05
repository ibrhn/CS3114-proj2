// ----------------------------------------------------------
/**
 * KVPair class definition: Pretty specific for Project 2.
 *
 * @author CS3114 Instructor and TAs
 * @version 9/22/2014
 */
public class KVPair
    implements Comparable<KVPair>
{
    private Handle key;
    private Handle value;


    // ----------------------------------------------------------
    /**
     * Constructor for the KVPair.
     *
     * @param key
     *            the key (first Handle)
     * @param value
     *            the value (second Handle)
     */
    public KVPair(Handle key, Handle value)
    {
        this.key = key;
        this.value = value;
    }


    // ----------------------------------------------------------
    /**
     * The magic that lets us compare two KVPairs. KVPairs are all that this
     * knows how to compare against First compare the key field. If they are
     * identical, then break the tie with the value field.
     *
     * @return the usual for a comparable (+, 0, -)
     * @param pair
     *            the KVPair to compare "this" against
     */
    public int compareTo(KVPair pair)
    {
        int compKey = key.compareTo(pair.key());
        return ((compKey == 0) ? value.compareTo(pair.value()) : compKey);
    }


    // ----------------------------------------------------------
    /**
     * Getter for "key" Handle.
     *
     * @return the key
     */
    public Handle key()
    {
        return key;
    }


    // ----------------------------------------------------------
    /**
     * Getter for "value" Handle.
     *
     * @return the value
     */
    public Handle value()
    {
        return value;
    }


    // ----------------------------------------------------------
    /**
     * Setter for "key" Handle.
     *
     * @param key
     *            new "key" Handle
     */
    public void setKey(Handle key)
    {
        this.key = key;
    }


    // ----------------------------------------------------------
    /**
     * Setter for "value" Handle.
     *
     * @param value
     *            new "value" Handle
     */
    public void setValue(Handle value)
    {
        this.value = value;
    }


    // ----------------------------------------------------------
    /**
     * Setter for KVPair.
     * @param pair new KVPair
     * @return this
     */
    public KVPair set(KVPair pair)
    {
        setKey(pair.key());
        setValue(pair.value());
        return this;
    }


    // ----------------------------------------------------------
    /**
     * Setter for KVPair for both "key" and "value".
     * @param key new "key" Handle
     * @param value new "value" Handle
     * @return this
     */
    public KVPair set(Handle key, Handle value)
    {
        setKey(key);
        setValue(value);
        return this;
    }
}

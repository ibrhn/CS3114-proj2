// -------------------------------------------------------------------------
/**
 * A "node" represents a single element in a sequence. Think of it like a link
 * in a chain -- the node contains an element of data and a reference to the
 * next element in the sequence. This node is "doubly linked" -- it has
 * references to the next node in the chain and the previous node in the chain.
 * The join and split methods manage both connections simultaneously to ensure
 * that the consistency of links in the chain is preserved at all times.
 *
 * @param <T>
 *            the element type stored in the Node
 * @author Tony Allevato
 * @version Oct 10, 2012
 */
public class DoublyLinkedNode<T>
{
    // ~ Fields ................................................................

    // The data element stored in the node.
    private T       data;

    // The next node in the sequence.
    private DoublyLinkedNode<T> next;

    // The previous node in the sequence.
    private DoublyLinkedNode<T> previous;


    // ~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new Node with the specified data.
     *
     * @param data
     *            the data for the node
     */
    public DoublyLinkedNode(T data)
    {
        this.data = data;
    }


    // ~ Public methods ........................................................

    // ----------------------------------------------------------
    /**
     * Gets the data in the node.
     *
     * @return the data in the node
     */
    public T get()
    {
        return data;
    }


    // ----------------------------------------------------------
    /**
     * Sets the data in the node.
     *
     * @param newData
     *            the new data to put in the node
     */
    public void set(T newData)
    {
        data = newData;
    }


    // ----------------------------------------------------------
    /**
     * Gets the node that follows this one in the sequence.
     *
     * @return the node that follows this one in the sequence
     */
    public DoublyLinkedNode<T> next()
    {
        return next;
    }


    // ----------------------------------------------------------
    /**
     * Gets the node that precedes this one in the sequence.
     *
     * @return the node that precedes this one in the sequence
     */
    public DoublyLinkedNode<T> previous()
    {
        return previous;
    }


    // ----------------------------------------------------------
    /**
     * Joins this node to the specified node so that the one passed as a
     * parameter follows this node. In other words, writing {@code A.join(B)}
     * would create the linkage A <-> B. An exception will be thrown if this
     * node already has another node following it, or if {@code newNext} already
     * has another node preceding it. In this case, you must {@link #split()}
     * the nodes before you can join it to something else.
     *
     * @param newNext
     *            the node that should follow this one
     * @return this node, so that you can chain methods like
     *         {@code A.join(B.join(C))}
     * @throws IllegalStateException
     *             if there is already a node following this node or if there is
     *             already a node preceding {@code newNext}
     */
    public DoublyLinkedNode<T> join(DoublyLinkedNode<T> newNext)
    {
        if (this.next != null)
            throw new IllegalStateException("A node is already following "
                + "this one.");

        else if (newNext != null && newNext.previous != null)
            throw new IllegalStateException("A node is already preceding "
                + "the one passed to this method.");

        this.next = newNext;
        return (newNext != null) ? (newNext.previous = this) : this;
    }


    // ----------------------------------------------------------
    /**
     * Splits this node from its follower and then returns the follower.
     *
     * @return the node that used to follow this node before they were split
     */
    public DoublyLinkedNode<T> split()
    {
        DoublyLinkedNode<T> oldNext = this.next;

        if (next != null)
            next.previous = null;

        this.next = null;
        return oldNext;
    }

    // ----------------------------------------------------------
    /**
     * @return String representation of data
     */
    public String toString()
    {
        return data.toString();
    }
}

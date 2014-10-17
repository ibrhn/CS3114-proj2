import java.util.NoSuchElementException;

//----------------------------------------------------------
/**
 * A doubly-linked list is a linked data structure that consists of sequentially
 * linked Nodes. Each Node has a previous and next address that point to the
 * previous and next Nodes linked to that Node, respectively. The list also
 * contains two sentinel Nodes, head and tail, which contain null data and
 * signal the start and end points of the list. List traversal is maintained by
 * a "current" Node, which can access the its next and previous Nodes by setting
 * the "current" Node to one of the two. Data can only be accessed from the
 * "current" Node. There are also extra methods, such as isCurrent[First/Last]
 * and AddAt[Front/Rear] which make checking the state of the list a lot easier.
 *
 * @param <T>
 *            the element type stored in each Node
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 4, 2014
 */
public class DoublyLinkedList<T>
{
    private DoublyLinkedNode<T> head;
    private DoublyLinkedNode<T> tail;
    private DoublyLinkedNode<T> current;
    private int     size;


    // ----------------------------------------------------------
    /**
     * Initializes the list with a head and tail sentinel Nodes and a current
     * Node.
     */
    public DoublyLinkedList()
    {
        head = new DoublyLinkedNode<T>(null);
        tail = new DoublyLinkedNode<T>(null);
        current = new DoublyLinkedNode<T>(null);

        head.join(tail);
    }


    // ----------------------------------------------------------
    /**
     * Returns the data at the current location, throws an exception if the Node
     * is null.
     *
     * @return current Node data
     */
    public T get()
    {
        if (size != 0)
        {
            return current.get();
        }

        else
        {
            throw new NoSuchElementException("The list is empty.");
        }
    }


    // ----------------------------------------------------------
    /**
     * Adds a new Node with data right before current and sets the current to
     * the newly added Node.
     *
     * @param data
     *            the data being added to the list
     */
    public void add(T data)
    {
        DoublyLinkedNode<T> node = new DoublyLinkedNode<T>(data);

        if (size == 0)
        {
            current = node;
            head.join(current.join(head.split()));
        }
        else
        {
            DoublyLinkedNode<T> prev = current.previous();
            current = node;
            prev.join(current.join(prev.split()));
        }
        size++;
    }


    // ----------------------------------------------------------
    /**
     * Moves to the beginning of the linked list.
     */
    public void moveToFront()
    {
        if (size != 0)
        {
            current = head.next();
        }
    }


    // ----------------------------------------------------------
    /**
     * Moves to the end of the linked list.
     */
    public void moveToRear()
    {
        if (size != 0)
        {
            current = tail.previous();
        }
    }


    // ----------------------------------------------------------
    /**
     * Moves to the next node in the list.
     */
    public void next()
    {
        if (current.next() != tail)
        {
            current = current.next();
        }
    }


    // ----------------------------------------------------------
    /**
     * Moves to the previous node in the list.
     */
    public void previous()
    {
        if (current.previous() != head)
        {
            current = current.previous();
        }
    }


    // ----------------------------------------------------------
    /**
     * @return if current is the first Node in the list
     */
    public boolean isFirst()
    {
        return current.previous() == head;
    }


    // ----------------------------------------------------------
    /**
     * @return if current is the last Node in the list
     */
    public boolean isLast()
    {
        return current.next() == tail;
    }


    // ----------------------------------------------------------
    /**
     * Updates the data in the current Node.
     *
     * @param data
     *            data to update current with
     */
    public void set(T data)
    {
        current.set(data);
    }


    // ----------------------------------------------------------
    /**
     * Removes the current Node and sets the Node before it to the new current
     * Node. If the current Node, to be removed, is the first Node in the list,
     * then it is removed and the Node after is set to the current Node.
     * If the list is empty, it throws an exception.
     *
     * @return removed Node data
     */
    public DoublyLinkedNode<T> remove()
    {
        if (size == 0)
        {
            throw new NoSuchElementException(
                "The list is empty, there is nothing to remove.");
        }

        DoublyLinkedNode<T> prev = current.previous();

        // current is set to the next Node if it's the first Node in the list
        current = (size > 1 && isFirst()) ? current.next() : prev;

        DoublyLinkedNode<T> old = prev.split();
        prev.join(old.split());

        size--;
        return old;
    }


    // ----------------------------------------------------------
    /**
     * Insert a new item at the rear (the tail) of the list.
     *
     * @param value
     *            the item to insert
     * @postcondition [new-contents] == [old-contents] * [value]
     */
    public void append(T value)
    {
        DoublyLinkedNode<T> end = tail.previous();

        end.join((new DoublyLinkedNode<T>(value)).join(end.split()));
        size++;
        moveToRear();
    }


    // ----------------------------------------------------------
    /**
     * Insert a new item at the front (the head) of the list.
     *
     * @param value
     *            the item to insert.
     * @postcondition [new-contents] = [value] * [old-contents]
     */
    public void prepend(T value)
    {
        head.join((new DoublyLinkedNode<T>(value)).join(head.split()));
        size++;
        moveToFront();
    }


    // ----------------------------------------------------------
    /**
     * Get the number of items in this list. Does not alter the list.
     *
     * @return The number of items this list contains.
     * @postcondition result = |[contents]|
     */
    public int size()
    {
        return size;
    }


    // ----------------------------------------------------------
    /**
     * Empty the list.
     *
     * @postcondition [new-contents] = []
     */
    public void clear()
    {
        head.split();
        head.join(tail.previous().split());
        size = 0;
    }


    // ----------------------------------------------------------
    /**
     * Returns a String representation of this list. A list's String
     * representation is written as an arrow-separated list of its contents (in
     * front-to-rear order) like this:
     *
     * <pre>
     * 52 -> 14 -> 12 -> 119 -> 73 -> 80 -> 35
     * </pre>
     * <p>
     * An empty list is simply blank.
     * </p>
     *
     * @return a String representation of the list
     */
    public String toString()
    {
        DoublyLinkedNode<T> temp = head.next();
        String str = "";
        for (int i = 0; i < size; i++)
        {
            str += temp.get() + " -> ";
            temp = temp.next();
        }
        return (size > 0) ? str.substring(0, str.length() - 4) : str;
    }
}

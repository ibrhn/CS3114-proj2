// ----------------------------------------------------------
/**
 * Tests the DoublyLinkedNode class.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 23, 2014
 */
public class DoublyLinkedNodeTest
    extends student.TestCase
{
    private DoublyLinkedNode<Integer> node;
    private DoublyLinkedNode<Integer> prev;
    private DoublyLinkedNode<Integer> next;


    // ----------------------------------------------------------
    /**
     * Node<Integer> setUp.
     */
    public void setUp()
    {
        node = new DoublyLinkedNode<Integer>(0);
        prev = new DoublyLinkedNode<Integer>(1);
        next = new DoublyLinkedNode<Integer>(3);
    }


    // ----------------------------------------------------------
    /**
     * Tests the set method.
     */
    public void testSet()
    {
        assertEquals(0, node.get().intValue());

        node.set(2);
        assertEquals(2, node.get().intValue());
    }


    // ----------------------------------------------------------
    /**
     * Tests the join method.
     */
    public void testJoin()
    {
        prev.join(node.join(next));
        assertEquals(prev, node.previous());
        assertEquals(next, node.next());

        try
        {
            node.join(prev);
        }
        catch (IllegalStateException ex)
        {
            assertEquals(
                "A node is already following this one.",
                ex.getMessage());
        }

        try
        {
            next.join(node);
        }
        catch (IllegalStateException ex)
        {
            assertEquals("A node is already preceding the one passed to this "
                + "method.", ex.getMessage());
        }
    }

    // ----------------------------------------------------------
    /**
     * Tests the split method.
     */
    public void testSplit()
    {
        prev.join(node.join(next));

        node.split();
        assertNull(node.next());
        assertNull(next.previous());

        assertEquals(prev, node.previous());
        assertEquals(node, prev.next());
    }


    // ----------------------------------------------------------
    /**
     * Tests the toString method.
     */
    public void testToString()
    {
        assertEquals(node.get().toString(), node.toString());
    }
}

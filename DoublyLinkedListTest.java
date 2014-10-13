import java.util.NoSuchElementException;

// ----------------------------------------------------------
/**
 * Tests the DoublyLinkedList class.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)s
 * @version Sep 23, 2014
 */
public class DoublyLinkedListTest
    extends student.TestCase
{
    private DoublyLinkedList<Integer> list;


    // ----------------------------------------------------------
    /**
     * DoublyLinkedList<Integer> setUp.
     */
    public void setUp()
    {
        list = new DoublyLinkedList<Integer>();
    }


    // ----------------------------------------------------------
    /**
     * Tests the get method.
     */
    public void testGet()
    {
        try
        {
            list.get();
        }
        catch (NoSuchElementException ex)
        {
            assertEquals("The list is empty.", ex.getMessage());
        }
    }


    // ----------------------------------------------------------
    /**
     * Tests the add method.
     */
    public void testAdd()
    {
        list.add(1);
        assertEquals(1, list.get().intValue());
        assertEquals(1, list.size());

        list.add(0);
        assertEquals(0, list.get().intValue());
        assertEquals(2, list.size());

        list.next();
        assertEquals(1, list.get().intValue());

        list.previous();
        assertEquals(0, list.get().intValue());

        list.add(-1);
        assertEquals(-1, list.get().intValue());
        assertEquals(3, list.size());
    }


    // ----------------------------------------------------------
    /**
     * Tests the moveToFront and moveToRear methods.
     */
    public void testMoveToFrontAndMoveToRear()
    {
        list.add(2);
        list.add(1);
        list.add(0);

        list.next();
        list.moveToFront();
        assertEquals(0, list.get().intValue());

        list.next();
        list.moveToRear();
        assertEquals(2, list.get().intValue());
    }


    // ----------------------------------------------------------
    /**
     * Tests the isFirst and isLast methods.
     */
    public void testIsFirstAndIsLast()
    {
        list.add(2);
        list.add(1);
        list.add(0);

        assertTrue(list.isFirst());
        assertFalse(list.isLast());

        list.moveToRear();
        assertTrue(list.isLast());
        assertFalse(list.isFirst());
    }


    // ----------------------------------------------------------
    /**
     * Tests the set method.
     */
    public void testSet()
    {
        list.add(2);
        assertEquals(2, list.get().intValue());

        list.set(0);
        assertEquals(0, list.get().intValue());
    }


    // ----------------------------------------------------------
    /**
     * Tests the remove method.
     */
    public void testRemove()
    {
        list.add(2);
        list.add(1);
        list.add(0);

        assertEquals(0, list.get().intValue());

        list.remove();
        assertEquals(1, list.get().intValue());

        list.next();
        assertEquals(2, list.get().intValue());
        list.remove();
        assertEquals(1, list.get().intValue());

        try
        {
            list.remove();
        }
        catch (NoSuchElementException ex)
        {
            assertEquals(
                "The list is empty, there is nothing to remove.",
                ex.getMessage());
        }
    }


    // ----------------------------------------------------------
    /**
     * Tests the append method.
     */
    public void testAppend()
    {
        list.add(1);
        list.add(0);

        list.append(2);
        list.moveToRear();
        assertEquals(2, list.get().intValue());
    }


    // ----------------------------------------------------------
    /**
     * Tests the prepend method.
     */
    public void testPrepend()
    {
        list.add(2);
        list.add(1);

        list.prepend(0);
        list.moveToFront();
        assertEquals(0, list.get().intValue());
    }


    // ----------------------------------------------------------
    /**
     * Tests the clear method.
     */
    public void testClear()
    {
        list.add(2);
        list.add(1);
        list.add(0);
        assertEquals(3, list.size());

        list.clear();
        assertEquals(0, list.size());
    }


    // ----------------------------------------------------------
    /**
     * Tests the toString method.
     */
    public void testToString()
    {
        assertEquals("", list.toString());

        list.add(2);
        assertEquals("2", list.toString());

        list.add(1);
        assertEquals("1 -> 2", list.toString());

        list.add(0);
        assertEquals("0 -> 1 -> 2", list.toString());

        list.remove();
        assertEquals("1 -> 2", list.toString());

        list.remove();
        assertEquals("2", list.toString());

        list.remove();
        assertEquals("", list.toString());
    }


    // ----------------------------------------------------------
    /**
     * Tests uncovered minor code.
     */
    public void testOther()
    {
        list.add(2);
        list.add(1);
        list.add(0);

        list.next();
        list.previous();
        list.previous();
        assertEquals(0, list.get().intValue());
    }
}

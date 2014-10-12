// ----------------------------------------------------------
/**
 * Tests the Hashtable class.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 28, 2014
 */
public class HashtableTest
    extends student.TestCase
{
    private Hashtable map;


    // ----------------------------------------------------------
    /**
     * Hashtable setUp.
     */
    public void setUp()
    {
        map = new Hashtable(new Controller(3, 32));
    }


    // ----------------------------------------------------------
    /**
     * Tests the remove method.
     * @throws Exception
     */
    public void testRemove()
        throws Exception
    {
        assertFalse(map.remove("ob"));

        assertTrue(map.put("ob"));
        assertTrue(map.remove("ob"));

        assertFalse(map.remove("ob"));
    }


    // ----------------------------------------------------------
    /**
     * Tests the put method.
     * @throws Exception
     */
    public void testPut()
        throws Exception
    {
        assertTrue(map.put("ob"));

        assertFalse(map.put("ob"));

        assertTrue(map.remove("ob"));
        assertTrue(map.put("ob"));

        assertEquals(3, map.capacity());
        assertEquals(1, map.usage());

        map.put("od");
        assertEquals(6, map.capacity());
        assertEquals(2, map.usage());
    }


    // ----------------------------------------------------------
    /**
     * Tests the findPos and getPos methods.
     * @throws Exception
     */
    public void testFindPosAndGetPos() throws Exception
    {
        map.put("a");
        map.remove("a");

        map.put("b");
        map.remove("b");

        map.put("c");
        map.remove("c");

        assertEquals(3, map.capacity());
        assertEquals(0, map.usage());
    }

    // ----------------------------------------------------------
    /**
     * Tests the print method.
     * @throws Exception
     */
    public void testPrint()
        throws Exception
    {
        map.put("ob");
        map.put("oc");
        map.put("od");

        assertEquals("|od| 1\n" + "|oc| 3\n" + "|ob| 5\n", map.print());
    }
}

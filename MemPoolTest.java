import java.util.Arrays;

// ----------------------------------------------------------
/**
 * Tests the MemPool class.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 25, 2014
 */
public class MemPoolTest
    extends student.TestCase
{
    private MemPool pool;


    // ----------------------------------------------------------
    /**
     * MemPool setUp.
     */
    public void setUp()
    {
        pool = new MemPool(12);
    }


    // ----------------------------------------------------------
    /**
     * Tests the remove method.
     */
    public void testRemove()
    {
        assertEquals("(0,12)", pool.dump());

        Handle one = new Handle(pool.insert("b".getBytes()));
        assertEquals("(3,9)", pool.dump());

        Handle two = new Handle(pool.insert("bo".getBytes()));
        assertEquals("(7,5)", pool.dump());

        Handle three = new Handle(pool.insert("bon".getBytes()));
        assertEquals("", pool.dump());

        pool.remove(one);
        assertEquals("(0,3)", pool.dump());

        pool.remove(three);
        assertEquals("(0,3) -> (7,5)", pool.dump());

        pool.remove(two);
        assertEquals("(0,12)", pool.dump());

        pool.insert("a".getBytes());
        Handle b = new Handle(pool.insert("b".getBytes()));
        pool.insert("c".getBytes());
        Handle d = new Handle(pool.insert("d".getBytes()));
        assertEquals("", pool.dump());

        pool.remove(b);
        pool.remove(d);
        assertEquals("(3,3) -> (9,3)", pool.dump());
    }


    // ----------------------------------------------------------
    /**
     * Tests the insert method.
     */
    public void testInsert()
    {
        assertEquals(12, pool.capacity());
        assertEquals("(0,12)", pool.dump());

        Handle dict = new Handle(pool.insert("dictionary".getBytes()));
        assertTrue(Arrays.equals("dictionary".getBytes(), pool.getBytes(dict)));
        assertEquals("", pool.dump());

        pool.insert("willy".getBytes());
        assertEquals("(19,5)", pool.dump());

        pool.insert("wonka".getBytes());
        assertEquals("(26,10)", pool.dump());
    }
}

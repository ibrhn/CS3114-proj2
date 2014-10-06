// -------------------------------------------------------------------------
/**
 * Tests the Handle class.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 29, 2014
 */
public class HandleTest
    extends student.TestCase
{
    private Handle handle;


    // ----------------------------------------------------------
    /**
     * Handle setUp.
     */
    public void setUp()
    {
        handle = new Handle(1);
    }


    // ----------------------------------------------------------
    /**
     * Tests all the Handle class methods.
     */
    public void test()
    {
        assertEquals(1, handle.get());

        handle.set(2);
        assertEquals(2, handle.get());

        Handle comp = new Handle(5);
        assertEquals(-1, handle.compareTo(comp));
        assertEquals(1, comp.compareTo(handle));

        comp.set(2);
        assertEquals(0, handle.compareTo(comp));

        assertEquals("2", handle.toString());
    }
}

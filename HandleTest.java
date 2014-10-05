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

        assertFalse(handle.equals(new Object()));
        assertFalse(handle == null);
        assertFalse(handle.equals(new Handle(0)));

        assertTrue(handle.equals(new Handle(2)));
    }
}

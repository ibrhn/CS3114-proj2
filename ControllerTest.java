// ----------------------------------------------------------
/**
 * Tests the Controller (and Memman) class(es).
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 28, 2014
 */
public class ControllerTest
    extends student.TestCase
{
    private Memman mm;


    // ----------------------------------------------------------
    /**
     * Controller setUp.
     */
    public void setUp()
    {
        mm = new Memman();
    }


    // ----------------------------------------------------------
    /**
     * Tests the parse method (sort of).
     *
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public void testParse()
        throws Exception
    {
        String[] in = { "10", "32", "input.txt" };
        mm.main(in);

        assertEquals(10, mm.controller().initHashSz());
        assertEquals(320, mm.controller().pool().capacity());
    }
}

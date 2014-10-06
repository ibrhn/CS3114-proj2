// -------------------------------------------------------------------------
/**
 * Tests the KVPair class.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Oct 5, 2014
 */
public class KVPairTest
    extends student.TestCase
{
    private KVPair pair;


    // -------------------------------------------------------------------------
    /**
     * KVPair setUp.
     */
    public void setUp()
    {
        pair = new KVPair(new Handle(2), new Handle(4));
    }


    // ----------------------------------------------------------
    /**
     * Tests all the methods of KVPair.
     */
    public void test()
    {
        assertEquals(2, pair.key().get());
        assertEquals(4, pair.value().get());

        KVPair comp = new KVPair(new Handle(3), new Handle(5));
        assertEquals(-1, pair.compareTo(comp));

        comp.set(new KVPair(new Handle(2), new Handle(4)));
        assertEquals(0, pair.compareTo(comp));

        comp.set(new Handle(1), new Handle(5));
        assertEquals(1, pair.compareTo(comp));

        assertEquals("2 4", pair.toString());

        assertFalse(pair.equals(new Object()));
        assertFalse(pair.equals(comp));
        assertFalse(pair.equals(new KVPair(new Handle(2), new Handle(3))));
        assertTrue(pair.equals(new KVPair(new Handle(2), new Handle(4))));
    }
}

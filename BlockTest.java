// ----------------------------------------------------------
/**
 * Tests the Block class.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 24, 2014
 */
public class BlockTest
    extends student.TestCase
{
    private Block block;


    // ----------------------------------------------------------
    /**
     * Block setUp.
     */
    public void setUp()
    {
        block = new Block(1, 2);
    }


    // ----------------------------------------------------------
    /**
     * Tests the set, setPos, and setLen methods.
     */
    public void testSet()
    {
        assertEquals(1, block.getPos());
        assertEquals(2, block.getLen());

        block.setPos(0);
        block.setLen(4);
        assertEquals(0, block.getPos());
        assertEquals(4, block.getLen());

        block.set(2, 1);
        assertEquals(2, block.getPos());
        assertEquals(1, block.getLen());
    }


    // ----------------------------------------------------------
    /**
     * Tests the equals method.
     */
    public void testEquals()
    {
        assertFalse(block.equals(new Object()));
        assertFalse(block.equals(new Block(2, 1)));
        assertTrue(block.equals(new Block(1, 2)));
    }


    // ----------------------------------------------------------
    /**
     * Tests the toString method.
     */
    public void testToString()
    {
        assertEquals("(1,2)", block.toString());
    }
}

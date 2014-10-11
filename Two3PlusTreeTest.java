// -------------------------------------------------------------------------
/**
 *  Tests the Two3PlusTree class.
 *
 *  @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 *  @version Oct 6, 2014
 */
public class Two3PlusTreeTest extends student.TestCase
{
    private Two3PlusTree tree;
    private Controller ctrl;

    // ----------------------------------------------------------
    /**
     * Two3PlusTree setUp.
     */
    public void setUp()
    {
        tree = new Two3PlusTree(ctrl);
    }


    // ----------------------------------------------------------
    /**
     * Tests the insert method.
     */
    public void testInsert()
    {
        assertEquals(0, tree.size());
        assertEquals("Printing 2-3 tree:\n", tree.print());

        Handle one = new Handle(1);
        Handle eight = new Handle(8);

        tree.insert(one, eight);
        assertEquals(1, tree.size());
        assertEquals("Printing 2-3 tree:\n"
            +   "1 8\n", tree.print());

        assertFalse(tree.insert(one, eight));
        assertEquals("Printing 2-3 tree:\n"
            + "1 8\n", tree.print());

        tree.insert(new Handle(1), new Handle(16));
        assertEquals("Printing 2-3 tree:\n"
            + "1 8 1 16\n", tree.print());

        tree.insert(new Handle(1), new Handle(32));
        assertEquals("Printing 2-3 tree:\n"
            + "1 16\n"
            + "  1 8\n"
            + "  1 16 1 32\n", tree.print());

        tree.insert(new Handle(1), new Handle(24));
        assertEquals("Printing 2-3 tree:\n"
            + "1 16 1 24\n"
            + "  1 8\n"
            + "  1 16\n"
            + "  1 24 1 32\n", tree.print());

        tree.insert(new Handle(1), new Handle(40));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 16\n"
            + "    1 8\n"
            + "    1 16\n"
            + "  1 32\n"
            + "    1 24\n"
            + "    1 32 1 40\n", tree.print());

        tree.insert(new Handle(1), new Handle(4));
        tree.insert(new Handle(1), new Handle(5));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 5 1 16\n"
            + "    1 4\n"
            + "    1 5 1 8\n"
            + "    1 16\n"
            + "  1 32\n"
            + "    1 24\n"
            + "    1 32 1 40\n", tree.print());

        tree.insert(new Handle(1), new Handle(27));
        tree.insert(new Handle(1), new Handle(28));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 5 1 16\n"
            + "    1 4\n"
            + "    1 5 1 8\n"
            + "    1 16\n"
            + "  1 27 1 32\n"
            + "    1 24\n"
            + "    1 27 1 28\n"
            + "    1 32 1 40\n", tree.print());

        tree.insert(new Handle(1), new Handle(12));
        assertEquals("Printing 2-3 tree:\n"
            + "1 8 1 24\n"
            + "  1 5\n"
            + "    1 4\n"
            + "    1 5\n"
            + "  1 16\n"
            + "    1 8 1 12\n"
            + "    1 16\n"
            + "  1 27 1 32\n"
            + "    1 24\n"
            + "    1 27 1 28\n"
            + "    1 32 1 40\n", tree.print());

        tree.insert(new Handle(1), new Handle(25));
        tree.insert(new Handle(1), new Handle(26));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 8\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 16\n"
            + "      1 8 1 12\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 25\n"
            + "      1 24\n"
            + "      1 25 1 26\n"
            + "    1 32\n"
            + "      1 27 1 28\n"
            + "      1 32 1 40\n", tree.print());
    }


    // ----------------------------------------------------------
    /**
     * Tests the search method.
     */
    public void testSearch()
    {
        assertNull(tree.search(new Handle(1)));

        tree.insert(new Handle(1), new Handle(1));
        assertNull(tree.search(new Handle(2)));

        tree.insert(new Handle(2), new Handle(1));
        assertEquals(2, tree.search(new Handle(2)).right().key().get());
        assertEquals(1, tree.search(new Handle(1)).left().key().get());

        tree.insert(new Handle(3), new Handle(1));
        tree.insert(new Handle(0), new Handle(1));
        tree.insert(new Handle(4), new Handle(1));
        assertEquals(0, tree.search(new Handle(0)).left().key().get());
        assertEquals(2, tree.search(new Handle(2)).left().key().get());
        assertEquals(4, tree.search(new Handle(4)).right().key().get());
    }


    // ----------------------------------------------------------
    /**
     * Tests the list method.
     * @throws Exception
     */
    public void testList() throws Exception
    {
        assertEquals("", tree.list(new Handle(0)));
    }
}

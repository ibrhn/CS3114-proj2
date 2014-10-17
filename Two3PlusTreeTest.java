import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * Tests the Two3PlusTree class.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Oct 6, 2014
 */
public class Two3PlusTreeTest
    extends student.TestCase
{
    private Two3PlusTree tree;
    private Controller   ctrl;


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
        assertEquals("Printing 2-3 tree:\n", tree.print());

        Handle one = new Handle(1);
        Handle eight = new Handle(8);

        tree.insert(one, eight);
        assertEquals("Printing 2-3 tree:\n"
            + "1 8\n", tree.print());

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
            + "    1 32 1 40\n",
            tree.print());

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
    public void testList()
        throws Exception
    {
        assertEquals(new ArrayList<String>(), tree.list(new Handle(0)));
    }


    // ----------------------------------------------------------
    /**
     * Tests the delete method.
     */
    public void testDelete()
    {
        tree.insert(new Handle(1), new Handle(8));
        tree.insert(new Handle(1), new Handle(16));

        assertTrue(tree.delete(new Handle(1), new Handle(16)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 8\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(8)));
        assertEquals("Printing 2-3 tree:\n", tree.print());

        tree.insert(new Handle(1), new Handle(8));
        tree.insert(new Handle(1), new Handle(16));
        tree.insert(new Handle(1), new Handle(32));
        tree.insert(new Handle(1), new Handle(24));
        tree.insert(new Handle(1), new Handle(40));


        tree.insert(new Handle(1), new Handle(4));
        tree.insert(new Handle(1), new Handle(5));
        tree.insert(new Handle(1), new Handle(27));
        tree.insert(new Handle(1), new Handle(28));
        tree.insert(new Handle(1), new Handle(12));
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

        assertTrue(tree.delete(new Handle(1), new Handle(40)));
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
            + "      1 32\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(28)));
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
            + "      1 27\n"
            + "      1 32\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(25)));
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
            + "    1 26\n"
            + "      1 24\n"
            + "      1 26\n"
            + "    1 32\n"
            + "      1 27\n"
            + "      1 32\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(8)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 12\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 16\n"
            + "      1 12\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 26\n"
            + "      1 24\n"
            + "      1 26\n"
            + "    1 32\n"
            + "      1 27\n"
            + "      1 32\n", tree.print());


        assertTrue(tree.insert(new Handle(1), new Handle(33)));
        assertTrue(tree.insert(new Handle(1), new Handle(34)));
        assertTrue(tree.insert(new Handle(1), new Handle(14)));
        assertTrue(tree.insert(new Handle(1), new Handle(15)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 12\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 14 1 16\n"
            + "      1 12\n"
            + "      1 14 1 15\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 26\n"
            + "      1 24\n"
            + "      1 26\n"
            + "    1 32 1 33\n"
            + "      1 27\n"
            + "      1 32\n"
            + "      1 33 1 34\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(33)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 12\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 14 1 16\n"
            + "      1 12\n"
            + "      1 14 1 15\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 26\n"
            + "      1 24\n"
            + "      1 26\n"
            + "    1 32 1 34\n"
            + "      1 27\n"
            + "      1 32\n"
            + "      1 34\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(32)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 12\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 14 1 16\n"
            + "      1 12\n"
            + "      1 14 1 15\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 26\n"
            + "      1 24\n"
            + "      1 26\n"
            + "    1 34\n"
            + "      1 27\n"
            + "      1 34\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(12)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 14\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 15 1 16\n"
            + "      1 14\n"
            + "      1 15\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 26\n"
            + "      1 24\n"
            + "      1 26\n"
            + "    1 34\n"
            + "      1 27\n"
            + "      1 34\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(14)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 15\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 16\n"
            + "      1 15\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 26\n"
            + "      1 24\n"
            + "      1 26\n"
            + "    1 34\n"
            + "      1 27\n"
            + "      1 34\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(36)));
        assertTrue(tree.insert(new Handle(1), new Handle(35)));
        assertTrue(tree.delete(new Handle(1), new Handle(36)));
        assertTrue(tree.delete(new Handle(1), new Handle(35)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 15\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 16\n"
            + "      1 15\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 26\n"
            + "      1 24\n"
            + "      1 26\n"
            + "    1 34\n"
            + "      1 27\n"
            + "      1 34\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(4)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24 1 27\n"
            + "  1 15 1 16\n"
            + "    1 5\n"
            + "    1 15\n"
            + "    1 16\n"
            + "  1 26\n"
            + "    1 24\n"
            + "    1 26\n"
            + "  1 34\n"
            + "    1 27\n"
            + "    1 34\n", tree.print());

        assertFalse(tree.delete(new Handle(3), new Handle(2)));
        assertFalse(tree.delete(new Handle(1), new Handle(89)));
        assertFalse(tree.delete(null, null));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24 1 27\n"
            + "  1 15 1 16\n"
            + "    1 5\n"
            + "    1 15\n"
            + "    1 16\n"
            + "  1 26\n"
            + "    1 24\n"
            + "    1 26\n"
            + "  1 34\n"
            + "    1 27\n"
            + "    1 34\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(4)));
        assertTrue(tree.insert(new Handle(1), new Handle(36)));
        assertTrue(tree.insert(new Handle(1), new Handle(38)));
        assertTrue(tree.insert(new Handle(1), new Handle(40)));
        assertTrue(tree.delete(new Handle(1), new Handle(4)));
        assertTrue(tree.delete(new Handle(1), new Handle(5)));

        assertTrue(tree.delete(new Handle(1), new Handle(16)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 27 1 36\n"
            + "  1 24 1 26\n"
            + "    1 15\n"
            + "    1 24\n"
            + "    1 26\n"
            + "  1 34\n"
            + "    1 27\n"
            + "    1 34\n"
            + "  1 38\n"
            + "    1 36\n"
            + "    1 38 1 40\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(41)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 27 1 36\n"
            + "  1 24 1 26\n"
            + "    1 15\n"
            + "    1 24\n"
            + "    1 26\n"
            + "  1 34\n"
            + "    1 27\n"
            + "    1 34\n"
            + "  1 38 1 40\n"
            + "    1 36\n"
            + "    1 38\n"
            + "    1 40 1 41\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(27)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 26 1 36\n"
            + "  1 24\n"
            + "    1 15\n"
            + "    1 24\n"
            + "  1 34\n"
            + "    1 26\n"
            + "    1 34\n"
            + "  1 38 1 40\n"
            + "    1 36\n"
            + "    1 38\n"
            + "    1 40 1 41\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(34)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 26 1 38\n"
            + "  1 24\n"
            + "    1 15\n"
            + "    1 24\n"
            + "  1 36\n"
            + "    1 26\n"
            + "    1 36\n"
            + "  1 40\n"
            + "    1 38\n"
            + "    1 40 1 41\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(4)));
        assertTrue(tree.insert(new Handle(1), new Handle(3)));
        assertTrue(tree.delete(new Handle(1), new Handle(36)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24 1 38\n"
            + "  1 4\n"
            + "    1 3\n"
            + "    1 4 1 15\n"
            + "  1 26\n"
            + "    1 24\n"
            + "    1 26\n"
            + "  1 40\n"
            + "    1 38\n"
            + "    1 40 1 41\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(38)));
        assertTrue(tree.delete(new Handle(1), new Handle(40)));
        assertTrue(tree.delete(new Handle(1), new Handle(24)));
        assertTrue(tree.delete(new Handle(1), new Handle(26)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 4 1 41\n"
            + "  1 3\n"
            + "  1 4 1 15\n"
            + "  1 41\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(26)));
        assertTrue(tree.delete(new Handle(1), new Handle(26)));
        assertTrue(tree.delete(new Handle(1), new Handle(41)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 4 1 15\n"
            + "  1 3\n"
            + "  1 4\n"
            + "  1 15\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(15)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 4\n"
            + "  1 3\n"
            + "  1 4\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(4)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 3\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(4)));
        assertTrue(tree.delete(new Handle(1), new Handle(3)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 4\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(3)));
        assertTrue(tree.insert(new Handle(1), new Handle(5)));
        assertTrue(tree.delete(new Handle(1), new Handle(5)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 4\n"
            + "  1 3\n"
            + "  1 4\n", tree.print());
        assertTrue(tree.delete(new Handle(1), new Handle(3)));
        assertEquals("Printing 2-3 tree:\n"
            + "1 4\n", tree.print());
        assertTrue(tree.delete(new Handle(1), new Handle(4)));
        assertEquals("Printing 2-3 tree:\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(3)));
        assertTrue(tree.insert(new Handle(3), new Handle(1)));
        assertTrue(tree.insert(new Handle(1), new Handle(4)));
        assertTrue(tree.insert(new Handle(4), new Handle(1)));
        assertTrue(tree.insert(new Handle(2), new Handle(5)));
        assertTrue(tree.insert(new Handle(5), new Handle(2)));
        assertEquals("Printing 2-3 tree:\n"
            + "3 1\n"
            + "  1 4\n"
            + "    1 3\n"
            + "    1 4 2 5\n"
            + "  4 1\n"
            + "    3 1\n"
            + "    4 1 5 2\n", tree.print());

        assertTrue(tree.delete(new Handle(1), new Handle(3)));
        assertTrue(tree.delete(new Handle(3), new Handle(1)));
//        assertEquals("Printing 2-3 tree:\n"
//            + "3 1\n"
//            + "  1 4\n"
//            + "    1 3\n"
//            + "    1 4 2 5\n"
//            + "  4 1\n"
//            + "    3 1\n"
//            + "    4 1 5 2\n", tree.print());
    }

    /**
     * also tests remove method
     */
    public void testTwo() {
        assertTrue(tree.insert(new Handle(1), new Handle(0)));
        assertTrue(tree.insert(new Handle(1), new Handle(5)));
        assertTrue(tree.insert(new Handle(1), new Handle(10)));
        assertTrue(tree.insert(new Handle(1), new Handle(15)));
        assertTrue(tree.insert(new Handle(1), new Handle(20)));
        assertTrue(tree.insert(new Handle(1), new Handle(25)));
        assertTrue(tree.insert(new Handle(1), new Handle(30)));
        assertTrue(tree.insert(new Handle(1), new Handle(18)));
        assertTrue(tree.insert(new Handle(1), new Handle(19)));
        assertTrue(tree.delete(new Handle(1), new Handle(20)));
        assertTrue(tree.delete(new Handle(1), new Handle(25)));

        assertEquals("Printing 2-3 tree:\n"
            + "1 10 1 18\n"
            + "  1 5\n"
            + "    1 0\n"
            + "    1 5\n"
            + "  1 15\n"
            + "    1 10\n"
            + "    1 15\n"
            + "  1 30\n"
            + "    1 18 1 19\n"
            + "    1 30\n", tree.print());

        assertTrue(tree.insert(new Handle(1), new Handle(35)));
        assertTrue(tree.insert(new Handle(1), new Handle(40)));
        assertTrue(tree.insert(new Handle(1), new Handle(45)));
        assertTrue(tree.insert(new Handle(1), new Handle(20)));

        assertTrue(tree.delete(new Handle(1), new Handle(40)));
        assertTrue(tree.delete(new Handle(1), new Handle(35)));

    }

    // ----------------------------------------------------------
    /**
     * Tests the remove method.
     * @throws Exception
     */
    public void testRemove() throws Exception
    {
        tree.insert(new Handle(3), new Handle(10));
        tree.insert(new Handle(3), new Handle(8));
        tree.insert(new Handle(1), new Handle(14));
        assertEquals(tree.search(new Handle(2)), null);
        assertEquals(tree.search(new Handle(3)).toString(), "3 8 3 10");
        tree.insert(new Handle(5), new Handle(14));
        tree.insert(new Handle(3), new Handle(14));
        assertEquals(tree.search(new Handle(3)).toString(), "3 8");
        tree.insert(new Handle(9), new Handle(14));
        tree.insert(new Handle(6), new Handle(14));
        assertEquals(tree.search(new Handle(3)).toString(), "3 8");
        assertEquals(tree.search(new Handle(1)).toString(), "1 14");
        assertEquals(tree.search(new Handle(5)).toString(), "5 14");
        assertEquals("Printing 2-3 tree:\n"
            + "3 10 5 14\n"
            + "  3 8\n"
            + "    1 14\n"
            + "    3 8\n"
            + "  3 14\n"
            + "    3 10\n"
            + "    3 14\n"
            + "  6 14\n"
            + "    5 14\n"
            + "    6 14 9 14\n", tree.print());
        assertEquals(1, tree.remove(new Handle(1)).size());
        assertEquals("Printing 2-3 tree:\n"
            + "5 14\n"
            + "  3 10 3 14\n"
            + "    3 8\n"
            + "    3 10\n"
            + "    3 14\n"
            + "  6 14\n"
            + "    5 14\n"
            + "    6 14 9 14\n", tree.print());

        tree.clear();

        tree.insert(new Handle(1), new Handle(8));
        tree.insert(new Handle(1), new Handle(16));
        tree.insert(new Handle(1), new Handle(32));
        tree.insert(new Handle(1), new Handle(24));
        tree.insert(new Handle(1), new Handle(40));
        tree.insert(new Handle(1), new Handle(4));
        tree.insert(new Handle(1), new Handle(5));
        tree.insert(new Handle(1), new Handle(27));
        tree.insert(new Handle(1), new Handle(28));
        tree.insert(new Handle(1), new Handle(12));
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

        tree.delete(new Handle(1), new Handle(40));
        tree.delete(new Handle(1), new Handle(28));
        tree.delete(new Handle(1), new Handle(26));
        tree.delete(new Handle(1), new Handle(12));
        assertEquals("Printing 2-3 tree:\n"
            + "1 24\n"
            + "  1 8\n"
            + "    1 5\n"
            + "      1 4\n"
            + "      1 5\n"
            + "    1 16\n"
            + "      1 8\n"
            + "      1 16\n"
            + "  1 27\n"
            + "    1 25\n"
            + "      1 24\n"
            + "      1 25\n"
            + "    1 32\n"
            + "      1 27\n"
            + "      1 32\n", tree.print());

        tree.remove(new Handle(1));
        assertEquals("Printing 2-3 tree:\n", tree.print());
        // assertEquals("Printing 2-3 tree:\n", tree.print());
    }

}

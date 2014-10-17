// -------------------------------------------------------------------------
/**
 * Tests the TreeNode (and Internal and Leaf) class(es).
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Oct 5, 2014
 */
public class TreeNodeTest
    extends student.TestCase
{
    private TreeNode node;
    private Internal internal;
    private Leaf     leaf;

    // ----------------------------------------------------------
    /**
     * TreeNode setUp.
     */
    public void setUp()
    {
        KVPair pair = new KVPair(new Handle(2), new Handle(4));

        node = new Leaf(pair);
        internal = new Internal(pair, null, null);
        leaf = new Leaf(pair);
    }


    // ----------------------------------------------------------
    /**
     * Tests all the methods of the TreeNode class.
     */
    public void testTreeNode()
    {

        assertTrue(node.containsEqual(new KVPair(new Handle(2),
            new Handle(4))));
        assertTrue(node.containsEqual(null));
        assertFalse(node.containsEqual(new KVPair(new Handle(2),
            new Handle(5))));

        node = new Leaf(new KVPair(new Handle(3), new Handle(5)),
            new KVPair(new Handle(2), new Handle(4)));

        assertFalse(node.contains(new KVPair(new Handle(2), new Handle(4))));
        assertFalse(node.contains(new KVPair(new Handle(3), new Handle(5))));
        assertTrue(node.containsEqual(new KVPair(new Handle(2), new
            Handle(4))));
        assertTrue(node.containsEqual(new KVPair(new Handle(3), new
            Handle(5))));
        assertEquals("2 4 3 5", node.toString());

        assertNotNull(node.right());

        assertEquals("2 4 3 5", node.toString());

        assertNotNull(node.containsEqual(null));
        assertFalse(node.containsEqual(new KVPair(new Handle(0),
            new Handle(1))));
    }


    // ----------------------------------------------------------
    /**
     * Tests all the methods of the Internal class.
     */
    public void testInternal()
    {
        TreeNode low = new Leaf(new KVPair(new Handle(0), new Handle(1)));
        TreeNode mid = new Leaf(new KVPair(new Handle(1), new Handle(2)));
        TreeNode high = new Leaf(new KVPair(new Handle(2), new Handle(3)));

        internal.set(new KVPair(new Handle(3), new Handle(4)), low, mid);
        internal.setRight(new KVPair(new Handle(4), new Handle(5)));
        internal.setHigh(high);

        assertEquals(low, internal.low());
        assertEquals(mid, internal.mid());
        assertEquals(high, internal.high());
    }


    // ----------------------------------------------------------
    /**
     * Tests all the methods of the Leaf class.
     */
    public void testLeaf()
    {
        Leaf next = new Leaf(new KVPair(new Handle(5), new Handle(6)), null);
        leaf.setNext(next);
        assertEquals(next, leaf.next());
    }
}

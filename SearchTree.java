/**
 * SearchTree is the main class. It contains a static Controller, and calls it
 * to parse through the file.
 *
 *
 *  On my honor:
 *
 * - I have not used source code obtained from another student,
 * or any other unauthorized source, either modified or
 * unmodified.
 *
 * - All source code and documentation used in my program is
 * either my original work, or was derived by me from the
 * source code published in the textbook for this course.
 *
 * - I have not discussed coding details about this project with
 * anyone other than my partner (in the case of a joint
 * submission), instructor, ACM/UPE tutors or the TAs assigned
 * to this course. I understand that I may discuss the concepts
 * of this program with other students, and that another student
 * may help me debug my program so long as neither of us writes
 * anything during the discussion or modifies any computer file
 * during the discussion. I have violated neither the spirit nor
 * letter of this restriction.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 28, 2014
 */
public class SearchTree
{
    private static Controller ctrl;


    // ----------------------------------------------------------
    /**
     * Calls the controller to parse through file args[2].
     *
     * @param args Strings of the argument inputs (initial Hashtable size,
     *             initial MemPool (memory pool) size, file name)
     * @throws Exception
     */
    public static void main(String[] args)
        throws Exception
    {
        int arg0 = Integer.parseInt(args[0]);
        int arg1 = Integer.parseInt(args[1]);
        ctrl = new Controller(arg0, arg1);
        ctrl.parse(args[2]);
    }


    // ----------------------------------------------------------
    /**
     * @return the controller
     */
    public Controller controller()
    {
        return ctrl;
    }
}

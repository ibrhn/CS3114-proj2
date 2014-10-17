import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

// ----------------------------------------------------------
/**
 * The Controller is the mediator for the memory pool and BOTH the artist and
 * song Hashtables. Any changes to these three data structures (from parsed
 * input) are facilitated by the Controller.
 *
 * @author Burhan Ishaq (iburhan), Amin Davoodi (amind1)
 * @version Sep 28, 2014
 */
public class Controller
{
    private Hashtable    artists;
    private Hashtable    songs;
    private MemPool      pool;
    private Two3PlusTree tree;

    private static int   initPoolSz;
    private static int   initHashSz;


    // ----------------------------------------------------------
    /**
     * Initializes the memory pool, and the artists and songs Hashtables.
     *
     * @param hashSz
     *            initial Hashtable(s) size
     * @param poolSz
     *            initial MemPool (memory pool) size
     */
    public Controller(int hashSz, int poolSz)
    {
        initPoolSz = poolSz;
        pool = new MemPool(initPoolSz);

        initHashSz = hashSz;
        artists = new Hashtable(this);
        songs = new Hashtable(this);
        tree = new Two3PlusTree(this);
    }


    // ----------------------------------------------------------
    /**
     * Using a BufferedReader to access the file for input, parse continuously
     * parses through the file line-by-line and updates the Controller (which in
     * turn updates the data structures) accordingly.
     *
     * @param path
     *            input file name (or path to the input file)
     * @throws Exception
     */
    public void parse(String path)
        throws Exception
    {
        BufferedReader in = new BufferedReader(new FileReader(path));

        String line;
        String[] command;
        String[] info;

        // loops line-by-line as long as there is a next line to read
        while (in.ready())
        {
            line = in.readLine();

            // splits the line into two Strings, the first of which defines the
            // action requested
            command = line.split(" ", 2);

            Handle artist;
            Handle song;
            switch (command[0].trim())
            {
                case "insert":
                    // splits the second part of the line into two Strings,
                    // where the first specifies an artist, and the second
                    // specifies a song
                    info = command[1].split("<SEP>");

                    int prePoolSz = pool.capacity();
                    int preArtistSz = artists.capacity();

                    info[0] = info[0].trim();
                    // if artist insertion is successful (or, the artist does
                    // not duplicate an artist already in the memory pool)
                    if (artists.put(info[0]))
                    {
                        if (preArtistSz < artists.capacity())
                        {
                            System.out
                                .println("Artist hash table size doubled.");
                        }

                        // loops to indicate the expansion of the memory pool
                        // for each time it is expanded
                        for (int i = 1; i <= (pool.capacity() - prePoolSz)
                            / initPoolSz; i++)
                        {
                            System.out.println("Memory pool expanded to be "
                                + ((i * initPoolSz) + prePoolSz) + " bytes.");
                        }

                        System.out.println("|" + info[0]
                            + "| is added to the artist database.");
                    }
                    else
                    {
                        System.out.println("|" + info[0]
                            + "| duplicates a record already in the artist "
                            + "database.");
                    }

                    prePoolSz = pool.capacity();
                    int preSongSz = songs.capacity();

                    // if song insertion is successful (or, the song does not
                    // duplicate a song already in the memory pool
                    info[1] = info[1].trim();
                    if (songs.put(info[1]))
                    {
                        if (preSongSz < songs.capacity())
                        {
                            System.out.println("Song hash table size doubled.");
                        }

                        // loops to indicate the expansion of the memory pool
                        // for each time it is expanded
                        for (int i = 1; i <= (pool.capacity() - prePoolSz)
                            / initPoolSz; i++)
                        {
                            System.out.println("Memory pool expanded to be "
                                + ((i * initPoolSz) + prePoolSz) + " bytes.");
                        }

                        System.out.println("|" + info[1]
                            + "| is added to the song database.");
                    }
                    else
                    {
                        System.out.println("|" + info[1]
                            + "| duplicates a record already in the song "
                            + "database.");
                    }

                    if (tree.insert(
                        artists.getHandle(info[0]),
                        songs.getHandle(info[1])))
                    {
                        tree.insert(
                            songs.getHandle(info[1]),
                            artists.getHandle(info[0]));
                        System.out.println("The KVPair (|" + info[0] + "|,|"
                            + info[1] + "|),(" + artists.getHandle(info[0])
                            + "," + songs.getHandle(info[1])
                            + ") is added to the tree.");

                        System.out.println("The KVPair (|" + info[1] + "|,|"
                            + info[0] + "|),(" + songs.getHandle(info[1]) + ","
                            + artists.getHandle(info[0])
                            + ") is added to the tree.");
                    }
                    else
                    {
                        System.out.println("The KVPair (|" + info[0] + "|,|"
                            + info[1] + "|),(" + artists.getHandle(info[0])
                            + "," + songs.getHandle(info[1])
                            + ") duplicates a record already in the tree.");
                        System.out.println("The KVPair (|" + info[1] + "|,|"
                            + info[0] + "|),(" + songs.getHandle(info[1]) + ","
                            + artists.getHandle(info[0])
                            + ") duplicates a record already in the tree.");
                    }
                    break;

                case "list":
                    info = command[1].split(" ", 2);
                    ArrayList<String> printing;
                    info[0] = info[0].trim();
                    if (info[0].equals("artist"))
                    {
                        artist = artists.getHandle(info[1]);
                        if (artist != null)
                        {
                            printing = tree.list(artist);
                            for (int i = 0; i < printing.size(); i++)
                            {
                                System.out.print("|" + printing.get(i)
                                    + "|\n");
                            }
                        }
                        else
                        {
                            System.out.println("|" + info[1]
                                + "| does not "
                                + "exist in the artist database.");
                        }
                    }
                    else if (info[0].equals("song"))
                    {
                        song = songs.getHandle(info[1]);
                        if (song != null)
                        {
                            printing = tree.list(song);
                            for (int i = 0; i < printing.size(); i++)
                            {
                                System.out.print("|" + printing.get(i)
                                    + "|\n");
                            }
                        }
                        else
                        {
                            System.out.println("|" + info[1]
                                + "| does not "
                                + "exist in the song database.");
                        }
                    }
                    break;

                case "delete":
                    info = command[1].split("<SEP>");

                    artist = artists.getHandle(info[0].trim());
                    song = songs.getHandle(info[1].trim());

                    if (artist == null)
                    {
                        System.out.println("|" + info[0] + "| does not exist "
                            + "in the artist database.");
                    }
                    else if (song == null)
                    {
                        System.out.println("|" + info[1] + "| does not exist "
                            + "in the song database.");
                    }

                    else if (tree.delete(artist, song))
                    {
                        tree.delete(song, artist);
                        System.out.println("The KVPair (|" + info[0] + "|,|"
                            + info[1] + "|) is deleted from the tree.");
                        System.out.println("The KVPair (|" + info[1] + "|,|"
                            + info[0] + "|) is deleted from the tree.");

                        if (tree.search(artists.getHandle(info[0])) == null)
                        {
                            artists.remove(info[0]);
                            System.out.println("|" + info[0]
                                + "| is deleted from the artist database.");
                        }
                        if (tree.search(songs.getHandle(info[1])) == null)
                        {
                            songs.remove(info[1]);
                            System.out.println("|" + info[1]
                                + "| is deleted from the song database.");
                        }
                    }
                    break;

                case "remove":
                    info = command[1].split(" ", 2);
                    ArrayList<String> list;
                    info[0] = info[0].trim();
                    switch (info[0])
                    {
                        case "artist":
                            list = tree.list(artists.getHandle(info[1]));
                            if (list.size() != 0)
                            {
                                artist = artists.getHandle(info[1]);
                                tree.remove(artist);

                                for (int i = 0; i < list.size(); i++)
                                {
                                    System.out.println("The KVPair (|"
                                        + info[1] + "|,|" + list.get(i)
                                        + "|) is deleted from the tree.");
                                    System.out.println("The KVPair (|"
                                        + list.get(i) + "|,|" + info[1]
                                        + "|) is deleted from the tree.");

                                    if (i == list.size() - 1)
                                    {
                                        artists.remove(info[1]);
                                        System.out.println("|" + info[1]
                                            + "| is deleted from the "
                                            + "artist database.");
                                    }
                                    if (tree
                                        .search(songs.getHandle(
                                            list.get(i))) == null)
                                    {
                                        songs.remove(list.get(i));
                                        System.out.println("|" + list.get(i)
                                            + "| is deleted from the "
                                            + "song database.");
                                    }
                                }

                                list.clear();
                            }
                            else
                            {
                                System.out.println("|" + info[1]
                                    + "| does not "
                                    + "exist in the artist database.");
                            }

                            // if artist removal is successful (or, the artist
                            // does actually exists in the memory pool and was
                            // removed)

                            break;
                        case "song":
                            list = tree.list(songs.getHandle(info[1]));
                            if (list.size() != 0)
                            {
                                song = songs.getHandle(info[1]);
                                tree.remove(song);

                                for (int i = 0; i < list.size(); i++)
                                {
                                    System.out.println("The KVPair (|"
                                        + info[1] + "|,|" + list.get(i)
                                        + "|) is deleted from the tree.");
                                    System.out.println("The KVPair (|"
                                        + list.get(i) + "|,|" + info[1]
                                        + "|) is deleted from the tree.");

                                    if (i == list.size() - 1)
                                    {
                                        songs.remove(info[1]);
                                        System.out.println("|" + info[1]
                                            + "| is deleted from the "
                                            + "song database.");
                                    }
                                    if (tree.search(artists.getHandle(list
                                        .get(i))) == null)
                                    {
                                        artists.remove(list.get(i));
                                        System.out.println("|" + list.get(i)
                                            + "| is deleted from the "
                                            + "artist database.");
                                    }
                                }

                                list.clear();
                            }
                            else
                            {
                                System.out.println("|" + info[1]
                                    + "| does not "
                                    + "exist in the song database.");
                            }
                            // if song removal is successful (or, the song does
                            // actually exists in the memory pool and was
                            // removed)

                            break;
                        default:
                            break;
                    }
                    break;

                case "print":
                    switch (command[1].trim())
                    {
                        case "artist":
                            System.out.println(artists.print()
                                + "total artists: " + artists.usage());
                            break;
                        case "song":
                            System.out.println(songs.print() + "total songs: "
                                + songs.usage());
                            break;
                        case "blocks":
                            System.out.println(pool.dump());
                            break;
                        case "tree":
                            System.out.print(tree.print());
                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
        in.close();
    }


    // ----------------------------------------------------------
    /**
     * @return memory pool
     */
    public MemPool pool()
    {
        return pool;
    }


    // ----------------------------------------------------------
    /**
     * @return initial Hashtable size
     */
    public int initHashSz()
    {
        return initHashSz;
    }
}

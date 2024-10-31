import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Date;
import java.util.Set;
import java.util.Collection;
import java.util.Map;

public class Database {
    private String path;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean avail;
    private HashMap<String, Object> dict;
    private boolean DEBUG_MODE;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public Database()
    {
        this.path = "./db.dat";
        this.avail = false;
        this.DEBUG_MODE = false;
        this.init();
    }

    public Database(String path)
    {
        this.path = path;
        this.avail = false;
        this.DEBUG_MODE = false;
        this.init();
    }

    public Database(boolean DEBUG_MODE)
    {
        this.path = "./db.dat";
        this.avail = false;
        this.DEBUG_MODE = DEBUG_MODE;
        this.init();
    }

    public Database(String path, boolean DEBUG_MODE)
    {
        this.path = path;
        this.avail = false;
        this.DEBUG_MODE = DEBUG_MODE;
        this.init();
    }

    public void init() 
    {
        try {
            File file = new File(this.path);
            if(!file.isFile())
            {
                this.dict = new HashMap<String, Object>();
                this.avail = true;
            }
            else
            {
                FileInputStream fis = new FileInputStream(file);
                while(fis.available() > 0)
                {
                    this.ois = new ObjectInputStream(fis);
                    this.dict = (HashMap<String, Object>) this.ois.readObject();
                }
                if(this.ois != null)
                {
                    this.ois.close();
                    fis.close();
                }
                if(this.dict == null)
                {
                    this.dict = new HashMap<String, Object>();
                }
                this.avail = true;
            }

            this.oos = new ObjectOutputStream(new FileOutputStream(new File(this.path)));
            this.oos.writeObject(this.dict);
            this.oos.close();

            if(this.DEBUG_MODE)
            {
                System.out.println(ANSI_GREEN + "Initialized database at " + this.path + ANSI_RESET);
            }

        } catch (Exception e) {
            System.err.println(ANSI_RED + "An error occurred." + ANSI_RESET);
            e.printStackTrace();
            this.avail = false;
        }
    }

    public String getPath()
    {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        return this.path;
    }

    public Object set(Object id, Object obj) throws FileNotFoundException, IOException, ClassNotFoundException {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        this.dict.put(id.toString(), obj);
        this.write();
        return obj;
    }

    public Object get(Object id) {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        if(!this.has(id))
        {
            System.err.println(ANSI_RED + "ID {" + id.toString() + "} is not in the database" + ANSI_RESET);
            return null;
        }
        return this.dict.get(id.toString());
    }

    public boolean delete(Object id) throws FileNotFoundException, IOException, ClassNotFoundException {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return false;
        }
        if(!this.has(id))
        {
            System.err.println(ANSI_RED + "ID {" + id.toString() + "} is not in the database" + ANSI_RESET);
            return false;
        }
        else
        {
            this.dict.remove(id.toString());
            this.write();
            return true;
        }
    }

    public boolean has(Object id) {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return false;
        }
        return this.dict.containsKey(id.toString());
    }

    public boolean isEmpty()
    {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return false;
        }
        return this.dict.isEmpty();
    }
    
    public Set<String> keys() {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        return this.dict.keySet();
    }

    public Collection<Object> values() {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        return this.dict.values();
    }

    public Set<Map.Entry<String, Object>> entries() {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        return this.dict.entrySet();
    }

    public Integer size() {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        return this.dict.size();
    }

    public boolean clear() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return false;
        }
        this.oos = new ObjectOutputStream(new FileOutputStream(new File(this.path)));
        this.oos.writeObject(new HashMap<String, Object>());
        this.oos.close();
        this.dict.clear();
        if(this.DEBUG_MODE)
        {
            System.out.println(ANSI_GREEN + "Successfully cleared database to " + this.path + " at " + ANSI_BLUE + (new Date()).toString() + ANSI_RESET);
        }
        return true;
    }

    public Object read() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        this.ois = new ObjectInputStream(new FileInputStream(new File(this.path)));
        Object res = this.ois.readObject();
        this.ois.close();
        return res;
    }

    public Object write() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return null;
        }
        this.oos = new ObjectOutputStream(new FileOutputStream(new File(this.path)));
        this.oos.writeObject(this.dict);
        this.oos.close();
        if(this.DEBUG_MODE)
        {
            System.out.println(ANSI_GREEN + "Successfully saved database to " + this.path + " at " + ANSI_BLUE + (new Date()).toString() + ANSI_RESET);
        }
        return true;
    }
    
    public void backupTo(String path) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if(!this.avail)
        {
            System.err.println(ANSI_RED + "The database is not available" + ANSI_RESET);
            return;
        }
        Object data = this.read();
        this.oos = new ObjectOutputStream(new FileOutputStream(new File(path)));
        this.oos.writeObject(data);
        this.oos.close();
        if(this.DEBUG_MODE)
        {
            System.out.println(ANSI_GREEN + "Successfully copied database to " + path + ANSI_RESET);
        }
        //(new File(this.path)).delete();
        //this.path = path;
        return;
    }
}


class BookDB extends Database {
    public BookDB()
    {
        super("./bookdb.dat");
    }
}

class ToyDB extends Database {
    public ToyDB()
    {
        super("./toydb.dat");
    }
}

class UserDB extends Database {
    public UserDB()
    {
        super("./userdb.dat");
    }
}
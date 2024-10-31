import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class db_old {
    private String path;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean avail;

    public db_old()
    {
        this.path = "./db.dat";
        this.avail = false;
    }

    public db_old(String path)
    {
        this.path = path;
        this.avail = false;
    }

    public void init() 
    {
        try {
            this.ois = new ObjectInputStream(new FileInputStream(new File(this.path)));
            this.oos = new ObjectOutputStream(new FileOutputStream(new File(this.path)));
            this.avail = true;
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            this.avail = false;
        }
    }

    public Object read() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if(!this.avail)
        {
            System.err.println("The database is not available");
            return null;
        }
        return ois.readObject();
    }

    public Object write(Object obj) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if(!this.avail)
        {
            System.err.println("The database is not available");
            return null;
        }
        oos.writeObject(obj);
        return obj;
    }
}

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        ToyDB toydb = new ToyDB();
        BookDB bookdb = new BookDB();
        UserDB userdb = new UserDB();

        Database db = new Database();

        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("kai");
        customer.setAddress("ha noi");
        
        if(!db.has(1))
        {
            db.set(customer.getId(), customer);
        }

        Customer[] arr = new Customer[3];
        arr[0] = new Customer("BCM");
        arr[1] = new Customer(2, "mvnm", "Ha Noi");
        arr[2] = new Customer(102348);

        db.set("asdasyud8haishdoi", arr);

        ArrayList<Customer> list = new ArrayList<>(3);
        list.add(new Customer("pdd"));
        list.add(new Customer(2, "mvnm", "HCM City"));
        list.add(new Customer(128631237));
        db.set("list", list);

        System.out.println(db.get("list"));

        db.backupTo("./backup.dat");
        
        System.out.println(db.keys());
        db.delete("asdhasdaisd");
        db.delete(1);
        System.out.println(db.keys());

        System.out.println("\n\n");
        System.out.println(db.entries());

        db.clear();
    }
}

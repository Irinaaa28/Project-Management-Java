import java.util.ArrayList;
import java.util.List;

public class UserService 
{
    private List<Customer> customers = new ArrayList<>();
    private List<Manager> managers = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    public void addUser(User user) 
    {
        if (user instanceof Customer) 
            customers.add((Customer) user);
        else if (user instanceof Manager) 
            managers.add((Manager) user);
        else if (user instanceof Member) 
            members.add((Member) user);
        else 
            System.out.println("Invalid user type.");
    }
    public void removeUser(User user) 
    {
        if (user instanceof Customer) 
            customers.remove(user);
        else if (user instanceof Manager) 
            managers.remove(user);
        else if (user instanceof Member) 
            members.remove(user);
        else 
            System.out.println("Invalid user type.");
    }
    public List<User> getUsers(String type)
    { 
        if (type.toUpperCase().equals("CUSTOMER")) 
            return new ArrayList<>(customers);
        else if (type.toUpperCase().equals("MANAGER")) 
            return new ArrayList<>(managers);
        else if (type.toUpperCase().equals("MEMBER")) 
            return new ArrayList<>(members);
        else 
        {
            System.out.println("Invalid user type.");
            return null;
        }
    }
}

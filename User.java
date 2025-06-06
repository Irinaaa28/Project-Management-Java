public abstract sealed class User permits Manager, Member, Customer
{
    protected int id;
    protected String name;
    protected String email;

    public User(int id, String name, String email) 
    {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(User user)
    {
        this.id = user.id;
        this.name = user.name;
        this.email = user.email;
    }
    public User()
    {
        this.name = "John Doe";
        this.email = "johndoe@email.com";
    }
    public int getId()
    {
        return id;
    }
    public String getName() 
    {
        return name;
    }
    public void setName(String name) 
    {
        this.name = name;
    }
    public String getEmail() 
    {
        return email;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }
    public abstract String getRole();
}
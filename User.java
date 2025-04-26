public abstract class User 
{
    protected String name;
    protected String email;

    public User(String name, String email) 
    {
        this.name = name;
        this.email = email;
    }
    public User()
    {
        this.name = "John Doe";
        this.email = "johndoe@email.com";
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
public final class Manager extends User 
{
    private String department;
    private final String managerID;

    public Manager(int id, String name, String email, String department, String managerID) 
    {
        super(id, name, email);
        this.department = department.toUpperCase();
        this.managerID = managerID;
    }
    public Manager(int id, String name, String email, String managerID) 
    {
        super(id, name, email);
        this.department = "None";
        this.managerID = managerID;
    }
    public Manager() 
    {
        super();
        this.department = "None";
        this.managerID = "None";
    }

    public Manager(Manager manager) 
    {
        super(manager);
        this.department = manager.department;
        this.managerID = manager.managerID;
    }

    public String getManagerID() 
    {
        return managerID;
    }
    public String getName() 
    {
        return super.getName();
    }
    public void setName(String name) 
    {
        super.setName(name);
    }
    public String getEmail() 
    {
        return super.getEmail();
    }
    public void setEmail(String email) 
    {
        super.setEmail(email);
    }
    public String getDepartment() 
    {
        return department;
    }
    public void setDepartment(String department) 
    {
        try 
        {
            switch(department.toUpperCase())
            {
                case "PROJECT", "MARKETING", "HR" -> this.department = department.toUpperCase();
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Invalid department.");
        }
    }
    @Override
    public String getRole() 
    {
        if (department.compareTo("None") != 0)
            return department + " Manager";
        return "Manager";
    }
    
}

public final class Member extends User
{
    private String level;
    private final String memberID;

    public Member(String name, String email, String level, String memberID) 
    {
        super(name, email);
        this.level = level.toUpperCase();
        this.memberID = memberID;
    }
    public Member(String name, String email, String memberID) 
    {
        super(name, email);
        this.level = "None";
        this.memberID = memberID;
    }
    public Member()
    {
        super();
        this.level = "None";
        this.memberID = "None";
    }
    public String getLevel() 
    {
        return level;
    }
    public void setLevel(String level) 
    {
        try
        {
            switch(level.toUpperCase())
            {
                case "JUNIOR", "SENIOR", "LEAD" -> this.level = level.toUpperCase();
            }
        }
        catch (Exception e)
        {
            System.out.println("Invalid level.");
        }
    }
    public String getMemberID() 
    {
        return memberID;
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

    @Override
    public String getRole() 
    {
        return "Team Member";
    }
    
}

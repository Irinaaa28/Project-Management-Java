import java.time.LocalDate;

public class Invoice 
{
    private static int idCounter;

    private final int id;
    private final Project project;
    private final LocalDate releaseDate;
    private final double amount;
    private boolean isReleased;

    static 
    {
        idCounter = 100;
    }
    {
        this.id = ++idCounter;
    }

    public Invoice(Project project)
    {
        this.project = project;
        this.releaseDate = LocalDate.now();
        project.calculateFinalPrice();
        this.amount = project.getFinalPrice();
        this.isReleased = false;
    }

    //generateInvoice - de pus in service

    public int getId() 
    {
        return id;
    }
    public Project getProject() 
    {
        return project;
    }
    public LocalDate getReleaseDate() 
    {
        return releaseDate;
    }
    public double getAmount() 
    {
        return amount;
    }
    public boolean isReleased() 
    {
        return isReleased;
    }
    public void setReleased(boolean isReleased) 
    {
        this.isReleased = isReleased;
    }
}

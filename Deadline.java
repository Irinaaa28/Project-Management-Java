import java.time.LocalDate;

public class Deadline 
{
    
    private LocalDate date;

    public Deadline(LocalDate date) 
    {
        this.date = date;
    }
    public Deadline(Deadline d)
    {
        this.date = d.date;
    }
    public Deadline()
    {
        this.date = null;
    }
    public LocalDate getDate() 
    {
        return this.date;
    }
    public void setDate(LocalDate date) 
    {
        this.date = date;
    }

    public boolean isOverdue() 
    {
        return LocalDate.now().isAfter(this.date);
    }
    public boolean isToday() 
    {
        return LocalDate.now().isEqual(this.date);
    }


}

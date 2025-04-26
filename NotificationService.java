import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NotificationService 
{

    public void threeSecondsTimer()
    {
        try {
            Thread.sleep(3000); // întârziere de 3 secunde (3000 milisecunde)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //checked
    public void NotifAssign(Task task)
    {
        if (task.getAssignedMember() != null)
        {
            threeSecondsTimer();
            System.out.println("Notification: Task " + task.getName() + " has been assigned to " + task.getAssignedMember().getName() + ".");
        }
    }
    //checked
    public void NotifDeadline(Task task)
    {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline().getDate());
        threeSecondsTimer();
        if (daysLeft == 1)
            System.out.println("Notification: Task " + task.getName() + " is due tomorrow.");
        else if (daysLeft == 0)
            System.out.println("Notification: Task " + task.getName() + " is due today.");
        else if (daysLeft < 0)
            System.out.println("Notification: Task " + task.getName() + " is overdue by " + Math.abs(daysLeft) + " days.");
    }
    //checked
    public void NotifDeadline(Project project)
    {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), project.getDeadline().getDate());
        threeSecondsTimer();
        if (daysLeft == 1)
            System.out.println("Notification: Project " + project.getName() + " is due tomorrow.");
        else if (daysLeft == 0)
            System.out.println("Notification: Project " + project.getName() + " is due today.");
        else if (daysLeft < 0)
            System.out.println("Notification: Project " + project.getName() + " is overdue by " + Math.abs(daysLeft) + " days.");
    }
}

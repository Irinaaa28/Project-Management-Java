import java.time.LocalDate;

public class Task 
{
    private static int idCounter;
    private final int id;
    private String name;
    private Status status;
    private Priority priority;
    private Deadline deadline;
    private Member assignedMember;

    static 
    {
        idCounter = 0;
    }

    {
        this.id = ++idCounter;
    }

    public Task(String name, Priority priority, Deadline deadline, Member assignedMember) 
    {
        this.name = name;
        this.status = Status.TO_DO;
        this.priority = priority;
        this.deadline = new Deadline(deadline);
        this.assignedMember = assignedMember;
    }

    public Task(String name, Priority priority, Deadline deadline) 
    {
        this.name = name;
        this.status = Status.TO_DO;
        this.priority = priority;
        this.deadline = new Deadline(deadline);
        this.assignedMember = null;
    }

    public Task(String name)
    {
        this.name = name;
        this.status = Status.TO_DO;
        this.priority = Priority.LOW;
        this.deadline = new Deadline();
        this.assignedMember = null;
    }

    public Task(Task task) 
    {
        this.name = task.name;
        this.status = task.status;
        this.priority = task.priority;
        this.deadline = new Deadline(task.deadline);
        this.assignedMember = task.assignedMember;
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
    public Status getStatus() 
    {
        return status;
    }
    public void setStatus(Status status) 
    {
        if (this.status == Status.DONE) 
        {
            System.out.println("Task is already completed. Cannot change status.");
            return;
        }
        this.status = status;
    }
    public Priority getPriority() 
    {
        return priority;
    } 
    public void setPriority(Priority priority) 
    {
        if (this.priority == priority) 
        {
            System.out.println("Task is already in this priority. Cannot change priority.");
            return;
        }
        if (this.status == Status.DONE) 
        {
            System.out.println("Task is already completed. Cannot change priority.");
            return;
        }
        this.priority = priority;
        System.out.println("Priority changed to " + priority + " successfully!");
    }
    public Deadline getDeadline() 
    {
        return deadline;
    }
    public void setDeadline(Deadline deadline) 
    {
        if (deadline == null) 
        {
            System.out.println("Invalid deadline. Cannot set deadline.");
            return;
        }
        if (deadline.getDate().isBefore(LocalDate.now())) 
        {
            System.out.println("Invalid deadline. Cannot set deadline before the current time.");
            return;
        }
        this.deadline = deadline;
    }
    public Member getAssignedMember() 
    {
        return assignedMember;
    }
    public void setAssignedMember(Member assignedMember) 
    {
        this.assignedMember = assignedMember;
    }
    public boolean isComplete()
    {
        return status == Status.DONE;
    }
}

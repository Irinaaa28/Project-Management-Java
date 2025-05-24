import java.time.LocalDate;
import java.util.*;


public class Project 
{
    private static int idProjectCounter;
    private final int id;
    private final String name;
    private Manager manager;
    private List<Task> tasks;
    private Set<Member> members;
    private final Customer customer;
    private Deadline deadline;
    private int estimatePrice;
    private double finalPrice;
    private double progress;
    private int lateDays;

    static 
    {
        idProjectCounter = 0;
    }

    {
        this.id = ++idProjectCounter;
    }

    private int getRandomPrice()
    {
        int[] prices = {1000, 1500, 2000, 2500, 3000};
        Random random = new Random();
        return prices[random.nextInt(prices.length)];
    }

    //checked
    public Project(String name, Manager manager, Customer customer, Deadline deadline) throws Exception 
    {
        this.name = name;

        // try
        // {
        //     if (manager.getDepartment().compareTo("PROJECT") == 0)
        //         this.manager = manager;
        //     else
        //         throw new Exception("The manager must be a project manager.");
        // }
        // catch (Exception e)
        // {
        //     System.out.println(e.getMessage());
        //     this.manager = null;
        // }

        if (manager.getDepartment().compareTo("PROJECT") == 0)
                this.manager = manager;
        else
        {
            this.manager = null;
            throw new Exception("The manager must be a project manager.");
        }

        this.customer = customer;
        
        // try
        // {
        //     if (deadline.getDate().isBefore(LocalDate.now()))
        //         throw new Exception("Invalid deadline. Cannot set deadline before the current time.");
        //     this.deadline = new Deadline(deadline);
        // }
        // catch (Exception e)
        // {
        //     System.out.println(e.getMessage());
        //     this.deadline = new Deadline();
        //     return;

        // }

        if (deadline.getDate().isBefore(LocalDate.now()))
            throw new Exception("Invalid deadline. Cannot set deadline before the current time.");

        this.deadline = new Deadline(deadline);
        this.estimatePrice = getRandomPrice();
        this.finalPrice = estimatePrice;
        this.tasks = new ArrayList<>();
        this.members = new HashSet<>();
        this.progress = 0.0;
        this.lateDays = 0;
    }

    //checked
    public Project(String name, Customer customer, Deadline deadline)
    {
        this.name = name;
        this.manager = null;
        this.customer = customer;
        this.deadline = new Deadline(deadline);
        this.estimatePrice = getRandomPrice();
        this.finalPrice = estimatePrice;
        this.tasks = new ArrayList<>();
        this.members = new HashSet<>();
        this.progress = 0.0;
        this.lateDays = 0;
    }

    public int getId() 
    {
        return id;
    }
    public String getName() 
    {
        return name;
    }
    public Manager getManager() 
    {
        return manager;
    }

    //checked
    public void setManager(Manager manager) 
    {
        try
        {
            if (manager.getDepartment().compareTo("PROJECT") == 0)
                this.manager = manager;
            else
                throw new Exception("The manager must be a project manager.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public Customer getCustomer() 
    {
        return customer;
    }
    public Deadline getDeadline() 
    {
        return deadline;
    }

    //checked
    public void setDeadline(Deadline deadline) 
    {
        this.deadline = new Deadline(deadline);
        // try
        // {
        //     if (deadline.getDate().isBefore(LocalDate.now()))
        //         throw new Exception("Invalid deadline. Cannot set deadline before the current time.");
        //     else
        //         this.deadline = new Deadline(deadline);
        // }
        // catch (Exception e)
        // {
        //     System.out.println(e.getMessage());
        // }
    }
    public int getEstimatePrice() 
    {
        return estimatePrice;
    }
    public double getFinalPrice() 
    {
        return finalPrice;
    }

    //de pus in service
    public void calculateFinalPrice() 
    {
        for (Task task: tasks)
        {
            if(task.getStatus() != Status.DONE)
            {
                System.out.println("Task is not completed. Cannot calculate final price.");
                return;
            }
            if (lateDays < LocalDate.now().getDayOfYear() - task.getDeadline().getDate().getDayOfYear())
                lateDays = LocalDate.now().getDayOfYear() - task.getDeadline().getDate().getDayOfYear();
        }
        if (lateDays < LocalDate.now().getDayOfYear() - deadline.getDate().getDayOfYear())
                lateDays = LocalDate.now().getDayOfYear() - deadline.getDate().getDayOfYear();
        if (lateDays > 0)
            finalPrice = estimatePrice - (lateDays * 5 /  100) * estimatePrice;
    }
    public List<Task> getTasks() 
    {
        return tasks;
    }
    public Set<Member> getMembers() 
    {
        return members;
    }

    //de pus in service
    public void addTask(Task task)
    {
        if (task == null) 
        {
            System.out.println("Invalid task. Cannot add task.");
            return;
        }
        tasks.add(task);
    }

    //de pus in service
    public void removeTask(Task task) throws Exception
    {
        if (task == null) 
            throw new Exception("Invalid task. Cannot remove task.");
        if (!tasks.contains(task)) 
            throw new Exception("Task not found in the project. Cannot remove task.");
        tasks.remove(task);
    }

    //de pus in service
    //checked
    public void addMember(Member member) throws Exception
    {
        int juniors = 0, seniors = 0, leads = 0;
        if (member == null) 
        {
            System.out.println("Invalid member. Cannot add member.");
            return;
        }
        for (Member m: members)
        {
            if (m.getLevel().compareTo("JUNIOR") == 0)
                juniors++;
            else if (m.getLevel().compareTo("SENIOR") == 0)
                seniors++;
            else if (m.getLevel().compareTo("LEAD") == 0)
                leads++;
        }
        if (member.getLevel().compareTo("JUNIOR") == 0)
        {
            if (juniors < 2)
            {
                members.add(member);
            }
            else 
                throw new Exception("Too many juniors in the team.");
        }
        else if (member.getLevel().compareTo("SENIOR") == 0)
        {
            if (seniors < 2)
                members.add(member);
            else 
                throw new Exception("Too many seniors in the team.");
        }
        else if (member.getLevel().compareTo("LEAD") == 0)
        {
            if (leads < 1)
                members.add(member);
            else 
                throw new Exception("Too many leads in the team.");
        }
    }

    //checked
    public void removeMember(Member member) throws Exception
    {
        if (member == null) 
            throw new Exception("Invalid member. Cannot remove member.");
        if (!members.contains(member)) 
            throw new Exception("Member not found in the project. Cannot remove member.");
        members.remove(member);
    }
    
    public double getProgress()
    {
        if (progress == 0)
            return 0;
        int numOfTasks = tasks.size();
        int completedTasks = 0;
        for (Task task: tasks)
        {
            if (task.getStatus() == Status.DONE)
                completedTasks++;
        }
        progress = (double) completedTasks / numOfTasks * 100;
        return progress;
    }

    public int getLateDays() 
    {
        return lateDays;
    }
}

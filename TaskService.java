import java.util.*;

public class TaskService 
{
    //checked
    public void createTask(Project project, String name, Priority priority, Deadline deadline, Member assignedMember) throws Exception
    {
        long count = project.getTasks().stream().filter(t -> t.getAssignedMember() != null && t.getAssignedMember().equals(assignedMember)).count();
        if (count >= 3) 
            throw new Exception("Member " + assignedMember.getName() + " already has 3 tasks assigned. Cannot assign more tasks.");
        project.addTask(new Task(name, priority, deadline, assignedMember));
        System.out.println("Task " + name + " created and assigned to " + assignedMember.getName() + " successfully!");
    }

    //checked
    public void createTask(Project project, String name) 
    {
        project.addTask(new Task(name));
        System.out.println("Task " + name + " created successfully!");
    }

    //checked
    public void assignTask(Task task, Member member, Project project) 
    {
        long count = project.getTasks().stream().filter(t -> t.getAssignedMember() != null && t.getAssignedMember().equals(member)).count();
        if (count >= 3) {
            System.out.println("Member " + member.getName() + " already has 3 tasks assigned. Cannot assign more tasks.");
            return;
        }
        try
        {
            project.removeTask(task);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
            task.setAssignedMember(member);
        project.addTask(task);
        System.out.println("Task assigned to " + member.getName() + " successfully!");
    }

    //checked
    public void modifyStatus(Task task, Status status) 
    {
        task.setStatus(status);
        System.out.println("Task status modified to " + status + " successfully!");
    }
    public void modifyPriority(Task task, Priority priority) 
    {
        task.setPriority(priority);
    }

    public void modifyDeadline(Task task, Deadline deadline) 
    {
        task.setDeadline(deadline);
        System.out.println("Task deadline modified to " + deadline.getDate().toString() + " successfully!");
    }
    public void modifyDeadline(Project project, Deadline deadline) 
    {
        project.setDeadline(deadline);
        System.out.println("Project deadline modified to " + deadline.getDate().toString() + " successfully!");
    }

    //checked
    public void showDetailsTask(Task task) 
    {
        System.out.println("Task Name: " + task.getName());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Deadline: " + task.getDeadline().getDate().toString());
        System.out.println("Assigned Member: " + (task.getAssignedMember() != null ? task.getAssignedMember().getName() : "None"));
    }
    //checked
    public void displayTasks(Project project)
    {
        for (Task task : project.getTasks()) 
        {
            System.out.println("Task Name: " + task.getName() + ", Status: " + task.getStatus() + ", Priority: " + task.getPriority() + ", Deadline: " + task.getDeadline().getDate().toString());
        }
    }
    //checked
    public void displayTasksByUser(List<Project> projects, Member member) 
    {
        for (Project project : projects) 
            for (Task task : project.getTasks()) 
                if (task.getAssignedMember() != null && task.getAssignedMember().equals(member)) 
                    System.out.println("Task Name: " + task.getName() + ", Status: " + task.getStatus() + ", Priority: " + task.getPriority() + ", Deadline: " + task.getDeadline().getDate().toString()); 
    }

    public void displayTasksByPriority(List<Task> tasks)
    {
        tasks.sort(Comparator.comparing(Task::getPriority).reversed());
        for (Task t: tasks)
            System.out.println("Task Name: " + t.getName() + ", Status: " + t.getStatus() + ", Priority: " + t.getPriority() + ", Deadline: " + t.getDeadline().getDate().toString());
    }

    public void displayTasksByDeadline(List<Task> tasks)
    {
        tasks.sort(Comparator.comparing(t -> t.getDeadline().getDate()));
        for (Task t: tasks)
            System.out.println("Task Name: " + t.getName() + ", Status: " + t.getStatus() + ", Priority: " + t.getPriority() + ", Deadline: " + t.getDeadline().getDate().toString());
    }

    //checked
    public void deleteTask(Task task, Project project) 
    {
        try
        {
            project.removeTask(task);
            System.out.println("Task deleted successfully!");
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }
}

import java.util.*;

public class ProjectService 
{
    private List<Project> projects;

    public ProjectService()
    {
        this.projects = new ArrayList<>();
    }

    //checked
    public Project createProject(String name, Manager manager, Customer customer, Deadline deadline, int estimatePrice) throws Exception
    {
        
        Project project = new Project(name, manager, customer, deadline, estimatePrice);
        projects.add(project);
        //System.out.println("Project created successfully!");
        return project;
        
    }

    // checked
    public void addMember(Project project, Member member)
    {
        try
        {
            if (project.getMembers().contains(member))
                throw new Exception("Member already exists in the project.");
            project.addMember(member);
            System.out.println("Member added successfully!");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }


    //checked
    public void removeMember(Project project, Member member)
    {
        try
        {
            project.removeMember(member);
            System.out.println("Member removed successfully!");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //checked
    public void showDetailsProject(Project project)
    {
        System.out.println("Project ID: " + project.getId());
        System.out.println("Project Name: " + project.getName());
        System.out.println("Manager: " + project.getManager().getName());
        System.out.println("Customer: " + project.getCustomer().getName());
        System.out.println("Deadline: " + project.getDeadline().getDate().toString());
        System.out.println("Estimate Price: " + project.getEstimatePrice() + "$");
        System.out.println("Final Price: " + project.getFinalPrice() + "$");
        System.out.println("Progress: " + project.getProgress() + "%");
        System.out.println("Late Days: " + project.getLateDays());

        if (project.getMembers().isEmpty()) 
        {
            System.out.println("No members assigned to this project.");
        } 
        else 
        {
            System.out.println("Members: ");
            for (Member member : project.getMembers()) 
            {
                System.out.println(" - " + member.getName() + " (" + member.getLevel() + ")");
            }
        }
        if (project.getTasks().isEmpty()) 
        {
            System.out.println("No tasks assigned to this project.");
        } 
        else 
        {
            System.out.println("Tasks: ");
            for (Task task : project.getTasks()) 
            {
                System.out.println(" - " + task.getName() + " (Status: " + task.getStatus() + ", Priority: " + task.getPriority() + ")");
            }
        }
    }

    //checked
    public void showAllProjects()
    {
        for (Project project : projects) {
            System.out.println("Project ID: " + project.getId() + ", Name: " + project.getName() + ", Manager: " + project.getManager().getName());
        }
    }
    //checked
    public void deleteProject(Project project)
    {
        try
        {
            if (!projects.contains(project))
                throw new Exception("Project not found.");
            else
            projects.remove(project);
            System.out.println("Project deleted successfully!");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public boolean isFinished(Project project)
    {
        return project.getProgress() == 100;
    }

    public List<Project> getProjects()
    {
        return projects;
    }

    public List<Task> getTasks(Project project)
    {
        return project.getTasks();
    }

}

import java.time.LocalDate;
import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        ProjectService projectService = new ProjectService();
        TaskService taskService = new TaskService();
        NotificationService notificationService = new NotificationService();
        InvoiceService invoiceService = new InvoiceService();

        Manager manager1 = new Manager("Andrei Popescu", "andreipopescu@yahoo.com", "project");
        Manager manager2 = new Manager("Mihaela Dascalu", "mihaeladascalu@gmail.com", "hr");
        Manager manager3 = new Manager("Daniela Rosca", "danielarosca@gmail.com", "Marketing");

        Member member1 = new Member("Matei Dinu", "mateidinu@yahoo.com", "Senior");
        Member member2 = new Member("Ana Damian", "anadamian@gmail.com", "Junior");
        Member member3 = new Member("Anca Dima", "ancadima@gmail.com", "Senior");
        Member member4 = new Member("Alexandru Stanescu", "alexstan@gmail.com", "Junior");
        Member member5 = new Member("Stefania Maxim", "stefaniamaxim@gmail.com", "Junior");

        Customer customer1 = new Customer("Alexandru Mihaescu", "alexandrumihaescu@gmail.com", "1002392");
        Customer customer2 = new Customer("Stefan Andrei", "stefanandrei@yahoo.com", "3629101");

        // while (true)
        // {
        //     System.out.println("Welcome to the Project Management Platform!");
        //     System.out.println("Before we start, you should sign in first.");
        //     System.out.println("Please say which type of user you are: ");
        //     System.out.println("1. Manager");
        //     System.out.println("2. Member");
        //     System.out.println("3. Customer");
        //     String userType = scanner.nextLine();
        //     if (userType.toUpperCase().equals("MANAGER")) {
        //         System.out.println("Please enter your manager ID to login.");
        //         String managerID = scanner.nextLine();
                
        //     } else if (userType.equals("2")) {
        //         System.out.println("You are logged in as a Member.");
        //         break;
        //     } else if (userType.equals("3")) {
        //         System.out.println("You are logged in as a Customer.");
        //         break;
        //     } else {
        //         System.out.println("Invalid input. Please try again.");
        //     }
        // }













        try 
        {
            Project project1 = projectService.createProject("Project Management Platform", manager1, customer1, new Deadline(LocalDate.of(2025, 4, 30)), 3000);
            System.out.println("Project created successfully!");


            //check if more than two juniors can be added to the project: no!                     expected answer: no!
            // projectService.addMember(project1, member2);
            // projectService.addMember(project1, member4);
            // projectService.addMember(project1, member5);

            //check if a member can be removed twice from the project: no!                        expected answer: no!
            // projectService.addMember(project1, member2);
            // projectService.removeMember(project1, member2);
            // projectService.removeMember(project1, member2);

            //projectService.showDetailsProject(project1);

            //check if a project can be deleted twice: no!                                        expected answer: no!
            // projectService.deleteProject(project1);
            // projectService.deleteProject(project1);
            
            projectService.addMember(project1, member1);
            projectService.addMember(project1, member2);
            projectService.addMember(project1, member5);

            taskService.createTask(project1, "Create project plan", Priority.HIGH, new Deadline(LocalDate.of(2025, 4, 15)), member1);
            taskService.createTask(project1, "Design database schema", Priority.MEDIUM, new Deadline(LocalDate.of(2025, 4, 20)), member2);
            taskService.createTask(project1, "Implement user authentication", Priority.LOW, new Deadline(LocalDate.of(2025, 4, 25)), member1);
            taskService.createTask(project1, "Develop user interface", Priority.HIGH, new Deadline(LocalDate.of(2025, 4, 28)), member2);
            taskService.createTask(project1, "Test project features", Priority.MEDIUM, new Deadline(LocalDate.of(2025, 4, 29)), member1);

            //check if a task can be assigned to a member who already has 3 tasks: no!            expected answer: no!
            //taskService.createTask(project1, "Prepare project documentation", Priority.LOW, new Deadline(LocalDate.of(2025, 4, 30)), member1);
            // taskService.createTask(project1, "Prepare project documentation");
            // taskService.assignTask(project1.getTasks().get(5), member1, project1);
            
            
            Task task1 = project1.getTasks().get(0);
            taskService.assignTask(task1, member2, project1);
            notificationService.NotifAssign(task1);
            Task task3 = project1.getTasks().get(2);

            //check if modify functions work: yes!                                                expected answer: yes!
            // taskService.modifyStatus(task1, Status.IN_PROGRESS);
            // taskService.modifyPriority(task3, Priority.MEDIUM);
            // taskService.modifyDeadline(task3, new Deadline(LocalDate.of(2025, 4, 27)));

            //check the display functions: yes!                                                  expected answer: yes!
            //taskService.showDetailsTask(task1);
            //taskService.displayTasks(project1);
            //taskService.displayTasksByUser(projectService.getProjects(), member2);
            //taskService.displayTasksByPriority(project1.getTasks());
            //taskService.displayTasksByDeadline(project1.getTasks());

            //check if a task can be deleted twice: no!                                          expected answer: no!
            // taskService.deleteTask(task3, project1);
            // taskService.deleteTask(task3, project1);

            // notificationService.NotifDeadline(task1);
            // notificationService.NotifDeadline(project1);

            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        projectService.showAllProjects();
    }
    
}

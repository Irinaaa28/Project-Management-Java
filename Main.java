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
        UserService userService = new UserService();

        Manager manager1 = new Manager("Andrei Popescu", "andreipopescu@yahoo.com", "project", "projectmanager");
        Manager manager2 = new Manager("Mihaela Dascalu", "mihaeladascalu@gmail.com","hr", "hrmanager");
        Manager manager3 = new Manager("Daniela Rosca", "danielarosca@gmail.com", "Marketing", "marketingmanager");

        Member member1 = new Member("Matei Dinu", "mateidinu@yahoo.com", "Senior");
        Member member2 = new Member("Ana Damian", "anadamian@gmail.com", "Junior");
        Member member3 = new Member("Anca Dima", "ancadima@gmail.com", "Senior");
        Member member4 = new Member("Alexandru Stanescu", "alexstan@gmail.com", "Junior");
        Member member5 = new Member("Stefania Maxim", "stefaniamaxim@gmail.com", "Junior");

        Customer customer1 = new Customer("Alexandru Mihaescu", "alexandrumihaescu@gmail.com", "100");
        Customer customer2 = new Customer("Stefan Andrei", "stefanandrei@yahoo.com", "101");

        userService.addUser(manager1);
        userService.addUser(manager2);
        userService.addUser(manager3);
        userService.addUser(member1);
        userService.addUser(member2);
        userService.addUser(member3);
        userService.addUser(member4);
        userService.addUser(member5);
        userService.addUser(customer1);
        userService.addUser(customer2);

        boolean isRunning = true;
        while (isRunning)
        {
            System.out.println("Welcome to the Project Management Platform!");
            System.out.println("Before we start, you should sign in first.");
            System.out.println("Please say which type of user you are: ");
            System.out.println("1. Customer");
            System.out.println("2. Team Member");
            System.out.println("3. Manager");
            System.out.println("4. Exit");

            String input = scanner.nextLine();
            switch (input)
            {
                case "1": // Customer
                    System.out.println("You have selected Customer.");
                    System.out.println("Please enter your customer ID to login.");
                    String customerID = scanner.nextLine();
                    if (customerID.equals(customer1.getCustomerID()) || customerID.equals(customer2.getCustomerID())) 
                    {
                        System.out.println("You are logged in as a Customer.");
                        isRunning = false;
                        boolean customerMenu = true;
                        while (customerMenu) 
                        {
                            System.out.println("Customer Menu:");
                            System.out.println("1. Request a new project");
                            System.out.println("2. Request the progress of a project");
                            System.out.println("3. Request an invoice");
                            System.out.println("4. Logout");
                            String customerChoice = scanner.nextLine();
                            
                            switch (customerChoice) 
                            {
                                case "1":
                                    Customer customer = (Customer) userService.getUserbyID(customerID);
                                    System.out.println("Please enter the project name:");
                                    String projectName = scanner.nextLine();
                                    System.out.println("Until when do you want the project to be done? (DD/MM/YYYY)");
                                    String deadlineInput = scanner.nextLine();
                                    LocalDate deadlineDate = LocalDate.parse(deadlineInput, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                    Deadline deadline = new Deadline(deadlineDate);

                                    System.out.println("Do you have a prefered manager? (yes/no)");
                                    String hasManager = scanner.nextLine();
                                    Manager assignedManager = null;
                                    if (hasManager.equalsIgnoreCase("yes")) 
                                    {
                                        System.out.println("Please enter the manager's name:");
                                        String managerName = scanner.nextLine();
                                        try
                                        {
                                            if (managerName.compareTo(manager1.getName()) == 0) 
                                                assignedManager = manager1;
                                            else if (managerName.equalsIgnoreCase(manager2.getName())) 
                                                assignedManager = manager2;
                                            else if (managerName.equalsIgnoreCase(manager3.getName())) 
                                                assignedManager = manager3;
                                            else 
                                                System.out.println("Manager not found. Please try again.");
                                            if (assignedManager != null) 
                                            {
                                                Project newProject = projectService.createProject(projectName, assignedManager, customer, deadline);
                                                System.out.println("Project created successfully!");
                                            }
                                        }
                                        catch (Exception e) 
                                        {
                                            System.out.println("Error creating project: " + e.getMessage());
                                        }
                                    } 
                                    else 
                                    {
                                        Project newProject = projectService.createProject(projectName, customer, deadline);
                                        System.out.println("Project created successfully!");
                                    }
                                    break;
                                case "2":
                                    System.out.println("Please enter the project ID to check its progress:");
                                    String projectID = scanner.nextLine();
                                    boolean found = false;
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        if (project.getId() == Integer.parseInt(projectID)) 
                                        {
                                            found = true;
                                            System.out.println("Progress: " + project.getProgress() + "%");
                                            break;
                                        }
                                    }
                                    if (!found) 
                                        System.out.println("Project not found. Please try again.");
                                    break;
                                case "3":
                                    System.out.println("Please enter the project ID to request an invoice:");
                                    String invoiceProjectID = scanner.nextLine();
                                    boolean found1 = false;
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        if (project.getId() == Integer.parseInt(invoiceProjectID)) 
                                        {
                                            found1 = true;
                                            try
                                            {
                                                Invoice invoice = invoiceService.generateInvoice(project);
                                                System.out.println("Invoice created successfully!");
                                            }
                                            catch (Exception e) 
                                            {
                                                System.out.println("Error generating invoice: " + e.getMessage());
                                            }
                                            break;
                                        }
                                    }
                                    if (!found1)
                                        System.out.println("Project not found. Please try again.");
                                    break;
                                case "4":
                                    customerMenu = false;
                                    isRunning = true;
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    } 
                    else 
                    {
                        System.out.println("Invalid Customer ID. Please try again.");
                    }
                    break;
            }
        }
    }













                // case "1": // Manager
                //     System.out.println("You have selected Manager.");
                //     System.out.println("Please enter your manager ID to login.");
                //     String managerID = scanner.nextLine();
                //     if (managerID.equals(manager1.getManagerID()) || managerID.equals(manager2.getManagerID()) || managerID.equals(manager3.getManagerID())) 
                //     {
                //         System.out.println("You are logged in as a Manager.");
                //         //isRunning = false;
                //         boolean managerMenu = true;
                //         while (managerMenu)
                //         {
                //             System.out.println("Manager Menu:
                //             System.out.println")
                //         }
                //     } 
                //     else 
                //     {
                //         System.out.println("Invalid Manager ID. Please try again.");
                //     }
                //     break;
        //}







    //     while (true)
    //     {
            
    //         String userType = scanner.nextLine();
    //         if (userType.toUpperCase().equals("MANAGER")) 
    //         {
    //             System.out.println("Please enter your manager ID to login.");
    //             String managerID = scanner.nextLine();
    //             if (managerID.equals(manager1.getId()) || managerID.equals(manager2.getId()) || managerID.equals(manager3.getId())) {
    //                 System.out.println("You are logged in as a Manager.");
    //             } else {
    //                 System.out.println("Invalid Manager ID. Please try again.");
    //             }
                
    //         } else if (userType.equals("2")) {
    //             System.out.println("You are logged in as a Member.");
    //             break;
    //         } else if (userType.equals("3")) {
    //             System.out.println("You are logged in as a Customer.");
    //             break;
    //         } else {
    //             System.out.println("Invalid input. Please try again.");
    //         }
    //     }













    //     try 
    //     {
    //         Project project1 = projectService.createProject("Project Management Platform", manager1, customer1, new Deadline(LocalDate.of(2025, 4, 30)), 3000);
    //         System.out.println("Project created successfully!");


    //         //check if more than two juniors can be added to the project: no!                     expected answer: no!
    //         // projectService.addMember(project1, member2);
    //         // projectService.addMember(project1, member4);
    //         // projectService.addMember(project1, member5);

    //         //check if a member can be removed twice from the project: no!                        expected answer: no!
    //         // projectService.addMember(project1, member2);
    //         // projectService.removeMember(project1, member2);
    //         // projectService.removeMember(project1, member2);

    //         //projectService.showDetailsProject(project1);

    //         //check if a project can be deleted twice: no!                                        expected answer: no!
    //         // projectService.deleteProject(project1);
    //         // projectService.deleteProject(project1);
            
    //         projectService.addMember(project1, member1);
    //         projectService.addMember(project1, member2);
    //         projectService.addMember(project1, member5);

    //         taskService.createTask(project1, "Create project plan", Priority.HIGH, new Deadline(LocalDate.of(2025, 4, 15)), member1);
    //         taskService.createTask(project1, "Design database schema", Priority.MEDIUM, new Deadline(LocalDate.of(2025, 4, 20)), member2);
    //         taskService.createTask(project1, "Implement user authentication", Priority.LOW, new Deadline(LocalDate.of(2025, 4, 25)), member1);
    //         taskService.createTask(project1, "Develop user interface", Priority.HIGH, new Deadline(LocalDate.of(2025, 4, 28)), member2);
    //         taskService.createTask(project1, "Test project features", Priority.MEDIUM, new Deadline(LocalDate.of(2025, 4, 29)), member1);

    //         //check if a task can be assigned to a member who already has 3 tasks: no!            expected answer: no!
    //         //taskService.createTask(project1, "Prepare project documentation", Priority.LOW, new Deadline(LocalDate.of(2025, 4, 30)), member1);
    //         // taskService.createTask(project1, "Prepare project documentation");
    //         // taskService.assignTask(project1.getTasks().get(5), member1, project1);
            
            
    //         Task task1 = project1.getTasks().get(0);
    //         taskService.assignTask(task1, member2, project1);
    //         notificationService.NotifAssign(task1);
    //         Task task3 = project1.getTasks().get(2);

    //         //check if modify functions work: yes!                                                expected answer: yes!
    //         // taskService.modifyStatus(task1, Status.IN_PROGRESS);
    //         // taskService.modifyPriority(task3, Priority.MEDIUM);
    //         // taskService.modifyDeadline(task3, new Deadline(LocalDate.of(2025, 4, 27)));

    //         //check the display functions: yes!                                                  expected answer: yes!
    //         //taskService.showDetailsTask(task1);
    //         //taskService.displayTasks(project1);
    //         //taskService.displayTasksByUser(projectService.getProjects(), member2);
    //         //taskService.displayTasksByPriority(project1.getTasks());
    //         //taskService.displayTasksByDeadline(project1.getTasks());

    //         //check if a task can be deleted twice: no!                                          expected answer: no!
    //         // taskService.deleteTask(task3, project1);
    //         // taskService.deleteTask(task3, project1);

    //         // notificationService.NotifDeadline(task1);
    //         // notificationService.NotifDeadline(project1);

            
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //     }
        
    //     projectService.showAllProjects();
    // }
    
}

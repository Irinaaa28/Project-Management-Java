import java.time.LocalDate;
import java.util.Iterator;
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

        //Test if db works
        ManagerDAO managerDAO = ManagerDAO.getInstance();
        try 
        {
            managerDAO.add(manager1);
            System.out.println("Manager 1 added successfully.");
        } 
        catch (Exception e) 
        {
            System.out.println("Error adding managers: " + e.getMessage());
        }

        Member member1 = new Member("Matei Dinu", "mateidinu@yahoo.com", "Senior", "1001");
        Member member2 = new Member("Ana Damian", "anadamian@gmail.com", "Junior", "1002");
        Member member3 = new Member("Anca Dima", "ancadima@gmail.com", "Senior", "1003");
        Member member4 = new Member("Alexandru Stanescu", "alexstan@gmail.com", "Junior", "1004");
        Member member5 = new Member("Stefania Maxim", "stefaniamaxim@gmail.com", "Junior", "1005");

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

        try
        {

            Project project1 = projectService.createProject("Project Management Platform", manager1, customer1, new Deadline(LocalDate.of(2025, 6, 30)));

            projectService.addMember(project1, member2);
            projectService.addMember(project1, member4);
            projectService.addMember(project1, member1);

            taskService.createTask(project1, "Create project plan", Priority.HIGH, new Deadline(LocalDate.of(2025, 6, 15)), member1);
            taskService.createTask(project1, "Design database schema", Priority.MEDIUM, new Deadline(LocalDate.of(2025, 10, 20)), member2);
            taskService.createTask(project1, "Implement user authentication", Priority.LOW, new Deadline(LocalDate.of(2025, 7, 25)), member1);
            taskService.createTask(project1, "Develop user interface", Priority.HIGH, new Deadline(LocalDate.of(2025, 7, 28)), member2);
            taskService.createTask(project1, "Test project features", Priority.MEDIUM, new Deadline(LocalDate.of(2025, 8, 29)), member1);
            
            Task task1 = project1.getTasks().get(0);
            taskService.assignTask(task1, member2, project1);

            projectService.showAllProjects();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }




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



                case "2": // Team Member
                    System.out.println("You have selected Team Member.");
                    System.out.println("Please enter your member ID to login.");
                    String memberID = scanner.nextLine();
                    if (memberID.equals(member1.getMemberID()) || memberID.equals(member2.getMemberID()) || memberID.equals(member3.getMemberID()) || memberID.equals(member4.getMemberID()) || memberID.equals(member5.getMemberID())) 
                    {
                        System.out.println("You are logged in as a Team Member.");
                        isRunning = false;
                        boolean memberMenu = true;
                        while (memberMenu) 
                        {
                            System.out.println("Member Menu:");
                            System.out.println("1. View a project");
                            System.out.println("2. View a task");
                            System.out.println("3. My projects");
                            System.out.println("4. My tasks");
                            System.out.println("5. Logout");
                            String memberChoice = scanner.nextLine();
                            
                            switch (memberChoice) 
                            {
                                case "1":
                                    System.out.println("Please enter the project ID to view its details:");
                                    String projectID = scanner.nextLine();
                                    boolean found3 = false;
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        if (project.getId() == Integer.parseInt(projectID)) 
                                        {
                                            found3 = true;
                                            System.out.println("a. Show tasks");
                                            System.out.println("b. Assign the project to yourself");
                                            System.out.println("c. Create a task");
                                            System.out.println("d. Exit");
                                            String projectChoice = scanner.nextLine();
                                            switch (projectChoice) 
                                            {
                                                case "a":
                                                    System.out.println("i. Simple display");
                                                    System.out.println("ii. Display by priority");
                                                    System.out.println("iii. Display by deadline");
                                                    String displayChoice = scanner.nextLine();
                                                    switch (displayChoice) 
                                                    {
                                                        case "i":
                                                            taskService.displayTasks(project);
                                                            break;
                                                        case "ii":
                                                            taskService.displayTasksByPriority(project.getTasks());
                                                            break;
                                                        case "iii":
                                                            taskService.displayTasksByDeadline(project.getTasks());
                                                            break;
                                                        default:
                                                            System.out.println("Invalid choice. Please try again.");
                                                    }
                                                    break;
                                                case "b":
                                                    if (!project.getMembers().contains(userService.getUserbyID(memberID))) 
                                                    {
                                                        projectService.addMember(project, (Member)userService.getUserbyID(memberID));
                                                        System.out.println("You have been assigned to the project.");
                                                    } 
                                                    else 
                                                        System.out.println("You are already assigned to this project.");
                                                    break;
                                                case "c":
                                                    System.out.println("Please enter the task name:");
                                                    String taskName = scanner.nextLine();
                                                    System.out.println("Please enter the task priority (HIGH, MEDIUM, LOW):");
                                                    String priorityInput = scanner.nextLine();
                                                    Priority priority = Priority.valueOf(priorityInput.toUpperCase());
                                                    System.out.println("Please enter the task deadline (DD/MM/YYYY):");
                                                    String deadlineInput = scanner.nextLine();
                                                    LocalDate deadlineDate = LocalDate.parse(deadlineInput, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                                    Deadline deadline = new Deadline(deadlineDate);
                                                    
                                                    try
                                                    {
                                                        taskService.createTask(project, taskName, priority, deadline, (Member) userService.getUserbyID(memberID));
                                                        System.out.println("Task created successfully!");
                                                    }
                                                    catch (Exception e) 
                                                    {
                                                        System.out.println("Error creating task: " + e.getMessage());
                                                    }
                                                    break;
                                                case "d":
                                                    break;
                                                default:
                                                    System.out.println("Invalid choice. Please try again.");
                                            }
                                            break;
                                        }
                                    }
                                    if (!found3) 
                                        System.out.println("Project not found. Please try again.");
                                    break;
                                case "2":
                                    System.out.println("Please enter the task ID to view its details:");
                                    String taskID = scanner.nextLine();
                                    boolean found4 = false;

                                    
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        Iterator<Task> it = project.getTasks().iterator();
                                        while (it.hasNext())
                                        {
                                            Task task = it.next();
                                            if (task.getId() == Integer.parseInt(taskID)) 
                                            {
                                                found4 = true;
                                                Task task1 = task;
                                                System.out.println("a. Deatils");
                                                System.out.println("b. Complete Task");
                                                System.out.println("c. Test Task");
                                                System.out.println("d. Modify priority");
                                                System.out.println("e. Modify deadline");
                                                System.out.println("f. Assign task to yourself");
                                                System.out.println("g. Exit");
                                                String taskChoice = scanner.nextLine();
                                                switch (taskChoice) 
                                                {
                                                    case "a":
                                                        taskService.showDetailsTask(task);
                                                        break;
                                                    case "b":
                                                        if (task.getStatus() == Status.TO_DO)
                                                        {
                                                            taskService.modifyStatus(task, Status.IN_PROGRESS);
                                                            System.out.println("Task marked as in progress.");
                                                        } 
                                                        else if (task.getStatus() == Status.IN_PROGRESS) 
                                                        {
                                                            taskService.modifyStatus(task, Status.TESTING);
                                                            System.out.println("Task marked as testing.");
                                                        } 
                                                        else 
                                                            System.out.println("Task is already completed or in testing phase.");
                                                        break;
                                                    case "c":
                                                        if (task.getStatus() == Status.TESTING)
                                                        {
                                                            int num = (int) (Math.random() * 10);
                                                            if (num < 5) 
                                                            {
                                                                taskService.modifyStatus(task, Status.DONE);
                                                                System.out.println("Task completed successfully!");
                                                            } 
                                                            else 
                                                                System.out.println("Task failed the testing phase. Please try again.");
                                                        }
                                                        else 
                                                            System.out.println("Task is not in testing phase. Cannot complete it.");
                                                        break;
                                                    case "d":
                                                        System.out.println("Please enter the new priority (HIGH, MEDIUM, LOW):");
                                                        String newPriorityInput = scanner.nextLine();
                                                        Priority newPriority = Priority.valueOf(newPriorityInput.toUpperCase());
                                                        taskService.modifyPriority(task, newPriority);
                                                        break;
                                                    case "e":
                                                        System.out.println("Please enter the new deadline (DD/MM/YYYY):");
                                                        String newDeadlineInput = scanner.nextLine();
                                                        LocalDate newDeadlineDate = LocalDate.parse(newDeadlineInput, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                                        Deadline newDeadline = new Deadline(newDeadlineDate);
                                                        taskService.modifyDeadline(task, newDeadline);
                                                        break;
                                                    case "f":
                                                        if (!task.getAssignedMember().equals(userService.getUserbyID(memberID))) 
                                                        {
                                                            taskService.assignTask(task, (Member) userService.getUserbyID(memberID), project);
                                                            System.out.println("You have been assigned to the task.");
                                                        } 
                                                        else 
                                                            System.out.println("You are already assigned to this task.");
                                                        break;
                                                    case "g":
                                                        break;
                                                    default:
                                                        System.out.println("Invalid choice. Please try again.");
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case "3":
                                    System.out.println("Your projects:");
                                    boolean found5 = false;
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        if (project.getMembers().contains(userService.getUserbyID(memberID))) 
                                        {
                                            found5 = true;
                                            System.out.println("Project ID: " + project.getId() + ", Name: " + project.getName());
                                        }
                                    }
                                    if (!found5) 
                                        System.out.println("You have no projects assigned.");
                                    break;
                                case "4":
                                    System.out.println("Your tasks:");
                                    boolean found6 = false;
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        for (Task task : project.getTasks()) 
                                        {
                                            if (task.getAssignedMember() != null && task.getAssignedMember().getMemberID().equals(memberID)) 
                                            {
                                                found6 = true;
                                                System.out.println("Task ID: " + task.getId() + ", Name: " + task.getName() + ", Status: " + task.getStatus());
                                            }
                                        }
                                    }
                                    if (!found6) 
                                        System.out.println("You have no tasks assigned.");
                                    break;
                                case "5":
                                    memberMenu = false;
                                    isRunning = true;
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    } 
                    else 
                    {
                        System.out.println("Invalid Manager ID. Please try again.");
                    }
                    break;
                case "3": // Manager
                    System.out.println("You have selected Manager.");
                    System.out.println("Please enter your manager ID to login.");
                    String managerID = scanner.nextLine();
                    if (managerID.equals(manager1.getManagerID()) || managerID.equals(manager2.getManagerID()) || managerID.equals(manager3.getManagerID()))
                    {
                        System.out.println("You are logged in as a  Manager.");
                        isRunning = false;
                        boolean managerMenu = true;
                        while (managerMenu) 
                        {
                            System.out.println("Manager Menu:");
                            System.out.println("1. Projects");
                            System.out.println("2. View a project");
                            System.out.println("3. View a task");
                            System.out.println("4. My projects");
                            System.out.println("5. Logout");
                            String memberChoice = scanner.nextLine();
                            
                            switch (memberChoice) 
                            {
                                case "1": 
                                    projectService.showAllProjects();
                                    break;
                                case "2":
                                    System.out.println("Please enter the project ID to view its details:");
                                    String projectID = scanner.nextLine();
                                    boolean found3 = false;
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        if (project.getId() == Integer.parseInt(projectID)) 
                                        {
                                            found3 = true;
                                            System.out.println("a. Show tasks");
                                            System.out.println("b. Assign the project to yourself");
                                            System.out.println("c. Create a task");
                                            System.out.println("d. Add a member to the project");
                                            System.out.println("e. Remove a member from the project");
                                            System.out.println("f. Generate invoice");
                                            System.out.println("g. Exit");
                                            String projectChoice = scanner.nextLine();
                                            switch (projectChoice) 
                                            {
                                                case "a":
                                                    System.out.println("i. Simple display");
                                                    System.out.println("ii. Display by priority");
                                                    System.out.println("iii. Display by deadline");
                                                    System.out.println("iv. Display by user");
                                                    String displayChoice = scanner.nextLine();
                                                    switch (displayChoice) 
                                                    {
                                                        case "i":
                                                            taskService.displayTasks(project);
                                                            break;
                                                        case "ii":
                                                            taskService.displayTasksByPriority(project.getTasks());
                                                            break;
                                                        case "iii":
                                                            taskService.displayTasksByDeadline(project.getTasks());
                                                            break;
                                                        case "iv":
                                                            System.out.println("Please enter the member ID to filter tasks by user:");
                                                            String membeID = scanner.nextLine();
                                                            Member member = (Member) userService.getUserbyID(membeID);
                                                            if (member != null) 
                                                            {
                                                                taskService.displayTasksByUser(projectService.getProjects(), member);
                                                            } 
                                                            else 
                                                                System.out.println("Member not found. Please try again.");
                                                            break;
                                                        default:
                                                            System.out.println("Invalid choice. Please try again.");
                                                    }
                                                    break;
                                                case "b":
                                                    if (!project.getMembers().contains(userService.getUserbyID(managerID))) 
                                                        projectService.assignManager(project, (Manager)userService.getUserbyID(managerID));
                                                    else 
                                                        System.out.println("You are already assigned to this project.");
                                                    break;

                                                case "c":
                                                    System.out.println("Please enter the task name:");
                                                    String taskName = scanner.nextLine();
                                                    System.out.println("Please enter the task priority (HIGH, MEDIUM, LOW):");
                                                    String priorityInput = scanner.nextLine();
                                                    Priority priority = Priority.valueOf(priorityInput.toUpperCase());
                                                    System.out.println("Please enter the task deadline (DD/MM/YYYY):");
                                                    String deadlineInput = scanner.nextLine();
                                                    LocalDate deadlineDate = LocalDate.parse(deadlineInput, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                                    Deadline deadline = new Deadline(deadlineDate);
                                                    
                                                    try
                                                    {
                                                        taskService.createTask(project, taskName, priority, deadline);
                                                    }
                                                    catch (Exception e) 
                                                    {
                                                        System.out.println("Error creating task: " + e.getMessage());
                                                    }
                                                    break;
                                                case "d":
                                                    System.out.println("Please enter the member ID to add to the project:");
                                                    String memberIDToAdd = scanner.nextLine();  
                                                    Member memberToAdd = (Member) userService.getUserbyID(memberIDToAdd);
                                                    if (memberToAdd != null) 
                                                        projectService.addMember(project, memberToAdd);
                                                    else 
                                                        System.out.println("Member not found. Please try again.");
                                                    break;
                                                case "e":
                                                    System.out.println("Please enter the member ID to remove from the project:");
                                                    String memberIDToRemove = scanner.nextLine();
                                                    Member memberToRemove = (Member) userService.getUserbyID(memberIDToRemove);
                                                    if (memberToRemove != null) 
                                                        projectService.removeMember(project, memberToRemove);
                                                    else 
                                                        System.out.println("Member not found. Please try again.");
                                                    break;
                                                case "f":
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
                                                case "g":
                                                    break;
                                                default:
                                                    System.out.println("Invalid choice. Please try again.");
                                            }
                                            break;
                                        } 
                                    }
                                    if (!found3) 
                                        System.out.println("Project not found. Please try again.");
                                    break;
                                case "3":
                                    System.out.println("Please enter the task ID to view its details:");
                                    String taskID = scanner.nextLine();
                                    boolean found6 = false;
                                    Task task1 = null;
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        Iterator<Task> it = project.getTasks().iterator();
                                        while (it.hasNext())
                                        {
                                            Task task = it.next();
                                            if (task.getId() == Integer.parseInt(taskID)) 
                                            {
                                                found6 = true;
                                                task1 = task;
                                                break;
                                            }
                                        }
                                        if (task1 != null)
                                        {
                                            System.out.println("a. Deatils");
                                            System.out.println("b. Modify priority");
                                            System.out.println("c. Modify deadline");
                                            System.out.println("d. Assign task to a member");
                                            System.out.println("e. Exit");
                                            String taskChoice = scanner.nextLine();
                                            switch (taskChoice) 
                                            {
                                                case "a":
                                                    taskService.showDetailsTask(task1);
                                                    break;
                                                case "b":
                                                    System.out.println("Please enter the new priority (HIGH, MEDIUM, LOW):");
                                                    String newPriorityInput = scanner.nextLine();
                                                    Priority newPriority = Priority.valueOf(newPriorityInput.toUpperCase());
                                                    taskService.modifyPriority(task1, newPriority);
                                                    break;
                                                case "c":
                                                    System.out.println("Please enter the new deadline (DD/MM/YYYY):");
                                                    String newDeadlineInput = scanner.nextLine();
                                                    LocalDate newDeadlineDate = LocalDate.parse(newDeadlineInput, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                                    Deadline newDeadline = new Deadline(newDeadlineDate);
                                                    taskService.modifyDeadline(task1, newDeadline);
                                                    break;
                                                case "d":
                                                    System.out.println("Please enter the member ID to assign the task to:");
                                                    String memberIDToAssign = scanner.nextLine();
                                                    Member memberToAssign = (Member) userService.getUserbyID(memberIDToAssign);
                                                    if (memberToAssign != null) 
                                                        taskService.assignTask(task1, memberToAssign, project);
                                                    else 
                                                        System.out.println("Member not found. Please try again.");
                                                    break;
                                                case "e":
                                                    break;
                                                default:
                                                    System.out.println("Invalid choice. Please try again.");
                                            }
                                        }
                                    }
                                    if (!found6) 
                                        System.out.println("Task not found. Please try again.");
                                    break;
                                case "4":
                                    System.out.println("Your projects:");
                                    boolean found7 = false;
                                    for (Project project : projectService.getProjects()) 
                                    {
                                        if (project.getManager().getManagerID().equals(managerID)) 
                                        {
                                            found7 = true;
                                            System.out.println("Project ID: " + project.getId() + ", Name: " + project.getName());
                                        }
                                    }
                                    if (!found7) 
                                        System.out.println("You have no projects assigned.");
                                    break;
                                case "5":
                                    managerMenu = false;
                                    isRunning = true;
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    }
                    else 
                        System.out.println("Invalid Manager ID. Please try again.");
                    break;
                case "4": // Exit
                    System.out.println("Exiting the application. Goodbye!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            
            }
        }
    }
}

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        CustomerService customerService = CustomerService.getInstance();
        ProjectService projectService = ProjectService.getInstance();
        TaskService taskService = TaskService.getInstance();
        InvoiceService invoiceService = InvoiceService.getInstance();
        ManagerService managerService = ManagerService.getInstance();
        MemberService memberService = MemberService.getInstance();


        //There are some examples of data that can be used to test the application.
        Manager manager1 = new Manager(1, "Andrei Popescu", "andreipopescu@yahoo.com", "project", "projectmanager");
        Manager manager2 = new Manager(2, "Mihaela Dascalu", "mihaeladascalu@gmail.com","hr", "hrmanager");
        Manager manager3 = new Manager(3, "Daniela Rosca", "danielarosca@gmail.com", "Marketing", "marketingmanager");

        Member member1 = new Member(101, "Matei Dinu", "mateidinu@yahoo.com", "Senior", "senior1");
        Member member2 = new Member(102, "Ana Damian", "anadamian@gmail.com", "Junior", "junior1");
        Member member3 = new Member(103, "Anca Dima", "ancadima@gmail.com", "Senior", "senior2");
        Member member4 = new Member(104, "Alexandru Stanescu", "alexstan@gmail.com", "Junior", "junior2");
        Member member5 = new Member(105, "Stefania Maxim", "stefaniamaxim@gmail.com", "Junior", "junior3");

        Customer customer1 = new Customer(1001, "Alexandru Mihaescu", "alexandrumihaescu@gmail.com", "#cust01");
        Customer customer2 = new Customer(1002, "Stefan Andrei", "stefanandrei@yahoo.com", "#cust02");

        try
        {
            Project project1 = new Project(1, "Project Management Platform", manager1, customer1, new Deadline(LocalDate.of(2025, 12, 31)));
            Project project2 = new Project(2, "E-commerce Website", customer2, new Deadline(LocalDate.of(2025, 11, 30)));
            // taskService.add(new Task(1, "Create project plan", Priority.HIGH, new Deadline(LocalDate.of(2025, 6, 15)), member1, project1));
            // taskService.add(new Task(2,"Design database schema", Priority.MEDIUM, new Deadline(LocalDate.of(2025, 10, 20)), member2, project1));
            // taskService.add(new Task(3, "Implement user authentication", Priority.LOW, new Deadline(LocalDate.of(2025, 7, 25)), member1, project1));
            // taskService.add(new Task (4, "Develop user interface", Priority.HIGH, new Deadline(LocalDate.of(2025, 7, 28)), member2, project1));
            // taskService.add(new Task(5, "Test project features", Priority.MEDIUM, new Deadline(LocalDate.of(2025, 8, 29)), member1, project1));
        }
        catch (Exception e)
        {
            System.out.println("Eroare: " + e.getMessage());
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
                    try
                    {
                        String customerID = scanner.nextLine();
                        List<Customer> customers = customerService.getAll();
                        for (Customer customer: customers)
                        {
                            if (customer.getCustomerID().equals(customerID))
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
                                                    List<Manager> managers = managerService.getAll();
                                                    for (Manager manager : managers) 
                                                    {
                                                        if (manager.getName().equalsIgnoreCase(managerName)) 
                                                        {
                                                            assignedManager = manager;
                                                            break;
                                                        }
                                                    }
                                                    if (assignedManager == null)
                                                        System.out.println("Manager not found. Please try again.");
                                                    if (assignedManager != null) 
                                                    {
                                                        int nextIndex = projectService.getAll().size() + 1;
                                                        projectService.add(new Project(nextIndex, projectName, assignedManager, customer, deadline));
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
                                                int nextIndex = projectService.getAll().size() + 1;
                                                projectService.add(new Project(nextIndex, projectName, customer, deadline));
                                                System.out.println("Project created successfully!");
                                            }
                                            break;
                                        case "2":
                                            System.out.println("Please enter the project ID to check its progress:");
                                            String projectID = scanner.nextLine();
                                            boolean found = false;
                                            for (Project project : projectService.getAll()) 
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
                                            for (Project project : projectService.getAll()) 
                                            {
                                                if (project.getId() == Integer.parseInt(invoiceProjectID)) 
                                                {
                                                    found1 = true;
                                                    try
                                                    {
                                                        invoiceService.add(new Invoice(project));
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
                                break;
                            }
                        }
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Invalid Customer ID. Please try again.");
                    }
                    break;

                case "2": // Team Member
                    System.out.println("You have selected Team Member.");
                    System.out.println("Please enter your member ID to login.");
                    try
                    {
                        String memberID = scanner.nextLine();
                        List<Member> members = memberService.getAll();
                        for (Member member : members) 
                        {
                            if (member.getMemberID().equals(memberID)) 
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
                                            for (Project project : projectService.getAll()) 
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
                                                                    taskService.getByProject(project);
                                                                    break;
                                                                case "ii":
                                                                    taskService.displayTasksByPriority();
                                                                    break;
                                                                case "iii":
                                                                    taskService.displayTasksByDeadline();
                                                                    break;
                                                                default:
                                                                    System.out.println("Invalid choice. Please try again.");
                                                            }
                                                            break;
                                                        case "b":
                                                            boolean alreadyAssigned = false;
                                                            List<Project> projects = projectService.getByMember(member.getId());
                                                            for (Project p : projects) 
                                                            {
                                                                if (p.getId() == project.getId()) 
                                                                {
                                                                    alreadyAssigned = true;
                                                                    System.out.println("You are already assigned to this project.");
                                                                    break;
                                                                }
                                                            } 
                                                            if (!alreadyAssigned)
                                                            {
                                                                projectService.addMember(project, member);
                                                                System.out.println("You have been assigned to the project.");
                                                            } 
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
                                                                int nextIndex = taskService.getAll().size() + 1;
                                                                taskService.add(new Task(nextIndex, taskName, priority, deadline, member, project));
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
                                            
                                            for (Project project : projectService.getAll()) 
                                            {
                                                Iterator<Task> it = projectService.getTasksByProject(project.getId()).iterator();
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
                                                                if (!(task.getAssignedMember().getId() == member.getId())) 
                                                                {
                                                                    taskService.assignTask(task, member);
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
                                            for (Project project : projectService.getByMember(member.getId())) 
                                            {
                                                    System.out.println("Project ID: " + project.getId() + ", Name: " + project.getName());
                                                    found5 = true;
                                            }
                                            if (!found5) 
                                                System.out.println("You have no projects assigned.");
                                            break;
                                        case "4":
                                            System.out.println("Your tasks:");
                                            boolean found6 = false;
                                            for (Project project : projectService.getAll()) 
                                            {
                                                for (Task task : projectService.getTasksByProject(project.getId())) 
                                                {
                                                    if (task.getAssignedMember() != null && task.getAssignedMember().getId() == member.getId()) 
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
                                break;
                            }
                        }
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Invalid Member ID. Please try again.");
                    }
                    break;
                case "3": // Manager
                    System.out.println("You have selected Manager.");
                    System.out.println("Please enter your manager ID to login.");
                    try
                    {
                        String managerID = scanner.nextLine();
                        List<Manager> managers = managerService.getAll();
                        for (Manager manager: managers)
                        {
                            if (manager.getManagerID().equals(managerID))
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
                                            List<Project> projectsD = projectService.getAll();
                                            if (projectsD.isEmpty()) 
                                            {
                                                System.out.println("No projects available.");
                                            } 
                                            else 
                                            {
                                                for (Project project : projectsD) 
                                                    System.out.println("Project ID: " + project.getId() + ", Name: " + project.getName());
                                            }
                                            break;
                                        case "2":
                                            System.out.println("Please enter the project ID to view its details:");
                                            String projectID = scanner.nextLine();
                                            boolean found3 = false;
                                            for (Project project : projectService.getAll()) 
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
                                                                    projectService.getTasksByProject(project.getId());
                                                                    break;
                                                                case "ii":
                                                                    taskService.displayTasksByPriority();
                                                                    break;
                                                                case "iii":
                                                                    taskService.displayTasksByDeadline();
                                                                    break;
                                                                case "iv":
                                                                    System.out.println("Please enter the member ID to filter tasks by user:");
                                                                    String membeID = scanner.nextLine();
                                                                    List<Member> members = memberService.getAll();
                                                                    for (Member member : members) 
                                                                    {
                                                                        if (member.getMemberID().equals(membeID)) 
                                                                        {
                                                                            found3 = true;
                                                                            taskService.displayTasksByUser(member);
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!found3) 
                                                                        System.out.println("Member not found. Please try again.");
                                                                    break;
                                                                default:
                                                                    System.out.println("Invalid choice. Please try again.");
                                                            }
                                                            break;
                                                        case "b":
                                                            boolean alreadyAssigned = false;
                                                            List<Project> projects = projectService.getByMember(manager.getId());
                                                            for (Project p : projects) 
                                                            {
                                                                if (p.getId() == project.getId()) 
                                                                {
                                                                    alreadyAssigned = true;
                                                                    System.out.println("You are already assigned to this project.");
                                                                    break;
                                                                }
                                                            } 
                                                            if (!alreadyAssigned)
                                                            {
                                                                projectService.assignManager(project, manager);
                                                                System.out.println("You have been assigned to the project.");
                                                            } 
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
                                                                int nextIndex = taskService.getAll().size() + 1;
                                                                taskService.add(new Task(nextIndex, taskName, priority, deadline, project));
                                                            }
                                                            catch (Exception e) 
                                                            {
                                                                System.out.println("Error creating task: " + e.getMessage());
                                                            }
                                                            break;
                                                        case "d":
                                                            System.out.println("Please enter the member ID to add to the project:");
                                                            String memberIDToAdd = scanner.nextLine();  
                                                            List<Member> members = memberService.getAll();
                                                            Member memberToAdd = null;
                                                            for (Member member : members) 
                                                            {
                                                                if (member.getMemberID().equals(memberIDToAdd)) 
                                                                {
                                                                    memberToAdd = member;
                                                                    projectService.addMember(project, memberToAdd);
                                                                    System.out.println("Member added to the project successfully!");
                                                                    break;
                                                                }
                                                            } 
                                                            if (memberToAdd == null)
                                                                System.out.println("Member not found. Please try again.");
                                                            break;
                                                        case "e":
                                                            System.out.println("Please enter the member ID to remove from the project:");
                                                            String memberIDToRemove = scanner.nextLine();
                                                            List<Member> membersR = memberService.getAll();
                                                            Member memberToRemove = null;
                                                            for (Member member : membersR) 
                                                            {
                                                                if (member.getMemberID().equals(memberIDToRemove)) 
                                                                {
                                                                    memberToRemove = member;
                                                                    projectService.removeMember(project, memberToRemove);
                                                                    System.out.println("Member removed from the project successfully!");
                                                                    break;
                                                                }
                                                            }
                                                            if (memberToRemove == null)
                                                                System.out.println("Member not found. Please try again.");
                                                            break;
                                                        case "f":
                                                            try
                                                            {
                                                                invoiceService.add(new Invoice(project));
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
                                            for (Project project : projectService.getAll()) 
                                            {
                                                Iterator<Task> it = projectService.getTasksByProject(project.getId()).iterator();
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
                                                            List<Member> members = memberService.getAll();
                                                            Member memberToAssign = null;
                                                            for (Member member : members) 
                                                            {
                                                                if (member.getMemberID().equals(memberIDToAssign)) 
                                                                {
                                                                    memberToAssign = member;
                                                                    taskService.assignTask(task1, memberToAssign);
                                                                    System.out.println("Task assigned to member successfully!");
                                                                    break;
                                                                }
                                                            }
                                                            if (memberToAssign == null) 
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
                                            for (Project project : projectService.getAll()) 
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
                                break;
                            }
                        }
                    }
                    catch (Exception e) 
                    {
                        System.out.println("Invalid Manager ID. Please try again.");
                    }
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

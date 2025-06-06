import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectService extends GenericService<Project> 
{

    private static ProjectService instance;

    private ProjectService() 
    {
        super();
    }

    public static synchronized ProjectService getInstance() 
    {
        if (instance == null) 
            instance = new ProjectService();
        return instance;
    }

    @Override
    public void add(Project project) throws SQLException 
    {
        String sql = "INSERT INTO projects (id, name, manager_id, customer_id, deadline, estimate_price, final_price, progress, late_days) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        if (project.getCustomer() == null) {
            throw new SQLException("Customer must not be null");
        }
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, project.getId());
            pstmt.setString(2, project.getName());
            if (project.getManager() != null) {
                pstmt.setInt(3, project.getManager().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER); 
            }
            pstmt.setInt(4, project.getCustomer().getId());
            pstmt.setDate(5, java.sql.Date.valueOf(project.getDeadline().getDate()));
            pstmt.setDouble(6, project.getEstimatePrice());
            pstmt.setDouble(7, project.getFinalPrice());
            pstmt.setDouble(8, project.getProgress());
            pstmt.setInt(9, project.getLateDays());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Project> getAll() throws SQLException 
    {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int managerId = rs.getInt("manager_id");
                int customerId = rs.getInt("customer_id");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                double estimatePrice = rs.getDouble("estimate_price");
                double finalPrice = rs.getDouble("final_price");
                double progress = rs.getDouble("progress");
                int lateDays = rs.getInt("late_days");
                try
                {
                    Customer customer = CustomerService.getInstance().getById(customerId);
                    Manager manager = null;
                    Project project = null;
                    if (managerId != 0)
                    {
                        manager = ManagerService.getInstance().getById(managerId);
                        project = new Project(id, name, manager, customer, new Deadline(deadlineDate));
                    }
                    else
                        project = new Project(id, name, customer, new Deadline(deadlineDate));
                    projects.add(project);
                }
                catch (Exception e)
                {
                    System.out.println("Error creating project with ID: " + id + ". " + e.getMessage());
                }
            }
        }
        return projects;
    }

    public Project getById(int id) throws SQLException 
    {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                int managerId = rs.getInt("manager_id");
                int customerId = rs.getInt("customer_id");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                double estimatePrice = rs.getDouble("estimate_price");
                double finalPrice = rs.getDouble("final_price");
                double progress = rs.getDouble("progress");
                int lateDays = rs.getInt("late_days");

                Customer customer = CustomerService.getInstance().getById(customerId);
                
                try{
                    Manager manager = null;
                    if (managerId != 0)
                    {
                        manager = ManagerService.getInstance().getById(managerId);
                        return new Project(id, name, manager, customer, new Deadline(deadlineDate));
                    }
                    else
                        return new Project(id, name, customer, new Deadline(deadlineDate));
                }
                catch (Exception e)
                {
                    System.out.println("Error creating project with ID: " + id + ". " + e.getMessage());
                    return null;
                }
            }
        }
        return null;
    }

    public List<Project> getByMember(int member_id) throws SQLException 
    {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT p.* FROM projects p JOIN project_members pm ON p.id = pm.project_id WHERE pm.member_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int managerId = rs.getInt("manager_id");
                int customerId = rs.getInt("customer_id");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                double estimatePrice = rs.getDouble("estimate_price");
                double finalPrice = rs.getDouble("final_price");
                double progress = rs.getDouble("progress");
                int lateDays = rs.getInt("late_days");

                Customer customer = CustomerService.getInstance().getById(customerId);
                
                try
                {
                    Manager manager = null;
                    if (managerId != 0)
                    {
                        manager = ManagerService.getInstance().getById(managerId);
                        projects.add(new Project(id, name, manager, customer, new Deadline(deadlineDate)));
                    }
                    else
                        projects.add(new Project(id, name, customer, new Deadline(deadlineDate)));
                }
                catch (Exception e)
                {
                    System.out.println("Error creating project with ID: " + id + ". " + e.getMessage());
                }
            }
        }
        return projects;
    }

    public List<Project> getByManager(int managerId) throws SQLException 
    {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE manager_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, managerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int customerId = rs.getInt("customer_id");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                double estimatePrice = rs.getDouble("estimate_price");
                double finalPrice = rs.getDouble("final_price");
                double progress = rs.getDouble("progress");
                int lateDays = rs.getInt("late_days");

                Customer customer = CustomerService.getInstance().getById(customerId);
                
                try
                {
                    Manager manager = ManagerService.getInstance().getById(managerId);
                    projects.add(new Project(id, name, manager, customer, new Deadline(deadlineDate)));
                }
                catch (Exception e)
                {
                    System.out.println("Error creating project with ID: " + id + ". " + e.getMessage());
                }
            }
        }
        return projects;
    }

    public List<Task> getTasksByProject(int projectId) throws SQLException 
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE project_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Status status = Status.valueOf(rs.getString("status"));
                Priority priority = Priority.valueOf(rs.getString("priority"));
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                int assignedMemberId = rs.getInt("assigned_member_id");

                Member assignedMember = null;
                if (assignedMemberId != 0) {
                    assignedMember = MemberService.getInstance().getById(assignedMemberId);
                }

                Task task = new Task(id, name, priority, new Deadline(deadlineDate), assignedMember);
                task.setStatus(status);
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void assignManager(Project project, Manager manager) throws Exception
    {
        try
        {
            if (manager.getDepartment().compareTo("PROJECT") != 0)
                throw new Exception("The manager must be a project manager.");
            if (project.getManager() != null)
                throw new Exception("Project already has a manager assigned.");
            String sql = "UPDATE projects SET manager_id = ? WHERE id = ?";
            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                     pstmt.setInt(1, manager.getId());
                     pstmt.setInt(2, project.getId());
                pstmt.executeUpdate();
            }

        }
        catch (Exception e)
        {
            System.out.println("Error assigning the manager: " + e.getMessage());
        }
    }

    public void addMember(Project project, Member member) throws Exception 
    {
        try 
        {
            if (project.getMembers().contains(member)) 
                throw new Exception("Member already exists in the project.");
            project.addMember(member);
            String sql = "INSERT INTO project_members (project_id, member_id) VALUES (?, ?)";
            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, project.getId());
                pstmt.setInt(2, member.getId());
                pstmt.executeUpdate();
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Error adding member to project: " + e.getMessage());
        }
    }

    public void removeMember(Project project, Member member) throws Exception 
    {
        try 
        {
            project.removeMember(member);
            String sql = "DELETE FROM project_members WHERE project_id = ? AND member_id = ?";
            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, project.getId());
                pstmt.setInt(2, member.getId());
                pstmt.executeUpdate();
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Error removing member from project: " + e.getMessage());
        }
    }

    public void showDetailsProject(int id)
    {
        Project project;
        try 
        {
            project = getById(id);
            if (project == null) 
            {
                System.out.println("Project with ID " + id + " not found.");
                return;
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error retrieving project: " + e.getMessage());
            return;
        }

        System.out.println("Project ID: " + project.getId());
        System.out.println("Project Name: " + project.getName());
        if (project.getManager() == null) 
        {
            System.out.println("Manager: Not assigned");
        } 
        else 
        {
            System.out.println("Manager: " + project.getManager().getName());
        }
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

    @Override
    public void update(Project project) throws SQLException 
    {
        String sql = "UPDATE projects SET name = ?, manager = ?, customer = ?, deadline = ?, estimate_price = ?, final_price = ?, progress = ?, late_days = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getManager().getManagerID());
            pstmt.setString(3, project.getCustomer().getCustomerID());
            pstmt.setDate(4, java.sql.Date.valueOf(project.getDeadline().getDate()));
            pstmt.setDouble(5, project.getEstimatePrice());
            pstmt.setDouble(6, project.getFinalPrice());
            pstmt.setDouble(7, project.getProgress());
            pstmt.setInt(8, project.getLateDays());
            pstmt.setInt(9, project.getId());
            pstmt.executeUpdate();
        }
    }

    public void modifyDeadline( Project project, Deadline deadline) throws SQLException 
    {
        String sql = "UPDATE projects SET deadline = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(deadline.getDate()));
            pstmt.setInt(2, project.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(Project project) throws SQLException 
    {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, project.getId());
            pstmt.executeUpdate();
        }
    }

    public boolean isFinished(Project project)
    {
        return project.getProgress() == 100;
    }

    public List<Task> getTasks(Project project)
    {
        return project.getTasks();
    }
}

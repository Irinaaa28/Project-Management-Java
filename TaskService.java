import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskService extends GenericService<Task> 
{

    private static TaskService instance;

    private TaskService() 
    {
        super();
    }

    public static synchronized TaskService getInstance() 
    {
        if (instance == null) 
            instance = new TaskService();
        return instance;
    }

    @Override
    public void add(Task task) throws SQLException 
    {
        String sql = "INSERT INTO tasks (id, name, status, priority, deadline, assigned_member_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getId());
            pstmt.setString(2, task.getName());
            pstmt.setString(3, task.getStatus().toString());
            pstmt.setString(4, task.getPriority().toString());
            pstmt.setDate(5, java.sql.Date.valueOf(task.getDeadline().getDate()));
            if (task.getAssignedMember() != null) 
                pstmt.setInt(6, task.getAssignedMember().getId());
            else 
                pstmt.setNull(6, Types.INTEGER); 
            pstmt.setInt(7, task.getProject().getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("AddTask");
        }   
    }

    @Override
    public List<Task> getAll() throws SQLException 
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Status status = Status.valueOf(rs.getString("status").toUpperCase());
                String priorityStr = rs.getString("priority");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                int assigned_member_id = rs.getInt("assigned_member_id");
                int projectId = rs.getInt("project_id");

                Priority priority = Priority.valueOf(priorityStr.toUpperCase());
                Member assignedMember = null;
                if (assigned_member_id != 0) 
                    assignedMember = MemberService.getInstance().getById(assigned_member_id);
                else
                    assignedMember = new Member(); 
                Project project = ProjectService.getInstance().getById(projectId);
                Task task = new Task(id, name, priority, new Deadline(deadlineDate), assignedMember, project);
                tasks.add(task);
            }
            //AuditService.getInstance().logAction("GetAllTasks");
            return tasks;
        }
    }

    public Task getById(int id) throws SQLException 
    {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    Status status = Status.valueOf(rs.getString("status").toUpperCase());
                    String priorityStr = rs.getString("priority");
                    LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                    int assigned_member_id = rs.getInt("assigned_member_id");
                    int projectId = rs.getInt("project_id");

                    Priority priority = Priority.valueOf(priorityStr.toUpperCase());
                    Member assignedMember = null;
                    if (assigned_member_id != 0) 
                        assignedMember = MemberService.getInstance().getById(assigned_member_id);
                    else
                        assignedMember = new Member(); 
                    Project project = ProjectService.getInstance().getById(projectId);
                    //AuditService.getInstance().logAction("GetTaskById");
                    return new Task(id, name, priority, new Deadline(deadlineDate), assignedMember, project);
                }
            }
        }
        return null;
    }

    public List<Task> getByProject(Project project) throws SQLException 
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE project_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, project.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Status status = Status.valueOf(rs.getString("status").toUpperCase());
                    String priorityStr = rs.getString("priority");
                    LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                    int assigned_member_id = rs.getInt("assigned_member_id");

                    Priority priority = Priority.valueOf(priorityStr.toUpperCase());
                    Member assignedMember = null;
                    if (assigned_member_id != 0) 
                        assignedMember = MemberService.getInstance().getById(assigned_member_id);
                    else
                        assignedMember = new Member(); 
                    Task task = new Task(id, name, priority, new Deadline(deadlineDate), assignedMember, project);
                    tasks.add(task);
                }
            }
        }
        for (Task t: tasks)
            System.out.println("Task Name: " + t.getName() + ", Status: " + t.getStatus() + ", Priority: " + t.getPriority() + ", Deadline: " + t.getDeadline().getDate().toString());
        //AuditService.getInstance().logAction("GetTasksByProject");    
        return tasks;
    }

    public void showDetailsTask(Task task) 
    {
        System.out.println("Task Name: " + task.getName());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Deadline: " + task.getDeadline().getDate().toString());
        System.out.println("Assigned Member: " + (task.getAssignedMember() != null ? task.getAssignedMember().getName() : "None"));
    }

    public void displayTasksByUser(Member member) 
    {
        String sql = "SELECT * FROM tasks WHERE assigned_member_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String priorityStr = rs.getString("priority");
                    LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                    Member assignedMember = MemberService.getInstance().getById(rs.getInt("assigned_member_id"));
                    Status status = Status.valueOf(rs.getString("status").toUpperCase());
                    int projectId = rs.getInt("project_id");
                    Project project = ProjectService.getInstance().getById(projectId);
                    Priority priority = Priority.valueOf(priorityStr.toUpperCase());
                    Task task = new Task(id, name, priority, new Deadline(deadlineDate), member);
                    showDetailsTask(task);

                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public void displayTasksByPriority() 
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String priorityStr = rs.getString("priority");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                int assigned_member_id = rs.getInt("assigned_member_id");
                Status status = Status.valueOf(rs.getString("status").toUpperCase());
                int projectId = rs.getInt("project_id");

                Priority priority = Priority.valueOf(priorityStr.toUpperCase());
                Member assignedMember = null;
                if (assigned_member_id != 0) 
                    assignedMember = MemberService.getInstance().getById(assigned_member_id);
                else
                    assignedMember = new Member();
                Project project = ProjectService.getInstance().getById(projectId);
                Task task = new Task(id, name, priority, new Deadline(deadlineDate), assignedMember, project);
                tasks.add(task);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        tasks.sort(Comparator.comparing(Task::getPriority).reversed());
        for (Task t: tasks)
            System.out.println("Task Name: " + t.getName() + ", Status: " + t.getStatus() + ", Priority: " + t.getPriority() + ", Deadline: " + t.getDeadline().getDate().toString());
    }

    public void displayTasksByDeadline() 
    {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String priorityStr = rs.getString("priority");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                int assigned_member_id = rs.getInt("assigned_member_id");
                Status status = Status.valueOf(rs.getString("status").toUpperCase());
                int projectId = rs.getInt("project_id");

                Priority priority = Priority.valueOf(priorityStr.toUpperCase());
                Member assignedMember = null;
                if (assigned_member_id != 0) 
                    assignedMember = MemberService.getInstance().getById(assigned_member_id);
                else
                    assignedMember = new Member();
                Project project = ProjectService.getInstance().getById(projectId);
                Task task = new Task(id, name, priority, new Deadline(deadlineDate), assignedMember, project);
                tasks.add(task);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        tasks.sort(Comparator.comparing(t -> t.getDeadline().getDate()));
        for (Task t: tasks)
            System.out.println("Task Name: " + t.getName() + ", Status: " + t.getStatus() + ", Priority: " + t.getPriority() + ", Deadline: " + t.getDeadline().getDate().toString());
    }

    @Override
    public void update(Task task) throws SQLException 
    {
        String sql = "UPDATE tasks SET name = ?, priority = ?, deadline = ?, assigned_member_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getId());
            pstmt.setString(2, task.getName());
            pstmt.setString(3, task.getPriority().toString());
            pstmt.setDate(4, java.sql.Date.valueOf(task.getDeadline().getDate()));
            if (task.getAssignedMember() != null) 
                pstmt.setInt(5, task.getAssignedMember().getId());
            else 
                pstmt.setNull(5, Types.INTEGER); 
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("UpdateTask");
        }
    }

    public void assignTask(Task task, Member member) throws SQLException 
    {
        String sql = "UPDATE tasks SET assigned_member_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getId());
            pstmt.setInt(2, task.getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("AssignTask");
        }
    }

    public void modifyStatus(Task task, Status status) throws SQLException 
    {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.toString());
            pstmt.setInt(2, task.getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("ModifyTaskStatus");
        }
    }

    public void modifyPriority(Task task, Priority priority) throws SQLException 
    {
        String sql = "UPDATE tasks SET priority = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, priority.toString());
            pstmt.setInt(2, task.getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("ModifyTaskPriority");
        }
    }

    public void modifyDeadline(Task task, Deadline deadline) throws SQLException 
    {
        String sql = "UPDATE tasks SET deadline = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(deadline.getDate()));
            pstmt.setInt(2, task.getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("ModifyTaskDeadline");
        }
    }

    @Override
    public void delete(Task task) throws SQLException 
    {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("DeleteTask");
        }
    }
}

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO extends GenericDAO<Task> 
{

    private static TaskDAO instance;

    private TaskDAO() 
    {
        super();
    }

    public static synchronized TaskDAO getInstance() 
    {
        if (instance == null) 
            instance = new TaskDAO();
        return instance;
    }

    @Override
    public void add(Task task) throws SQLException 
    {
        String sql = "INSERT INTO tasks (id, name, priority, deadline, member_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, task.getId());
            pstmt.setString(2, task.getName());
            pstmt.setString(3, task.getPriority().toString());
            pstmt.setDate(4, java.sql.Date.valueOf(task.getDeadline().getDate()));
            if (task.getAssignedMember() != null) {
                pstmt.setString(5, task.getAssignedMember().getMemberID());
            } else {
                pstmt.setNull(5, Types.INTEGER); 
            }
            pstmt.executeUpdate();
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
                String priorityStr = rs.getString("priority");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                String memberId = rs.getString("member_id");

                Priority priority = Priority.valueOf(priorityStr.toUpperCase());
                Member assignedMember = null;
                if (memberId != null) {
                    assignedMember = MemberDAO.getInstance().getById(memberId);
                }

                Task task = new Task(name, priority, new Deadline(deadlineDate), assignedMember);
                tasks.add(task);
            }
        }
        return null;
    }

    @Override
    public void update(Task task) throws SQLException 
    {
        String sql = "UPDATE tasks SET name = ?, priority = ?, deadline = ?, member_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getName());
            pstmt.setString(2, task.getPriority().toString());
            pstmt.setDate(3, java.sql.Date.valueOf(task.getDeadline().getDate()));
            if (task.getAssignedMember() != null) 
                pstmt.setString(4, task.getAssignedMember().getMemberID());
            else 
                pstmt.setNull(4, Types.INTEGER); 
            pstmt.setInt(5, task.getId());
            pstmt.executeUpdate();
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
        }
    }
    
}

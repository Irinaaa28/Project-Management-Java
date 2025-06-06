import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerService extends GenericService<Manager> 
{

    private static ManagerService instance;

    private ManagerService() 
    {
        super();
    }

    public static ManagerService getInstance() 
    {
        if (instance == null) 
            instance = new ManagerService();
        return instance;
    }

    @Override
    public void add(Manager manager) throws SQLException 
    {
        String sql = "INSERT INTO managers (id, name, email, department, manager_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, manager.getId());
            pstmt.setString(2, manager.getName());
            pstmt.setString(3, manager.getEmail());
            pstmt.setString(4, manager.getDepartment());
            pstmt.setString(5, manager.getManagerID());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("AddManager");
        }
    }

    @Override
    public List<Manager> getAll() throws SQLException 
    {
        List<Manager> managers = new ArrayList<>();
        String sql = "SELECT * FROM managers";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) 
        {
            while (rs.next()) 
            {
                Manager manager = new Manager(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("department"), rs.getString("manager_id"));
                managers.add(manager);
            }
            //AuditService.getInstance().logAction("GetAllManagers");
        }
        return managers;
    }

    public Manager getById(int id) throws SQLException 
    {
        String sql = "SELECT * FROM managers WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    //AuditService.getInstance().logAction("GetManagerById");
                    return new Manager(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("department"), rs.getString("manager_id"));
                }
            }

        }
        return null; 
    }
    
    @Override
    public void update(Manager manager) throws SQLException 
    {
        String sql = "UPDATE managers SET name = ?, email = ?, department = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, manager.getName());
            pstmt.setString(2, manager.getEmail());
            pstmt.setString(3, manager.getDepartment());
            pstmt.setInt(4, manager.getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("UpdateManager");
        }


    }

    @Override
    public void delete(Manager manager) throws SQLException 
    {
        String sql = "DELETE FROM managers WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, manager.getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("DeleteManager");
        }
    }    
}

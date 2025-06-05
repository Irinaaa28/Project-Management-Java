import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerDAO extends GenericDAO<Manager> 
{

    private static ManagerDAO instance;

    private ManagerDAO() 
    {
        super();
    }

    public static ManagerDAO getInstance() 
    {
        if (instance == null) 
            instance = new ManagerDAO();
        return instance;
    }

    @Override
    public void add(Manager manager) throws SQLException 
    {
        String sql = "INSERT INTO managers (id, name, email, department) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, manager.getManagerID());
            pstmt.setString(2, manager.getName());
            pstmt.setString(3, manager.getEmail());
            pstmt.setString(4, manager.getDepartment());
            pstmt.executeUpdate();
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
                Manager manager = new Manager(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("department"));
                managers.add(manager);
            }
        }
        return managers;
    }

    public Manager getById(String id) throws SQLException 
    {
        String sql = "SELECT * FROM managers WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return new Manager(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("department"));
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
            pstmt.setString(4, manager.getManagerID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(Manager manager) throws SQLException 
    {
        String sql = "DELETE FROM managers WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, manager.getManagerID());
            pstmt.executeUpdate();
        }
    }

    
}

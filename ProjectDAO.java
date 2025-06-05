import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends GenericDAO<Project> 
{

    private static ProjectDAO instance;

    private ProjectDAO() 
    {
        super();
    }

    public static synchronized ProjectDAO getInstance() 
    {
        if (instance == null) 
            instance = new ProjectDAO();
        return instance;
    }

    @Override
    public void add(Project project) throws SQLException 
    {
        String sql = "INSERT INTO projects (id, name, manager, customer, deadline, estimate_price, final_price, progress, late_days) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        if (project.getManager() == null || project.getCustomer() == null) {
            throw new SQLException("Manager and Customer must not be null");
        }
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, project.getId());
            pstmt.setString(2, project.getName());
            pstmt.setString(3, project.getManager().getManagerID());
            pstmt.setString(4, project.getCustomer().getCustomerID());
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
                String managerId = rs.getString("manager");
                String customerId = rs.getString("customer");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                double estimatePrice = rs.getDouble("estimate_price");
                double finalPrice = rs.getDouble("final_price");
                double progress = rs.getDouble("progress");
                int lateDays = rs.getInt("late_days");

                Manager manager = ManagerDAO.getInstance().getById(managerId);
                Customer customer = CustomerDAO.getInstance().getById(customerId);
                
                if (manager == null || customer == null) 
                {
                    System.out.println("Manager or Customer not found for project ID: " + id);
                    continue; 
                }
                try
                {

                    Project project = new Project(name, manager, customer, new Deadline(deadlineDate));
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
                String managerId = rs.getString("manager");
                String customerId = rs.getString("customer");
                LocalDate deadlineDate = rs.getDate("deadline").toLocalDate();
                double estimatePrice = rs.getDouble("estimate_price");
                double finalPrice = rs.getDouble("final_price");
                double progress = rs.getDouble("progress");
                int lateDays = rs.getInt("late_days");

                Manager manager = ManagerDAO.getInstance().getById(managerId);
                Customer customer = CustomerDAO.getInstance().getById(customerId);
                
                if (manager == null || customer == null) 
                {
                    System.out.println("Manager or Customer not found for project ID: " + id);
                    return null; 
                }
                
                try{
                    return new Project(name, manager, customer, new Deadline(deadlineDate));
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

    
}

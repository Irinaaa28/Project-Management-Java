import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO extends GenericDAO<Invoice> 
{

    private static InvoiceDAO instance;

    private InvoiceDAO() 
    {
        super();
    }

    public static synchronized InvoiceDAO getInstance() 
    {
        if (instance == null) 
            instance = new InvoiceDAO();
        return instance;
    }

    @Override
    public void add(Invoice invoice) throws SQLException 
    {
        String sql = "INSERT INTO invoices (id, project_id, release_date, amount, is_released) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getId());
            pstmt.setInt(2, invoice.getProject().getId());
            pstmt.setDate(3, java.sql.Date.valueOf(invoice.getReleaseDate()));
            pstmt.setDouble(4, invoice.getAmount());
            pstmt.setBoolean(5, invoice.isReleased());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Invoice> getAll() throws SQLException 
    {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) 
            {
                int id = rs.getInt("id");
                int projectId = rs.getInt("project_id");
                Date releaseDate = rs.getDate("release_date");
                double amount = rs.getDouble("amount");
                boolean isReleased = rs.getBoolean("is_released");

                Project project = ProjectDAO.getInstance().getById(projectId); 
                Invoice invoice = new Invoice(project);
                invoice.setReleased(isReleased);
                invoices.add(invoice);
            }
        }
        return null; 
    }

    @Override
    public void update(Invoice invoice) throws SQLException 
    {
        String sql = "UPDATE invoices SET project_id = ?, release_date = ?, amount = ?, is_released = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getProject().getId());
            pstmt.setDate(2, java.sql.Date.valueOf(invoice.getReleaseDate()));
            pstmt.setDouble(3, invoice.getAmount());
            pstmt.setBoolean(4, invoice.isReleased());
            pstmt.setInt(5, invoice.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(Invoice invoice) throws SQLException 
    {
        String sql = "DELETE FROM invoices WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getId());
            pstmt.executeUpdate();
        }
    }
    
}

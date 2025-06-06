import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceService extends GenericService<Invoice> 
{

    private static InvoiceService instance;

    private InvoiceService() 
    {
        super();
    }

    public static synchronized InvoiceService getInstance() 
    {
        if (instance == null) 
            instance = new InvoiceService();
        return instance;
    }

    @Override
    public void add(Invoice invoice) throws SQLException 
    {
        if (invoice.getProject().getProgress() < 100) 
        {
            throw new SQLException("Cannot add invoice for a project that is not completed.");
        }
        String sql = "INSERT INTO invoices (id, project_id, release_date, amount, released) VALUES (?, ?, ?, ?, ?)";
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
                boolean isReleased = rs.getBoolean("released");

                Project project = ProjectService.getInstance().getById(projectId); 
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
        String sql = "UPDATE invoices SET project_id = ?, release_date = ?, amount = ?, released = ? WHERE id = ?";
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

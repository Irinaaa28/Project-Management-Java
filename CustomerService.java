import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService extends GenericService<Customer> 
{
    private static CustomerService instance;

    private CustomerService() 
    {
        super();
    }

    public static CustomerService getInstance() 
    {
        if (instance == null) 
            instance = new CustomerService();
        return instance;
    }

    @Override
    public void add(Customer customer) throws SQLException 
    {
        String sql = "INSERT INTO customers (id, name, email, customer_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, customer.getId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getCustomerID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Customer> getAll() throws SQLException 
    {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) 
        {
            while (rs.next()) 
            {
                Customer customer = new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("customer_id"));
                customers.add(customer);
            }
        }
        return customers;
    }

    public Customer getById(int id) throws SQLException 
    {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) 
            {
                return new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("customer_id"));
            }
        }
        return null;
    }

    @Override
    public void update(Customer customer) throws SQLException 
    {
        String sql = "UPDATE customers SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setInt(3, customer.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(Customer customer) throws SQLException 
    {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, customer.getId());
            pstmt.executeUpdate();
        }
    }
}

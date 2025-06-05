import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO extends GenericDAO<Member> 
{

    private static MemberDAO instance;

    private MemberDAO() 
    {
        super();
    }

    public static MemberDAO getInstance() 
    {
        if (instance == null) 
            instance = new MemberDAO();
        return instance;
    }

    @Override
    public void add(Member member) throws SQLException {
        String sql = "INSERT INTO members (id, name, email, level) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getMemberID());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getLevel());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Member> getAll() throws SQLException 
    {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) 
            {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String level = rs.getString("level");
                members.add(new Member(name, email, level, id));
            }
        }
        return members;
    }

    public Member getById(String id) throws SQLException 
    {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String level = rs.getString("level");
                    return new Member(name, email, level, id);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Member member) throws SQLException 
    {
        String sql = "UPDATE members SET name = ?, email = ?, level = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getLevel());
            pstmt.setString(4, member.getMemberID());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(Member member) throws SQLException 
    {
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getMemberID());
            pstmt.executeUpdate();
        }
    }
    
}

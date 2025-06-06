import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberService extends GenericService<Member> 
{
    private static MemberService instance;

    private MemberService() 
    {
        super();
    }

    public static MemberService getInstance() 
    {
        if (instance == null) 
            instance = new MemberService();
        return instance;
    }

    @Override
    public void add(Member member) throws SQLException {
        String sql = "INSERT INTO members (id, name, email, level, member_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getLevel());
            pstmt.setString(5, member.getMemberID());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("AddMember");
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
                int id = rs.getInt("id");
                String member_id = rs.getString("member_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String level = rs.getString("level");
                members.add(new Member(id, name, email, level, member_id));
            }
            //AuditService.getInstance().logAction("GetAllMembers");
        }
        return members;
    }

    public Member getById(int id) throws SQLException 
    {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String level = rs.getString("level");
                    String member_id = rs.getString("member_id");
                    //AuditService.getInstance().logAction("GetMemberById");
                    return new Member(id, name, email, level, member_id);
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
                 pstmt.setInt(4, member.getId());
                 pstmt.executeUpdate();
                 //AuditService.getInstance().logAction("UpdateMember");
        }
    }

    @Override
    public void delete(Member member) throws SQLException 
    {
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getId());
            pstmt.executeUpdate();
            //AuditService.getInstance().logAction("DeleteMember");
        }
    }
    
}

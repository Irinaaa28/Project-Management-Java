import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/test"; // schimbă cu numele bazei tale
    private static final String USER = "root";       // schimbă dacă folosești alt utilizator
    private static final String PASSWORD = ""; // înlocuiește cu parola ta

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectare reușită la MySQL!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Eroare la conectare: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        connect();
    }
}

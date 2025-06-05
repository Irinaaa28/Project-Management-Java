import java.sql.*;

public class DBConnection {

    public static Connection connect() {
        String URL = "jdbc:mysql://localhost:3306/test"; // schimbă cu numele bazei tale
        String USER = "root";       // schimbă dacă folosești alt utilizator
        String PASSWORD = "irina2004"; // înlocuiește cu parola ta
        try {
             Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectare reușită la MySQL!");
            return conn;
        } 
        catch (ClassNotFoundException e) {
            System.out.println("Driverul JDBC nu a fost găsit!");
            return null;
        }
        catch (SQLException e) {
            System.out.println("Eroare la conectare: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        connect();
    }
}

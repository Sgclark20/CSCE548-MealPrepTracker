import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // Update these to match your environment
    private static final String URL = "jdbc:mysql://localhost:3306/meal_prep_tracker?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "sgc123";

    private DBUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

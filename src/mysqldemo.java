import java.util.Properties;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class mysqldemo {
    public static void main(String args[]) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");

        Properties props = new Properties();
        props.load(new FileInputStream(".env.local"));

        String db_name = props.getProperty("DB_NAME");
        String url ="jdbc:mysql://localhost:3306/" + db_name;
        String user = props.getProperty("DB_USER");
        String password = props.getProperty("DB_PASS");

        try (Connection connection = DriverManager.getConnection(url,user,password);){
            System.out.println("connected!");
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
            e.printStackTrace(); // helpful while debugging
            System.out.println("sql error: " + e.getMessage()); // show real sql error "WHY IT FAILED"
        }
    }
}

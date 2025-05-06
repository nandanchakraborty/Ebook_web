import java.sql.Connection;
import com.DB.DBConnect;

public class TestConnection {
    public static void main(String[] args) {
        // Use absolute path to your .db file — update this!
    	
    	Connection conn = DBConnect.getConn();

        if (conn != null) {
            System.out.println("Connection test passed.");
        } else {
            System.out.println("Connection test failed.");
        }
    }
}

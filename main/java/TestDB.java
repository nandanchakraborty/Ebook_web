
import java.sql.Connection;

import com.DAO.UserDAOImpl;
import com.DB.DBConnect;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnect.getConn();
            System.out.println("Connection successful: " + conn);
            
            UserDAOImpl dao = new UserDAOImpl(conn);
            String testEmail = "test@example.com";
            boolean exists = dao.checkUser(testEmail);
            System.out.println("User with email " + testEmail + " exists: " + exists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

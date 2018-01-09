
package pages;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class dbConnection {
    public static Connection getConnection(){
        
        Connection conn = null;
        try{
        
            Class.forName("com.mysql.jdbc.Driver");
            conn=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/moneyLand","root","");
        }
        catch(ClassNotFoundException | SQLException e){
        
            JOptionPane.showMessageDialog(null, e);
        }
        

        return conn;

}
}

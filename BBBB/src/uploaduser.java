
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class uploaduser {

    String drv = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://112.74.53.229:3306/moneyless?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    String usr = "root";
    String pwd = "19991009";
    String err = "";
    public boolean userdataupload(String id,String password,String budget,String phone,String username,String sex){

        boolean b = false;
        try{
            Class.forName(drv).newInstance();
            Connection conn = DriverManager.getConnection(url,usr,pwd);
            Statement stm = conn.createStatement();
            String sql = "update User(password,budget,phone,username,sex) values('"+password+"',"+budget+",'"+phone+"','" + username + "'," + sex +") WHERE USERID = " + id;
            stm.execute(sql);
            b = true;
            stm.close();
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            err = sw.toString();
            System.out.println(err);
        }
        if(b)
        {
            return true;
        }
        else return false;
    }
}
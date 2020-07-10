
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class register {

    String drv = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://112.74.53.229:3306/moneyless?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    String usr = "root";
    String pwd = "19991009";
    String err = "";
    public long userregister(String username,String password){

        boolean b = false;
        long id = System.currentTimeMillis();
        String sql = "select * from User where userid="+id;
        try{
            Class.forName(drv).newInstance();
            Connection conn = DriverManager.getConnection(url,usr,pwd);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(!rs.next()){
                sql = "insert into User(userid,password,username) values("+id+",'"+password+"','"+username+"')";
                stm.execute(sql);
                b = true;
            }
            rs.close();
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
            return id;
        }
        else return -1;
    }
}
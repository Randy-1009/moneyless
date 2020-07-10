import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class login {

    String drv = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://112.74.53.229:3306/moneyless?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    String usr = "root";
    String pwd = "19991009";
    String err = "";


    public long isuserlogin(String username,String password){
        boolean isValid = false;
        long id = -1;
        String sql="select * from User where username='"+username+"' and password='"+password+"'";
        try{
            Class.forName(drv).newInstance();
            Connection conn = DriverManager.getConnection(url,usr,pwd);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if(rs.next()){
                isValid = true;
                id = rs.getLong("userid");
            }
            rs.close();
            stm.close();
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        if(isValid){//判断用户名以及密码是否与设定相符
            return id;
        }
        else return id;
    }
}
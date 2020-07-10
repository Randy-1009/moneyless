import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class getwastebook {

    String drv = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://112.74.53.229:3306/moneyless?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    String usr = "root";
    String pwd = "19991009";
    String err = "";


    public String getbyid(String userid){
        boolean isValid = false;
        String result = "";
        String sql="select * from WasteBook where userid = "+userid;
        try{
            Class.forName(drv).newInstance();
            Connection conn = DriverManager.getConnection(url,usr,pwd);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            System.out.println(sql);
            while (rs.next()){
                isValid = true;
                result += String.valueOf(rs.getLong("id")) + " ";
                result += userid + ' ';
                result += String.valueOf(rs.getLong("category_id")) + " ";
                result += String.valueOf(rs.getBoolean("w_type")) + " ";
                result += rs.getString("category_icon") + " ";
                result += String.valueOf(rs.getLong("create_time")) + " ";
                result += rs.getString("note") + " ";
                result += String.valueOf(rs.getDouble("amount")) + " ";
                result += rs.getString("category") + " ";
                result += "&&&";
                System.out.println(result);
            }
            rs.close();
            stm.close();
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        if(isValid){//判断用户名以及密码是否与设定相符
            return result;
        }
        else return "false";
    }
}
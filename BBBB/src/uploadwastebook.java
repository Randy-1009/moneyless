import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class uploadwastebook {
    String drv = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://112.74.53.229:3306/moneyless?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    String usr = "root";
    String pwd = "19991009";
    String err = "";
    public boolean uploadonce(String id,String userid,String category_id,String w_type,String category_icon,String create_time,String note,String amount,String category){

        boolean b = false;

        String sql = "select * from WasteBook where id = " + id + " and userid = " + userid;
        try{
            Class.forName(drv).newInstance();
            Connection conn = DriverManager.getConnection(url,usr,pwd);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(!rs.next()){
                sql = "insert into WasteBook(id, userid, category_id, w_type, category_icon, create_time, note, amount, category) values(" + id +',' + userid + ',' + category_id + ',' + w_type + ",'" + category_icon + "'," + create_time + ",'" + note + "'," + amount + ",'" + category +"')";
                System.out.println(category);
                System.out.println(sql);
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
            return true;
        }
        else return false;
    }
}
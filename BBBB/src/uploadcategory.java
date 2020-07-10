import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class uploadcategory {
    String drv = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://112.74.53.229:3306/moneyless?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
    String usr = "root";
    String pwd = "19991009";
    String err = "";
    public boolean uploadcategory(String category_id,String userid,String category_name,String category_icon,String category_order,String category_type){

        boolean b = false;

        String sql = "select * from Category where category_id = " + category_id + " and userid = " + userid;
        System.out.println(sql);
        try{
            Class.forName(drv).newInstance();
            Connection conn = DriverManager.getConnection(url,usr,pwd);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(!rs.next()){
                sql = "insert into Category(category_id,userid,category_name,category_icon,category_order,category_type) values(" + category_id +',' + userid + ",'" + category_name + "','" + category_icon + "'," + category_order + "," + category_type + ")";
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
        return b;
    }
}
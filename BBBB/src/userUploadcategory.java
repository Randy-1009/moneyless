import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
public class userUploadcategory extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

        //设置客户端的解码方式为utf-8
        response.setContentType("text/html;charset=utf-8");
        //
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        boolean b = true;
        uploadcategory myPOJO = new uploadcategory();
        String category_id = request.getParameter("category_id");
        String userid = request.getParameter("userid");
        String category_name = request.getParameter("category_name");
        String category_icon = request.getParameter("category_icon");
        String category_order = request.getParameter("category_order");
        String category_type = request.getParameter("category_type");

        String result = "";

        b = myPOJO.uploadcategory(category_id,userid,category_name,category_icon,category_order,category_type);//连接数据库，插入该用户信息

        PrintWriter out = response.getWriter();//回应请求
        if (b) {
            result = "success";
        } else {
            result = "fail";
        }
        out.write(result);
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

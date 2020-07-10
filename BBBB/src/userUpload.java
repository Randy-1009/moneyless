import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
public class userUpload extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

        //设置客户端的解码方式为utf-8
        response.setContentType("text/html;charset=utf-8");
        //
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        boolean b = false;
        uploaduser myPOJO = new uploaduser();
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String budget = request.getParameter("budget");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String sex = request.getParameter("sex");

        String result = "";

        b = myPOJO.userdataupload(id, password, budget, phone, username, sex);//连接数据库，插入该用户信息

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

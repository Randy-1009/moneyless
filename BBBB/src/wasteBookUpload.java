import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
public class wasteBookUpload extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

        //设置客户端的解码方式为utf-8
        response.setContentType("text/html;charset=utf-8");
        //
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        boolean b = false;
        uploadwastebook myPOJO = new uploadwastebook();
        String id = request.getParameter("id");
        String userid = request.getParameter("userid");
        String category_id = request.getParameter("category_id");
        String w_type = request.getParameter("w_type");
        String category_icon = request.getParameter("category_icon");
        String create_time = request.getParameter("create_time");
        String note = request.getParameter("note");
        String amount = request.getParameter("amount");
        String category = request.getParameter("category");
        String result = "";
        b = myPOJO.uploadonce(id, userid, category_id, w_type, category_icon, create_time, note, amount, category);//连接数据库，插入该用户信息

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



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class userRegister extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

        //设置客户端的解码方式为utf-8
        response.setContentType("text/html;charset=utf-8");
        //
        response.setCharacterEncoding("UTF-8");

        long id = 0;
        register myPOJO=new register();

        String password=request.getParameter("password");
        String email = request.getParameter("email");

        String result = "";

        id = myPOJO.userregister(password,email);//连接数据库，插入该用户信息

        PrintWriter out = response.getWriter();//回应请求
        if(id != -1){
            result += "success";
        }
        else{
            result += "fail";
        }
        out.write(result);
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
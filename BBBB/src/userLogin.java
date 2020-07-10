

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class userLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

        //设置客户端的解码方式为utf-8
        response.setContentType("text/html;charset=utf-8");
        //
        response.setCharacterEncoding("UTF-8");

        long id = -1;
        login MyPOJO=new login();//新建MyPOJO类的对象myPOJO

        //根据标示名获取JSP文件中表单所包含的参数
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String result = "";

        id = MyPOJO.isuserlogin(username,password);//使用模型对账号和密码进行验证，返回一个boolean类型的对象
        PrintWriter out = response.getWriter();//回应请求
        if(id != -1){  //如果验证结果为真，跳转至登录成功页面
            result = "success" + " " + String.valueOf(id);
        }
        else {  //如果验证结果为假，跳转至登录失败页面
            result = "fail" + " " + String.valueOf(id);
        }
        out.write(result);
        out.flush();
        out.close();
        System.out.println(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
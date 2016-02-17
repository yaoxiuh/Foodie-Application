package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DbHelper;

/**
 * Servlet implementation class UserUpdate
 */
@WebServlet("/UserUpdate")
public class UserUpdate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserUpdate() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        long userId = Long.valueOf(request.getParameter("userId"));
        if (type.equals("password")) {
            String newPassword = request.getParameter("newPassword");
            boolean result = new DbHelper().changePassWord(userId, newPassword);
            if (result == true) {
                System.out.println(userId + " password update success");
                response.getWriter().write("SUCCESS");
            } else {
                System.out.println(userId + " password update fail");
                response.getWriter().write("FAIL");
            }
        } else if (type.equals("avatar")) {
            String name = request.getParameter("name");
            boolean result = new DbHelper().updateUserAvatar(userId, name);
            if (result == true) {
                System.out.println(userId + " avatar update success");
                response.getWriter().write("SUCCESS");
            } else {
                System.out.println(userId + " avatar update fail");
                response.getWriter().write("FAIL");
            }
        }
    }

}

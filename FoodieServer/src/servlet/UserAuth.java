package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.ClientUser;
import model.User;
import util.DbHelper;

/**
 * Servlet implementation class SignIn
 */
@WebServlet("/auth")
public class UserAuth extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserAuth() {
        super();
    }

    /**
     * handle new user query
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("username");
        boolean isNewUser = new DbHelper().isNewUser(userName);
        if (isNewUser) {
            System.out.println(userName + " is new user");
            response.getWriter().write("TRUE");
        } else {
            System.out.println(userName + " is not new user");
            response.getWriter().write("FALSE");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        if (type.equals("create")) {
            User user = createUser(userName, password);
            System.out.println("Create User " + user.getUserId());
            String userJson = new Gson().toJson(user);
            response.getWriter().write(userJson);
        } else if (type.equals("auth")) {
            long userId = new DbHelper().userAuth(userName, password);
            if (userId > 0) {
                System.out.println(userName + " auth success");
                response.getWriter().write(String.valueOf(userId));
            } else {
                System.out.println(userName + " auth fail");
                response.getWriter().write(String.valueOf(-1));
            }
        }
    }

    private User createUser(String userName, String password) {
        User newUser = new ClientUser(userName, password);
        new DbHelper().insertUser(newUser);
        return newUser;
    }

}

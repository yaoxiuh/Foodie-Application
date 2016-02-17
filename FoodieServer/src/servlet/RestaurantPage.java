package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Comment;
import model.Restaurant;
import util.DbHelper;
import util.GsonHelper;

/**
 * Servlet implementation class RestaurantPage
 */
@WebServlet("/restaurant")
public class RestaurantPage extends HttpServlet {
    private static final String NO_RESULT = "NO_RESULT";

    private static final long serialVersionUID = 1L;

    public RestaurantPage() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long para = 0;
        try {
            para = Long.decode(request.getParameter("rstId"));
        } catch (NullPointerException e) {
            response.getWriter().write(NO_RESULT);
            return;
        }
        Restaurant restaurant = null;
        try {
            restaurant = new DbHelper().getRestaurant(para);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (restaurant == null) {
            response.getWriter().write(NO_RESULT);
            return;
        }
        String rstJson = new Gson().toJson(restaurant);
        response.getWriter().write(rstJson);
        System.out.println("send restaurant " + para); // TODO add to log
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long para = Long.decode(request.getParameter("rstId"));
        InputStream in = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        Comment comment = new GsonHelper().restoreComment(content.toString());
        new DbHelper().insertComment(comment, para);
        System.out.println("Receive comment from " + comment.getCreator().getUserId() + " with comment word "
                + comment.getCommentWord());
        response.getWriter().write("SUCCESS");
    }
}

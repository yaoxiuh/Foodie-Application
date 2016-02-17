package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Location;
import model.Location.LocationType;
import util.DbHelper;
import model.User;

/**
 * Servlet implementation class PeopleNearbyList
 */
@WebServlet("/PeopleNearbyList")
public class PeopleNearbyList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public PeopleNearbyList() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        double longitude = Double.valueOf(request.getParameter("longitude"));
        double latitude = Double.valueOf(request.getParameter("latitude"));
        Location location = new Location(longitude, latitude, LocationType.USER);
        long userId = Long.valueOf(request.getParameter("userId"));
        List<User> userList = new DbHelper().getPeopleNearbyList(location, userId);
        String userListJson = new Gson().toJson(userList);
        System.out.println("send " + userList.size() + " nearby user");
        response.getWriter().write(userListJson);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}

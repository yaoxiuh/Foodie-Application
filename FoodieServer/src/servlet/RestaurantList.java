package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Location;
import model.Restaurant;
import model.RstFlavorTag;
import model.SortType;
import util.DbHelper;


/**
 * Servlet implementation class RestaurantList
 */
@WebServlet("/RestaurantList")
public class RestaurantList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RestaurantList() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        Location location = new Location(longitude, latitude, new Date());
        SortType sortType = SortType.valueOf(request.getParameter("sortType"));
        RstFlavorTag tag = RstFlavorTag.valueOf(request.getParameter("tags"));

        List<RstFlavorTag> tags = new ArrayList<>();
        if (!tag.equals(RstFlavorTag.NULL)){
            tags.add(tag);
        }
        List<Restaurant> rstList = new DbHelper().getRestaurantList(location, sortType, tags);
        List<Long> rstIdList = new ArrayList<>();
        for (Restaurant r : rstList) {
            rstIdList.add(r.getId());
        }
        String rstIdListJson = new Gson().toJson(rstIdList);
        response.getWriter().write(rstIdListJson);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}

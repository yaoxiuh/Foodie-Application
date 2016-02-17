package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.Default;
import model.Restaurant;
import model.RstFlavorTag;
import model.SortType;
import util.DbHelper;

public class RstListTest {

    List<Restaurant> rstList;

    @Test
    public void testByDistance() {
        rstList = new DbHelper().getRestaurantList(Default.generateRstDefaultLocation(), SortType.BY_DISTACNE,
                new ArrayList<RstFlavorTag>());
        System.out.println("\n\n Sort by distance");
        for (Restaurant r : rstList) {
            double longitude = Default.generateRstDefaultLocation().getLongitude();
            double latitude = Default.generateRstDefaultLocation().getLatitude();
            double distance = Math.pow((longitude - r.getLocation().getLongitude()), 2)
                    + Math.pow((latitude - r.getLocation().getLatitude()), 2);
            System.out.println(r.getName() + "    " + distance);
        }

    }

    @Test
    public void testByRating() {
        rstList = new DbHelper().getRestaurantList(Default.generateRstDefaultLocation(), SortType.BY_RATE,
                new ArrayList<RstFlavorTag>());
        System.out.println("\n\n Sort by rating");
        printRstList(rstList);
    }

    @Test
    public void testByPrice() {
        rstList = new DbHelper().getRestaurantList(Default.generateRstDefaultLocation(), SortType.BY_PRICE,
                new ArrayList<RstFlavorTag>());
        System.out.println("\n\n Sort by price");
        printRstList(rstList);
    }

    @Test
    public void testTags() {
        List<RstFlavorTag> tags = new ArrayList<>();
        tags.add(RstFlavorTag.CHINESE);
        rstList = new DbHelper().getRestaurantList(Default.generateRstDefaultLocation(), SortType.BY_PRICE, tags);
        System.out.println("\n\n Tag by Chinese");
        printRstList(rstList);
    }

    private void printRstList(List<Restaurant> rstList) {
        for (Restaurant r : rstList) {
            System.out.println(r.getName());
        }
    }
}

package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.Restaurant;
import util.DbHelper;

public class CreateRstTest {
    @Test
    public void test() throws SQLException, InterruptedException {
        List<Restaurant> restaurants = new ArrayList<>();
//        restaurants.add(new Restaurant.Builder("Burger King", "Rose Park More Outlet")
//                .addCoverPhotoURL("1YEU2r6Yh0qEh553y7tGQQ.png").build());
//        restaurants.add(new Restaurant.Builder("KFC", "Gates-Hillman Center")
//                .addCoverPhotoURL("H0hpKU624Wt3KpitGnOoKw.png").build());
        restaurants.add(new Restaurant.Builder("Mcdonald", "Hamercheleg Hall")
                .addCoverPhotoURL("OaWnFTL0kSmC7xwdSiGRkQ.png").build());
        Thread.sleep(100);
        restaurants.add(new Restaurant.Builder("ShaXian XiaoChi", "CMU campus")
                .addCoverPhotoURL("MuzpA12nfqhOAtRbndCTAQ.png").build());
        Thread.sleep(100);
        restaurants.add(new Restaurant.Builder("Lanzhou Lamian", "Center Avenue")
                .addCoverPhotoURL("ERQT512InYz99CIVgvDdnQ.png").build());

        for (Restaurant r : restaurants) {
            new DbHelper().insertRestaurant(r);
        }
    }
}

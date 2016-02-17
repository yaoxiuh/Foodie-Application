package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Restaurant;
import model.User;
import model.ClientComment;
import model.ClientUser;
import util.DbHelper;

public class RstInsertTest {

    DbHelper helper;
    Restaurant rst1;
    Restaurant rst2;
    User user;

    @Before
    public void init() {
        helper = new DbHelper();
        user = new ClientUser("test", "test");
        rst1 = new Restaurant.Builder("Chinese Xiang", "Newell-Simon Hall Atrium, 5000 Forbes Ave.").build();

        List<String> commentPhotoList1 = new ArrayList<>();
        commentPhotoList1.add("cAqTxBWa_PZmJingiwUYRA.png");
        rst1.addComment(new ClientComment(user, 5, "This is the best food trunk I have ever seen, really!",
                commentPhotoList1));
        
        rst2 = new Restaurant.Builder("Taste of India","S Craig Street, Oakland").build();
        List<String> commentPhotoList2 = new ArrayList<>();
        commentPhotoList2.add("4WaaTE8DWDHUz8jQJsYOUg.png");
        commentPhotoList2.add("1VGEjW8t0RlfLn3AFVl0-w.png");
        commentPhotoList2.add("NqIefPQSE5EVuUDFHBPVFA.png");
        rst2.addComment(new ClientComment(user, 2, "Too expensive", commentPhotoList2));
    }

    @Test
    public void test() throws SQLException {
        helper.insertUser(user);
        helper.insertRestaurant(rst1);
        helper.insertRestaurant(rst2);
    }
}

package test;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import dataBase.DbSchema;
import model.Restaurant;
import util.DbHelper;

public class RstGetTests {
    DbHelper helper;
    @Before
    public void init() {
        helper = new DbHelper();
    }
    @Test
    public void test() throws SQLException {
        Restaurant rst = helper.getRestaurant(1448854053635L);
        System.out.println(rst.getComments().size());
        System.out.println(rst.getComments().get(0).getCommentWord());
        System.out.println(rst.getComments().get(0).getPhotoList().size());
        System.out.println(rst.getComments().get(0).getPhotoList().get(0));
    }
}

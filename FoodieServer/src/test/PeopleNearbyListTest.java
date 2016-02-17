package test;

import java.util.List;

import org.junit.Test;

import model.Default;
import model.User;
import util.DbHelper;

public class PeopleNearbyListTest {
    @Test
    public void test() {
        List<User> userList = new DbHelper().getPeopleNearbyList(Default.generateUserDefaultLocation(), 1449092596051L);
        for (User u : userList) {
            System.out.println(u.getUserName() + "\t" + u.getLocation().getExpireTime());
        }
    }
}

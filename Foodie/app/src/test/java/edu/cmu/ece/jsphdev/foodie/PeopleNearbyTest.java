package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.UserListCallBackListener;
import edu.cmu.ece.jsphdev.foodie.model.Default;
import edu.cmu.ece.jsphdev.foodie.model.Location;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Created by guangyu on 12/2/15.
 */
public class PeopleNearbyTest {
    @Test
    public void test() {
        Location location = Default.generateUserDefaultLocation();
        User user = HttpFacade.getUser(1449092596051L);
        final List<User> result = new ArrayList<>();
        HttpFacade.getPeopleNearby(location, user, new UserListCallBackListener() {

            @Override
            public void onFinish(List<User> rstList) {
                result.addAll(rstList);
            }

            @Override
            public void onError(Exception e) {
                System.out.print("fail");
            }
        });
        try {
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(result.size());
        System.out.println(result.get(0).getUserName() + result.get(0).getLocation().getExpireTime().getTime());
        System.out.println(result.get(6).getUserName() + result.get(6).getLocation().getExpireTime().getTime());
    }
}

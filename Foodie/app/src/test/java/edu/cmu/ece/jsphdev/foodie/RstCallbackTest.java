package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.RstCallBackListener;
import edu.cmu.ece.jsphdev.foodie.model.Restaurant;

/**
 * Created by guangyu on 11/30/15.
 */
public class RstCallbackTest {
    Restaurant rstDemo;
    @Test
    public void test() throws InterruptedException {
        HttpFacade.getRestaurant(1448854053635L, new RstCallBackListener() {
            @Override
            public void onFinish(Restaurant restaurant) {
                rstDemo = restaurant;
            }

            @Override
            public void onError(Exception e) {

            }
        });

        Thread.sleep(2000);
        System.out.println(rstDemo.getAddress());
        System.out.println(rstDemo.getLocation().getExpireTime());
        System.out.println(rstDemo.getLocation().getLongitude());
        System.out.println(rstDemo.getAverageRating());
        System.out.println(rstDemo.getTagList().get(0));
        System.out.println(rstDemo.getComments().get(0).getCommentID());
        System.out.println(rstDemo.getComments().get(0).getCommentWord());
        System.out.println(rstDemo.getComments().get(0).getCreator().getUserName());
        System.out.println(rstDemo.getComments().get(0).getPhotoList().get(0));
    }
}

package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Created by guangyu on 12/1/15.
 */
public class CreateUserTest {
    @Test
    public void test() {
        User user = HttpFacade.createUser("tiancaioyzy", "aiyuanshen");
        System.out.println(user.getUserId());
    }
}

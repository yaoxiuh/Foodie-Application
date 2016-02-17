package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Created by guangyu on 12/2/15.
 */
public class GetUserTest {
    @Test
    public void test() {
        User user = HttpFacade.getUser(1449032110223L);
        System.out.println(user.getPassword());
    }
}

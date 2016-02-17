package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;
import static org.junit.Assert.*;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;

/**
 * Created by guangyu on 12/6/15.
 */
public class NewUserTest {
    @Test
    public void test() {
        assertEquals(false, HttpFacade.isNewUser("tiancaioyzy"));
        assertEquals(true, HttpFacade.isNewUser("tiancaioyzysb"));
    }
}

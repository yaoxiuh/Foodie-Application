package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;

import static org.junit.Assert.*;

/**
 * Created by guangyu on 12/6/15.
 */
public class UserAuthTest {
    @Test
    public void test() {
        assertNull(HttpFacade.userAuth("tiancaioyzy", "oyzysb"));
        assertNotNull(HttpFacade.userAuth("tiancaioyzy", "aiyuanshen"));
        assertEquals(1449032110223L, HttpFacade.userAuth("tiancaioyzy", "aiyuanshen").getUserId());
    }
}

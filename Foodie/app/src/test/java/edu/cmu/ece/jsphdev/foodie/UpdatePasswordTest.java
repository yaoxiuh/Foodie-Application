package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;

import  static org.junit.Assert.*;

/**
 * Created by guangyu on 12/6/15.
 */
public class UpdatePasswordTest {
    @Test
    public void test() {
        assertEquals(true, HttpFacade.changePassword(1449091803889L, "newpassword"));
        assertEquals(false, HttpFacade.changePassword(14498991803889L, "newpassword"));
    }
}


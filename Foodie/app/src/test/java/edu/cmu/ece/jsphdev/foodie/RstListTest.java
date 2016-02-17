package edu.cmu.ece.jsphdev.foodie;

import junit.extensions.TestSetup;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.model.Default;
import edu.cmu.ece.jsphdev.foodie.model.RstFlavorTag;
import edu.cmu.ece.jsphdev.foodie.model.SortType;

/**
 * Created by guangyu on 12/1/15.
 */
public class RstListTest {
    @Ignore
    @Test
    public void testNoTag() {
        List<RstFlavorTag> tagList = new ArrayList<>();
        tagList.add(RstFlavorTag.NULL);
        HttpFacade.getRestaurantList(Default.generateUserDefaultLocation(), SortType.BY_PRICE, tagList);
    }

    @Test
    public void testTag() {
        List<RstFlavorTag> tagList = new ArrayList<>();
        tagList.add(RstFlavorTag.CHINESE);
        HttpFacade.getRestaurantList(Default.generateUserDefaultLocation(), SortType.BY_PRICE, tagList);
    }
}

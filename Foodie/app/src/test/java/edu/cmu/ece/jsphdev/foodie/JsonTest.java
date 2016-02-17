package edu.cmu.ece.jsphdev.foodie;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.model.Restaurant;
import edu.cmu.ece.jsphdev.foodie.model.RstFlavorTag;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public final class JsonTest {
    @Test
    public void testGson() {
        List<RstFlavorTag> tagList = new ArrayList<>();
        tagList.add(RstFlavorTag.AMERICAN);
        tagList.add(RstFlavorTag.FREE_WIFI);
        Restaurant sampleRst = new Restaurant.Builder("Chinese Xiang", "5000 Forbes Ave")
                .addCoverPhotoURL("/sample URL").addPhoneNumber("10086").addTagList(tagList).build();
        System.out.println(sampleRst.getId());
        Gson gson = new Gson();
        String jsonResult = gson.toJson(sampleRst);
        System.out.println(jsonResult);

        Restaurant rebuildRST = gson.fromJson(jsonResult, Restaurant.class);
        System.out.println(rebuildRST.getId());

        String jsonResultSecond = gson.toJson(rebuildRST);
        System.out.println(jsonResultSecond);

        Restaurant rebuildRstSecond = gson.fromJson(jsonResultSecond, Restaurant.class);
        System.out.println(rebuildRstSecond.getId());
    }
}
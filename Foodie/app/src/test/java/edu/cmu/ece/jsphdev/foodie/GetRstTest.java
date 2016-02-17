package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import edu.cmu.ece.jsphdev.foodie.httpUtil.GetRequest;
import edu.cmu.ece.jsphdev.foodie.model.Restaurant;

/**
 * Created by guangyu on 11/12/15.
 */
public class GetRstTest {
    @Test
    public void test() throws IOException {
        long id = 1448854053636L;
        Restaurant rstDemo = GetRequest.getRestaurant(id);
        System.out.println(rstDemo.getAddress());
        System.out.println(rstDemo.getLocation().getExpireTime());
        System.out.println(rstDemo.getLocation().getLongitude());
        System.out.println(rstDemo.getAverageRating());
        System.out.println(rstDemo.getTagList().get(0));
        System.out.println(rstDemo.getComments().get(0).getCommentID());
        System.out.println(rstDemo.getComments().get(0).getCommentWord());
        System.out.println(rstDemo.getComments().get(0).getCreator().getUserName());
        System.out.println(rstDemo.getComments().get(0).getPhotoList().get(0));

        String imageId = rstDemo.getComments().get(0).getPhotoList().get(0);

        byte[] bytes = GetRequest.getImage(imageId);
        System.out.println(bytes);
        File file = new File("/Users/guangyu/Desktop/testImage.png");
        FileOutputStream out = new FileOutputStream(file);
        out.write(bytes);
    }
}

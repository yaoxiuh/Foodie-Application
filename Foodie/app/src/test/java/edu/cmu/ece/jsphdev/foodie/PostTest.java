package edu.cmu.ece.jsphdev.foodie;


import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;

/**
 * Created by guangyu on 11/30/15.
 */
public class PostTest {
    @Test
    public void test() throws IOException {
        File image = new File("/Users/guangyu/Desktop/postTest.png");
        InputStream in = new FileInputStream(image);
        byte[] imageBytes = new byte[(int)image.length()];
        in.read(imageBytes);

        String result = HttpFacade.postCommentPhoto(imageBytes);
        System.out.println(result);
    }
}

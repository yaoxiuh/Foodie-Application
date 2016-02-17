package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;

/**
 * Created by guangyu on 12/7/15.
 */
public class UpdateAvatarTest {
    @Test
    public void test() throws IOException {
        File file = new File("/Users/guangyu/Desktop/obama.png");
        InputStream in = new FileInputStream(file);
        byte[] imageBytes = new byte[(int)file.length()];
        in.read(imageBytes);
        HttpFacade.postUserAvatar(1448985926563L, imageBytes);
    }
}

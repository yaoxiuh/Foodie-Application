package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import edu.cmu.ece.jsphdev.foodie.httpUtil.GetRequest;
import edu.cmu.ece.jsphdev.foodie.model.Default;

/**
 * Created by guangyu on 11/13/15.
 */
public class GetImageTest {
    @Test
    public void test() throws IOException {
        byte[] response = GetRequest.getImage("4H3ulEEGNxPNyfV_PXtZRw.png");
        System.out.println(response);

        File newImage = new File("C:\\Users\\Guangyu\\Downloads\\yushen.png");
        FileOutputStream out = new FileOutputStream(newImage);
        out.write(response);
        out.close();
    }

}
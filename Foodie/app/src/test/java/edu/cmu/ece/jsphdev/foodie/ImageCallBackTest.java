package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.ImageCallBackListener;

/**
 * Created by guangyu on 11/30/15.
 */
public class ImageCallBackTest {
    @Test
    public void test() throws InterruptedException {

        HttpFacade.getImage("4H3ulEEGNxPNyfV_PXtZRw.png", new ImageCallBackListener() {

                                @Override
                                public void onFinished(byte[] bytes) {
                                    System.out.print(bytes);

                                    File newImage = new File
                                            ("/Users/guangyu/Desktop/testCallBackImage.png");

                                    FileOutputStream out = null;
                                    try {
                                        out = new FileOutputStream(newImage);
                                        out.write(bytes);
                                        out.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    System.out.println("Fails to get image");
                                }
                            }

        );

        Thread.sleep(2000);
    }
}

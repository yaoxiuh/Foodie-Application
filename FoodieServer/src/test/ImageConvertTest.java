package test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class ImageConvertTest {

    @Test
    public void testImageConvert() throws NoSuchAlgorithmException, IOException{
        CreateDemoImages createDemoImages = new CreateDemoImages();
        createDemoImages.convertBatch();
    }
}
    
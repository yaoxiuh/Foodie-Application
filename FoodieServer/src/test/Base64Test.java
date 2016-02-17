package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dataBase.DbSchema;
import util.ImageHelper;

public class Base64Test {

    File testImage;
    byte imageData[];
    String base64String;

    @Before
    public void Init() throws IOException {
        testImage = new File(DbSchema.getDBPath() + "/image/1.png");
        FileInputStream in;
        in = new FileInputStream(testImage);
        byte imageData[] = new byte[(int) testImage.length()];
        in.read(imageData);
        in.close();

        base64String = Base64.encodeBase64URLSafeString(imageData);
    }

    @Ignore
    @Test
    public void testEncodeAndDecode() throws IOException {

        byte[] receivedData = Base64.decodeBase64(base64String);

        FileOutputStream out = new FileOutputStream(new File(DbSchema.getDBPath() + "/sample/receivedImage.png"));
        out.write(receivedData);
        out.close();
    }

    @Test
    public void testmd5() throws NoSuchAlgorithmException, IOException {
        String md5Str = ImageHelper.Base64ToMd5(base64String);
        System.out.println(md5Str);
    }
}

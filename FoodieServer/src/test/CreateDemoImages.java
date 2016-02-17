package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;

import dataBase.DbSchema;
import util.ImageHelper;

public class CreateDemoImages {

    @Test
    public void convertBatch() throws IOException, NoSuchAlgorithmException {
        // 1, 10 is a magic number
        for (int i = 1; i < 10; i++) {
            String imagePath = DbSchema.getDBPath() + "image/" + i + ".png";
            File file = new File(imagePath);
            FileInputStream in = new FileInputStream(file);
            byte imageByte[] = new byte[(int) file.length()];
            in.read(imageByte);
            in.close();

            String imageBase64Str = Base64.encodeBase64URLSafeString(imageByte);
            String md5St = ImageHelper.Base64ToMd5(imageBase64Str);

            String newImageName = md5St + ".png";
            System.out.println(newImageName);
            String newImagePath = DbSchema.getDBPath() + "image/" + newImageName;

            byte[] receivedByte = Base64.decodeBase64(imageBase64Str);
            File newImage = new File(newImagePath);
            FileOutputStream out = new FileOutputStream(newImage);
            out.write(receivedByte);
            out.close();
        }
    }

}

package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.codec.binary.Base64;


public class ImageHelper {

    public static String Base64ToMd5(String base64Input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] imageByte = Base64.decodeBase64(base64Input);

        byte[] md5 = md.digest(imageByte);

        String md5Str = Base64.encodeBase64URLSafeString(md5);
        return md5Str;
    }
}

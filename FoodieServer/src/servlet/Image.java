package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import dataBase.DbSchema;
import util.ImageHelper;

/**
 * Servlet implementation class Image
 */
@WebServlet("/image")
public class Image extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
      //  String imagePath = DbSchema.getDBPath() + "image/" + name;
        String imagePath = DbSchema.getImagepath() + "image/" + name;
        File image = new File(imagePath);
        InputStream in = new FileInputStream(image);
        byte imageByte[] = new byte[(int) image.length()];
        in.read(imageByte);
        in.close();

        String imageBase64Str = Base64.encodeBase64URLSafeString(imageByte);
        response.getWriter().write(imageBase64Str);
        // System.out.println("send image " + name);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InputStream in = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        String fileName = ImageHelper.Base64ToMd5(content.toString()) + ".png";
        //String filePath = DbSchema.getDBPath() + "image/" + fileName;
        String filePath = DbSchema.getImagepath() + "image/" + fileName;

        byte[] imageBytes = Base64.decodeBase64(content.toString());
        FileOutputStream out = new FileOutputStream(new File(filePath));
        out.write(imageBytes);
        out.close();
        System.out.println("receive image " + fileName);
        response.getWriter().write(fileName);
    }
}

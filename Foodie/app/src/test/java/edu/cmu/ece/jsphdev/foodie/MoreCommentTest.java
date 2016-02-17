package edu.cmu.ece.jsphdev.foodie;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.model.ClientComment;
import edu.cmu.ece.jsphdev.foodie.model.ClientUser;
import edu.cmu.ece.jsphdev.foodie.model.Comment;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Created by guangyu on 12/1/15.
 */
public class MoreCommentTest {
    @Test
    public void toRst1() throws IOException {
        User creator = new ClientUser("delicious", "food");
        List<String> commentPhotoList1 = new ArrayList<>();
        commentPhotoList1.add(uploadImage("1.png"));
        commentPhotoList1.add(uploadImage("2.png"));
        Comment comment1 = new ClientComment(creator, 5, "Comment the second time", commentPhotoList1);
        HttpFacade.postComment(comment1, 1448853555731L);

        List<String> commentPhotoList2 = new ArrayList<>();
        commentPhotoList2.add(uploadImage("3.png"));
        commentPhotoList2.add(uploadImage("4.png"));
        commentPhotoList2.add(uploadImage("5.png"));
        Comment comment2 = new ClientComment(creator, 1, "The food sucks, really", commentPhotoList2);
        HttpFacade.postComment(comment2, 1448853555731L);
    }

    @Ignore
    @Test
    public void toRst2() throws IOException {
        User creator = new ClientUser("nimei", "qushiba");
        List<String> commentPhotoList = new ArrayList<>();
        commentPhotoList.add(uploadImage("6.png"));
        commentPhotoList.add(uploadImage("7.png"));
        Comment comment1 = new ClientComment(creator, 3, "Fairly Good Restaurant", commentPhotoList);
        HttpFacade.postComment(comment1, 1448839564428L);
    }

    private String uploadImage(String fileName) throws IOException {
        String dirPath = "/Users/guangyu/Desktop/";
        String filePath = dirPath + fileName;
        File image = new File(filePath);
        InputStream in = new FileInputStream(image);
        byte[] imageBytes = new byte[(int)image.length()];
        in.read(imageBytes);
        String imageName = HttpFacade.postCommentPhoto(imageBytes);
        return imageName;
    }
}

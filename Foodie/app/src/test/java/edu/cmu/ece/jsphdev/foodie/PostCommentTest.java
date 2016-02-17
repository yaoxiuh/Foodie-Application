package edu.cmu.ece.jsphdev.foodie;

import org.junit.Test;

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
public class PostCommentTest {
    @Test
    public void test() {
        User creator = new ClientUser("guangstick", "stickguang");
        List<String> commentPhotoList = new ArrayList<>();
        commentPhotoList.add("4WaaTE8DWDHUz8jQJsYOUg.png");
        commentPhotoList.add("1VGEjW8t0RlfLn3AFVl0-w.png");
        commentPhotoList.add("NqIefPQSE5EVuUDFHBPVFA.png");
        Comment comment = new ClientComment(creator, 5, "Test comment post", commentPhotoList);
        HttpFacade.postComment(comment, 1448854053635L);
    }

}

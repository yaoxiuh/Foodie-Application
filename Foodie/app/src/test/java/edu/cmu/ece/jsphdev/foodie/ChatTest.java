package edu.cmu.ece.jsphdev.foodie;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import edu.cmu.ece.jsphdev.foodie.httpUtil.GetRequest;
import edu.cmu.ece.jsphdev.foodie.httpUtil.HttpFacade;
import edu.cmu.ece.jsphdev.foodie.httpUtil.PostRequest;
import edu.cmu.ece.jsphdev.foodie.model.ClientUser;
import edu.cmu.ece.jsphdev.foodie.model.Msg;
import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Created by guangyu on 12/1/15.
 */
public class ChatTest {
    final User user1;
    final User user2;


    public ChatTest() {
        user1 = new ClientUser("testChatUser1", "1");
        user2 = new ClientUser("testChatUser2", "2");
    }

    @Test
    public void test() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Msg msg1 = new Msg(user1, user2, "This is the first message from 1 to 2");
                boolean result = PostRequest.postMsg(msg1);
                System.out.println("Message 1 " + result);
                try {
                    Thread.sleep(1* 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Msg msg2 = new Msg(user1, user2, "This is the second message from 1 to 2");
                boolean result2 = PostRequest.postMsg(msg2);
                System.out.println("Message 2 " + result2);
            }
        }).start();

        new Thread((new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(2 *1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<Msg> messages = GetRequest.getMsg(user2);
                    System.out.println(messages.size());
                    for (Msg m : messages) {
                        System.out.println(m.getContent());
                    }
                }
            }
        })).start();
        Thread.sleep(20 * 1000);
    }
}

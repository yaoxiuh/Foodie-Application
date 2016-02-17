package edu.cmu.ece.jsphdev.foodie.httpUtil;

import java.util.List;

import edu.cmu.ece.jsphdev.foodie.model.Msg;

/**
 * Created by guangyu on 12/2/15.
 */
public interface MsgListCallBackListener {
    void onNewMessages(List<Msg> messages);

    void onNoMessages();

    void onError(Exception e);
}

package edu.cmu.ece.jsphdev.foodie.httpUtil;

import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Created by guangyu on 12/2/15.
 */
public interface UserCallBackListener {
    void onFinish(User user);

    void onError(Exception e);
}

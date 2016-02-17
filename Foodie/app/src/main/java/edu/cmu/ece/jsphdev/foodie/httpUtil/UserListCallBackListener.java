package edu.cmu.ece.jsphdev.foodie.httpUtil;

import java.util.List;

import edu.cmu.ece.jsphdev.foodie.model.User;

/**
 * Created by guangyu on 12/2/15.
 */
public interface UserListCallBackListener {
    void onFinish(List<User> rstList);

    void onError(Exception e);
}

package edu.cmu.ece.jsphdev.foodie.httpUtil;

import edu.cmu.ece.jsphdev.foodie.model.Restaurant;

/**
 * Created by guangyu on 11/30/15.
 */
public interface RstCallBackListener {
    void onFinish(Restaurant restaurant);

    void onError(Exception e);
}

package edu.cmu.ece.jsphdev.foodie.httpUtil;

/**
 * Created by guangyu on 12/2/15.
 */
public interface CallBackListener {
    void onFinished(String string);

    void onError(Exception e);
}

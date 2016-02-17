package edu.cmu.ece.jsphdev.foodie.httpUtil;

/**
 * Created by guangyu on 11/30/15.
 */
public interface ImageCallBackListener {
    void onFinished(byte[] bytes);

    void onError(Exception e);
}

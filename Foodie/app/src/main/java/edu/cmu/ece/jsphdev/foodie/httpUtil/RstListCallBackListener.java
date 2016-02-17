package edu.cmu.ece.jsphdev.foodie.httpUtil;

import java.util.List;

/**
 * Created by guangyu on 12/2/15.
 */
public interface RstListCallBackListener {
    void onFinish(List<Long> rstList);

    void onError(Exception e);
}

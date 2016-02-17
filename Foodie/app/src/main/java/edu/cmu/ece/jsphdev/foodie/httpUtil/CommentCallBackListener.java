package edu.cmu.ece.jsphdev.foodie.httpUtil;

import edu.cmu.ece.jsphdev.foodie.model.Comment;

/**
 * Created by guangyu on 12/2/15.
 */
public interface CommentCallBackListener {
    void onFinished(Comment comment);

    void onError(Exception e);
}

package edu.cmu.ece.jsphdev.foodie.exception;

import edu.cmu.ece.jsphdev.foodie.model.Comment;

/**
 * @author Yaoxiu Hu
 * Commont not found exception
 */
public class CommentNotFoundException extends Exception {
    private String errMsg;

    public void setErrMsg(String errMsg){
        this.errMsg = errMsg;
    }

    public String getErrMsg(){
        return this.errMsg;
    }

    public CommentNotFoundException(ErrorType errorType){
        setErrMsg(errorType.toString());
    }
}

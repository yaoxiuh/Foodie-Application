package edu.cmu.ece.jsphdev.foodie.exception;

/**
 * Created by Yaoxiu Hu on 2015/12/11.
 */
public class RestaurantNotFoundException extends Exception {
    private String errMsg;

    public void setErrMsg(String errMsg){
        this.errMsg = errMsg;
    }

    public String getErrMsg(){
        return this.errMsg;
    }
    public RestaurantNotFoundException(ErrorType errorType){
        setErrMsg(errorType.toString());
    }
}

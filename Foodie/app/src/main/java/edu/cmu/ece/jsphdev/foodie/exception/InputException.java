package edu.cmu.ece.jsphdev.foodie.exception;

/**
 * Created by Yaoxiu Hu on 2015/11/24.
 */
public class InputException extends Exception{

    private String errMsg;
    private int errNo;

    public int getErrNo() {
        return errNo;
    }

    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }



    public String getErrMsg() {
        switch (errNo){
            case 0:
                return "Input is empty";
            case 1:
                return "UserName is invalid";
            case 2:
                return "Password is invalid";
            default:
                break;
        }
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


    public InputException(ErrorType errorType){
           setErrMsg(errorType.toString());
           setErrNo(errorType.ordinal());
    }


}

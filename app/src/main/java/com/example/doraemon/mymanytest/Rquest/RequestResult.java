package com.example.doraemon.mymanytest.Rquest;

import android.text.TextUtils;

/**
 * Created by csy on 2015/9/18.
 */
public class RequestResult {

    public boolean connectSuccess = true;

    public int code;

    public String errCode;

    public String message;

    public boolean isRequestSuccess() {
        return connectSuccess && code == 1 && TextUtils.isEmpty(errCode) && TextUtils.isEmpty(message);
    }

    public boolean isConnectSuccess() {
        return connectSuccess;
    }

    public String getErrorMsg() {
        return message;
    }
}

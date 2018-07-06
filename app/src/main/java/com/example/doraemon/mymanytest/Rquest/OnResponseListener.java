package com.example.doraemon.mymanytest.Rquest;

/**
 * Created by csy on 2015/9/18.
 */
public abstract class OnResponseListener {
    public void onStart() {

    }

    public abstract void onComplete(boolean isSuccess, RequestResult requestResult);
}

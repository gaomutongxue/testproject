package com.example.doraemon.mymanytest.Rquest;

/**
 * Created by csy on 2015/9/18.
 */
public class ResponseResultError {
    public String status;

    public Body body;

    public class Body{
        public String status;

        public String errCode;

        public String errMessage;
    }
}

package com.example.doraemon.mymanytest.UploadIconRequest;


import com.example.doraemon.mymanytest.Rquest.RequestResult;



/**
 * Created by csy on 2015/9/18.
 */
public class UploadIconResult extends RequestResult {

    public Response response;

    public static class Response {
        public int code;
        public String message;
        public String data;
    }
}
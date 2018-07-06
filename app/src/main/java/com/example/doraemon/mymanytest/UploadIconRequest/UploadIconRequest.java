package com.example.doraemon.mymanytest.UploadIconRequest;

import com.example.doraemon.mymanytest.Rquest.BaseRequest;
import com.example.doraemon.mymanytest.Rquest.OnResponseListener;


/**
 * Created by csy on 2015/9/18.
 */
public class UploadIconRequest extends BaseRequest {

    public static final String URL = "Members/upload_head_img";

    public UploadIconRequest() {
        url = URL;
        result = new UploadIconResult();
        requestByGet = false;
    }

    public boolean requestBackground(UploadIconRequestParams params, OnResponseListener listener) {
        if (!params.checkParams()) {
            return false;
        }
        return super.requestBackground(params, listener);
    }

    public UploadIconResult request(UploadIconRequestParams params) {
        return (UploadIconResult) request(params);
    }

    public void loadResponse(String responseData) {
        ((UploadIconResult) result).response = gson.fromJson(responseData, UploadIconResult.Response.class);
    }
}

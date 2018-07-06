package com.example.doraemon.mymanytest.UploadIconRequest;

import com.example.doraemon.mymanytest.Rquest.RequestParams;



/**
 * Created by csy on 2015/9/18.
 */
public class UploadIconRequestParams extends RequestParams {

    public UploadIconRequestParams() {
        needParamsList.add("photo"); //文件的base64编码字符串
    }

    // 文件的base64编码字符串
    public UploadIconRequestParams setFile(String file) {
        requestParamsMap.put("photo", file);
        return this;
    }
}
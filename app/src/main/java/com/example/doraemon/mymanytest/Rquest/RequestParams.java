package com.example.doraemon.mymanytest.Rquest;

import android.text.TextUtils;
import android.util.Log;

import com.example.doraemon.mymanytest.Util.DefaultUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;


/**
 * Created by csy on 2015/9/18.
 */
public class RequestParams {


    protected LinkedHashMap<String, String> requestParamsMap;
    public ArrayList<String> needParamsList;

    public RequestParams() {
        requestParamsMap = new LinkedHashMap<String, String>();
        needParamsList = new ArrayList<String>();
    }

    public void addParams(String ParamsName, String ParamsValue) {
        requestParamsMap.put(ParamsName, ParamsValue);
    }

    public LinkedHashMap<String, String> getParams() {
        return requestParamsMap;
    }

    public boolean checkParams() {
        return true;
    }

    public static String appendHead(String url) {
        if (url.toLowerCase().startsWith("http") || url.toLowerCase().startsWith("https")) {
            return url;
        }
        return UrlConst.URL_HOST + url;
    }
//TODO  文件传输
    public String appendOaUrlParams(String url) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(url);
        Iterator entries = requestParamsMap.entrySet().iterator();
        StringBuffer sbb = new StringBuffer();
        if (entries.hasNext()) {
            while (entries.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) entries.next();
                if (stringBuffer.indexOf("?") == -1) {
                    stringBuffer.append("?");
                } else {
                    stringBuffer.append("&");
                }
                stringBuffer.append(entry.getKey());
                stringBuffer.append("=");
                stringBuffer.append(entry.getValue());
                sbb.append(entry.getKey());
                sbb.append(entry.getValue());
            }
        }
        //后面加个token加密参数
        return stringBuffer.toString();
    }

    public String appendParams(String url) {
        if (TextUtils.isEmpty(url)) {
            Log.e("error", "empty url!");
            return "";
        }
        if (url.startsWith(UrlConst.URL_OA_hEAD)) return appendOaUrlParams(url);
        StringBuffer stringBuffer = new StringBuffer();
        if (!url.toLowerCase().startsWith("http")) {
            stringBuffer.append(UrlConst.URL_HOST);
        }
        stringBuffer.append(url);

        Iterator entries = requestParamsMap.entrySet().iterator();
        StringBuffer sbb = new StringBuffer();
        if (entries.hasNext()) {
            while (entries.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry) entries.next();
                if (stringBuffer.indexOf("?") == -1) {
                    stringBuffer.append("?");
                } else {
                    stringBuffer.append("&");
                }
                stringBuffer.append(entry.getKey());
                stringBuffer.append("=");
                stringBuffer.append(entry.getValue());
                sbb.append(entry.getKey());
                sbb.append(entry.getValue());
            }
            stringBuffer.append("&");
            stringBuffer.append("token=");
        } else {
            stringBuffer.append("?");
            stringBuffer.append("token=");
        }
        //后面加个token加密参数
        return stringBuffer.append(getFinalParams(sbb.toString())).toString();
    }

    private String getFinalParams(String key) {
        return DefaultUtils.md5(DefaultUtils.sha1(DefaultUtils.md5(key + UrlConst.URL_key) + "+1") + "-1");
    }
}

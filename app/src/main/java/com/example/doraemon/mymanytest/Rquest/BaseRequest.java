package com.example.doraemon.mymanytest.Rquest;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.doraemon.mymanytest.UploadIconRequest.UploadIconRequest;
import com.example.doraemon.mymanytest.Util.DefaultUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by csy on 2015/9/18.
 */
public abstract class BaseRequest<Result extends RequestResult> {
    public static final int REQUEST_TIME_OUT = 10;

    protected Result result;
    protected Gson gson;
    protected String url;
    private OnResponseListener onResponseListener;
    protected boolean requestByGet;
    private Request volleyRequest;

    public BaseRequest() {
        gson = new Gson();
        requestByGet = true;
    }

    public boolean OaRequestBackground(final RequestParams params, OnResponseListener listener) {
        onResponseListener = listener;
        if (!params.checkParams() || result == null || url == null) {
            return false;
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("error", response);
                RequestResult result = getOaRequestResult(true, response);
                onResponseListener.onComplete(result.isRequestSuccess(), result);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result.code = 0;
                result.connectSuccess = false;
                result.message = "网络连接失败";
                onResponseListener.onComplete(false, result);
                Log.e("error", volleyError.toString());
            }
        };
        volleyRequest = null;
        if (requestByGet) {
            String newUrl = params.appendParams(url);
            Log.e("url", newUrl);
            volleyRequest = new StringRequest(Request.Method.GET, newUrl, responseListener, errorListener);
        } else {
            String newUrl = params.appendHead(url);
            Log.e("url", newUrl);
            volleyRequest = new StringRequest(Request.Method.POST, newUrl, responseListener, errorListener) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> map = new HashMap<String, String>();
                    Iterator entries = params.getParams().entrySet().iterator();
                    StringBuffer sbb = new StringBuffer();
                    while (entries.hasNext()) {
                        HashMap.Entry entry = (HashMap.Entry) entries.next();
                        map.put(entry.getKey().toString(), entry.getValue().toString());
                        sbb.append(entry.getKey());
                        sbb.append(entry.getValue());
                    }
                    map.put("token", getFinalParams(sbb.toString()));
                    return map;
                }
            };
        }
//        if (BaseRequest.this instanceof LoginRequest
//                || BaseRequest.this instanceof GetHostRequest
//                || BaseRequest.this instanceof RegisterRequest
//                || BaseRequest.this instanceof GetCheckCodeRequest
//                || BaseRequest.this instanceof ModifyPasswordRequest
//                || BaseRequest.this instanceof UpdateCheckRequest) {
//            RequestManager.getInstance().addRequestWithoutSessionCheck(volleyRequest);
//        } else {
        RequestManager.getInstance().addRequest(volleyRequest);
        // }
        return true;
    }

    public boolean requestBackground(final RequestParams params, OnResponseListener listener) {
        onResponseListener = listener;
        if (!params.checkParams() || result == null || url == null) {
            return false;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("error", response);
                RequestResult result = getRequestResult(true, response);
                onResponseListener.onComplete(result.isRequestSuccess(), result); //YO==TODO 这个是抽象类是做什么的
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result.code = 0;
                result.connectSuccess = false;
                result.message = "网络连接失败";
                onResponseListener.onComplete(false, result);
                Log.e("error", volleyError.toString());
            }
        };
        volleyRequest = null;
        if (requestByGet) {
            String newUrl = params.appendParams(url);
            Log.e("url", newUrl);
            volleyRequest = new StringRequest(Request.Method.GET, newUrl, responseListener, errorListener);
        } else {

            if (this.getClass().equals(UploadIconRequest.class) ) {
                String path = params.getParams().get("photo");
                if (TextUtils.isEmpty(path)) {
                    return false;
                }
                final File file = new File(params.getParams().get("photo"));
                if (!file.exists()) {
                    return false;
                }
                String newUrl = params.appendHead(url);
                Log.e("url", newUrl);
                volleyRequest = new MultiPartStringRequest(
                        Request.Method.POST, newUrl, responseListener, errorListener) {

                    @Override
                    public Map<String, File> getFileUploads() {
                        HashMap<String, File> map = new HashMap<String, File>();
                        map.put("photo", file);
                        return map;
                    }

                    @Override
                    public Map<String, String> getStringUploads() {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("token", getFinalParams(""));
                        Log.e("tooken", map.get("token"));
                        return map;
                    }
                };
                RequestManager.getInstance().getRequestUploadQueue().add(volleyRequest);
                return true;
            } else {
                final String newUrl = params.appendHead(url);
                Log.e("url", newUrl);
                volleyRequest = new StringRequest(Request.Method.POST, newUrl, responseListener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() {
                        if (newUrl.endsWith("merchant.do")) {
                            return params.getParams();
                        } else {
                            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                            StringBuffer sbb = new StringBuffer();
                            for (String key : params.getParams().keySet()) {
                                map.put(key, params.getParams().get(key));
                            }
                            for (String key : map.keySet()) {
                                sbb.append(key);
                                sbb.append(map.get(key));
                            }
                            map.put("token", getFinalParams(sbb.toString()));
                            return map;
                        }
                    }
                };
            }
        }
        RequestManager.getInstance().addRequest(volleyRequest);
        return true;
    }

    private String getFinalParams(String key) {
        Log.e("key", key);
        String finalString = DefaultUtils.md5(DefaultUtils.sha1(DefaultUtils.md5(key + UrlConst.URL_key) + "+1") + "-1");
        Log.e("finalkey", finalString);
        return finalString;
    }

    public void request(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(method, url, listener, errorListener);
        RequestManager.getInstance().addRequest(request);
    }

    protected RequestResult request(RequestParams params) {
        result.code = 0;
//        try {
//            RequestFuture future = RequestFuture.newFuture();
//            RequestQueue mQueue = Volley.newRequestQueue(getApplicatinContext());
//            StringRequest  stringRequest = new StringRequest("http://www.baidu.com",future, future);
//            mQueue.add(stringRequest);
//            String response = (String) future.get(REQUEST_TIME_OUT, TimeUnit.SECONDS);
//            return getRequestResult(true, response);
//        } catch (Exception e) {
//            result.status = 0;
//            result.errCode = e.toString();
//            Log.e("", e.toString());
//        }
        return result;
    }

    public RequestResult getOaRequestResult(boolean isConnectSuccess, String response) {
        result.connectSuccess = isConnectSuccess;
        if (!isConnectSuccess) {
            return result;
        }
        if (TextUtils.isEmpty(response)) {
            result.code = 0;
            result.errCode = "empty response";
            result.message = "操作失败";
            return result;
        } else {
            result.code = 1;
            loadResponse(response);
            return result;
        }
    }

    public RequestResult getRequestResult(boolean isConnectSuccess, String response) {
        result.connectSuccess = isConnectSuccess;
        JSONObject object = null;
        try {
            object = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            result.connectSuccess = false;
            result.code = 0;
            result.message = "server error";
            return result;
        }
        if (!isConnectSuccess) {
            return result;
        }
        if (TextUtils.isEmpty(response)) {
            result.code = 0;
            result.errCode = "empty response";
            result.message = "操作失败";
            return result;
        } else try {
            if (TextUtils.isEmpty(response) || (object != null && object.getInt("code") == 0)) {
                ResponseError responseError = gson.fromJson(response, ResponseError.class);
                result.code = 0;
                result.errCode = responseError.code;
                result.message = responseError.message;
                return result;
            } else if (response.indexOf("errCode") != -1) {
                ResponseResultError resultError = gson.fromJson(response, ResponseResultError.class);
                result.code = 1;
                result.errCode = resultError.body.errCode;
                result.message = resultError.body.errMessage;
                return result;
            } else {  //<!DOCTYPE html>
                result.code = 1;
                loadResponse(response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isCacheEnable;

    public void cancel() {

    }

    public abstract void loadResponse(String responseData);
}

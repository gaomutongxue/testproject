package com.example.doraemon.mymanytest.Rquest;

import android.app.DownloadManager;
import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.doraemon.mymanytest.Common.Setting;

import java.util.ArrayList;
import java.util.Map;



/**
 * Created by csy on 2015/9/24.
 */
public class RequestManager<T> {

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    public static final String SESSION_COOKIE = "JSESSIONID";

    // 正常请求协议请求队列
    private RequestQueue requestNormalQueue;

    // 请求较久的，或者大量普通请求（例如图片下载之类）
    private RequestQueue requestBackgroundQueue;

    private RequestQueue requestUploadQueue;

    private RequestManager() {
    }

    private static class RequestManagerHolder {
        protected static RequestManager requestManager = new RequestManager();
    }

    public static RequestManager getInstance() {
        return RequestManagerHolder.requestManager;
    }

    public void init(Context context) {
        requestNormalQueue = Volley.newRequestQueue(context);
        requestBackgroundQueue = Volley.newRequestQueue(context);
        requestUploadQueue = Volley.newRequestQueue(context, new MultiPartStack());
    }

    public void addRequestWithoutSessionCheck(Request<T> request) {
        resetRetrySetting(request);
        requestNormalQueue.add(request);
    }

    public void addRequest(Request<T> request) {
        resetRetrySetting(request);
        addRequestWithoutSessionCheck(request);
//        if (!User.getInstance().hasLogin()) {
//            addToWaitQueue(request);
//        } else {
//            addRequestWithoutSessionCheck(request);
//        }
    }

    public void resetRetrySetting(Request<T> request) {
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,//默认超时时间，应设置一个稍微大点儿的，例如本处的10000
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
    }

    public RequestQueue getRequestBackgroundQueue() {
        return requestBackgroundQueue;
    }

    public RequestQueue getRequestUploadQueue() {
        return requestUploadQueue;
    }

    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     *
     * @param headers Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                Setting.setNormalValue(SESSION_COOKIE, cookie);
            }
        }
    }

    public final void addCookie(DownloadManager.Request request) {
        request.addRequestHeader(COOKIE_KEY, createNewCookieValue(""));
        request.addRequestHeader("x-requested-with", "ohmrong-android");
    }

    /**
     * Adds session cookie to headers if exists.
     *
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {
        String currentCookieValue = "";
        if (headers.containsKey(COOKIE_KEY)) {
            currentCookieValue = headers.get(COOKIE_KEY);
        }
        headers.put(COOKIE_KEY, createNewCookieValue(currentCookieValue));
        headers.put("x-requested-with", "ohmrong-android");
    }

    public void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, createNewCookieValue(""));
        CookieSyncManager.getInstance().sync();
    }

    public String createNewCookieValue(String currentCookieValue) {
        StringBuilder builder = new StringBuilder();
        String sessionId = Setting.getNormalValue(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            builder.append(';');
        }
        if (!TextUtils.isEmpty(currentCookieValue)) {
            builder.append("; ");
            builder.append(currentCookieValue);
        }
        return builder.toString();
    }

//    public final GlideUrl getGlideUrl(String iconId) {
//        Map<String, String> headers = new HashMap<String, String>();
//        addSessionCookie(headers);
//        GlideUrl glideUrl = new GlideUrl(DownloadResource.getIconUrl(iconId),
//                new LazyHeaders.Builder().addHeader(COOKIE_KEY, headers.get(COOKIE_KEY)).build());
//        return glideUrl;
//    }

    public ArrayList<Request<T>> waitQueue = new ArrayList<Request<T>>();

    public void addToWaitQueue(Request<T> request) {
        synchronized (waitQueue) {
            waitQueue.add(request);
        }
    }

    public void executeWaitQueue() {
        synchronized (waitQueue) {
            for (Request<T> request : waitQueue) {
                addRequest(request);
            }
            waitQueue.clear();
        }
    }

    public void onLoginFail() {
        synchronized (waitQueue) {
            for (Request<T> request : waitQueue) {
                if (request instanceof StringRequest && ((StringRequest) request).isSessionError()) {
                    ((StringRequest) request).onLoginFail();
                } else {
                    addRequestWithoutSessionCheck(request);
                }
            }
            waitQueue.clear();
        }
    }
}

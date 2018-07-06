package com.example.doraemon.mymanytest.Rquest;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by csy on 2015/9/24.
 */
public class StringRequest extends com.android.volley.toolbox.StringRequest {

    private static final String SESSION_INVALID_ERROR_CODE_1 = "notLoginOrSessionOutOfTime";
    private static final String SESSION_INVALID_ERROR_CODE_2 = "session-expired";
    private static final String SESSION_INVALID_ERROR_CODE_3 = "device-changed";


    private boolean sessionError = false;
    private String sessionErrorResponse;


    /**
     * @param method
     * @param url
     * @param listener
     * @param errorListener
     */
    public StringRequest(int method, String url, Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    protected void deliverResponse(String response) {
        if (processSessionError(response)) {
            super.deliverResponse(response);
        }
    }

    public boolean isSessionError() {
        return sessionError;
    }

    public String getErrorCode(String response) {
        if (TextUtils.isEmpty(response) || response.indexOf("\"status\":0") != -1) {
            ResponseError responseError = new Gson().fromJson(response, ResponseError.class);
            return responseError.code;
        } else if (response.indexOf("errCode") != -1) {
            ResponseResultError resultError = new Gson().fromJson(response, ResponseResultError.class);
            return resultError.body.errCode;
        }
        return "";
    }

    protected boolean processSessionError(String response) {
        if (response.indexOf("errCode") != -1 && isNeedCheck()) {
            String errorCode = getErrorCode(response);
            if (!TextUtils.isEmpty(errorCode) && !sessionError) {
                if (errorCode.startsWith(SESSION_INVALID_ERROR_CODE_1)
                        || errorCode.startsWith(SESSION_INVALID_ERROR_CODE_2)) {
                    sessionError = true;
                    sessionErrorResponse = response;
                    RequestManager.getInstance().addToWaitQueue(this);
//                    synchronized (User.getInstance()) {
//                        if (User.getInstance().hasLogin()) {
//                            User.getInstance().setLoginState(false);
//                            EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_SESSION_INVALID, false));
//                        }
//                    }
                    return false;
                }
//                else if (errorCode.startsWith(SESSION_INVALID_ERROR_CODE_3)) {
//                    EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_SESSION_INVALID, true));
//                }
            }
        }
        return true;
    }


    public void onLoginFail() {
        if (isSessionError()) {
            deliverResponse(sessionErrorResponse);
        }
    }

    public final boolean isNeedCheck() {
        // return getUrl().indexOf(LoginRequest.URL) == -1 && getUrl().indexOf(GetHostRequest.URL) == -1;
        return true;
    }


    /* (non-Javadoc)
     * @see com.android.volley.toolbox.StringRequest#parseNetworkResponse(com.android.volley.NetworkResponse)
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // since we don't know which of the two underlying network vehicles
        // will Volley use, we have to handle and store session cookies manually
        RequestManager.getInstance().checkSessionCookie(response.headers);
        return super.parseNetworkResponse(response);
    }

    /* (non-Javadoc)
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if ((headers == null)
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        RequestManager.getInstance().addSessionCookie(headers);

        return headers;
    }
}
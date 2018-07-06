package com.example.doraemon.mymanytest.Common;

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.doraemon.mymanytest.R;
import com.example.doraemon.mymanytest.Rquest.RequestParams;


/**
 * Created by csy on 2015/9/10.
 */
public class Waiting {

    private boolean isWaiting;
    private Context context;
    private WindowManager mWindowManager;
    private View waitView;

    public Waiting(Context context) {
        this.context = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public final void showWaiting(boolean isBlur) {
        showWaiting(isBlur, "请稍后");
    }

    public final void showWaiting(boolean isBlur, String waitText) {
        if (isWaiting) {
            return;
        }
        isWaiting = true;
        try {
            WindowManager.LayoutParams lp = null;
            if (isBlur) {
                lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_BLUR_BEHIND | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, PixelFormat.TRANSLUCENT);
            } else {
                lp = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, PixelFormat.TRANSLUCENT);
            }

            if (waitView == null) {
//                LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                waitView = inflate.inflate(R.layout.watting_layout, null);
                waitView=View.inflate(context,R.layout.watting_layout,null);
            }

            if (!TextUtils.isEmpty(waitText)) {
                waitView.findViewById(R.id.toastText).setVisibility(View.VISIBLE);
                ((TextView) waitView.findViewById(R.id.toastText)).setText(waitText);
            }

            mWindowManager.addView(waitView, lp);
        } catch (Throwable e) {
            isWaiting = false;
            Log.e("Wait", e.getMessage());
        }
    }

    public final void hideWaiting() {
        isWaiting = false;
        try {
            if (waitView != null) {
                mWindowManager.removeView(waitView);
                waitView = null;
            }
        } catch (Throwable e) {
            Log.e("Waiting", "[showWaiting]", e);
        }
    }
}

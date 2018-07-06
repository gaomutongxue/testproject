package com.example.doraemon.mymanytest.Common;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by csy on 2015/9/23.
 */
public class ExitAppHelper {

    private static final int CONTROL_TIME = 2000;

    private long backKeyLastTime = 0;

    public boolean isNeedExitOnBackKey(Context context) {
        if (((Activity) context).getWindow().getAttributes()
                .softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            return false;
        }
        if (System.currentTimeMillis() - backKeyLastTime < CONTROL_TIME) {
            return true;
        } else {
            backKeyLastTime = System.currentTimeMillis();
            Toast.makeText(context, "再点一次后退键，将退出应用", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}

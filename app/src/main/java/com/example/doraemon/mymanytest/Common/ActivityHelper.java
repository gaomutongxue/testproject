package com.example.doraemon.mymanytest.Common;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;


/**
 * Created by csy on 2015/11/23.
 */
public class ActivityHelper {

    private static final long CHECK_TIME = 10 * 60 * 1000;

    // last onResume activity
    private Activity currentActivity;

    private long switchBackgroundTime;
    private boolean isFromBackGround = false;

    private ActivityHelper() {
    }

    public static ActivityHelper getInstance() {
        return ActivityHelperHolder.instance;
    }

    private static class ActivityHelperHolder {
        private static ActivityHelper instance = new ActivityHelper();
    }

    public void init() {
        isFromBackGround = false;
        currentActivity = null;
    }

    public boolean isAtBackground() {
        return isFromBackGround;
    }

    public void onActivityStart(Activity activity) {
//        if (isFromBackGround) {
//            ((NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
//            if (Utils.getVersionType() != Utils.VERSION_PERSONAL &&
//                    System.currentTimeMillis() - switchBackgroundTime > CHECK_TIME
//                    && LoginAction.isNeedLoginByGesture()) {
//
//                if (currentActivity instanceof UnlockGesturePasswordActivity
//                    || (currentActivity instanceof MainActivity
//                        && (((MainActivity)currentActivity).getCurrentFullScreenFragment() instanceof LoginNormalFragment
//                        || ((MainActivity)currentActivity).getCurrentFullScreenFragment() instanceof UnlockGesturePasswordFragment))) {
//                    // login or unlock needn't show UnlockActivity
//                } else {
//                    Intent intent = new Intent(activity, UnlockGesturePasswordActivity.class);
//                    activity.startActivity(intent);
//                    activity.overridePendingTransition(R.anim.activity_hold, R.anim.activity_hold);
//                }
//            }
//            isFromBackGround = false;
//        }
    }

    public void onActivityResume(Activity activity) {
        currentActivity = activity;
    }


    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (currentActivity == null) {
                        handler.sendEmptyMessageDelayed(1, 2000);
                    }
                    break;
                case 1:
                    if (currentActivity == null) {
                        switchBackgroundTime = System.currentTimeMillis() - 4000;
                        isFromBackGround = true;
                    }
                    break;
                default:
            }
        }
    };

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void onActivityPause(Activity activity) {
        if (currentActivity != null && currentActivity == activity) {
            currentActivity = null;
        }
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, 2000);
    }

}

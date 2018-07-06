package com.example.doraemon.mymanytest;

import android.app.Application;
import android.content.Context;


/**
 * Created by Administrator on 2016/2/25.
 */
public class DbuniApplication extends Application {

    public static Context baseContext;
    private static DbuniApplication mApplication;
    //    public Stack<Context> contextStack = new Stack<>();
    private String[] business = new String[]{"quickpayMobile", "p2pMobile"};
    //        private String envFlag = "3";//测试环境1 正式环境0 fbtest环境3
    private String envFlag = "0";//测试环境1 正式环境0 fbtest环境3
    private boolean isSumaLogin = false;
    private boolean isSumaResponse = false;

//    private PHP client = null;
//    private String source = null;

    @Override
    public void onCreate() {
        super.onCreate();
        baseContext = getBaseContext();
        mApplication = this;

        rigestSumapay();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void rigestSumapay() {
        isSumaResponse = false;

    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            while (!isSumaLogin && isSumaResponse) {
                rigestSumapay();
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

//    //监听网络状态改变的广播网络从没网络到有网络的时候要进行数据同步
//    public BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//            }
//        }
//    };
//    //监听home键的广播
//    public BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
//        String SYSTEM_REASON = "reason";
//        String SYSTEM_HOME_KEY = "homekey";
//        String SYSTEM_HOME_KEY_LONG = "recentapps";
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
//                String reason = intent.getStringExtra(SYSTEM_REASON);
//                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
//                    //表示按了home键,程序到了后台
//                }
//            }
//        }
//    };

    public synchronized static DbuniApplication getInstance() {
        return mApplication;
    }

//    public void registerBrocast() {
//        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
//                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
//        registerReceiver(connectionReceiver, new IntentFilter(
//                ConnectivityManager.CONNECTIVITY_ACTION));
//    }

//    public void unRegisterBrocast() {
//        if (mHomeKeyEventReceiver != null) {
//            unregisterReceiver(mHomeKeyEventReceiver);
//        }
//        if (connectionReceiver != null) {
//            unregisterReceiver(connectionReceiver);
//        }
//    }
}

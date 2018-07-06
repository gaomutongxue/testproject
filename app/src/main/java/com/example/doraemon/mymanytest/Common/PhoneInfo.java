package com.example.doraemon.mymanytest.Common;

import android.util.DisplayMetrics;

import com.example.doraemon.mymanytest.DbuniApplication;



public class PhoneInfo {
    private int screntHeight;
    private int screntWidth;
    private String imei;
    private String imsi;
    private int densityDpi;

    private static PhoneInfo instance;

    private static class PhoneInfoHolder {
        private static PhoneInfo instance = new PhoneInfo();
    }

    public static PhoneInfo getInstance() {
        return PhoneInfoHolder.instance;
    }

    private PhoneInfo() {
        DisplayMetrics dm = DbuniApplication.baseContext.getResources().getDisplayMetrics();
        screntHeight = dm.heightPixels;
        screntWidth = dm.widthPixels;
        densityDpi = dm.densityDpi;
    }

    private void initImsi() {

    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public int getScrentHeight() {
        return screntHeight;
    }

    public int getScrentWidth() {
        return screntWidth;
    }

    public String getImei() {
        return imei;
    }

    public String getImsi() {
        return imsi;
    }
}

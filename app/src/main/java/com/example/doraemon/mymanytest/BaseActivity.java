package com.example.doraemon.mymanytest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.doraemon.mymanytest.Common.FragmentManagerInterface;
import com.example.doraemon.mymanytest.Common.Waiting;
import com.example.doraemon.mymanytest.Ui.testFragment.FristFragment;
import com.example.doraemon.mymanytest.Ui.testFragment.SecondFragment;

public class BaseActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener,FragmentManagerInterface {
    private static RequestQueue sRequestQueue;
    public Waiting waiting;


    public static RequestQueue getRequestQueue(Context context) {
        if (sRequestQueue == null) {
            synchronized (RequestQueue.class) {
                if (sRequestQueue == null) {
                    sRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return sRequestQueue;
    }

    public void showWaiting(boolean isBlur) {
        waiting.showWaiting(isBlur);
    }
    public final void showWaiting(boolean isBlur, String waitText) {
        waiting.showWaiting(isBlur, waitText);
    }
    public void hideWaiting() {
        waiting.hideWaiting();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waiting=new Waiting(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public BaseFragment openNewWebViewFragment(String url, String title, boolean useAnimation) {
        return null;
    }

    @Override
    public BaseFragment openNewWebViewFragment(String url, String title, boolean useAnimation, String[] jsUrl) {
        return null;
    }

    @Override
    public void startFragment(Class<? extends BaseFragment> fragmentClass) {

    }

    @Override
    public void startFragment(Class<? extends BaseFragment> fragmentClass, boolean useAnimation) {

    }

    @Override
    public void startFragment(Class<? extends BaseFragment> fragmentClass, Object object, boolean useAnimation) {

    }

    @Override
    public void startFragment(BaseFragment fragment, Object object, boolean useAnimation) {

    }

    @Override
    public void startFragment(BaseFragment fragment, String tag, Object object, boolean useAnimation) {

    }

    @Override
    public void finishFragment(BaseFragment fragment, boolean useAnimation) {

    }

    @Override
    public void finishFragment(BaseFragment fragment, boolean useAnimation, String classTag) {

    }

    @Override
    public void finishFragment(BaseFragment fragment, String classTag) {

    }

    @Override
    public void finishFragmentByFlag(String classTag) {

    }
}
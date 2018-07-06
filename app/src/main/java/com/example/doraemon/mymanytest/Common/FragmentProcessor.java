package com.example.doraemon.mymanytest.Common;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.doraemon.mymanytest.BaseFragment;
import com.example.doraemon.mymanytest.R;

import java.util.Stack;


/**
 * Created by csy on 2015/10/21.
 */
public class FragmentProcessor implements FragmentManagerInterface {
    private FragmentManager fragmentManager;
    private BaseFragment currentFragment;
    private int fragmentContentId;
    private Stack<BaseFragment> fragmentStack;

    public FragmentProcessor(FragmentManager fragmentManager, int fragmentContentId) {
        this.fragmentManager = fragmentManager;
        this.fragmentContentId = fragmentContentId;
        fragmentStack = new Stack<BaseFragment>();
    }

    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

    public int getFragmentCount() {
        return fragmentStack.size();
    }

    private void showFragment(final BaseFragment fragment, final boolean useAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (useAnimation) {
            if (fragment != null) {
                transaction.setCustomAnimations(R.anim.activity_enter, R.anim.activity_hold);
            }
        } else {
            transaction.setCustomAnimations(R.anim.activity_hold, R.anim.activity_hold);
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(fragmentContentId, fragment);
        }
        transaction.commitAllowingStateLoss();
        fragmentStack.push(fragment);
        currentFragment = fragment;
    }

    private void showFragment(final BaseFragment fragment, String classTag, final boolean useAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (useAnimation) {
            if (fragment != null) {
                transaction.setCustomAnimations(R.anim.activity_enter, R.anim.activity_hold);
            }
        } else {
            transaction.setCustomAnimations(R.anim.activity_hold, R.anim.activity_hold);
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(fragmentContentId, fragment);
        }
        //遍历栈如果找到指定的fragment则移除
        for (BaseFragment fragment1 : fragmentStack) {
            if (!fragmentStack.empty() && fragment1 != null && fragment1.getClass().getSimpleName().equals(classTag)) {
                fragmentStack.remove(fragment1);
                break;
            }
        }
        transaction.commitAllowingStateLoss();
        fragmentStack.push(fragment);
        currentFragment = fragment;
    }


    @Override
    public BaseFragment openNewWebViewFragment(String url, String title, boolean useAnimation) {
//        Log.e("open webview", "url: " + url);
//        NormalWebViewFragment fragment = new NormalWebViewFragment();
//        if (!TextUtils.isEmpty(url)) {
//            fragment.setUrl(url);
//        }
//        fragment.setTitle(title);
//        startFragment(fragment, null, useAnimation);
        return null;
    }

    @Override
    public BaseFragment openNewWebViewFragment(String url, String title, boolean useAnimation, final String[] jsUrl) {
//        Log.e("open webview", "url: " + url);
//        NormalWebViewFragment fragment = new NormalWebViewFragment();
//        if (!TextUtils.isEmpty(url)) {
//            fragment.setUrl(url);
//        }
//        fragment.setTitle(title);
//        fragment.setUsePageTitle(TextUtils.isEmpty(title));
//        fragment.setJsFiles(jsUrl);
//        startFragment(fragment, null, useAnimation);
//        return fragment;
        return null;
    }

    @Override
    public void startFragment(Class<? extends BaseFragment> fragmentClass) {
        startFragment(fragmentClass, null, true);
    }

    @Override
    public void startFragment(Class<? extends BaseFragment> fragmentClass, boolean useAnimation) {
        startFragment(fragmentClass, null, useAnimation);
    }

    @Override
    public synchronized void startFragment(Class<? extends BaseFragment> fragmentClass, Object object, boolean useAnimation) {
        try {
            BaseFragment fragment = fragmentClass.newInstance();
            fragment.objectParam = object;
            showFragment(fragment, useAnimation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void startFragment(BaseFragment fragment, Object object, boolean useAnimation) {
        try {
            fragment.objectParam = object;
            showFragment(fragment, useAnimation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void startFragment(BaseFragment fragment, String classTag, Object object, boolean useAnimation) {
        try {
            fragment.objectParam = object;
            showFragment(fragment, classTag, useAnimation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void finishFragment(BaseFragment fragment, boolean useAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (useAnimation) {
            transaction.setCustomAnimations(R.anim.activity_hold, R.anim.activity_exit);
        }
        transaction.remove(fragment);
        if (fragmentStack.size() > 0) {
            fragmentStack.pop();
        }
        if (fragmentStack.size() > 0) {
            currentFragment = fragmentStack.peek();
            transaction.show(currentFragment);
        } else {
            currentFragment = null;
        }
        transaction.commitAllowingStateLoss();
    }

    //移除掉当前的fragment并在后台移除掉指定tag的fragment
    @Override
    public void finishFragment(BaseFragment fragment, boolean useAnimation, String classTag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (useAnimation) {
            transaction.setCustomAnimations(R.anim.activity_hold, R.anim.activity_exit);
        }
        transaction.remove(fragment);
        if (fragmentStack.size() > 0) {
            fragmentStack.pop();
        }
        //遍历栈如果找到指定的fragment则移除
        for (BaseFragment fragment1 : fragmentStack) {
            if (!fragmentStack.empty() && fragment1 != null && fragment1.getClass().getSimpleName().equals(classTag)) {
                fragmentStack.remove(fragment1);
                break;
            }
        }
        if (fragmentStack.size() > 0) {
            currentFragment = fragmentStack.peek();
            transaction.show(currentFragment);
        } else {
            currentFragment = null;
        }
        transaction.commitAllowingStateLoss();
    }

    //在后台移除掉指定tag的fragment,前台不表现出来
    @Override
    public void finishFragment(BaseFragment fragment, String classTag) {
        //遍历栈如果找到指定的fragment则移除
        for (BaseFragment fragment1 : fragmentStack) {
            if (!fragmentStack.empty() && fragment1 != null && fragment1.getClass().getSimpleName().equals(classTag)) {
                fragmentStack.remove(fragment1);
                break;
            }
        }
    }

    @Override
    public void finishFragmentByFlag(String classTag) {
        //遍历栈如果找到指定的fragment则移除
        for (BaseFragment fragment1 : fragmentStack) {
            if (!fragmentStack.empty() && fragment1 != null && fragment1.getClass().getSimpleName().equals(classTag)) {
                fragmentStack.remove(fragment1);
                break;
            }
        }
    }
}

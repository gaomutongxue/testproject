package com.example.doraemon.mymanytest.Common;

import com.example.doraemon.mymanytest.BaseFragment;



/**
 * Created by csy on 2015/10/21.
 */

/**
 * TODO 这个接口的好处
 */
public interface FragmentManagerInterface {

    public BaseFragment openNewWebViewFragment(String url, String title, boolean useAnimation);

    public BaseFragment openNewWebViewFragment(String url, String title, boolean useAnimation, final String[] jsUrl);

    public void startFragment(Class<? extends BaseFragment> fragmentClass);

    public void startFragment(Class<? extends BaseFragment> fragmentClass, boolean useAnimation);

    public void startFragment(Class<? extends BaseFragment> fragmentClass, Object object, boolean useAnimation);

    public void startFragment(BaseFragment fragment, Object object, boolean useAnimation);

    public void startFragment(BaseFragment fragment, String tag, Object object, boolean useAnimation);

    public void finishFragment(BaseFragment fragment, boolean useAnimation);

    public void finishFragment(BaseFragment fragment, boolean useAnimation, String classTag);//移除掉当前的fragment并在后台移除掉指定tag的fragment

    public void finishFragment(BaseFragment fragment, String classTag);//在后台移除掉指定tag的fragment

    public void finishFragmentByFlag(String classTag);//根据标签移除fragment
}

package com.example.doraemon.mymanytest;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.example.doraemon.mymanytest.Common.FragmentManagerInterface;
import com.example.doraemon.mymanytest.Ui.testFragment.FristFragment;
import com.example.doraemon.mymanytest.Ui.testFragment.SecondFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondActivity extends BaseActivity {



    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    ArrayList<BaseFragment> baseFragments;
    ArrayList<String> mTitleDataList;
    ArrayList<android.support.v4.app.Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        initView();
        registerListener();
    }

    private void registerListener() {

    }

    private void initView() {
        baseFragments=new ArrayList<BaseFragment>();
        CommonNavigator commonNavigator = new CommonNavigator(this);
        mTitleDataList=new ArrayList<String>();
        mTitleDataList.add("第一个a");
        mTitleDataList.add("第二个");
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleDataList == null ? 0 : mTitleDataList.size();
            }

            @Override
            public IMeasurablePagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setText(mTitleDataList.get(index));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
//                        changeContentFragment(viewPager.getAdapter().geti);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
            fragments=new ArrayList<android.support.v4.app.Fragment>();
        magicIndicator.setNavigator(commonNavigator);
        FristFragment fristFragment=new FristFragment();
        SecondFragment secondFragment=new SecondFragment();
        fragments.add(fristFragment);
        fragments.add(secondFragment);
        viewPager.setAdapter( new MyTransferViewpagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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


    private class MyTransferViewpagerAdapter extends FragmentPagerAdapter {
        private ArrayList<android.support.v4.app.Fragment> fragmentsList;


        public MyTransferViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyTransferViewpagerAdapter(FragmentManager fm, ArrayList<android.support.v4.app.Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return new String("aaa");
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }
}

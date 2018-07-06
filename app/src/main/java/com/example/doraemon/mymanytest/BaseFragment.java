package com.example.doraemon.mymanytest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doraemon.mymanytest.Common.FragmentManagerInterface;
import com.example.doraemon.mymanytest.eventa.MessageEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class BaseFragment extends Fragment {
    //
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Object objectParam;

    protected View content;

    private OnFragmentInteractionListener mListener;

    public void setOnBackKeyFinish(boolean onBackKeyFinish) {
        this.onBackKeyFinish = onBackKeyFinish;
    }

    private boolean onBackKeyFinish = true;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment BaseFragment.
     */

//    public static BaseFragment newInstance(String param1, String param2) {
//        BaseFragment fragment = new BaseFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    public void initToolbar() {
//        if (((AppCompatActivity) getActivity()) == null) return;
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            WindowManager.LayoutParams localLayoutParams = ((AppCompatActivity) getActivity()).getWindow().getAttributes();
////            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
////        }
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        }
//    }
    @Override
    public void onResume() {
        super.onResume();
        if (content != null) {
            content.setClickable(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public BaseFragment() {
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        resisterEventBus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                //  EventBus.getDefault().post(new MessageEvent(MessageEvent.FLASHDATA, null));
            }
        }, 350);    //TODO 为什么眼延迟a
    }

    protected void initData() {
    }

    protected void resisterEventBus() {
    //    EventBus.getDefault().register(this);
    }

    protected void unResisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        unResisterEventBus();
        super.onDetach();
        mListener = null;
    }

    public void onEvent(MessageEvent event) {
    }


    protected void onLoginSuccessEvent() {
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

    public void setTitle(String title) {
        if (content != null && content.findViewById(R.id.topBarCenter) != null) {
            View view = content.findViewById(R.id.topBarCenter);
            if (view != null && view instanceof TextView) {
                ((TextView) view).setText(title);
            }
        }
    }

    public void setLeftDrable(int id) {
        if (content != null && content.findViewById(R.id.topBarLeft) != null) {
            TextView view = (TextView) content.findViewById(R.id.topBarLeft);
            if (view != null && view instanceof TextView) {
                Drawable drawable = getResources().getDrawable(id);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                view.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    public void setRightDrable(int id) {
        if (content != null && content.findViewById(R.id.topBarRight) != null) {
            TextView view = (TextView) content.findViewById(R.id.topBarRight);
            if (view != null && view instanceof TextView) {
                Drawable drawable = getResources().getDrawable(id);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                view.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    public void setLeftTxt(String leftTxt) {
        if (content != null && content.findViewById(R.id.topBarLeft) != null) {
            TextView view = (TextView) content.findViewById(R.id.topBarLeft);
            if (view != null && view instanceof TextView) {
                view.setText(leftTxt);
            }
        }
    }

    public void setRightTxt(String rightText) {
        if (content != null && content.findViewById(R.id.topBarRight) != null) {
            TextView view = (TextView) content.findViewById(R.id.topBarRight);
            if (view != null && view instanceof TextView) {
                view.setText(rightText);
            }
        }
    }


    // This method will be called when a MessageEvent is posted
//    public void onEvent(MessageEvent event){
//    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && onBackKeyFinish) {
            finish();
            return false;
        }
        return true;
    }

    public View findViewById(int id) {
        if (content == null) {
            return null;
        }
        return content.findViewById(id);
    }

    public void showWaiting(boolean isBlur) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showWaiting(isBlur);
        } else if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showWaiting(isBlur);
        }
    }

    public void showWaiting(boolean isBlur, String waiting) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showWaiting(isBlur, waiting);
        } else if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showWaiting(isBlur, waiting);
        }
    }

    public void hideWaiting() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideWaiting();
        } else if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideWaiting();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_hold);
    }

    public void showBackIcon() {
        Drawable drawable = getResources().getDrawable(R.drawable.back_icon_click);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ((TextView) content.findViewById(R.id.topBarLeft)).setCompoundDrawables(drawable, null, null, null);
        content.findViewById(R.id.topBarLeft).setVisibility(View.VISIBLE);
        content.findViewById(R.id.topBarLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void startFragment(Class<? extends BaseFragment> fragmentClass) {
        if (getActivity() instanceof FragmentManagerInterface) {
            ((FragmentManagerInterface) getActivity()).startFragment(fragmentClass, null, true);
        }
    }


    public void startFragment(Class<? extends BaseFragment> fragmentClass, Object object) {
        if (getActivity() instanceof FragmentManagerInterface) {
            ((FragmentManagerInterface) getActivity()).startFragment(fragmentClass, object, true);
        }
    }

    public void startFragment(BaseFragment fragment) {
        if (getActivity() instanceof FragmentManagerInterface) {
            ((FragmentManagerInterface) getActivity()).startFragment(fragment, null, true);
        }
    }

    public void startFragment(BaseFragment fragment, String classTag) {
        if (getActivity() instanceof FragmentManagerInterface) {
            ((FragmentManagerInterface) getActivity()).startFragment(fragment, classTag, null, true);
        }
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
        return animation;
    }

    public void finish() {
        if (getActivity() instanceof FragmentManagerInterface) {
            ((FragmentManagerInterface) getActivity()).finishFragment(this, true);
        }
    }

    //移除掉当前的fragment并在后台移除掉指定tag的fragment
    public void finish(String tag) {
        if (getActivity() instanceof FragmentManagerInterface) {
            ((FragmentManagerInterface) getActivity()).finishFragment(this, true, tag);
        }
    }

    //在后台移除掉指定tag的fragment
    public void finishBackGroundFragment(String tag) {
        if (getActivity() instanceof FragmentManagerInterface) {
            ((FragmentManagerInterface) getActivity()).finishFragment(this, tag);
        }
    }

    public void finish(boolean useAnimation) {
        if (getActivity() instanceof FragmentManagerInterface) {
            ((FragmentManagerInterface) getActivity()).finishFragment(this, useAnimation);
        }
    }

    public void finishDelayed(boolean useAnimation) {
        finishDelayed(useAnimation, 300);
    }

    public void finishDelayed(boolean useAnimation, long delayTime) {
        delayHandler.sendMessageDelayed(delayHandler.obtainMessage(0, useAnimation), delayTime);
    }

    Handler delayHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            finish((Boolean) msg.obj);
        }
    };

    public void showToast(String toast) {
        try {
            if (toast == null) toast = "";
            Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    public void openWebViewFragment(final String url, final String title) {
        if (!TextUtils.isEmpty(url)) {
//            Utils.executeAction(getActivity(), new OhmAction(url) {
//                @Override
//                public void onExecute() {
//                    if (getActivity() instanceof FragmentManagerInterface) {
//                        ((FragmentManagerInterface) getActivity()).openNewWebViewFragment(WebViewUtil.getAccessableUrl(url), title, true);
//                    }
//                }
//            });
        }
    }

    public void openWebViewFragment(final String url, final String title, final String[] jsFileUrl) {
        if (!TextUtils.isEmpty(url)) {
//            Utils.executeAction(getActivity(), new OhmAction(url) {
//                @Override
//                public void onExecute() {
//                    if (getActivity() instanceof FragmentManagerInterface) {
//                        ((FragmentManagerInterface) getActivity()).openNewWebViewFragment(WebViewUtil.getAccessableUrl(url), title, true, jsFileUrl);
//                    }
//                }
//            });
        }
    }
}

package com.example.doraemon.mymanytest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.doraemon.mymanytest.Common.Waiting;
import com.example.doraemon.mymanytest.Event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.hello)
    TextView textView;
    @BindView(R.id.ceshi)
    Button ceshi;
    View waitView;
    WindowManager windowManager;
    @BindView(R.id.bt_second)
    Button btSecond;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    Waiting waiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        textView.setText("成功了吗");
        EventBus.getDefault().register(this);
        JSONObject jsonObject = new JSONObject();
        // waitView = View.inflate(getApplicationContext(),R.layout.watting_layout, null);
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);


    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void Event(MessageEvent messageEvent) {
        messageEvent.putMessage();
        String sql = "select";
        Log.d("我是来自frist", sql);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, "https://www.sojson.com/open/api/weather/json.shtml?city=北京", new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                try {
                    Log.d("=================", response.getString("date"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("=================", "出问题了");
            }
        });
//        stringRequest=new StringRequest();

        BaseActivity.getRequestQueue(getApplicationContext()).add(stringRequest);

        // BaseActivity.getRequestQueue(getApplicationContext()).start();
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("发动了", "很强");
        return super.dispatchTouchEvent(ev);
    }

    @OnClick(R.id.ceshi)
    public void showToast() {
        BlankFragment blankFragment = new BlankFragment();
        Log.d("aaaa", blankFragment.getClass().getSimpleName());
        Context context;
        tvPassword.setVisibility(View.GONE);
//        WindowManager.LayoutParams layoutParams =new  WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, PixelFormat.TRANSLUCENT);
//        LayoutInflater inflate = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        waitView = inflate.inflate(R.layout.watting_layout, nu
       waiting = new Waiting(this);
        waiting.showWaiting(true);
        Log.d("aaa", "aaaa");

        /*

         */
        MessageEvent messageEvent;
        messageEvent = new MessageEvent();
        Toast.makeText(this, "aaaaaa", Toast.LENGTH_LONG).show();
        messageEvent.message = "OKOKOK";

        EventBus.getDefault().post(messageEvent);
        Intent intent = new Intent(this, SecondActivity.class);
          startActivity(intent);
    }
    public void showWaiting(boolean isBlur) {
        waiting.showWaiting(isBlur);
    }

    public void hideWaiting() {
        waiting.hideWaiting();
    }
    public final void showWaiting(boolean isBlur, String waitText) {
        waiting.showWaiting(isBlur, waitText);
    }

}

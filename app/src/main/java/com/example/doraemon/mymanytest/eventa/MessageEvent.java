package com.example.doraemon.mymanytest.eventa;

/**
 * Created by Administrator on 2016/2/25.
 */
public class MessageEvent {
    public static final int Event_Login_Success = 2;
    public static final int Event_Vieewpager_tab_selector = 3;
    public static final int Event_buy_Success = 4;
    public static final int Event_goto_tab = 5;
    public static final int Event_goto_home = 6;
    public static final int Event_pay_success = 7;
    public static final int Event_nameAuth_success = 8;
    public static final int Event_phoneAuth_success = 9;

    public final int EventType;
    public final Object obj;

    public MessageEvent(int eventType, Object obj) {
        this.EventType = eventType;
        this.obj = obj;
    }
}

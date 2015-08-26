package com.wf.phonestatelistener.phone;

import com.wf.phonestatelistener.activity.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import static com.wf.phonestatelistener.activity.MainActivity.handler;
import static com.wf.phonestatelistener.contacts.ContactUtil.getUsername;

// 方法二：全局广播，已注释掉，可正常使用
public class PhoneBroadcastReceiver extends BroadcastReceiver {

	private static String TAG = "PhoneBroadcastReceiver";

	public final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i(TAG, "[Broadcast]" + action);
		doReceivePhone(context, intent);
	}

	/**
	 * 处理电话广播
	 * @param context
	 * @param intent
	 */
	public void doReceivePhone(Context context, Intent intent) {
		String phoneNumber = intent
				.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int state = telephony.getCallState();
		if(handler == null){
			Log.d(TAG, "DebugText null");
			return;
		}
		Message msg = new Message();
		msg.what = 777;
		msg.obj = "";
		switch (state) {
		case TelephonyManager.CALL_STATE_OFFHOOK:
			Log.i(TAG, "[Broadcast]通话中=" + phoneNumber);
			//msg.obj = "通话中：" + phoneNumber;
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			Log.i(TAG, "[Broadcast]挂断=" + phoneNumber);
			//msg.obj = "挂断：" + phoneNumber;
			MainActivity.sendMail(context, "挂断", "挂断");
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			Log.i(TAG, "[Broadcast]来电=" + phoneNumber);
			String subject = "来自" + getUsername(phoneNumber.toString()) + "(" + phoneNumber + ")的电话";
			String content = phoneNumber + "来电";
			msg.obj = subject + "\n" + content;
			//MainActivity.sendMail(context, subject, content);
			break;
		}
		handler.sendMessage(msg);
	}
}

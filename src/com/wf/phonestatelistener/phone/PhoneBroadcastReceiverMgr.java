package com.wf.phonestatelistener.phone;

import com.wf.phonestatelistener.activity.MainActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import static com.wf.phonestatelistener.activity.MainActivity.handler;
import static com.wf.phonestatelistener.contacts.ContactUtil.getUsername;

public class PhoneBroadcastReceiverMgr extends BroadcastReceiver {

	private static String TAG = "BroadcastReceiverMgr";

	private static PhoneBroadcastReceiverMgr mBroadcastReceiver;

	public final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;
	
	//注册广播
	public static void registerPhoneListener(Activity activity) {
		Log.i(TAG, "registerIt");
		mBroadcastReceiver = new PhoneBroadcastReceiverMgr();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(B_PHONE_STATE);
		intentFilter.setPriority(Integer.MAX_VALUE);
		activity.registerReceiver(mBroadcastReceiver, intentFilter);
	}
	
	//撤销广播
	public void unregisterIt(Activity activity) {
		Log.i(TAG, "unregisterIt");
		activity.unregisterReceiver(mBroadcastReceiver);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i(TAG, "[Broadcast]" + action);

		// 呼入电话
		if (action.equals(B_PHONE_STATE)) {
			Log.i(TAG, "[Broadcast]PHONE_STATE");
			doReceivePhone(context, intent);
		}
	}

	/**
	 * 处理电话广播.
	 * 
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

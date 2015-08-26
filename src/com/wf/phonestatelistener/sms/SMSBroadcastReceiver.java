package com.wf.phonestatelistener.sms;

import com.wf.phonestatelistener.activity.MainActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import static com.wf.phonestatelistener.activity.MainActivity.handler;
import static com.wf.phonestatelistener.contacts.ContactUtil.getUsername;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			StringBuffer SMSAddress = new StringBuffer();
			StringBuffer SMSContent = new StringBuffer();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdusObjects = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdusObjects.length];
				for (int i = 0; i < pdusObjects.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdusObjects[i]);
				}
				for (SmsMessage message : messages) {
					SMSAddress.append(message.getDisplayOriginatingAddress());
					SMSContent.append(message.getDisplayMessageBody());Message msg = new Message();
					msg.what = 777;
					String subject = "来自" + getUsername(SMSAddress.toString()) + "(" + SMSAddress + ")的信息";
					String content = "信息内容："+ SMSContent;
					msg.obj = subject + "\n" + content;
					MainActivity.sendMail(context, subject, content);
					if(handler != null){
						handler.sendMessage(msg);
					}
				}
			}
		}
	}

}

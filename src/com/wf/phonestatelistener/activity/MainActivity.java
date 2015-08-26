package com.wf.phonestatelistener.activity;

import static com.wf.phonestatelistener.phone.PhoneBroadcastReceiverMgr.registerPhoneListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wf.phonestatelistener.R;
import com.wf.phonestatelistener.contacts.ContactUtil;
import com.wf.phonestatelistener.mail.MailSender;
import com.wf.phonestatelistener.mail.MailSenderInfo;
import static com.wf.phonestatelistener.mail.MailSender.getMailSenderInfo;

public class MainActivity extends Activity {
	
	public final static String TAG = "MainActivity";
	
	TextView DebugText;
	Button SettingButton;
	Button ExitButton;
	
	public static Handler handler;
	Activity thisActivity = this;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initHandler();
		ContactUtil.getPhoneContacts(thisActivity);
		// 方法一：activity注册广播
		// 也可使用全局广播方法，注释掉这行，且取消注释manifest文件中全局广播即可
		registerPhoneListener(this);
	}

	private void initUI() {
		setContentView(R.layout.layout_main);
		SettingButton = (Button) findViewById(R.id.SettingButton);
		SettingButton.setOnClickListener(MClickListener);
		ExitButton = (Button) findViewById(R.id.ExitButton);
		ExitButton.setOnClickListener(MClickListener);
		ExitButton.setBackgroundColor(Color.rgb(0, 0, 50));
	}	
	
	private void initHandler() {
		handler = new Handler() {
			public void handleMessage(Message msg){
				int what = msg.what;
				if(what != 777) return;				// 自定义该程序what值为777
				String str = (String)msg.obj;
				DebugText.setText(str);
			}
		};
	}

	public static void sendMail(final Context context, final String subject, final String content){
		
		Thread thread = new Thread(){
			public void run(){
				Message msg = new Message();
				msg.what = 777;
				MailSenderInfo mailInfo = getMailSenderInfo(context);
				mailInfo.setSubject(subject);
				mailInfo.setContent(content);
				MailSender sms = new MailSender();
				try {
					sms.sendTextMail(mailInfo);
					msg.obj = subject + "\n" + content + "\n成功向" + mailInfo.getToAddress() + "投递通知";
					Log.d(TAG, (String)msg.obj);
				} catch (Exception e) {
					msg.obj = subject + "\n" + content + "\n向" + mailInfo.getToAddress() + "投递失败：\n" + e.getMessage();
					Log.d(TAG, (String)msg.obj);
				}finally{
					if(handler != null){
						handler.sendMessage(msg);
					}
				}
			}
		};
		thread.start();
	}

	OnClickListener MClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.SettingButton:
				Intent intent = new Intent(thisActivity, SettingActivity.class);
				thisActivity.startActivity(intent);
				break;
			case R.id.ExitButton:
				//finish();
				Toast.makeText(thisActivity, "完全退出", Toast.LENGTH_SHORT).show();
				System.exit(0);
				break;
			}
			
		}
		
	};
	
	//override back press
	public void onBackPressed() {
		Toast.makeText(thisActivity, "进入后台运行", Toast.LENGTH_SHORT).show();
		moveTaskToBack(false);
	}
	
	
}
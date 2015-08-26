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
		// ����һ��activityע��㲥
		// Ҳ��ʹ��ȫ�ֹ㲥������ע�͵����У���ȡ��ע��manifest�ļ���ȫ�ֹ㲥����
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
				if(what != 777) return;				// �Զ���ó���whatֵΪ777
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
					msg.obj = subject + "\n" + content + "\n�ɹ���" + mailInfo.getToAddress() + "Ͷ��֪ͨ";
					Log.d(TAG, (String)msg.obj);
				} catch (Exception e) {
					msg.obj = subject + "\n" + content + "\n��" + mailInfo.getToAddress() + "Ͷ��ʧ�ܣ�\n" + e.getMessage();
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
				Toast.makeText(thisActivity, "��ȫ�˳�", Toast.LENGTH_SHORT).show();
				System.exit(0);
				break;
			}
			
		}
		
	};
	
	//override back press
	public void onBackPressed() {
		Toast.makeText(thisActivity, "�����̨����", Toast.LENGTH_SHORT).show();
		moveTaskToBack(false);
	}
	
	
}
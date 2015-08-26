package com.wf.phonestatelistener.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wf.phonestatelistener.R;
import com.wf.phonestatelistener.mail.MailSender;
import com.wf.phonestatelistener.mail.MailSenderInfo;
import static com.wf.phonestatelistener.mail.MailSender.saveMailSenderInfo;
import static com.wf.phonestatelistener.mail.MailSender.getMailSenderInfo;

public class SettingActivity extends Activity{

	private Button save; 
	private EditText userid;
	private EditText password;
	private EditText serverhost;
	private EditText serverport;
	private EditText from; 
	private EditText to; 
	private EditText subject; 
	private EditText body;
	
	Activity thisActivity = this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        initUI();
        fillForm();
    }

	private void initUI() {
        setContentView(R.layout.layout_setting);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(SClickListener);
        userid = (EditText) findViewById(R.id.userid); 
        password = (EditText) findViewById(R.id.password);
        serverhost = (EditText) findViewById(R.id.serverhost);
        serverport = (EditText) findViewById(R.id.serverport);
        from = (EditText) findViewById(R.id.from); 
        to = (EditText) findViewById(R.id.to); 
        subject = (EditText) findViewById(R.id.subject); 
        body = (EditText) findViewById(R.id.body);
        
        userid.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
        	@Override
        	public void onFocusChange(View v, boolean hasFocus) {
        		if(hasFocus) {	// �õ�����
        			
        		} else {		// ʧȥ����
        			String fromAddress = userid.getText().toString();
        			if(fromAddress.length() > 0 && fromAddress.contains("@")){
        				String[] str = fromAddress.split("@");
        				serverhost.setText("smtp." + str[1]);
        				serverport.setText("25");
        			}
        		}
        	}
        });
	}
	
	private void fillForm() {
		MailSenderInfo mailInfo = getMailSenderInfo(thisActivity);
		fillForm(mailInfo);
	}
	
	private void fillForm(MailSenderInfo mailInfo) {
		userid.setText(mailInfo.getUserName());				// �����û���
        password.setText(mailInfo.getPassword());			// �����½����
        serverhost.setText(mailInfo.getMailServerHost());
        serverport.setText(mailInfo.getMailServerPort());
        from.setText(mailInfo.getFromAddress());			// ��������
        from.setVisibility(View.GONE);						// ����ʾfrom
        to.setText(mailInfo.getToAddress());				// �ռ��ʼ�
        subject.setText(mailInfo.getSubject());
        body.setText(mailInfo.getContent());
	}
	
	OnClickListener SClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			MailSenderInfo mailInfo = setMailSenderInfo();
			switch(v.getId()){
			case R.id.save:
				saveMailSenderInfo(thisActivity, mailInfo);
				Toast.makeText(thisActivity, "����ɹ�", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	private MailSenderInfo setMailSenderInfo() {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(serverhost.getText().toString());
		mailInfo.setMailServerPort(serverport.getText().toString());
		mailInfo.setValidate(true);
		mailInfo.setUserName(userid.getText().toString());		//�����ַ
		mailInfo.setPassword(password.getText().toString());	//��������
		mailInfo.setFromAddress(from.getText().toString());
		mailInfo.setToAddress(to.getText().toString());
		mailInfo.setSubject(subject.getText().toString());
		mailInfo.setContent(body.getText().toString());
		return mailInfo;
	}

	public static boolean sendMail(MailSenderInfo mailInfo) {
		try{
			//�����ʼ�
			MailSender sms = new MailSender();
			sms.sendTextMail(mailInfo);							//���������ʽ
			//sms.sendHtmlMail(mailInfo);						//����html��ʽ
			return true;
		}
		catch (Exception e) {
			Log.e("SendMail", e.getMessage(), e);
			return false;
		}
	}
	
}

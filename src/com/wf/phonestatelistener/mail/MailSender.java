package com.wf.phonestatelistener.mail;
import java.util.Date;    
import java.util.Properties;   
import javax.mail.Address;    
import javax.mail.BodyPart;    
import javax.mail.Message;    
import javax.mail.MessagingException;    
import javax.mail.Multipart;    
import javax.mail.Session;    
import javax.mail.Transport;    
import javax.mail.internet.InternetAddress;    
import javax.mail.internet.MimeBodyPart;    
import javax.mail.internet.MimeMessage;    
import javax.mail.internet.MimeMultipart;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**   
 * ���ʼ��������������ʼ���������   
 */    
public class MailSender  
{    
	
	public static void saveMailSenderInfo(Context context, MailSenderInfo mailInfo){
		SharedPreferences sharedPreferences = context.getSharedPreferences("wf.psl", Activity.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("username", mailInfo.getUserName());
		editor.putString("password", mailInfo.getPassword());
		editor.putString("fromAddress", mailInfo.getUserName());
		editor.putString("toAddress", mailInfo.getToAddress());
		editor.putString("serverhost", mailInfo.getMailServerHost());
		editor.putString("serverport", mailInfo.getMailServerPort());
		editor.putBoolean("validate", mailInfo.isValidate());
		editor.commit();
	}
	
	public static MailSenderInfo getMailSenderInfo(Context context){
		MailSenderInfo mailInfo = new MailSenderInfo();
		SharedPreferences sharedPreferences = context.getSharedPreferences("wf.psl", Activity.MODE_PRIVATE);
		mailInfo.setUserName(sharedPreferences.getString("username", "default"));
		mailInfo.setPassword(sharedPreferences.getString("password", "default"));
		mailInfo.setPassword(sharedPreferences.getString("password", "default"));
		mailInfo.setFromAddress(sharedPreferences.getString("fromAddress", "default"));
		mailInfo.setToAddress(sharedPreferences.getString("toAddress", "default"));
		mailInfo.setMailServerHost(sharedPreferences.getString("serverhost", "default"));
		mailInfo.setMailServerPort(sharedPreferences.getString("serverport", "default"));
		mailInfo.setValidate(sharedPreferences.getBoolean("validate", false));
		return mailInfo;
	}
	
	/**   
	 * ���ı���ʽ�����ʼ�   
	 * @param mailInfo �����͵��ʼ�����Ϣ   
	 */    
	public boolean sendTextMail(MailSenderInfo mailInfo) 
	{
		// �ж��Ƿ���Ҫ�����֤    
		MailAuthenticator authenticator = null;    
		Properties pro = mailInfo.getProperties();   
		if (mailInfo.isValidate()) 
		{    
			// �����Ҫ�����֤���򴴽�һ��������֤��    
			authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
		}   
		// �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session    
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);    
		try 
		{    
			// ����session����һ���ʼ���Ϣ    
			Message mailMessage = new MimeMessage(sendMailSession);    
			// �����ʼ������ߵ�ַ    
			Address from = new InternetAddress(mailInfo.getFromAddress());    
			// �����ʼ���Ϣ�ķ�����    
			mailMessage.setFrom(from);    
			// �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��    
			Address to = new InternetAddress(mailInfo.getToAddress());    
			mailMessage.setRecipient(Message.RecipientType.TO,to);    
			// �����ʼ���Ϣ������    
			mailMessage.setSubject(mailInfo.getSubject());    
			// �����ʼ���Ϣ���͵�ʱ��    
			mailMessage.setSentDate(new Date());    
			// �����ʼ���Ϣ����Ҫ����    
			String mailContent = mailInfo.getContent();    
			mailMessage.setText(mailContent);    
			// �����ʼ�    
			Transport.send(mailMessage);   
			return true;    
		} 
		catch (MessagingException ex) 
		{    
			ex.printStackTrace();    
		}    
		return false;    
	}    

	/**   
	 * ��HTML��ʽ�����ʼ�   
	 * @param mailInfo �����͵��ʼ���Ϣ   
	 */    
	public static boolean sendHtmlMail(MailSenderInfo mailInfo)
	{    
		// �ж��Ƿ���Ҫ�����֤    
		MailAuthenticator authenticator = null;   
		Properties pro = mailInfo.getProperties();   
		//�����Ҫ�����֤���򴴽�һ��������֤��     
		if (mailInfo.isValidate()) 
		{    
			authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());   
		}    
		// �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session    
		Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
		try {    
			// ����session����һ���ʼ���Ϣ    
			Message mailMessage = new MimeMessage(sendMailSession);    
			// �����ʼ������ߵ�ַ    
			Address from = new InternetAddress(mailInfo.getFromAddress());    
			// �����ʼ���Ϣ�ķ�����    
			mailMessage.setFrom(from);    
			// �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��    
			Address to = new InternetAddress(mailInfo.getToAddress());    
			// Message.RecipientType.TO���Ա�ʾ�����ߵ�����ΪTO    
			mailMessage.setRecipient(Message.RecipientType.TO,to);    
			// �����ʼ���Ϣ������    
			mailMessage.setSubject(mailInfo.getSubject());    
			// �����ʼ���Ϣ���͵�ʱ��    
			mailMessage.setSentDate(new Date());    
			// MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ���    
			Multipart mainPart = new MimeMultipart();    
			// ����һ������HTML���ݵ�MimeBodyPart    
			BodyPart html = new MimeBodyPart();    
			// ����HTML����    
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");    
			mainPart.addBodyPart(html);    
			// ��MiniMultipart��������Ϊ�ʼ�����    
			mailMessage.setContent(mainPart);    
			// �����ʼ�    
			Transport.send(mailMessage);    
			return true;    
		} catch (MessagingException ex) {    
			ex.printStackTrace();    
		}    
		return false;    
	}    

}

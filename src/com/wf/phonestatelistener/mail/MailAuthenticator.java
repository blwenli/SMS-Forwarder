package com.wf.phonestatelistener.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/*
 * http://www.3lian.com/edu/2013/07-31/85448.html
 */
public class MailAuthenticator extends Authenticator 
{
	    String userName = null;
	    
		String password = null; 
	     
	    public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
  
	    public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public MailAuthenticator()
	    {
	    	
	    }
	    
	    public MailAuthenticator(String username, String password) 
	    {
	        this.userName = username;    
	        this.password = password;    
	    }    
	    
	    protected PasswordAuthentication getPasswordAuthentication()
	    {   
	        return new PasswordAuthentication(userName, password);   
	    }  
}

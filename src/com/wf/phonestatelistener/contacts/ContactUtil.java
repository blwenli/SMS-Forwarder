package com.wf.phonestatelistener.contacts;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactUtil {

	public static ArrayList<Contact> ContactList = new ArrayList<Contact>();
	
	public static ArrayList<Contact> getPhoneContacts(Context ctx){
		ContactList.clear();
		Cursor c = ctx.getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,null);
		while(c.moveToNext()){
			Contact contact = new Contact();
			contact.name = c.getString(c.getColumnIndex(Phone.DISPLAY_NAME)); 
			contact.number = c.getString(c.getColumnIndex(Phone.NUMBER));
			ContactList.add(contact);
		}
		c.close();
		return ContactList;
	}
	
	public static String getUsername(String phonenumber){
		String name = "";
		for(Contact contact: ContactList){
			if(contact.number.contains(phonenumber)){
				name = contact.name;
				break;
			}
		}
		return name;
	}
}

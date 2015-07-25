package com.warren.contact.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.warren.contact.domain.Contact;
import com.warren.contact.domain.FullContact;
import com.warren.contact.domain.User;
import com.warren.contact.utils.Constants;
import com.warren.contact.utils.JsonUtil;
import com.warren.contact.utils.StreamTool;
import com.warren.contact.utils.StringUtils;

public class ContactService {

	/**
	 * 
	 * @param fullContact
	 * @return
	 * @throws Exception
	 */
	public static boolean uploadContacts(FullContact fullContact)
			throws Exception {

		HttpURLConnection conn = (HttpURLConnection) new URL(
				Constants.UPDATE_CONTACT_PATH).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");
		List<Contact> contacts = fullContact.getContact();
		StringBuilder json = new StringBuilder();
		json.append("{owner:\"").append(fullContact.getContactOwner())
				.append("\",");
		json.append("contacts:");
		json.append('[');
		for (Contact contact : contacts) {
			json.append('{');
			json.append("name:\"").append(contact.getName()).append("\",");
			json.append("phone:\"").append(contact.getPhoneNumber())
					.append("\",");

			json.append("},");
		}
		json.deleteCharAt(json.length() - 1);
		json.append(']');
		json.append("}");
		conn.getOutputStream().write(json.toString().getBytes());
		if (conn.getResponseCode() == 200) {

			return true;
		} else {
			return false;
		}
	}
	
	public static boolean uploadContacts(User user)
			throws Exception {

		HttpURLConnection conn = (HttpURLConnection) new URL(
				Constants.UPDATE_CONTACT_PATH).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");
		List<Contact> contacts = user.getContacts();
		StringBuilder json = new StringBuilder();
		json.append("{");
		if(!StringUtils.isEmpty(user.getPhone())) {
			json.append(JsonUtil.PHONE_NODE).append(":\"").append(user.getPhone()).append("\",");
		} else {
			json.append(JsonUtil.PHONE_NODE).append(":\"").append("").append("\",");
		}
		if(!StringUtils.isEmpty(user.getUid())) {
			json.append(JsonUtil.UID_NODE).append(":\"").append(user.getUid()).append("\",");
		}else {
			json.append(JsonUtil.UID_NODE).append(":\"").append("").append("\",");
		}
		if(!StringUtils.isEmpty(user.getDeviceId())) {
			json.append(JsonUtil.DEVICE_NODE).append(":\"").append(user.getDeviceId()).append("\",");
		} else {
			json.append(JsonUtil.DEVICE_NODE).append(":\"").append("").append("\",");
		}

		json.append("contacts:");
		json.append('[');
		for (Contact contact : contacts) {
			json.append('{');
			json.append("name:\"").append(contact.getName()).append("\",");
			json.append("phone:\"").append(contact.getPhoneNumber())
					.append("\",");

			json.append("},");
		}
		json.deleteCharAt(json.length() - 1);
		json.append(']');
		json.append("}");
		conn.getOutputStream().write(json.toString().getBytes());
		if (conn.getResponseCode() == 200) {

			return true;
		} else {
			return false;
		}
	}

	public static String CheckUpateContact(String owner)
			throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(
				Constants.CHECK_UPDATE_CONTACT_URL).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");

		StringBuilder json = new StringBuilder();
		json.append("{owner:\"").append(owner).append("\"}");
		conn.getOutputStream().write(json.toString().getBytes());
		if (conn.getResponseCode() == 200) {
			try {
				InputStream jsonStream = conn.getInputStream();
				byte[] data;

				data = StreamTool.read(jsonStream);

				String repJson = new String(data, "UTF-8");
				JSONObject jsonObj = new JSONObject(repJson);
				String contactsSize = jsonObj.getString("contactsSize");
				return contactsSize;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		else {
			return null;
		}
	}

	/**
	 * json格式为: {owner:111111}
	 * @param owner
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<Contact> getAllContacts(String owner) throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(
				Constants.GET_ALL_CONTACTS_BY_OWNER_URL).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");

		StringBuilder json = new StringBuilder();
		json.append("{owner:\"").append(owner).append("\"}");
		conn.getOutputStream().write(json.toString().getBytes());
		
		List<Contact> allContacts = new ArrayList<Contact>();
		if (conn.getResponseCode() == 200) {
			

			try {
				InputStream jsonStream = conn.getInputStream();
				byte[] data;

				data = StreamTool.read(jsonStream);

				String repJson = new String(data, "UTF-8");
				JSONObject jsonObj = new JSONObject(repJson);
				 JSONArray contactArray = jsonObj.getJSONArray("contacts");
				 for(int i=0;i<contactArray.length();i++) {
						JSONObject jsonObject = contactArray.getJSONObject(i);
				        String name = jsonObject.getString("name");
				        String phone = jsonObject.getString("phone");
				        Contact contact = new Contact();
				        contact.setName(name);
				        contact.setPhoneNumber(phone);
				        allContacts.add(contact);
				 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	return allContacts;

		
	}
	
	/**
	 * Json返回数据格式形如：
	 * {
	 * 18081978345:
	 * [
	 * {13800456781:[{phoneNo="15678091231",name="小张"},{phoneNo="13890907865",name="小李"}]},
	 * {13908976543:[{phoneNo="15678091231",name="小张"},{phoneNo="13890907865",name="小李"}]}
	 * ]
	 * }
	 * @param phone
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Map<String,List<Contact>> findSameContacts(String phone,String uid) throws MalformedURLException, IOException {
		Map<String,List<Contact>> sameContacts = new HashMap<String,List<Contact>>();
		HttpURLConnection conn = (HttpURLConnection) new URL(
				Constants.FIND_SAME_CONTACT_URL).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");

		StringBuilder json = new StringBuilder();
		json.append("{");
		if(!StringUtils.isEmpty(phone)) {
			json.append(JsonUtil.PHONE_NODE).append(":\"").append(phone).append("\"}");
		}else if(!StringUtils.isEmpty(uid)) {
			json.append(JsonUtil.UID_NODE).append(":\"").append(uid).append("\"}");
		}
		conn.getOutputStream().write(json.toString().getBytes());
		if (conn.getResponseCode() == 200) {

			try {
			
				InputStream jsonStream = conn.getInputStream();
				byte[] data;

				data = StreamTool.read(jsonStream);

				String repJson = new String(data, "UTF-8");
				JSONObject jsonObj = new JSONObject(repJson);
				 JSONArray topArray=null;
				if(!StringUtils.isEmpty(phone)) {
					topArray= jsonObj.getJSONArray(phone);
				} else if(!StringUtils.isEmpty(uid)) {
					topArray= jsonObj.getJSONArray(uid);
				} else {
					Log.e("contact", "phone或uid不能同時為空");
				}
			
				 for(int i=0;i<topArray.length();i++) {
					     List<Contact> contactList = new ArrayList<Contact>();
						JSONObject topContactObject = topArray.getJSONObject(i);
						Iterator<String> keySet = topContactObject.keys();
						while(keySet.hasNext()) {
							String key= keySet.next();
							JSONArray secondArray= topContactObject.getJSONArray(key);
							for(int j=0;j<secondArray.length();j++) {
								JSONObject seondContactObj = secondArray.getJSONObject(j);
								Contact contact = new Contact();
								contact.setName(seondContactObj.getString(JsonUtil.USER_NAME_NODE));
								contact.setPhoneNumber(seondContactObj.getString(JsonUtil.PHONE_NODE));
								contactList.add(contact);
							}
						
							sameContacts.put(key, contactList);					
						}
						
				        
				 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	return sameContacts;

		
	}
}

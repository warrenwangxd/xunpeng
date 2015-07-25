package com.warren.contact.domain;

import java.util.List;

import com.warren.contact.utils.StringUtils;

public class User {
	private String phone;
	private String pwd;
	private String deviceId;
	private String userName;
	private String userSex;
	private String userImg;
	private String locationPublic;
	private String uid;// loginType+idµÄ×éºÏ
	private String screenName;
	private List<Contact> contacts;
	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getLocationPublic() {
		return locationPublic;
	}

	public void setLocationPublic(String locationPublic) {
		this.locationPublic = locationPublic;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.deviceId)
				&& StringUtils.isEmpty(this.phone)
				&& StringUtils.isEmpty(this.uid)) {
			return true;

		} else {
			return false;
		}
	}

}

package com.warren.contact.domain;

import java.util.List;

/**
 * 包含联系人owner信息的完整数据对象
 * @author dong.wangxd
 *
 */
public class FullContact {
	private String contactOwner;
	
	List<Contact> contacts;

	public String getContactOwner() {
		return contactOwner;
	}

	public void setContactOwner(String contactOwner) {
		this.contactOwner = contactOwner;
	}

	public List<Contact> getContact() {
		return contacts;
	}

	public void setContact(List<Contact> contact) {
		this.contacts = contact;
	}
	

}

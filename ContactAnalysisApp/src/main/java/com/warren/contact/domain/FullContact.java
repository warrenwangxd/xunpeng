package com.warren.contact.domain;

import java.util.List;

/**
 * ������ϵ��owner��Ϣ���������ݶ���
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

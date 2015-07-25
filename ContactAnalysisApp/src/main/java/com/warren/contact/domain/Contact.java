package com.warren.contact.domain;

public class Contact {
	private String name;
	private String phoneNumber;

	public Contact() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Contact(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public boolean equals(Object o) {
		Contact contact = (Contact) o;
		if (contact.getName().equals(this.name)
				&& contact.getPhoneNumber().equals(this.phoneNumber)) {
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		int hashCode = this.name.hashCode() + this.phoneNumber.hashCode();
		return hashCode;
	}
	
	public String toString() {
		return "\n"+name+"("+this.phoneNumber+")";
	}

}

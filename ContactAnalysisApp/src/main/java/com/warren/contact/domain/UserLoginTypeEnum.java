package com.warren.contact.domain;

public enum UserLoginTypeEnum {
	QQ("QQ"), WEIBO("WEIBO"),PHONE("PHONE");

	private String value;

	private UserLoginTypeEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}

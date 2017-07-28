package com.chat.bean;

import java.io.Serializable;

public class MyString implements Serializable{
	private String string;
	public MyString(String string) {
		// TODO Auto-generated constructor stub
		this.string = string;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	
}

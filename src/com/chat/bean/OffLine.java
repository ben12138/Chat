package com.chat.bean;

import java.io.Serializable;

public class OffLine implements Serializable{
	private String name;
	private LoginReturnType offline;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LoginReturnType getOffline() {
		return offline;
	}
	public void setOffline(LoginReturnType offline) {
		this.offline = offline;
	}
	
}

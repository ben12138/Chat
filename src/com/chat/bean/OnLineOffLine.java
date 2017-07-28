package com.chat.bean;

import java.io.Serializable;

public class OnLineOffLine implements Serializable{
	
	public static int TYPE_OFFLINE = 0;
	public static int TYPE_ONLINE  = 1;
	private int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

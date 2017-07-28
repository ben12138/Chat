package com.chat.bean;

import java.io.Serializable;

public class GetPersonPassword implements Serializable{
	private String name;
	private int result;
	private UserInformation u;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public UserInformation getU() {
		return u;
	}
	public void setU(UserInformation u) {
		this.u = u;
	}
}

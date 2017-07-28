package com.chat.bean;

import java.io.Serializable;

public class FindPassword implements Serializable{
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String email;
	private String password;
	private int type;
	private String yanzhengma;
	public String getYanzhengma() {
		return yanzhengma;
	}
	public void setYanzhengma(String yanzhengma) {
		this.yanzhengma = yanzhengma;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}

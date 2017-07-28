package com.chat.bean;

import java.io.Serializable;

public class User implements Serializable{
	private String name;
	private String password;
	private SendType type;
	private LoginReturnType ltype;
	public LoginReturnType getLtype() {
		return ltype;
	}
	public void setLtype(LoginReturnType ltype) {
		this.ltype = ltype;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	public User(String name,String pwd,SendType type){
		this.name = name;
		this.password = pwd;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public SendType getType() {
		return type;
	}
	public void setType(SendType type) {
		this.type = type;
	}
	
 }

package com.chat.bean;

import java.io.Serializable;

public class MessageContent implements Serializable{
	public static final int FILE = 0;
	public static final int STRING = 1;
	public static final int PICTURE = 2;
	public static final int OTHER = 3;
	public static final int VOICE = 4;
	private Object content;
	private int type;
	private String fileName;
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

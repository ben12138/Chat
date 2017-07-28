package com.chat.bean;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	public static final int TYPE_RECEIVED = 0;
	public static final int TYPE_SEND = 1;
	private MessageContent content;
	private int type;
	private Date sendTime;
	private Date receiveTime;
	private UserInformation sender;
	private UserInformation receiver;
	private int isRead;
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public UserInformation getReceiver() {
		return receiver;
	}
	public void setReceiver(UserInformation receiver) {
		this.receiver = receiver;
	}
	public UserInformation getSender() {
		return sender;
	}
	public void setSender(UserInformation sender) {
		this.sender = sender;
	}
	public MessageContent getContent() {
		return content;
	}
	public void setContent(MessageContent content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
}

package com.chat.bean;

import java.io.Serializable;

public class FriendList implements Serializable{
	private String myName;
	private String friendName;
	private String remark;
	private UserInformation u;
	private int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMyName() {
		return myName;
	}
	public void setMyName(String myName) {
		this.myName = myName;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public UserInformation getU() {
		return u;
	}
	public void setU(UserInformation u) {
		this.u = u;
	}
	
}

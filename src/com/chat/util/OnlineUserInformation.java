package com.chat.util;

import com.chat.bean.UserInformation;

public class OnlineUserInformation {
	private static UserInformation u = null;
	private static OnlineUserInformation onlineU = null;
	private OnlineUserInformation(){
		
	}
	public static OnlineUserInformation getInstance(){
		if(onlineU == null){
			onlineU = new OnlineUserInformation();
		}
		return onlineU;
	}
	public static void setOnlineU(UserInformation u){
		u = u;
	}
	public static UserInformation getOnlineU(){
		return u;
	}
}

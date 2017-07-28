package com.chat.util;

import com.chat.bean.UserInformation;

public class GetUserInformation {
	private static GetUserInformation getUserInf = null;
	
	private static boolean isGetUserInformation = false;
	private UserInformation u = null;
	
	public static boolean isGetUserInformation() {
		return isGetUserInformation;
	}
	public static void setGetUserInformation(boolean isGetUserInformation) {
		GetUserInformation.isGetUserInformation = isGetUserInformation;
	}
	public static UserInformation getU() {
		return getUserInf.u;
	}
	public static void setU(UserInformation u) {
		getUserInf.u = u;
	}
	static{
		getUserInf = new GetUserInformation();
	}
	public GetUserInformation getInstance(){
		return getUserInf;
	}
}

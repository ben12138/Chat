package com.chat.util;

import com.chat.bean.UserInformation;



public class PasswordGetResult {
	private static PasswordGetResult passwordGetResult = null;
	
	private static boolean isGetPasswordEmail = false;
	private static String password = null;
	private static String yanzhengma = null;
	
	private static boolean isGetUserInformation = false;
	private static UserInformation u = null;
	
	public static boolean isGetUserInformation() {
		return isGetUserInformation;
	}
	public static void setGetUserInformation(boolean isGetUserInformation) {
		PasswordGetResult.isGetUserInformation = isGetUserInformation;
	}
	public static UserInformation getU() {
		return u;
	}
	public static void setU(UserInformation u) {
		PasswordGetResult.u = u;
	}
	public static String getYanzhengma() {
		return yanzhengma;
	}
	public static void setYanzhengma(String yanzhengma) {
		PasswordGetResult.yanzhengma = yanzhengma;
	}
	public static boolean isGetPasswordEmail() {
		return isGetPasswordEmail;
	}
	public static void setGetPasswordEmail(boolean isGetPasswordEmail) {
		PasswordGetResult.isGetPasswordEmail = isGetPasswordEmail;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		PasswordGetResult.password = password;
	}
	private PasswordGetResult() {
		// TODO Auto-generated constructor stub
	}
	static{
		passwordGetResult = new PasswordGetResult();
	}
}

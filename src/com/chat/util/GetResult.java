package com.chat.util;

import android.database.CursorJoiner.Result;

import com.chat.bean.LoginReturnType;

public class GetResult {
	
	private static boolean isGetPassword = false;
	private static String password = null;
	public static boolean isGetPassword() {
		return GetResult.isGetPassword;
	}
	public static void setGetPassword(boolean isGetPassword) {
		GetResult.isGetPassword = isGetPassword;
	}
	public static void setPassword(String password){
		GetResult.password = password;
	}
	public static String getPassword(){
		return GetResult.password;
	}

	private static boolean isGetInformation = false;
	public static boolean isGetInformation(){
		return GetResult.isGetInformation;
	}
	public static void setGetInformation(boolean tag){
		GetResult.isGetInformation = tag;
	}
	
	private static int getInformationType = 0;
	public static int getGetInformationType() {
		return GetResult.getInformationType;
	}
	public static void setGetInformationType(int getInformationType) {
		GetResult.getInformationType = getInformationType;
	}
	

	private static boolean isRegister = true;
	public static boolean isRegister() {
		return GetResult.isRegister;
	}
	public static void setRegister(boolean isRegister) {
		GetResult.isRegister = isRegister;
	}
	
	
	
	private boolean isGetConfirm = false;
	public boolean isGetConfirm() {
		return isGetConfirm;
	}
	public void setGetConfirm(boolean isGetConfirm) {
		this.isGetConfirm = isGetConfirm;
	}
	
	
	
	private LoginReturnType lResult = null;
	private boolean isReceived = false;
	private static GetResult result = null;
	private static boolean GetEmailConfirm = false;
	private static String YanZhengMa = "";
	public static String getYanZhengMa() {
		return YanZhengMa;
	}
	public static void setYanZhengMa(String yanZhengMa) {
		YanZhengMa = yanZhengMa;
	}
	private GetResult() {
		// TODO Auto-generated constructor stub
	}
	static {
		result = new GetResult();
	}
	public static LoginReturnType getlResult() {
		return result.lResult;
	}
	public static void setlResult(LoginReturnType lResult) {
		result.lResult = lResult;
	}
	public static boolean isReceived(){
		return result.isReceived;
	}
	public static void setReceived(boolean a){
		result.isReceived = a;
	}
	public static boolean isGetEmailConfirm() {
		return GetEmailConfirm;
	}
	public static void setGetEmailConfirm(boolean getEmailConfirm) {
		GetEmailConfirm = getEmailConfirm;
	}
	
}

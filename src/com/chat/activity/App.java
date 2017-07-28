package com.chat.activity;

import com.cl253.smssdk.lib.SMSSDK;

import android.app.Application;

public class App extends Application {

	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "APPKEY";

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "APPSECRET";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		/**253SmsSDK 初始�*/
		SMSSDK.initSDK(getApplicationContext(), APPKEY, APPSECRET);
	}
}

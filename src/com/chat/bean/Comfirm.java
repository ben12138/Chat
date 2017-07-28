package com.chat.bean;

import java.io.Serializable;

public class Comfirm implements Serializable{
	private String confirm;
	private static boolean isRegistered;
	private String yanZhengMa = "";
	public String getYanZhengMa() {
		return yanZhengMa;
	}

	public void setYanZhengMa(String yanZhengMa) {
		this.yanZhengMa = yanZhengMa;
	}

	public static boolean isRegistered() {
		return isRegistered;
	}

	public static void setRegistered(boolean isRegistered) {
		Comfirm.isRegistered = isRegistered;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
}

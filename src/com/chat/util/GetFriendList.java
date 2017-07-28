package com.chat.util;

import java.util.ArrayList;
import java.util.List;

import com.chat.bean.FriendList;

public class GetFriendList {
	private GetFriendList() {
		// TODO Auto-generated constructor stub
	}
	public static List<FriendList> friends = new ArrayList<FriendList>();
	public static boolean GETFRIENDS = false;
	public static List<FriendList> getFriends() {
		return friends;
	}
	public static void setFriends(List<FriendList> friends) {
		GetFriendList.friends = friends;
	}
	public static boolean isGETFRIENDS() {
		return GETFRIENDS;
	}
	public static void setGETFRIENDS(boolean gETFRIENDS) {
		GETFRIENDS = gETFRIENDS;
	}
	
}

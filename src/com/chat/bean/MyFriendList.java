package com.chat.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyFriendList implements Serializable{
	public List<FriendList> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendList> friends) {
		this.friends = friends;
	}

	private List<FriendList> friends = new ArrayList<FriendList>();
}

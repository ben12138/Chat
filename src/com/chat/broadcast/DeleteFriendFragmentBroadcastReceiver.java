package com.chat.broadcast;

import com.chat.fragment.BodyContactFrament;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

public class DeleteFriendFragmentBroadcastReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Message message = new Message();
		message.what = 2;
		Bundle bundle = intent.getExtras();
		message.arg1 = bundle.getInt("delete_position");
		System.out.println(message.arg1);
		if(BodyContactFrament.handler2!=null){
			BodyContactFrament.handler2.sendMessage(message);
		}
	}
}

package com.chat.broadcast;

import com.ben.chat.R;
import com.chat.fragment.BodyContactFrament;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;

public class UpdateContactFragmentBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Message message = new Message();
		message.what = 1;
		Bundle bundle = intent.getExtras();
		message.obj = bundle.getSerializable("userinf");
		message.arg1 = bundle.getInt("click_position");
		if(BodyContactFrament.handler2!=null){
			BodyContactFrament.handler2.sendMessage(message);
		}
	}
	
}

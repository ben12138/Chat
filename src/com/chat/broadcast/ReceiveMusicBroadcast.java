package com.chat.broadcast;
import com.ben.chat.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class ReceiveMusicBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		MediaPlayer mediaplayer = MediaPlayer.create(context, R.raw.test);
		mediaplayer.start();
	}
	
}

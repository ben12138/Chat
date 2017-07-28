package com.chat.broadcast;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.chat.activity.LoginActivity;
import com.chat.util.ActivityCollector;
import com.chat.util.GetUserInformation;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

public class OffLineBroadcast extends BroadcastReceiver{
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
		String time = f.format(date);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("下线通知");
		dialog.setMessage("您的账号:"+GetUserInformation.getU().getName()+"在"+time+"异地登录，请尽快核实");
		dialog.setCancelable(false);
		dialog.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				ActivityCollector.finishAll();
			}
		});
		dialog.show();
		AlertDialog alertDialog = dialog.create();
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.show();
	}
}

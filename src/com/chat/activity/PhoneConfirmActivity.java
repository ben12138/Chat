package com.chat.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.cookie.SM;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ben.chat.R;
import com.chat.bean.Comfirm;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.GetResult;
import com.chat.util.GetUserInformation;
import com.cl253.smssdk.lib.SMSSDK;
import com.cl253.smssdk.listener.IGetVerificationCodeCallBack;

public class PhoneConfirmActivity extends Activity {
	private NetService netService = null;
	private String emailNum = null;
	private Context context = null;
	private EditText email = null;
	private Button confirm = null;
	private Button back = null;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
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
						startActivity(intent);
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
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.phone_confirm);
		init();
	}

	public void init() {
		ActivityCollector.addActivity(this);
		this.context = this;
		email = (EditText) findViewById(R.id.phone_num);
		confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(confirmOnClickListener);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(backOnClickListener);
	}

	private OnClickListener confirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			emailNum = email.getText().toString().trim();
			if (isEmail(emailNum)) {
				tryConfirm();
			} else {
				Toast.makeText(context, "请正确输入邮箱", Toast.LENGTH_LONG).show();
			}

		}
	};

	public void tryConfirm() {
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				Toast.makeText(context, "正在提交֤", Toast.LENGTH_LONG).show();
			}

			@Override
			protected Integer doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				netService = NetService.getInstance();
				//netService.closeConnection();
				netService.setConnection(context,handler);
				if(!netService.isConnected()){
					return 0;
				}
				Comfirm c = new Comfirm();
				c.setConfirm(emailNum);
				netService.send(c);
				while(!GetResult.isGetEmailConfirm())System.out.println("get email");;
				GetResult.setGetEmailConfirm(false);
				System.out.println(GetResult.isRegister());
				if(GetResult.isRegister()){
					System.out.println("1");
					return 1;
				}else{
					System.out.println("2");
					GetResult.setRegister(true);
					return 2;
				}
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				if (result == 0) {
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_LONG).show();
				}else if(result == 1){
					Toast.makeText(context, "该邮箱已被注册", Toast.LENGTH_LONG).show();
				}else if (result == 2) {
					Intent intent = new Intent(context,PhoneConfirmActivity2.class);
					System.out.println(emailNum);
					intent.putExtra("email", emailNum);
					startActivity(intent);
				}
			}
		}.execute();
	}

	private OnClickListener backOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			PhoneConfirmActivity.this.finish();
		}
	};

	public boolean isEmail(String email) {
		String format = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		if (email.matches(format)) {
			return true;
		}
		return false;
	}
}

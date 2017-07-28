package com.chat.activity;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ben.chat.R;
import com.chat.bean.LoginReturnType;
import com.chat.bean.MyString;
import com.chat.bean.SendType;
import com.chat.bean.User;
import com.chat.bean.UserInformation;
import com.chat.network.NetConnect;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.GetFriendList;
import com.chat.util.GetResult;
import com.chat.util.GetUserInformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private Context context;
	private LinearLayout user;
	private LinearLayout login_button;
	private Button login = null;
	private NetService netService = null;
	private Button find_password = null;
	private EditText accountEditText = null;
	private EditText passwordEditText = null;
	private User u = null;
	private UserInformation uif = null;
	private Button register = null;
	private static final Context mContext = new LoginActivity();
	private Handler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		context = this;
		init();
		initEvents();
	}

	public void init() {
		ActivityCollector.addActivity(this);
		user = (LinearLayout) findViewById(R.id.login_user);
		login_button = (LinearLayout) findViewById(R.id.login_button);
		login = (Button) findViewById(R.id.login);
		find_password = (Button) findViewById(R.id.find_password);
		login.setOnClickListener(loginOnClickListsner);
		find_password.setOnClickListener(findOnClickListener);
		accountEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(registerOnClickListener);
		uif = new UserInformation();
		handler = new Handler(){
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
	}

	public void initEvents() {
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.login_anim);
		anim.setFillAfter(true);
		user.startAnimation(anim);
		login_button.startAnimation(anim);
		find_password.startAnimation(anim);
	}

	private OnClickListener loginOnClickListsner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String account = accountEditText.getText().toString().trim();
			String password = passwordEditText.getText().toString().trim();
			u = new User(account, password, SendType.ACCOUNT_CONFIRM);
			tryLogin();
		}

	};

	public void tryLogin() {
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				Toast.makeText(context, "正在登陆", Toast.LENGTH_SHORT).show();
			}

			@Override
			protected Integer doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				netService = NetService.getInstance();
				netService.closeConnection();
				netService.setConnection(context,handler);
				if(!netService.isConnected()){
					return 0;
				}
				netService.send(u);
				while(!GetResult.isReceived());
				GetResult.setReceived(false);
				if (GetResult.getlResult() != null) {
					switch (GetResult.getlResult()) {
					case ERROR:
						return 1;
					case SUCCESS:
						GetResult.setlResult(LoginReturnType.ERROR);
						return 2;
					case ACCOUNT_ERROR:
						return 3;
					case PASSWORD_ERROR:
						return 4;
					}
				}
				return 0;
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				if (result == 0) {
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_LONG).show();
				} else if (result == 1) {
					Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show();
				} else if (result == 2) {
					tryGetUserInf();
				}else if(result == 3){
					Toast.makeText(context, "账号不存在", Toast.LENGTH_SHORT).show();
				}else if(result == 4){
					Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}
	
	public void tryGetUserInf(){
		new AsyncTask<Void, Void, Integer>(){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}
			
			@Override
			protected Integer doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				netService = NetService.getInstance();
//				netService.closeConnection();
				netService.setConnection(context,handler);
				if(!netService.isConnected()){
					return 0;
				}
				netService.send(new MyString(u.getName()));
				while(!GetUserInformation.isGetUserInformation());
				GetUserInformation.setGetUserInformation(false);
				System.out.println(GetUserInformation.getU());
				uif = GetUserInformation.getU();
				return 1;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				if(result == 1){
					loginToMainActivity();
				}else if(result == 0){
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_LONG).show();
				}
			}
			
		}.execute();
	}
	
	public void loginToMainActivity(){
		new AsyncTask<Void, Void, Integer>(){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}
			
			@Override
			protected Integer doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				while(!GetFriendList.isGETFRIENDS());
				GetFriendList.setGETFRIENDS(false);
				return 1;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == 1){
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					intent.putExtra("user", uif);
					startActivity(intent);
					finish();
				}
			}
			
		}.execute();
	}
	
	private OnClickListener findOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("忘记密码？");
			final String str[] = { "邮箱登录"};
			builder.setItems(str, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch(which){
					case 0:
						Intent intent = new Intent(context,FindPasswordActivity.class);
						startActivity(intent);
						ActivityCollector.removeActivity(LoginActivity.this);
						LoginActivity.this.finish();
						break;
					}
				}
			});
			builder.show();
		}
	};
	
	private OnClickListener registerOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(LoginActivity.this,PhoneConfirmActivity.class);
			startActivity(intent);
		}
	};
}

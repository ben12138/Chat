package com.chat.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.cookie.SM;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ben.chat.R;
import com.chat.bean.Comfirm;
import com.chat.bean.FindPassword;
import com.chat.bean.GetPersonPassword;
import com.chat.bean.MyString;
import com.chat.bean.SendType;
import com.chat.bean.User;
import com.chat.bean.UserInformation;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.FileUtil;
import com.chat.util.GetFriendList;
import com.chat.util.GetResult;
import com.chat.util.GetUserInformation;
import com.chat.util.PasswordGetResult;
import com.chat.util.Utils;
import com.cl253.smssdk.lib.SMSSDK;
import com.cl253.smssdk.listener.IGetVerificationCodeCallBack;

public class FindPasswordActivity extends Activity {
	private NetService netService = null;
	private String emailNum = null;
	private Context context = null;
	private EditText email = null;
	private Button confirm = null;
	private Button back = null;
	private Button check = null;
	private EditText yanzhengmaEditTextView = null;
	private ImageView headImageImageView = null;
	private TextView tipTextView = null;
	private TextView nickNameTextView = null;
	private UserInformation u = null;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.find_password);
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
		check = (Button) findViewById(R.id.confirm_yanzhengma);
		yanzhengmaEditTextView = (EditText) findViewById(R.id.yanzhengma);
		check.setVisibility(View.INVISIBLE);
		yanzhengmaEditTextView.setVisibility(View.INVISIBLE);
		check.setEnabled(false);
		check.setOnClickListener(checkOnClickListener);
		tipTextView = (TextView) findViewById(R.id.tip);
		nickNameTextView = (TextView) findViewById(R.id.nickName);
		headImageImageView = (ImageView) findViewById(R.id.headImage);
		tipTextView.setVisibility(View.INVISIBLE);
		nickNameTextView.setVisibility(View.INVISIBLE);
		headImageImageView.setVisibility(View.INVISIBLE);
		headImageImageView.setOnClickListener(headImageImageViewOnClickListener);
	}

	private OnClickListener confirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			emailNum = email.getText().toString().trim();
			if (isEmail(emailNum)) {
				tryConfirm();
			} else {
				Toast.makeText(context, "请正确填写邮箱", Toast.LENGTH_LONG).show();
			}

		}
	};

	public void tryConfirm() {
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				Toast.makeText(context, "正在提交", Toast.LENGTH_LONG).show();
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
				FindPassword f = new FindPassword();
				f.setEmail(emailNum);
				netService.send(f);
				while(!PasswordGetResult.isGetPasswordEmail());
				PasswordGetResult.setGetPasswordEmail(false);
				return 1;
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				if (result == 0) {
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_LONG).show();
				}else if(result == 1){
					check.setVisibility(View.VISIBLE);
					yanzhengmaEditTextView.setVisibility(View.VISIBLE);
					check.setEnabled(true);
				}
			}
		}.execute();
	}

	private OnClickListener backOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FindPasswordActivity.this.finish();
		}
	};
	
	private OnClickListener headImageImageViewOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
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
				//netService.closeConnection();
				netService.setConnection(context,handler);
				if(!netService.isConnected()){
					return 0;
				}
				User u1 = new User();
				u1.setName(u.getName());
				u1.setPassword(u.getPassword());
				u1.setType(SendType.ACCOUNT_CONFIRM);
				netService.send(u1);
				while(!GetResult.isReceived());
				GetResult.setReceived(false);
				return 1;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				if (result == 0) {
					Toast.makeText(context, "糟糕，网络出现异常了", Toast.LENGTH_LONG).show();
				} else if (result == 1) {
					loginToMainActivity();
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
					Intent intent = new Intent(FindPasswordActivity.this,MainActivity.class);
					u = PasswordGetResult.getU();
					intent.putExtra("user1", u);
					GetUserInformation.setU(u);
					startActivity(intent);
					finish();
				}
			}
			
		}.execute();
	}
	
	private OnClickListener checkOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(PasswordGetResult.getYanzhengma() == null){
				Toast.makeText(context, "请重新验证", Toast.LENGTH_SHORT).show();
			}
			String yanzhengma = yanzhengmaEditTextView.getText().toString().trim();
			if(yanzhengma.equals(PasswordGetResult.getYanzhengma())){
				tryConfirmYanzhemgma();
			}else{
				Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	public void tryConfirmYanzhemgma(){
		new AsyncTask<Void, Void, Integer>(){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				Toast.makeText(context, "正在验证֤", Toast.LENGTH_SHORT).show();
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
				GetPersonPassword p = new GetPersonPassword();
				p.setName(emailNum);
				p.setResult(1);
				p.setU(null);
				netService.send(p);
				while(!PasswordGetResult.isGetUserInformation());
				PasswordGetResult.setGetUserInformation(false);
				return 1;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == 0){
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_SHORT).show();
				}else if(result == 1){
					u = PasswordGetResult.getU();
					//PasswordGetResult.setU(null);
					FileUtil.createFile(u.getName(), u.getPhoto());
					Bitmap bitmap = Utils.getBitmap(u.getPhoto());
					headImageImageView.setImageBitmap(bitmap);
					headImageImageView.setVisibility(View.VISIBLE);
					nickNameTextView.setVisibility(View.VISIBLE);
					tipTextView.setVisibility(View.VISIBLE);
					
					nickNameTextView.setText(u.getNickname());
				}
			}
			
		}.execute();
	}
	
	public boolean isEmail(String email) {
		String format = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		if (email.matches(format)) {
			return true;
		}
		return false;
	}
}

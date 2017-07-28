package com.chat.activity;

import com.ben.chat.R;
import com.chat.bean.FriendList;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailedInformationActivity extends Activity{
	
	private Context context = null;
	private Button backButton = null;
	private FriendList friend = null;
	private TextView userNameTextView = null;
	private EditText remarkEditText = null;
	private TextView sexTextView = null;
	private TextView birthdayTextView = null;
	private TextView schoolTextView = null;
	private TextView introductionTextView = null;
	private Button deleteFriendButton = null;
	private Button okButton = null;
	private NetService netService = null;
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_detailed_information_layout);
		init();
	}
	
	public void init(){
		ActivityCollector.addActivity(this);
		context = this;
		Intent intent = getIntent();
		friend = (FriendList) intent.getSerializableExtra("contactinf");
		backButton = (Button) findViewById(R.id.back);
		backButton.setOnClickListener(backButtonOnClickListener);
		
		okButton = (Button) findViewById(R.id.ok);
		deleteFriendButton = (Button) findViewById(R.id.delete_friend);
		okButton.setOnClickListener(okButtonOnClickListener);
		deleteFriendButton.setOnClickListener(deleteFriendButtonOnClickListener);
		
		userNameTextView = (TextView) findViewById(R.id.user_name);
		remarkEditText = (EditText) findViewById(R.id.remark);
		sexTextView = (TextView) findViewById(R.id.sex);
		birthdayTextView = (TextView) findViewById(R.id.birthday);
		schoolTextView = (TextView) findViewById(R.id.school);
		introductionTextView = (TextView) findViewById(R.id.introduction);
		userNameTextView.setText(friend.getFriendName()+"("+friend.getU().getNickname()+")");
		if(friend.getRemark()!=null){
			remarkEditText.setText(friend.getRemark());
		}else{
			remarkEditText.setText("");
		}
		if(friend.getU().getSex() == 0){
			sexTextView.setText("男");
		}else if(friend.getU().getSex() == 1){
			sexTextView.setText("女");
		}
		birthdayTextView.setText(friend.getU().getBirthday());
		schoolTextView.setText(friend.getU().getSchool());
		introductionTextView.setText(friend.getU().getIntroduction());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private OnClickListener backButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	private OnClickListener deleteFriendButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			deleteFriend();
		}
	};
	
	private void deleteFriend(){
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
				netService.closeConnection();
				netService.setConnection(context,handler);
				if(!netService.isConnected()){
					return 0;
				}
				FriendList f = new FriendList();
				f.setFriendName(friend.getFriendName());
				f.setRemark(remarkEditText.getText().toString());
				f.setMyName(friend.getMyName());
				f.setType(2);//ɾ����ϵ�˱�ע
				netService.send(f);
				return 1;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == 0){
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_SHORT).show();
				}else if(result == 1){
					Message msg = new Message();
					msg.what = 2;
					ContactInformationActivity.handler.sendMessage(msg);
					finish();
				}
			}
			
		}.execute();
	}
	
	private OnClickListener okButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(remarkEditText.getText().toString().equals(friend.getRemark())){
				finish();
			}else{
				submit();
			}
		}
	};
	
	private void submit(){
		new AsyncTask<Void, Void, Integer>() {
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
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
				FriendList f = new FriendList();
				f.setFriendName(friend.getFriendName());
				f.setRemark(remarkEditText.getText().toString());
				f.setMyName(friend.getMyName());
				f.setType(1);//��ʾ������ϵ�˱�ע
				netService.send(f);
				return 1;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == 0){
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_SHORT).show();
				}else if(result == 1){
					Message msg = new Message();
					if(remarkEditText.getText().toString().equals("")){
						msg.obj = friend.getU().getNickname();
					}else{
						msg.obj = remarkEditText.getText().toString();
					}
					msg.what = 1;
					ContactInformationActivity.handler.sendMessage(msg);
					finish();
				}
			}
			
		}.execute();
	}
	
}

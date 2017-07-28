package com.chat.activity;

import com.ben.chat.R;
import com.chat.bean.FriendList;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.GetUserInformation;
import com.chat.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactInformationActivity extends Activity{
	
	private Context context;
	private ImageView headImageImageView = null;
	private TextView remarkTextView = null;
	private TextView userNameTextView = null;
	private TextView dynamicStateTextView = null;
	private TextView emailTextView = null;
	private Button moreButton = null;
	private Button backButton = null;
	private FriendList friend = null;
	private int clickPosition = -1;
	private NetService netService = null;
	public static Handler handler;
	private Button sendMessageButton = null;
	private Bitmap bitmap = null;
	private String online = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_information_layout);
		init();
	}
	
	public void init(){
		ActivityCollector.addActivity(this);
		context = this;
		Intent intent = getIntent();
		friend = (FriendList) intent.getSerializableExtra("contact");
		if(friend.getU().getIsOnline() == 0){
			online = "(在线)";
		}else if(friend.getU().getIsOnline() == 1){
			online = "(离线)";
		}
		clickPosition = intent.getIntExtra("click_position", -1);
		backButton = (Button) findViewById(R.id.back);
		backButton.setOnClickListener(backButtonOnClickListener);
		headImageImageView = (ImageView) findViewById(R.id.head_image);
		remarkTextView = (TextView) findViewById(R.id.remark);
		userNameTextView = (TextView) findViewById(R.id.user_name);
		dynamicStateTextView = (TextView) findViewById(R.id.dynamic_state);
		emailTextView = (TextView) findViewById(R.id.email);
		moreButton = (Button) findViewById(R.id.more);
		moreButton.setOnClickListener(moreButtonOnClickListener);
		sendMessageButton = (Button) findViewById(R.id.send_message);
		if(friend.getType() == 2){
			sendMessageButton.setVisibility(View.INVISIBLE);
			moreButton.setText("更多");
			moreButton.setTextColor(Color.GRAY);
			moreButton.setOnClickListener(moreButtonOnClickListener2);
		}else{
			sendMessageButton.setVisibility(View.VISIBLE);
			sendMessageButton.setOnClickListener(sendMessageButtonOnClickListener);
		}
		bitmap = Utils.getBitmap(friend.getU().getPhoto());
		headImageImageView.setImageBitmap(bitmap);
		headImageImageView.setOnClickListener(headImageImageViewOnClickListener);
		userNameTextView.setText(friend.getU().getNickname());
		if(friend.getRemark()==null||friend.getRemark().equals("")){
			if(friend.getU().getNickname() == null){
				remarkTextView.setText(friend.getU().getName()+online);
				dynamicStateTextView.setText(friend.getU().getName()+"的空间");
			}else{
				remarkTextView.setText(friend.getU().getNickname()+online);
				dynamicStateTextView.setText(friend.getU().getNickname()+"的空间");
			}
		}else{
			remarkTextView.setText(friend.getRemark()+online);
			dynamicStateTextView.setText(friend.getRemark()+"的空间");
		}
		emailTextView.setText(friend.getU().getName());
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == 1){
					remarkTextView.setText((String)msg.obj);
					dynamicStateTextView.setText((String)msg.obj+"的空间");
					Intent intent1 = new Intent("com.chat.broadcast.UPDATE_CONTACT_FRAGMENT_BROADCAST");
					friend.setRemark((String)msg.obj);
					Bundle bundle = new Bundle();
					bundle.putSerializable("userinf", friend);
					bundle.putInt("click_position", clickPosition);
					intent1.putExtras(bundle);
					sendBroadcast(intent1);
				}else if(msg.what == 2){
					Intent intent1 = new Intent("com.chat.broadcast.DELETE_FRIEND_FRAGMENT_BROADCAST");
					Bundle bundle = new Bundle();
					bundle.putInt("delete_position", clickPosition);
					intent1.putExtras(bundle);
					sendBroadcast(intent1);
					finish();
				}else if(msg.what == 3){
					bitmap = Utils.getBitmap(GetUserInformation.getU().getPhoto());
					headImageImageView.setImageBitmap(bitmap);
					sendMessageButton.setVisibility(View.INVISIBLE);
					moreButton.setText("编辑");
					moreButton.setTextColor(Color.GRAY);
					moreButton.setOnClickListener(moreButtonOnClickListener2);
					remarkTextView.setText(GetUserInformation.getU().getNickname());
					userNameTextView.setText(GetUserInformation.getU().getNickname());
					dynamicStateTextView.setText(GetUserInformation.getU().getNickname()+"�Ŀռ�");
				}
			}
		};
	}
	
	private OnClickListener headImageImageViewOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(context);
			View bigImageView = inflater.inflate(R.layout.dialog_photo_entry, null);
			final AlertDialog dialog = new AlertDialog.Builder(context).create();
			ImageView bigPhotoImageView = (ImageView) bigImageView.findViewById(R.id.big_photo);
			bigPhotoImageView.setImageBitmap(bitmap);
			bigPhotoImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			dialog.setView(bigImageView);
			dialog.show();
		}
	};
	
	private OnClickListener moreButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context,ContactDetailedInformationActivity.class);
			intent.putExtra("contactinf", friend);
			startActivity(intent);
		}
	};
	
	private OnClickListener backButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	private OnClickListener moreButtonOnClickListener2 = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context,EditMyUserInformationActivity.class);
			startActivity(intent);
		}
	};
	
	private OnClickListener sendMessageButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context,SendMessageActivity.class);
			intent.putExtra("contact", friend);
			startActivity(intent);
		}
	};
	
}

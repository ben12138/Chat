package com.chat.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ben.chat.R;
import com.chat.bean.FriendList;
import com.chat.bean.LoginReturnType;
import com.chat.bean.OffLine;
import com.chat.bean.UserInformation;
import com.chat.fragment.BodyContactFrament;
import com.chat.fragment.BodyDynamicStateFrament;
import com.chat.fragment.BodyMessageFrament;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.GetUserInformation;
import com.chat.util.Utils;

public class MainActivity extends Activity {
	
	public static final int SELECTED = 1;
	public static final int NORMAL = 0;
	public static Context context = null;
	private ImageView headImageImageView = null;
	private Intent intent = null;
	public static UserInformation userinf = null;
	public static Handler handler = null;
	private ImageView messageButton = null;
	private ImageView contactButton = null;
	private ImageView dynamicStateButton = null;
	private BodyMessageFrament bodyMessageFragment = null;
	private BodyContactFrament bodyContactFragment = null;
	private BodyDynamicStateFrament bodyDynamicStateFragment = null;
	private FragmentManager fragmentManager = null;
	private FragmentTransaction transaction = null;
	private ImageView drawerMenuHeadImageView = null;
	private TextView drawerMenuNickName = null;
	private TextView drawerMenuIntroduction = null;
	private Button exitButton = null;
	private Button changeAccountButton = null;
	private NetService netService = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	
	public void init(){
		ActivityCollector.addActivity(this);
		bodyContactFragment = new BodyContactFrament();
		bodyDynamicStateFragment = new BodyDynamicStateFrament();
		bodyMessageFragment = new BodyMessageFrament();
		intent = getIntent();
		UserInformation u = (UserInformation) intent.getSerializableExtra("user");
		UserInformation u1 = (UserInformation) intent.getSerializableExtra("user1");
		if(u!=null){
			userinf = u;
		}
		if(u1!=null){
			userinf = u1;
		}
		context = this;
		drawerMenuHeadImageView = (ImageView) findViewById(R.id.drawer_menu_head_image);
		drawerMenuHeadImageView.setImageBitmap(Utils.getBitmap(GetUserInformation.getU().getPhoto(), 2));
		drawerMenuHeadImageView.setOnClickListener(drawerMenuHeadImageImageViewOnClickListener);
		drawerMenuNickName = (TextView) findViewById(R.id.drawer_menu_nick_name);
		drawerMenuNickName.setText(GetUserInformation.getU().getNickname());
		drawerMenuIntroduction = (TextView) findViewById(R.id.drawer_menu_introduction);
		drawerMenuIntroduction.setText(GetUserInformation.getU().getIntroduction());
		exitButton = (Button) findViewById(R.id.exit);
		exitButton.setOnClickListener(exitButtonOnClickListener);
		changeAccountButton = (Button) findViewById(R.id.change_account);
		changeAccountButton.setOnClickListener(changeAccountButtonOnClickListener);
		headImageImageView = (ImageView) findViewById(R.id.message_head_image);
		headImageImageView.setImageBitmap(Utils.getBitmap(userinf.getPhoto()));
		headImageImageView.setOnClickListener(headImageImageViewOnClickListener);
		messageButton = (ImageView) findViewById(R.id.message);
		contactButton = (ImageView) findViewById(R.id.contact);
		dynamicStateButton = (ImageView) findViewById(R.id.dynamic_state);
		messageButton.setOnClickListener(messageOnClickListener);
		contactButton.setOnClickListener(contactOnClickListener);
		dynamicStateButton.setOnClickListener(dynamicStateOnClickListener);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what){
				case 1:
					Intent intent = new Intent("com.chat.broadcast.OFF_LINE_BROADCAST");
					sendBroadcast(intent);
					break;
				case 3:
					Bitmap temp = Utils.getBitmap(GetUserInformation.getU().getPhoto(),2);
					drawerMenuHeadImageView.setImageBitmap(temp);
					headImageImageView.setImageBitmap(temp);
					drawerMenuIntroduction.setText(GetUserInformation.getU().getIntroduction());
					drawerMenuNickName.setText(GetUserInformation.getU().getNickname());
					break;
				case 2:
					break;
				}
				
			}
		};
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
		
	private android.view.View.OnClickListener messageOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setBackgroundImage(SELECTED, NORMAL, NORMAL);
			fragmentManager = getFragmentManager();
			transaction = fragmentManager.beginTransaction();
			if(!bodyContactFragment.isAdded()){
				if(bodyContactFragment.isAdded()){
					transaction.hide(bodyContactFragment);
				}
				if(bodyDynamicStateFragment.isAdded()){
					transaction.hide(bodyDynamicStateFragment);
				}
				
				transaction.add(R.id.body_fragment,bodyMessageFragment);
			}else{
				transaction.hide(bodyContactFragment);
				transaction.hide(bodyDynamicStateFragment);
				transaction.show(bodyMessageFragment);
			}
			transaction.commit();
		}
	};
	
	private android.view.View.OnClickListener contactOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setBackgroundImage(NORMAL, SELECTED, NORMAL);
			fragmentManager = getFragmentManager();
			transaction = fragmentManager.beginTransaction();
			if(!bodyContactFragment.isAdded()){
				if(bodyDynamicStateFragment.isAdded()){
					transaction.hide(bodyDynamicStateFragment);
				}
				if(bodyMessageFragment.isAdded()){
					transaction.hide(bodyMessageFragment);
				}
				
				transaction.add(R.id.body_fragment,bodyContactFragment);
			}else{
				transaction.hide(bodyDynamicStateFragment);
				transaction.hide(bodyMessageFragment);
				transaction.show(bodyContactFragment);
			}
			transaction.commit();
		}
	};
	
	private android.view.View.OnClickListener dynamicStateOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setBackgroundImage(NORMAL, NORMAL, SELECTED);
			fragmentManager = getFragmentManager();
			transaction = fragmentManager.beginTransaction();
			if(!bodyContactFragment.isAdded()){
				if(bodyContactFragment.isAdded()){
					transaction.hide(bodyContactFragment);
				}
				if(bodyMessageFragment.isAdded()){
					transaction.hide(bodyMessageFragment);
				}
				transaction.add(R.id.body_fragment,bodyDynamicStateFragment);
			}else{
				if(bodyContactFragment.isAdded()){
					transaction.hide(bodyContactFragment);
				}
				if(bodyMessageFragment.isAdded()){
					transaction.hide(bodyMessageFragment);
				}
				transaction.show(bodyDynamicStateFragment);
			}
			transaction.commit();
		}
	};
	
	//���ѡ�б���ͼƬ
	public void setBackgroundImage(int messageButtonId,int contactButtonId,int dynamicStateButtonId){
		if(messageButtonId == 1){
			
			messageButton.setImageResource(R.drawable.skin_tab_icon_conversation_selected);
		}else if(messageButtonId == 0){
			messageButton.setImageResource(R.drawable.skin_tab_icon_conversation_normal);
		}
		if(contactButtonId == 1){
			contactButton.setImageResource(R.drawable.skin_tab_icon_contact_selected);
		}else if(contactButtonId == 0){
			contactButton.setImageResource(R.drawable.skin_tab_icon_contact_normal);
		}
		if(dynamicStateButtonId == 1){
			dynamicStateButton.setImageResource(R.drawable.skin_tab_icon_plugin_selected);
		}else if(dynamicStateButtonId == 0){
			dynamicStateButton.setImageResource(R.drawable.skin_tab_icon_plugin_normal);
		}
		
	}
	
	private android.view.View.OnClickListener exitButtonOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			offline();
			ActivityCollector.finishAll();
		}
	};
	
	private android.view.View.OnClickListener changeAccountButtonOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			offline();
			Intent intent = new Intent(context,LoginActivity.class);
			startActivity(intent);
		}
	};
	public void offline(){
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
				netService.setConnection(context, handler);
				if(!netService.isConnected()){
					return 0;
				}
				OffLine of = new OffLine();
				of.setName(GetUserInformation.getU().getName());
				of.setOffline(LoginReturnType.OFFLINE);
				netService.send(of);
				return 1;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == 0){
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_SHORT).show();
				}else if(result == 1){
					GetUserInformation.setU(null);
				}
			}
			
		}.execute();
	}
	
	private android.view.View.OnClickListener headImageImageViewOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(context);
			View bigImageView = inflater.inflate(R.layout.dialog_photo_entry, null);
			final AlertDialog dialog = new AlertDialog.Builder(context).create();
			ImageView bigPhotoImageView = (ImageView) bigImageView.findViewById(R.id.big_photo);
			bigPhotoImageView.setImageBitmap(Utils.getBitmap(GetUserInformation.getU().getPhoto(),1));
			bigPhotoImageView.setOnClickListener(new View.OnClickListener() {
				
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
	
	private android.view.View.OnClickListener drawerMenuHeadImageImageViewOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context,ContactInformationActivity.class);
			FriendList friend = new FriendList();
			friend.setU(GetUserInformation.getU());
			friend.setType(2);
			intent.putExtra("contact", friend);
			startActivity(intent);
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityCollector.finishAll();
			offline();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
}

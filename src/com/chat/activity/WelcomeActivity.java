package com.chat.activity;

import com.ben.chat.R;
import com.chat.util.ActivityCollector;
import com.cl253.smssdk.lib.SMSSDK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class WelcomeActivity extends Activity{
	private ImageView imageview;
	private Context context;
	public static final Context mContext = new WelcomeActivity();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome_layout);
		context = this;
		findview();
		init();
	}
	public void findview(){
		imageview = (ImageView)findViewById(R.id.welocme_view);
	}
	public void init(){
		ActivityCollector.addActivity(this);
		imageview.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent  =new Intent(context,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2000);
		
	}
}

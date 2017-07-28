package com.chat.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import com.ben.chat.R;
import com.chat.bean.Message;
import com.chat.bean.MessageContent;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.GetUserInformation;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SendPictureActivity extends Activity{
	
	private Context context = null;
	private ImageView sendPictureImageView = null;
	private ImageView backImageView = null;
	private ImageView sendImageView = null;
	private LinearLayout titleRelativeLayout = null;
	private LinearLayout bottomRelativeLayout = null;
	private Message message = null;
	private boolean isVisible = true;
	private String uri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.send_picture_layout);
		init();
	}
	
	@SuppressLint("NewApi")
	public void init(){
		context = this;
		ActivityCollector.addActivity(this);
		Intent intent = getIntent();
		uri = intent.getStringExtra("uri");
		System.out.println(uri);
		sendPictureImageView = (ImageView) findViewById(R.id.send_picture);
		sendPictureImageView.setOnClickListener(sendPictureImageViewOnClickListener);
		titleRelativeLayout = (LinearLayout) findViewById(R.id.title);
		bottomRelativeLayout = (LinearLayout) findViewById(R.id.bottom);
		backImageView = (ImageView) findViewById(R.id.back);
		backImageView.setOnClickListener(backImageViewOnClickListener);
		sendImageView = (ImageView) findViewById(R.id.send);
		sendImageView.setOnClickListener(sendImageViewOnClickListener);
		FileInputStream fis;
		try {
			fis = new FileInputStream(uri);
			Bitmap bitmap=BitmapFactory.decodeStream(fis);
			sendPictureImageView.setImageBitmap(bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private OnClickListener sendPictureImageViewOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(isVisible){
				titleRelativeLayout.setVisibility(View.GONE);
				bottomRelativeLayout.setVisibility(View.GONE);
				isVisible = false;
			}else{
				titleRelativeLayout.setVisibility(View.VISIBLE);
				bottomRelativeLayout.setVisibility(View.VISIBLE);
				isVisible = true;
			}
		}
	};
	
	private OnClickListener backImageViewOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	private OnClickListener sendImageViewOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			android.os.Message msg = new android.os.Message();
			msg.what = 2;
			msg.obj = uri;
			SendMessageActivity.handler.sendMessage(msg);
			finish();
		}
	};
	
}

package com.chat.activity;

import com.ben.chat.R;
import com.chat.util.ActivityCollector;
import com.chat.util.GetResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneConfirmActivity2 extends Activity{
	private Context context = null;
	private Button check = null;
	private TextView checkEditText = null;
	private String checkNum = null;
	private String emailNum = null;
	private TextView tvPhone = null;
	private EditText yanzhengma = null;
	private Button back = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.phone_confirm2);
		init();
	}
	
	public void init(){
		ActivityCollector.addActivity(this);
		context = this;
		checkEditText = (TextView) findViewById(R.id.tvPhone);
		Intent intent = getIntent();
		emailNum = intent.getStringExtra("email");
		yanzhengma = (EditText) findViewById(R.id.yanzhengma);
		checkEditText.setText(emailNum);
		check = (Button) findViewById(R.id.tvNext);
		check.setOnClickListener(checkOnClickListener);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(backOnClickListener);
	}
	
	private OnClickListener checkOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			checkNum = yanzhengma.getText().toString().trim();
			System.out.println(GetResult.getYanZhengMa());
			System.out.println(checkNum);
			if(GetResult.getYanZhengMa().equals(checkNum)){
				GetResult.setYanZhengMa("");
				Intent intent = new Intent(PhoneConfirmActivity2.this,PhoneConfirmActivity3.class);
				intent.putExtra("email", emailNum);
				startActivity(intent);
			}else{
				Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
			}
		}
	};
	private OnClickListener backOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			PhoneConfirmActivity2.this.finish();
		}
	};
}

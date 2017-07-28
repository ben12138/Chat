package com.chat.fragment;

import com.ben.chat.R;
import com.chat.activity.MainActivity;
import com.chat.util.GetUserInformation;
import com.chat.util.Utils;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BodyMessageFrament extends Fragment{
	
	private ImageView headImageImageView = null;
	public static Handler handler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main_message_fragment, container, false);
		headImageImageView = (ImageView) view.findViewById(R.id.message_head_image);
		System.out.println(GetUserInformation.getU());
		headImageImageView.setImageBitmap(Utils.getBitmap(GetUserInformation.getU().getPhoto()));
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == 3){
					headImageImageView.setImageBitmap(Utils.getBitmap(GetUserInformation.getU().getPhoto(),2));
				}
			}
		};
		return view;
	}
}

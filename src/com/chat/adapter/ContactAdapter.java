package com.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ben.chat.R;
import com.chat.bean.FriendList;
import com.chat.bean.UserInformation;
import com.chat.util.Utils;

public class ContactAdapter extends ArrayAdapter<FriendList>{
	
	private int resourceId;
	private List<FriendList> contactList;
	
	public ContactAdapter(Context context,int textViewResourceId, List<FriendList> contactList) {
		super(context, textViewResourceId, contactList);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		FriendList friend = getItem(position);
		UserInformation u = friend.getU();
		View view = null;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.headImageImageView = (ImageView)view.findViewById(R.id.head_image1);
			viewHolder.remarkTextView = (TextView) view.findViewById(R.id.remark);
			viewHolder.introductionImageView = (TextView) view.findViewById(R.id.introduction);
			viewHolder.isOnlineImageView = (ImageView) view.findViewById(R.id.is_online);
			viewHolder.isOnlineTextView = (TextView) view.findViewById(R.id.is_online_text_view);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		viewHolder.headImageImageView.setImageBitmap(Utils.getBitmap(u.getPhoto()));
		if(friend.getRemark() == null){
			viewHolder.remarkTextView.setText(u.getName().toString());
		}else{
			viewHolder.remarkTextView.setText(friend.getRemark().toString());
		}
		viewHolder.introductionImageView.setText(u.getIntroduction().toString());
		System.out.println("online------>"+u.getIsOnline());
		if(u.getIsOnline() == 0){
			viewHolder.isOnlineImageView.setImageResource(R.drawable.is_online);
			viewHolder.isOnlineTextView.setText("在线");
		}else if(u.getIsOnline() == 1){
			viewHolder.isOnlineImageView.setImageResource(R.drawable.not_online);
			viewHolder.isOnlineTextView.setText("离线");
		}
		return view;
	}
	
	class ViewHolder{
		ImageView headImageImageView;
		TextView remarkTextView;
		TextView introductionImageView;
		ImageView isOnlineImageView;
		TextView isOnlineTextView;
	}
	
	public void upDateItem(int position,ListView listview,FriendList friend,int type){
		int visibleFirstItemPosition = listview.getFirstVisiblePosition();
		int visibleLastItemPosition = listview.getLastVisiblePosition();
		switch(type){
		case 0:
			if(position>=visibleFirstItemPosition&&position<=visibleLastItemPosition){
				View view = listview.getChildAt(position - visibleFirstItemPosition);
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				viewHolder.isOnlineTextView.setText(friend.getRemark());
			}
			break;
		case 1://����
			if(position>=visibleFirstItemPosition&&position<=visibleLastItemPosition){
				View view = listview.getChildAt(position - visibleFirstItemPosition);
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				viewHolder.isOnlineImageView.setImageResource(R.drawable.is_online);
				viewHolder.isOnlineTextView.setText("在线");
			}
			break;
		case 2://����
			if(position>=visibleFirstItemPosition&&position<=visibleLastItemPosition){
				View view = listview.getChildAt(position - visibleFirstItemPosition);
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				viewHolder.isOnlineImageView.setImageResource(R.drawable.not_online);
				viewHolder.isOnlineTextView.setText("离线");
			}
			break;
		}
	}
     
}

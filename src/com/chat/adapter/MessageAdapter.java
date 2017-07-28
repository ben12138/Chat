package com.chat.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ben.chat.R;
import com.chat.activity.SendMessageActivity;
import com.chat.adapter.ContactAdapter.ViewHolder;
import com.chat.bean.FriendList;
import com.chat.bean.Message;
import com.chat.bean.MessageContent;
import com.chat.network.NetService;
import com.chat.util.GetUserInformation;
import com.chat.util.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageAdapter extends ArrayAdapter<Message>{
	
	public static Handler handler = null;
	private NetService netService = null;
	private int resourceId;
	private Context context = null;
	
	public MessageAdapter(Context context,int textViewResourceId, List<Message> messageList) {
		super(context, textViewResourceId, messageList);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
		this.context = context;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Message msg = getItem(position);
		View view;
		final ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.getTimeTextView = (TextView) view.findViewById(R.id.get_message_time);
			viewHolder.receiveTimeTextView = (TextView) view.findViewById(R.id.receive_message_time);
			viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
			viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
			viewHolder.receiverHeadImageImageView = (ImageView) view.findViewById(R.id.receiver_head_image);
			viewHolder.receiverMessageContentTextView = (TextView) view.findViewById(R.id.receiver_message_content);
			viewHolder.myHeadImageImageView = (ImageView) view.findViewById(R.id.my_head_image);
			viewHolder.myHeadImageImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			viewHolder.sendMessageContentTextView = (TextView) view.findViewById(R.id.send_message_content);
			viewHolder.sendFailImageView = (ImageView) view.findViewById(R.id.send_fail);
			viewHolder.sendFailImageView.setVisibility(View.GONE);
			viewHolder.isReadImageView = (ImageView) view.findViewById(R.id.is_read);
			viewHolder.sendMessageContentImageView = (ImageView) view.findViewById(R.id.send_message_content_image_view);
			viewHolder.receiverMessageContentImageView = (ImageView) view.findViewById(R.id.receiver_message_content_image_view);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		handler = new Handler(){
			@Override
			public void handleMessage(final android.os.Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == 0){
					viewHolder.isReadImageView.setVisibility(View.GONE);
					viewHolder.sendFailImageView.setVisibility(View.VISIBLE);
					viewHolder.sendFailImageView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							final Message message = (Message) msg.obj;
							final int position = msg.arg1;
							new AsyncTask<Void, Void, Integer>() {
								@Override
								protected Integer doInBackground(Void... arg0) {
									// TODO Auto-generated method stub
									netService = NetService.getInstance();
									netService.closeConnection();
									netService.setConnection(getContext(), handler);
									if(!netService.isConnected()){
										return 0;
									}
									netService.send(message);
									return 1;
								}
								
								@Override
								protected void onPostExecute(Integer result) {
									// TODO Auto-generated method stub
									super.onPostExecute(result);
									if(result == 0){
										android.os.Message msg = new android.os.Message();
										msg.what=0;
										msg.obj  = message;
										msg.arg1 = position;
										MessageAdapter.handler.sendMessage(msg);
										viewHolder.isReadImageView.setVisibility(View.GONE);
									}else if(result == 1){
										viewHolder.sendFailImageView.setVisibility(View.GONE);
										viewHolder.isReadImageView.setVisibility(View.VISIBLE);
										viewHolder.isReadImageView.setImageResource(R.drawable.not_read);
									}
								}
								
							}.execute();
						}
					});
				}
			}
		};
		if(msg.getType() == Message.TYPE_RECEIVED){
			viewHolder.leftLayout.setVisibility(View.VISIBLE);
			viewHolder.rightLayout.setVisibility(View.GONE);
			SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
			String time = f.format(msg.getReceiveTime());
			viewHolder.receiveTimeTextView.setText(time);
			viewHolder.receiverHeadImageImageView.setImageBitmap(Utils.getBitmap(msg.getSender().getPhoto(), 4));
			if(msg.getContent().getType() == MessageContent.STRING){
				viewHolder.receiverMessageContentImageView.setVisibility(View.GONE);
				viewHolder.receiverMessageContentTextView.setVisibility(View.VISIBLE);
				String str = (String)msg.getContent().getContent();		
				System.out.println(str);
				String zhengze = "f0[0-9]{2}|f10[0-7]";											
				SpannableString spannableString = com.chat.util.ExpressionUtil.getExpressionString(context, str, zhengze);
				viewHolder.receiverMessageContentTextView.setText (spannableString);
			}else if(msg.getContent().getType() == MessageContent.FILE){
				viewHolder.receiverMessageContentTextView.setText("");
			}else if(msg.getContent().getType() == MessageContent.PICTURE){
				final Bitmap bitmap = Utils.getBitmap((byte[])msg.getContent().getContent(), 2);
				System.out.println("收到图片");
				viewHolder.receiverMessageContentTextView.setVisibility(View.GONE);
				viewHolder.receiverMessageContentImageView.setVisibility(View.VISIBLE);
				viewHolder.receiverMessageContentImageView.setImageBitmap(bitmap);
				viewHolder.receiverMessageContentImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
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
				});
			}else if(msg.getContent().getType() == MessageContent.VOICE){
				viewHolder.receiverMessageContentTextView.setText("");
			}else if(msg.getContent().getType() == MessageContent.OTHER){
				viewHolder.sendMessageContentTextView.setText("");
			}
		}else if(msg.getType() == Message.TYPE_SEND){
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);
			SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			String time = f.format(msg.getSendTime());
			viewHolder.getTimeTextView.setText(time);
			viewHolder.myHeadImageImageView.setImageBitmap(Utils.getBitmap(GetUserInformation.getU().getPhoto(), 2));
			if(msg.getContent().getType() == MessageContent.STRING){
				viewHolder.sendMessageContentTextView.setVisibility(View.VISIBLE);
				viewHolder.sendMessageContentImageView.setVisibility(View.GONE);
				String str = (String)msg.getContent().getContent();														
				String zhengze = "f0[0-9]{2}|f10[0-7]";											
				SpannableString spannableString = com.chat.util.ExpressionUtil.getExpressionString(context, str, zhengze);
				viewHolder.sendMessageContentTextView.setText(spannableString);
			}else if(msg.getContent().getType() == MessageContent.FILE){
				viewHolder.sendMessageContentTextView.setText("");
			}else if(msg.getContent().getType() == MessageContent.PICTURE){
				final Bitmap bitmap = Utils.getBitmap((byte[])msg.getContent().getContent(), 2);
				viewHolder.sendMessageContentImageView.setVisibility(View.VISIBLE);
				viewHolder.sendMessageContentTextView.setVisibility(View.GONE);
				viewHolder.sendMessageContentImageView.setImageBitmap(bitmap);
				viewHolder.sendMessageContentImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
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
				});
			}else if(msg.getContent().getType() == MessageContent.VOICE){
//				LayoutParams para = (LayoutParams) viewHolder.sendMessageContentImageView.getLayoutParams();
//				para.height = 64;
//				para.width = 100;
//				viewHolder.sendMessageContentImageView.setLayoutParams(para);
				viewHolder.sendMessageContentImageView.setImageResource(R.drawable.send_voice);
				viewHolder.sendMessageContentImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							if(viewHolder.mplayer == null){
								viewHolder.mplayer = new MediaPlayer();
								viewHolder.mplayer.setDataSource(msg.getContent().getFileName()+"");
								viewHolder.mplayer.prepare();
								viewHolder.mplayer.start();
								viewHolder.mplayer = null;
							}else{
								if(viewHolder.mplayer.isPlaying()){
									viewHolder.mplayer.stop();
									viewHolder.mplayer.release();
								}
							}
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}else if(msg.getContent().getType() == MessageContent.OTHER){
				viewHolder.sendMessageContentTextView.setText("");
			}
			if(msg.getIsRead() == 0){
				viewHolder.isReadImageView.setImageResource(R.drawable.not_read);
			}else if(msg.getIsRead() == 1){
				viewHolder.isReadImageView.setImageResource(R.drawable.is_read);
			}
		}
		return view;
	}
	
	class ViewHolder{
		LinearLayout leftLayout;
		LinearLayout rightLayout;
		ImageView receiverHeadImageImageView;
		ImageView myHeadImageImageView;
		TextView receiverMessageContentTextView;
		TextView sendMessageContentTextView;
		ImageView sendFailImageView;
		ImageView isReadImageView;
		TextView getTimeTextView;
		TextView receiveTimeTextView;
		ImageView sendMessageContentImageView;
		ImageView receiverMessageContentImageView;
		MediaPlayer mplayer = null;
	}
	//����listview���ɵ��
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void upDateItem(int position,ListView listview,int type){
		int visibleFirstItemPosition = listview.getFirstVisiblePosition();
		int visibleLastItemPosition = listview.getLastVisiblePosition();
		if(type == 0){
			if(position>=visibleFirstItemPosition&&position<=visibleLastItemPosition){
				View view = listview.getChildAt(position - visibleFirstItemPosition);
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				viewHolder.isReadImageView.setImageResource(R.drawable.is_read);
			}
		}
	}
	
}

package com.chat.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ben.chat.R;
import com.chat.adapter.MessageAdapter;
import com.chat.bean.FriendList;
import com.chat.bean.IsRead;
import com.chat.bean.Message;
import com.chat.bean.MessageContent;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.FileUtil;
import com.chat.util.GetUserInformation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessageActivity extends Activity{
	
	private Context context = null;
	private ListView messageListView = null;
	private List<Message> messageList = null;
	private MessageAdapter messageAdapter = null;
	private EditText messageEditText = null;
	private ImageView moreButtonImageView = null;
	private ImageView voiceImageView = null;
	private ImageView moreInformationImageView = null;
	private Button backButton = null;
	private TextView nameTextView = null;
	private Button sendButton = null;
	private String online = null;
	private Message message = null;
	private NetService netService = null;
	private FriendList friend = null;
	public static Handler handler = null;
	public static final int SEND_FAIL = 0;
	private IsRead ir = null;
	public static SendMessageActivity activity;
	private LinearLayout bottomLinearLayout = null;
	private Button voiceButton = null;
	private Button cancelButton = null;
	private Button okButton = null;
	private TextView timeTextView = null;
	private ImageView voiceImageView2 = null;
	//存放音量的许多张图片
	private Drawable[] micImages;
	private MediaRecorder recorder = null;
	//sd卡的存储路径
	private String output_path = Environment.getExternalStorageDirectory()+ "/chat/voice/";
	//录音文件
	private File soundFile;
	//录音文件名
	private String voiceFileName = null;
	//录音开始时间
	private Date startTime = null;
	//录音结束时间
	private Date endTime = null;
	private int BASE = 60;
	private int SPACE = 1000;//间隔取样时间
	private int TIME = 60;
	private boolean isStart = true;
	private Runnable rUpDate = new Runnable(){
		private int time = 0;
        public void run() {  
        	while(++time<=60&&isStart){
        		android.os.Message msg = new android.os.Message();
				msg.what = 5;
				handler.sendMessage(msg);
        		try {
					Thread.sleep(SPACE);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		upDateMicStatus(); 
        	}
        	TIME = 60;
        	time = 0;
			android.os.Message msg = new android.os.Message();
			msg.what = 4;
			handler.sendMessage(msg);
			endTime = new Date();
			if(soundFile != null && soundFile.exists()&& recorder != null){
				//停止录音
				recorder.stop();
				//释放资源
				recorder.release();
				recorder = null;
			}
        } 
	};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.send_message_layout);
		init();
	}
	
	public void init(){
		File voiceFile = new File(output_path);
		if(!voiceFile.exists()){
			voiceFile.mkdirs();
		}
		activity = this;
		ActivityCollector.addActivity(this);
		context = this;
		ir = new IsRead();
		Intent intent = getIntent();
		friend = (FriendList) intent.getSerializableExtra("contact");
		if(friend.getU().getIsOnline() == 0){
			online = "(在线)";
		}else if(friend.getU().getIsOnline() == 1){
			online = "(离线)";
		}
		backButton = (Button) findViewById(R.id.back);
		nameTextView = (TextView) findViewById(R.id.user_name);
		if(friend.getRemark().equals("")){
			nameTextView.setText(friend.getU().getNickname()+online);
		}else {
			nameTextView.setText(friend.getRemark()+online);
		}
		moreInformationImageView = (ImageView) findViewById(R.id.more);
		moreInformationImageView.setOnClickListener(moreInformationImageViewOnClickListener);
		voiceImageView = (ImageView) findViewById(R.id.voice);
		voiceImageView.setOnClickListener(voiceImageViewOnClickListener);
		moreButtonImageView = (ImageView) findViewById(R.id.more_button);
		moreButtonImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					showMoreButton();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		messageEditText = (EditText) findViewById(R.id.message_edit_text);
		messageListView = (ListView) findViewById(R.id.message_list_view);
		sendButton = (Button) findViewById(R.id.send);
		messageList = new ArrayList<Message>();
		messageAdapter = new MessageAdapter(context, R.layout.message_item, messageList);
		messageListView.setAdapter(messageAdapter);
		sendButton.setOnClickListener(sendButtonOnClickListener);
		handler = new Handler(){
			@Override
			public void handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what == 0){
					//发送消息
					Message message = (Message) msg.obj;
					if(friend.getFriendName().equals(message.getSender().getName())){
						messageList.add(message);
						messageAdapter.notifyDataSetChanged();
						messageListView.setSelection(messageList.size());
						for(int i=0;i<messageList.size();i++){
							if(messageList.get(i).getType() == Message.TYPE_SEND){
								messageList.get(i).setIsRead(1);
								messageList.get(i).setReceiveTime(new Date(message.getReceiveTime().getTime()));
								messageList.get(i).setSendTime(new Date(message.getSendTime().getTime()));
								messageAdapter.upDateItem(i, messageListView, 0);
							}
						}
						Intent intent = new Intent("com.chat.broadcast.RECEIVE_MUSIC_BROADCAST");
						sendBroadcast(intent);
					}else{
						
					}
				}else if( msg.what == 1){
					//是否已读
					IsRead ir = (IsRead) msg.obj;
					if(GetUserInformation.getU().getName().equals(ir.getSenderName())){
						for(int i=0;i<messageList.size();i++){
							if(messageList.get(i).getType() == Message.TYPE_SEND){
								messageList.get(i).setIsRead(1);
								messageAdapter.upDateItem(i, messageListView, 0);
							}
						}
					}
				}else if(msg.what == 2){
					//发送图片
					MessageContent messageContent = new MessageContent();
					String uri = (String) msg.obj;
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(uri);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Bitmap bitmap=BitmapFactory.decodeStream(fis);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
					byte[] photo = baos.toByteArray();
					messageContent.setContent(photo);
					messageContent.setType(MessageContent.PICTURE);
					message = new Message();
					message.setContent(messageContent);
					message.setSendTime(new Date());
					message.setReceiveTime(new Date());
					message.setType(Message.TYPE_SEND);		
					message.setSender(GetUserInformation.getU());
					message.setReceiver(friend.getU());
					message.setIsRead(0);//0��ʾδ����1��ʾ�Ѷ�
					messageList.add(message);
//					messageAdapter.notifyDataSetChanged();
					SendMessageActivity.this.sendMessage(message,messageList.size());
				}else if(msg.what == 3){
					//录音喇叭变化
					voiceImageView2.setImageDrawable(micImages[msg.arg1]);
				}else if(msg.what == 4){
					voiceButton.setVisibility(View.GONE);
					voiceButton.setBackgroundResource(R.drawable.voice_normal);
					voiceImageView2.setImageDrawable(micImages[0]);
				}else if(msg.what == 5){
					if(TIME <= 10){
						timeTextView.setText("0:0"+(--TIME));
					}else{
						timeTextView.setText("0:"+(--TIME));
					}
				}
			}
		};
	}
	
	public void showMoreButton() throws NoSuchFieldException, NumberFormatException, IllegalAccessException, IllegalArgumentException{
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.send_more_files_pop_window,null,false);
		PopupWindow window = new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
		window.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.showAtLocation(findViewById(R.id.bottom),Gravity.BOTTOM, 0, 0);//ס
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		GridView expressionsGridView = (GridView) view.findViewById(R.id.expressions);
		final int[] imageIds = new int[107];
		for(int i=0;i<107;i++){
			if(i<10){
				Field field = R.drawable.class.getDeclaredField("f00" + i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			}else if(i<100){
				Field field = R.drawable.class.getDeclaredField("f0" + i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			}else{
				Field field = R.drawable.class.getDeclaredField("f" + i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			}
		}
		List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<imageIds.length;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", imageIds[i]);
			data_list.add(map);
		}
		String[]  from={"image"};
		int[] to={R.id.express};
		SimpleAdapter adapter = new SimpleAdapter(context, data_list, R.layout.espression_item,from,to);
		expressionsGridView.setAdapter(adapter);
		expressionsGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				// TODO Auto-generated method stub
				String str;
				if(id<10){
					str = "f00"+id;
				}else if(id<100){
					str = "f0"+id;
				}else{
					str = "f"+id;
				}
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(), imageIds[id % imageIds.length]);
				ImageSpan imageSpan = new ImageSpan(context, bitmap);
				SpannableString spannableString = new SpannableString(str);
				spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				messageEditText.append(spannableString);
			}
		});
		
		LinearLayout pictureLinearLayout = (LinearLayout) view.findViewById(R.id.picture);
		pictureLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,ChoosePhotoActivity.class);
				startActivity(intent);
			}
		});
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				
			}
		});
	}
	
	private OnClickListener sendButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			MessageContent messageContent = new MessageContent();
			if(!messageEditText.getText().toString().equals("")){
				ir.setSenderName(GetUserInformation.getU().getName());
				ir.setReceiverName(friend.getU().getName());
				messageContent.setContent(messageEditText.getText().toString());
				messageContent.setType(MessageContent.STRING);
				message = new Message();
				message.setContent(messageContent);
				message.setSendTime(new Date());
				message.setReceiveTime(new Date());
				message.setType(Message.TYPE_SEND);		
				message.setSender(GetUserInformation.getU());
				message.setReceiver(friend.getU());
				message.setIsRead(0);//0��ʾδ����1��ʾ�Ѷ�
				messageList.add(message);
//				messageAdapter.notifyDataSetChanged();
				sendMessage(message,messageList.size());
				messageListView.setSelection(messageList.size());
				messageEditText.setText("");
			}else{
				Toast.makeText(context,"输入信息不能为空", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	public void sendMessage(final Message message,final int position){
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				netService = NetService.getInstance();
				netService.closeConnection();
				netService.setConnection(context, handler);
				if(!netService.isConnected()){
					return 0;
				}
				netService.send(message);
				netService.send(ir);
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
				}else if(result == 1){
					
				}
			}
			
		}.execute();
	}
	
	private OnClickListener moreInformationImageViewOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context,ContactInformationActivity.class);
			intent.putExtra("contact", friend);
			startActivity(intent);
		}
	};
	
	private OnClickListener voiceImageViewOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showGetVoice();
		}
	};
	
	public void showGetVoice(){
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.get_voice_layout,null,false);
		timeTextView = (TextView) view.findViewById(R.id.time);
		bottomLinearLayout = (LinearLayout) view.findViewById(R.id.bottom);
		voiceImageView2 = (ImageView) view.findViewById(R.id.voice_image_view);
		cancelButton = (Button) view.findViewById(R.id.cancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				voiceButton.setVisibility(View.VISIBLE);
				if(soundFile.exists()){
					soundFile.delete();
				}
			}
		});
		okButton = (Button) view.findViewById(R.id.ok);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MessageContent messageContent = new MessageContent();
				voiceButton.setVisibility(View.VISIBLE);
				ir.setSenderName(GetUserInformation.getU().getName());
				ir.setReceiverName(friend.getU().getName());
				byte[] voice = FileUtil.getVoiceBytes(soundFile.getAbsolutePath());
				messageContent.setContent(voice);
				messageContent.setType(MessageContent.VOICE);
				messageContent.setFileName(soundFile.getAbsolutePath());
				message = new Message();
				message.setContent(messageContent);
				message.setSendTime(new Date());
				message.setReceiveTime(new Date());
				message.setType(Message.TYPE_SEND);		
				message.setSender(GetUserInformation.getU());
				message.setReceiver(friend.getU());
				message.setIsRead(0);
				messageList.add(message);
//				messageAdapter.notifyDataSetChanged();
				//sendMessage(message,messageList.size());
				messageListView.setSelection(messageList.size());
			}
		});
		voiceButton = (Button) view.findViewById(R.id.voice_btn);
		voiceButton.setOnTouchListener(new PressToSpeak());
		PopupWindow window = new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
		window.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.showAtLocation(findViewById(R.id.bottom),Gravity.BOTTOM, 0, 0);//ס
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		micImages = new Drawable[]{
				getResources().getDrawable(R.drawable.voice_0),
				getResources().getDrawable(R.drawable.voice_1),
				getResources().getDrawable(R.drawable.voice_2),
				getResources().getDrawable(R.drawable.voice_3),
				getResources().getDrawable(R.drawable.voice_4),
				getResources().getDrawable(R.drawable.voice_5),
				getResources().getDrawable(R.drawable.voice_6),
				getResources().getDrawable(R.drawable.voice_7)
		};
		
	}
	
	class PressToSpeak implements View.OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isStart = true;
				startTime = new Date();
				voiceFileName = output_path+new Date().getTime()+".amr";
				if(!isExitSDcard()){
					Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_SHORT).show();
					return false;
				}
				if(recorder != null){
					recorder.stop();
					recorder.release();
					recorder = null;
				}
				voiceButton.setBackgroundResource(R.drawable.voice_pressed);
				voiceButton.setText("请说话");
				new Thread(rUpDate).start();
				soundFile = new File(output_path+new Date().getTime()+".amr");
				recorder = new MediaRecorder();
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//声音来源是话筒
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//设置格式
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置解码方式
                recorder.setOutputFile(soundFile.getAbsolutePath());
                try {
					recorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                recorder.start();
                upDateMicStatus();
                return true;
                
			case MotionEvent.ACTION_UP:
				isStart = false;
				endTime = new Date();
				if(soundFile != null && soundFile.exists() && recorder != null){
					//停止录音
					recorder.stop();
					//释放资源
					recorder.release();
					recorder = null;
				}
				timeTextView.setText("1分钟");
				voiceButton.setBackgroundResource(R.drawable.voice_normal);
				voiceButton.setText("按住说话");
				voiceImageView2.setImageDrawable(micImages[0]);
				voiceButton.setVisibility(View.GONE);
				return true;
                
			default:
				break;
			}
			return false;
		}
	}
	
	//检测是否存在sd卡
	public boolean isExitSDcard(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		return false;
	}
	
	private void upDateMicStatus(){
		if(recorder != null){
			double ratio = (double)recorder.getMaxAmplitude();
			int db = 0;//分贝
			if(ratio > 1){
				db = (int) (20*Math.log10(ratio));
				int level = db/12;
				android.os.Message msg = new android.os.Message();
				msg.what = 3;
				msg.arg1 = level;
				handler.sendMessage(msg);
			}
		}
	}
	
}

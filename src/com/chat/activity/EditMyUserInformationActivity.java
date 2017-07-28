package com.chat.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import com.ben.chat.R;
import com.chat.activity.PhoneConfirmActivity3.SetDateDialog;
import com.chat.bean.UserInformation;
import com.chat.fragment.BodyContactFrament;
import com.chat.fragment.BodyDynamicStateFrament;
import com.chat.fragment.BodyMessageFrament;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.GetUserInformation;
import com.chat.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditMyUserInformationActivity extends Activity{
	
	private Context context = null;
	private Button backButton = null;
	private Button editButton = null;
	private Button cancelButton = null;
	private Button okButton = null;
	private ImageView headImageImageView = null;
	private LinearLayout xiangce = null;
	private LinearLayout camera = null;
	private TextView userNameTextView = null;
	private EditText nickNameEditText = null;
	private TextView sexTextView = null;
	private TextView birthdayTextView = null;
	private EditText schoolEditText = null;
	private EditText introductionEditText = null;
	private Bitmap bitmap = null;
	private UserInformation u = null;
	protected static final int CHOOSE_PICTURE = 0;
	protected static final int TAKE_PICTURE = 1;
	private static final int CROP_SMALL_PICTURE = 2;
	private Uri imageUri;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private NetService netService = null;
	private Handler handler = new Handler();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_my_userinformation_layout);
		init();
	}
	
	public void init(){
		System.out.println("init........");
		context = this;
		ActivityCollector.addActivity(this);
		u = new UserInformation();
		u.setBirthday(GetUserInformation.getU().getBirthday());
		u.setIntroduction(GetUserInformation.getU().getIntroduction());
		u.setIsOnline(GetUserInformation.getU().getIsOnline());
		u.setName(GetUserInformation.getU().getName());
		u.setNickname(GetUserInformation.getU().getNickname());
		u.setPhoto(GetUserInformation.getU().getPhoto());
		u.setSchool(GetUserInformation.getU().getSchool());
		u.setSex(GetUserInformation.getU().getSex());
		ActivityCollector.addActivity(this);
		backButton = (Button) findViewById(R.id.back);
		backButton.setOnClickListener(backButtonOnClickListener);
		cancelButton = (Button) findViewById(R.id.cancel);
		cancelButton.setOnClickListener(backButtonOnClickListener);
		editButton = (Button) findViewById(R.id.edit);
		editButton.setOnClickListener(editButtonOnClickListener);
		okButton = (Button) findViewById(R.id.ok);
		okButton.setOnClickListener(okButtonOnClickListener);
		headImageImageView = (ImageView) findViewById(R.id.head_image);
		bitmap = Utils.getBitmap(GetUserInformation.getU().getPhoto(), 1);
		headImageImageView.setImageBitmap(bitmap);
		headImageImageView.setOnClickListener(headImageImageViewOnClickListener);
		xiangce = (LinearLayout) findViewById(R.id.xiangce);
		xiangce.setOnClickListener(xiangceOnClickListener);
		xiangce.setClickable(false);
		camera = (LinearLayout) findViewById(R.id.camera);
		camera.setOnClickListener(cameraOnClikListener);
		camera.setClickable(false);
		userNameTextView = (TextView) findViewById(R.id.user_name);
		userNameTextView.setText(u.getName());
		nickNameEditText = (EditText) findViewById(R.id.nick_name);
		nickNameEditText.setText(u.getNickname());
		nickNameEditText.setEnabled(false);
		sexTextView = (TextView) findViewById(R.id.sex);
		if(u.getSex() == 0){
			sexTextView.setText("男");
		}else if(u.getSex() == 1){
			sexTextView.setText("女");
		}
		sexTextView.setOnClickListener(sexTextViewOnClickListener);
		sexTextView.setClickable(false);
		birthdayTextView = (TextView) findViewById(R.id.birthday);
		birthdayTextView.setText(u.getBirthday());
		birthdayTextView.setOnClickListener(birthdayOnClickListener);
		birthdayTextView.setClickable(false);
		schoolEditText = (EditText) findViewById(R.id.school);
		schoolEditText.setText(u.getIntroduction());
		schoolEditText.setEnabled(false);
		introductionEditText = (EditText) findViewById(R.id.introduction);
		introductionEditText.setText(u.getIntroduction());
		introductionEditText.setEnabled(false);
	}
	
	class SetDateDialog extends DialogFragment implements OnDateSetListener {

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dpd = new DatePickerDialog(getActivity(), this,
					year, month, day);
			return dpd;
		}

		@Override
		public void onDateSet(DatePicker v, int year, int month, int day) {
			// TODO Auto-generated method stub
			birthdayTextView
					.setText(year + "年" + (month + 1) + "月" + day + "日");
			u.setBirthday(year + "年" + (month + 1) + "月" + day + "日");
		}

	}
	
	private OnClickListener backButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	private OnClickListener editButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			camera.setClickable(true);
			xiangce.setClickable(true);
			nickNameEditText.setEnabled(true);
			sexTextView.setClickable(true);
			birthdayTextView.setClickable(true);
			introductionEditText.setEnabled(true);
			schoolEditText.setEnabled(true);
		}
	};
	
	private OnClickListener headImageImageViewOnClickListener = new OnClickListener() {
		
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
	};
	
	//ѡ�����
	private OnClickListener xiangceOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
			openAlbumIntent.setType("image/*");
			startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
		}
	};

	// ����

	private OnClickListener cameraOnClikListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			imageUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "image.jpg"));
			// ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(openCameraIntent, TAKE_PICTURE);
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if(imageUri!=null){
				startPhotoZoom(imageUri); // ��ʼ��ͼƬ���вü�����
			}
			break;
		case CHOOSE_PICTURE:
			if(data!=null){
				startPhotoZoom(data.getData()); // ��ʼ��ͼƬ���вü�����
			}
			break;
		case CROP_SMALL_PICTURE:
			if (data != null) {
				setImageToView(data); // �øղ�ѡ��ü��õ���ͼƬ��ʾ�ڽ�����
			}
			break;
		default:
			break;

		}
	};

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	protected void startPhotoZoom(Uri uri) {
		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
		}
		imageUri = uri;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// ���òü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_SMALL_PICTURE);
	}

	/**
	 * ����ü�֮���ͼƬ���
	 * 
	 * @param
	 * 
	 * @param picdata
	 */
	protected void setImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			photo = Utils.toRoundBitmap(photo, imageUri); // ���ʱ���ͼƬ�Ѿ��������Բ�ε���
			headImageImageView.setImageBitmap(photo);
			photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
			u.setPhoto(baos.toByteArray());
		}
	}
	
	private OnClickListener sexTextViewOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("ѡ���Ա�");
			final String str[] = { "��", "Ů" };
			builder.setItems(str, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0:
						u.setSex(0);
						sexTextView.setText("��");
						break;
					case 1:
						u.setSex(1);
						sexTextView.setText("Ů");
						break;
					}
				}
			});
			builder.show();
		}
	};
	
	private OnClickListener birthdayOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			SetDateDialog s = new SetDateDialog();
			s.show(getFragmentManager(), "选择日期");
		}
	};
	
	private OnClickListener okButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(isRight()){
				u.setNickname(nickNameEditText.getText().toString());
				u.setSchool(schoolEditText.getText().toString());
				u.setIntroduction(introductionEditText.getText().toString());
				trySubimt();
			}else{
				Toast.makeText(context, "请把信息填写完整", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	public boolean isRight(){
		if(u.getPhoto()!=null&&
				(u.getSex()==1||u.getSex()==0)&&
				u.getBirthday()!=null&&
				!nickNameEditText.getText().toString().equals("")){
			return true;
		}
		return false;
	}
	
	public void trySubimt(){
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				NetService netService = NetService.getInstance();
				netService.closeConnection();
				netService.setConnection(context,handler);
				if (!netService.isConnected()) {
					return 0;
				}
				u.setType(1);
				netService.send(u);
				return 1;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result == 0){
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_SHORT).show();
				}else if(result == 1){
					GetUserInformation.setU(u);
					Message msg = new Message();
					msg.what = 3;
					ContactInformationActivity.handler.sendMessage(msg);
					Message msg1 = new Message();
					msg1.what = 3;
					BodyContactFrament.handler2.sendMessage(msg1);
					Message msg2 = new Message();
					msg2.what = 3;
					BodyDynamicStateFrament.handler.sendMessage(msg2);
					Message msg3 = new Message();
					msg3.what = 3;
					BodyMessageFrament.handler.sendMessage(msg3);
					Message msg4 = new Message();
					msg4.what = 3;
					MainActivity.handler.sendMessage(msg4);
					finish();
				}
			}
			
		}.execute();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	
}

package com.chat.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.string;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ben.chat.R;
import com.chat.bean.UserInformation;
import com.chat.network.NetService;
import com.chat.util.ActivityCollector;
import com.chat.util.GetResult;
import com.chat.util.GetUserInformation;
import com.chat.util.Utils;

public class PhoneConfirmActivity3 extends Activity {

	private Context context = null;
	private TextView birthdayTextView = null;
	private Button submit = null;
	private EditText passwordTextView = null;
	private EditText confirmPasswordTextView = null;
	private EditText nicknameTextView = null;
	private EditText schoolTextView = null;
	private EditText introductionTextView = null;
	private ImageView headImage = null;
	private LinearLayout xiangce = null;
	private LinearLayout camera = null;
	private TextView sexTextView = null;
	private String name = null;
	private String password = null;
	private String confirmPassword = null;
	private String nickname = null;
	private String school = null;
	private String introduction = null;
	private String birthday = null;
	private int sex = 0;
	private UserInformation u = null;
	protected static final int CHOOSE_PICTURE = 0;
	protected static final int TAKE_PICTURE = 1;
	private static final int CROP_SMALL_PICTURE = 2;
	private Uri imageUri;
	private byte[] image = null;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				Date date = new Date();
				SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
				String time = f.format(date);
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle("下线通知");
				dialog.setMessage("您的账号:"+GetUserInformation.getU().getName()+"在"+time+"异地登录，请尽快核实");
				dialog.setCancelable(false);
				dialog.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context,LoginActivity.class);
						startActivity(intent);
					}
				});
				dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						ActivityCollector.finishAll();
					}
				});
				dialog.show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.phone_confirm3);
		init();
	}

	public void init() {
		ActivityCollector.addActivity(this);
		context = this;
		birthdayTextView = (TextView) findViewById(R.id.birthday);
		birthdayTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetDateDialog s = new SetDateDialog();
				s.show(getFragmentManager(), "选择日期");
			}
		});
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(submitOnClickListener);
		Intent intent = getIntent();
		name = intent.getStringExtra("email");
		passwordTextView = (EditText) findViewById(R.id.password);
		confirmPasswordTextView = (EditText) findViewById(R.id.confirm_password);
		nicknameTextView = (EditText) findViewById(R.id.personal_name);
		sexTextView = (TextView) findViewById(R.id.sex);
		schoolTextView = (EditText) findViewById(R.id.school);
		introductionTextView = (EditText) findViewById(R.id.personal_introduction);
		sexTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("性别");
				final String str[] = { "男", "女" };
				builder.setItems(str, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							sex = 0;
							sexTextView.setText("男");
							break;
						case 1:
							sex = 1;
							sexTextView.setText("女");
							break;
						}
					}
				});
				builder.show();
			}
		});
		headImage = (ImageView) findViewById(R.id.head_image);
		xiangce = (LinearLayout) findViewById(R.id.xiangce);
		camera = (LinearLayout) findViewById(R.id.camera);
		xiangce.setOnClickListener(xiangceOnClickListener);
		camera.setOnClickListener(cameraOnClikListener);
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
			birthday = year + "年" + (month + 1) + "月" + day + "日";
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private OnClickListener submitOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			password = passwordTextView.getText().toString().trim();
			confirmPassword = confirmPasswordTextView.getText().toString()
					.trim();
			nickname = nicknameTextView.getText().toString().trim();
			school = schoolTextView.getText().toString().trim();
			introduction = introductionTextView.getText().toString().trim();
			u = new UserInformation();
			u.setName(name);
			u.setBirthday(birthday);
			u.setIntroduction(introduction);
			u.setPassword(confirmPassword);
			u.setSchool(school);
			u.setType(0);
			u.setSex(sex);
			u.setNickname(nickname);
			u.setPhoto(image);
			if (isRight(u)) {
				trySubmit();
			} else {
				Toast.makeText(context, "请将信息填写完整", Toast.LENGTH_SHORT).show();
			}

		}
	};

	public void trySubmit() {
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				Toast.makeText(context, "正在提交", Toast.LENGTH_SHORT).show();
			}

			@Override
			protected Integer doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				NetService netService = NetService.getInstance();
				//netService.closeConnection();
				netService.setConnection(context,handler);
				if (!netService.isConnected()) {
					return 0;
				}
				netService.send(u);
				while (!GetResult.isReceived());
				GetResult.setReceived(false);
				if (GetResult.getGetInformationType() == 1) {
					return 1;
				} else if (GetResult.getGetInformationType() == 0) {
					return 2;
				}
				return 0;
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == 1) {
					GetUserInformation.setU(u);
					Intent intent = new Intent(context,MainActivity.class);
					intent.putExtra("user", u);
					startActivity(intent);
				} else if (result == 0) {
					Toast.makeText(context, "糟糕，网络出现问题了", Toast.LENGTH_SHORT).show();
				} else if (result == 2) {
					Toast.makeText(context, "初始化失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}.execute();
	}

	public boolean isRight(UserInformation u) {
		if (password == null || confirmPassword == null
				|| !password.equals(confirmPassword) || birthday == null
				|| image == null) {
			System.out.println(password);
			System.out.println(confirmPassword);
			System.out.println(birthday);
			System.out.println(image);
			System.out.println("aaa");
			return false;
		}
		return true;
	}
	
	//ѡ�����
	private OnClickListener xiangceOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
			openAlbumIntent.setType("image/*");
			startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
		}
	};

	//����
	
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
			startPhotoZoom(imageUri); // ��ʼ��ͼƬ���вü�����
			break;
		case CHOOSE_PICTURE:
			startPhotoZoom(data.getData()); // ��ʼ��ͼƬ���вü�����
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
			headImage.setImageBitmap(photo);
			photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
			image = baos.toByteArray();
		}
	}
	
}

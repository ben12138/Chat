package com.chat.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.chat.activity.MainActivity;
import com.chat.activity.SendMessageActivity;
import com.chat.adapter.ContactAdapter;
import com.chat.bean.Comfirm;
import com.chat.bean.FindPassword;
import com.chat.bean.FriendList;
import com.chat.bean.GetPersonPassword;
import com.chat.bean.IsRead;
import com.chat.bean.LoginReturnType;
import com.chat.bean.MessageContent;
import com.chat.bean.MyFriendList;
import com.chat.bean.MyString;
import com.chat.bean.OnLineOffLine;
import com.chat.bean.SendType;
import com.chat.bean.User;
import com.chat.bean.UserInformation;
import com.chat.fragment.BodyContactFrament;
import com.chat.util.GetFriendList;
import com.chat.util.GetResult;
import com.chat.util.GetUserInformation;
import com.chat.util.PasswordGetResult;

public class ClientListenThread extends Thread{
	private Socket socket = null;
	private boolean isStart = true;
	private ObjectInputStream ois = null;
	private Handler handler;
	
	public ClientListenThread(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}
	
	public ClientListenThread(Socket socket,Handler handler) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.handler = handler;
	}
	public void close() {
		isStart = false;
	}
	public void run(){
		while(isStart){
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				Object obj = ois.readObject();
				if(obj instanceof LoginReturnType){
					LoginReturnType temp = (LoginReturnType)obj;
					switch(temp){
					case SUCCESS:GetResult.setlResult(LoginReturnType.SUCCESS);GetResult.setReceived(true);break;
					case ACCOUNT_ERROR:GetResult.setlResult(LoginReturnType.ACCOUNT_ERROR);GetResult.setReceived(true);break;
					case PASSWORD_ERROR:GetResult.setlResult(LoginReturnType.PASSWORD_ERROR);GetResult.setReceived(true);break;
					case ERROR:GetResult.setlResult(LoginReturnType.ERROR);GetResult.setReceived(true);break;
					case FORCE_OFFLINE:
						Message message = new Message();
						message.what = 1;
						if(MainActivity.handler!=null){
							MainActivity.handler.sendMessage(message);
						}
						break;
					}
				}
				if(obj instanceof Comfirm){
					Comfirm c = (Comfirm)obj;
					System.out.println(c.getYanZhengMa());
					if(c.getYanZhengMa().equals("0")){
						System.out.println("aaa");
						GetResult.setRegister(true);
					}else{
						System.out.println("bbb");
						GetResult.setRegister(false);
						System.out.println(GetResult.isRegister());
						GetResult.setYanZhengMa(c.getYanZhengMa());
					}
					GetResult.setGetEmailConfirm(true);
				}
				if(obj instanceof MyString){
					MyString str = (MyString)obj;
					if(str.getString().equals("InitSuccess")){
						GetResult.setGetInformationType(1);
					}else if(str.getString().equals("InitFail")){
						GetResult.setGetInformationType(0);
					}
					GetResult.setReceived(true);
				}
				if(obj instanceof FindPassword){
					FindPassword f = (FindPassword) obj;
					if(f.getType() == 1){
						PasswordGetResult.setPassword(f.getPassword());
						PasswordGetResult.setYanzhengma(f.getYanzhengma());
					}else if(f.getType() == 2){
						PasswordGetResult.setYanzhengma(null);
						PasswordGetResult.setPassword(null);
					}
					PasswordGetResult.setGetPasswordEmail(true);
				}
				if(obj instanceof GetPersonPassword){
					PasswordGetResult.setU(((GetPersonPassword)obj).getU());
					PasswordGetResult.setGetUserInformation(true);
				}
				if(obj instanceof UserInformation){
					GetUserInformation.setU(((UserInformation)obj));
					GetUserInformation.setGetUserInformation(true);
				}
				if(obj instanceof MyFriendList){
					List<FriendList> friends = ((MyFriendList)obj).getFriends();
					GetFriendList.setFriends(friends);
					GetFriendList.setGETFRIENDS(true);
				}
				if(obj instanceof com.chat.bean.Message){
					com.chat.bean.Message message = (com.chat.bean.Message)obj;
					if(message.getContent().getType() == MessageContent.STRING){
						Message msg = new Message();
						msg.what = 0;
						msg.obj = message;
						if(SendMessageActivity.handler!=null){
							SendMessageActivity.handler.sendMessage(msg);
						}else{
							
						}
					}
					
				}
				if(obj instanceof OnLineOffLine){
					OnLineOffLine o = (OnLineOffLine) obj;
					if(o.getType() == OnLineOffLine.TYPE_ONLINE){
						Message msg = new Message();
						msg.what = 0;
						msg.obj = o.getName();
						BodyContactFrament.handler2.sendMessage(msg);
					}else if(o.getType() == OnLineOffLine.TYPE_OFFLINE){
						Message msg = new Message();
						msg.what = 4;
						msg.obj = o.getName();
						BodyContactFrament.handler2.sendMessage(msg);
					}
				}
				if(obj instanceof IsRead){
					Message msg = new Message();
					msg.what = 1;
					msg.obj = (IsRead)obj;
					SendMessageActivity.handler.sendMessage(msg);
				}
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				close();
			}
			
		}
	}
}

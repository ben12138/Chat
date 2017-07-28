package com.chat.network;

import java.io.IOException;
import java.net.Socket;

import com.chat.bean.User;

import android.R.bool;
import android.content.Context;
import android.os.Handler;

public class NetService {
	private static NetService instance = null;
	private Socket clientSocket = null;
	private NetConnect netConnect = null;
	private boolean isConnected = false;
	private ClientListenThread clientListenThread = null;
	private ClientSendThread clientSendThread = null;
	private Context context = null;
	private Handler handle = null;
	
	private NetService() {
		// TODO Auto-generated constructor stub
	}
	public static NetService getInstance(){
		if(instance == null){
			instance = new NetService();
		}
		return instance;
	}
	
	public void setConnection(Context context,Handler handle){
		this.context = context;
		this.handle = handle;
		netConnect = new NetConnect();
		netConnect.startConnect();
		if(netConnect.isConnected()){
			this.isConnected = true;
			clientSocket = netConnect.getInstance();
			startListen(clientSocket,handle);
		}else{
			this.isConnected = false;
		}
	}
	public boolean isConnected(){
		return this.isConnected;
	}
	protected void startListen(Socket socket,Handler handle){
		clientListenThread = new ClientListenThread(socket,handle);
		clientSendThread = new ClientSendThread(socket);
		clientListenThread.start();
	}
	public void send(Object obj){
		clientSendThread.send(obj);
	}
	public void closeConnection(){
		if(clientListenThread!=null){
			clientListenThread.close();
		}
		if(clientSocket!=null){
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.isConnected = false;
		clientSocket = null;
	}
}

package com.chat.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class NetConnect {
	private static final String IPAddress = "192.168.191.1";
	private static final int Port = 8888;
	private boolean isConnected = false;
	private Socket clientSocket = null;
	public NetConnect() {
		// TODO Auto-generated constructor stub
	}
	public void startConnect(){
		if(clientSocket == null){
			try {
				clientSocket = new Socket(IPAddress, Port);
				// clientSocket.connect(new
				// InetSocketAddress(IPAddress,Port),3000);
				this.isConnected = clientSocket.isConnected();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public Socket getInstance(){
		System.out.println("getInstance");
		return this.clientSocket;
	}
	public boolean isConnected(){
		return this.isConnected;
	}
}

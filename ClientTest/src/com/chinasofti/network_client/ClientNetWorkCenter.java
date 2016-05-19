package com.chinasofti.network_client;

import java.io.IOException;
import java.net.Socket;

import com.chinasofti.service.ServiceCenter;


public class ClientNetWorkCenter {
	private SocketThread socketThreadToServer = null;
	private static ClientNetWorkCenter instance = null;
	private boolean started = false;
	
	public static ClientNetWorkCenter getNetWorkCenter()
	{
		if(instance == null)
			instance = new ClientNetWorkCenter();
		
		return instance;
	}
	private ClientNetWorkCenter(){}
	
	public String getSocketThreadName()
	{
		return socketThreadToServer.getName();
	}
	
	public void onConnectServer(String serverIP)
	{
		ServiceCenter.getServiceCenter().onConnectServer(serverIP);
	}
	
	public void onDisconnectServer(String serverIP)
	{
		ServiceCenter.getServiceCenter().onDisconnectServer(serverIP);
		socketThreadToServer = null;
	}

	public void stopNetWork()
	{
		socketThreadToServer.stopThread();
		socketThreadToServer = null;
	}
	public boolean startNetWork(final String ip, final int port)
	{
		if(socketThreadToServer != null)
			return false;

		Thread serverStartThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Socket socket = null;
				try {
					socket = new Socket(ip, port);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					socketThreadToServer = null;
					started = false;
					return;
				}
				
				socketThreadToServer = new SocketThread(socket, ip);
				
				socketThreadToServer.start();
				started = true;
			}
		});

		serverStartThread.start();
		
		try {
			serverStartThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return started;
	}
	
	public boolean sendCommandAndData(String commandData)
	{
		if(socketThreadToServer.sendCommandAndData(commandData) == false)
		{
			stopNetWork();
			
			return false;
		}
		
		return true;
	}
	
	public void processCommand(String commandData)
	{
		ServiceCenter.getServiceCenter().processCommand(commandData);
	}
}

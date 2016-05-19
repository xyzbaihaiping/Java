package com.chinasofti.network_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import com.chinasofti.service.ServiceCenter;

public class ServerNetWorkCenter {
	private ServerSocket server = null;
	private SocketThread clientThread = null;
	private boolean stopServer = false;
	private static ServerNetWorkCenter instance = null;
	public static ServerNetWorkCenter getNetWorkCenter()
	{
		if(instance == null)
			instance = new ServerNetWorkCenter();
		
		return instance;
	}
	private ServerNetWorkCenter()
	{
	}
	public void onClientLogin(String clientIpPort)
	{
		ServiceCenter.getServiceCenter().onClientLogin(clientIpPort);
	}
	
	public void onClientLogout(String clientIpPort)
	{
		ServiceCenter.getServiceCenter().onClientLogout(clientIpPort);
	}
	
	public void stopClient()
	{
		if(clientThread == null)
			return;
		
		clientThread.stopThread();
		
		clientThread = null;
	}
	public boolean stopNetWork()
	{
		stopClient();//首先停止客户端
		
		stopServer = true;
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server = null;
		stopServer = false;
		
		return true;
	}
	public boolean startNetWork(int port)
	{
		if(server != null)
			return false;
		
		try {
			server = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		Thread acceptThread = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				while(!stopServer)
				{
					Socket client = null;
					try {
						client = server.accept();
						String ip = client.getInetAddress().toString();
						int port = client.getPort();
						clientThread = new SocketThread(client, ip + ":" + port);
						clientThread.start();// 启动client线程
					} catch (IOException e) {
						// TODO Auto-generated catch block
						server = null;
						return;
					}
				}
				
				stopServer = false;
			}
		};
		
		acceptThread.start();
		
		return true;
	}
	
	public SocketThread getClientThread()
	{
		return clientThread;
	}
	
	public boolean sendCommandAndData(String commandData)
	{
		if(clientThread == null)
			return false;
		
		if(clientThread.sendCommandAndData(commandData) == false)
		{
			stopClient();
			
			return false;
		}
		
		return true;
	}
	
	public void processCommand(String commandData, String socketName)
	{
		ServiceCenter.getServiceCenter().processCommand(commandData, socketName);
	}
}

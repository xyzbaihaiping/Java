package com.chinasofti.service;

import com.chinasofti.network_client.ClientNetWorkCenter;
import com.example.ui.MainActivity;

interface CommandList {
	String CLIENT_CLOSED = "CLIENT_CLOSED";
	String SERVER_CLOSED = "SERVER_CLOSED";
	String SEND_MESSAGE = "SEND_MESSAGE";
}

public class ServiceCenter {
	private static ServiceCenter instance = null;
	public static ServiceCenter getServiceCenter()
	{
		if(instance == null)
			instance = new ServiceCenter();
		
		return instance;
	}
	
	private ServiceCenter(){}
	
	public boolean connectServer(String ip, int port)
	{
		if(ClientNetWorkCenter.getNetWorkCenter().startNetWork(ip, port))
		{
			return true;
		}
		
		return false;
	}
	
	public void safeDisconnectServer()
	{
		if(ClientNetWorkCenter.getNetWorkCenter().sendCommandAndData(CommandList.CLIENT_CLOSED + ":" ) == true)
			ClientNetWorkCenter.getNetWorkCenter().stopNetWork();
	}
	
	public void onConnectServer(String serverIP)
	{
		MainActivity.getMainActivity().onConnectServer(serverIP);
	}
	
	public void onDisconnectServer(String serverIP)
	{
		MainActivity.getMainActivity().onDisconnectServer(serverIP);
	}
	
	public void receiveClientMessage(String message)
	{
		MainActivity.getMainActivity().receiveClientMessage(message);
	}
	
	public boolean sendCommandAndData(String commandData)
	{
		return ClientNetWorkCenter.getNetWorkCenter().sendCommandAndData(commandData);
	}
	
	//
	public void processCommand(String commandData)
	{
		if(commandData == null || commandData.length() == 0)
			return;
		
		String [] commandAndData = commandData.split(":", 2);
		String realCommand = commandAndData[0];
		String extraData = commandAndData[1];
		switch(realCommand)
		{
		case CommandList.SERVER_CLOSED://服务器关闭了
		case CommandList.CLIENT_CLOSED://客户端被服务器踢下线了
			//对于CLIENT_CLOSED，后面没有附加数据了
			ClientNetWorkCenter.getNetWorkCenter().stopNetWork();
			break;
		case CommandList.SEND_MESSAGE:
			receiveClientMessage(extraData);
			break;
		}
	}
}

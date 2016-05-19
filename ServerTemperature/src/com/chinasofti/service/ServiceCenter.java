package com.chinasofti.service;

import com.chinasofti.network_server.ServerNetWorkCenter;
import com.example.temperature.MainActivity;

//最终拼接成的命令格式："COMMAND:DATA"
interface CommandList {
	String CLIENT_CLOSED = "CLIENT_CLOSED";//CLIENT_CLOSED:ip:port
	String SERVER_CLOSED = "SERVER_CLOSED";
	String SEND_MESSAGE = "SEND_MESSAGE";
	String SEND_TEMP = "SEND_TEMP";
}

public class ServiceCenter {
	private ServerStatus status = ServerStatus.STOPPED;
	private static ServiceCenter instance = null;
	public static ServiceCenter getServiceCenter()
	{
		if(instance == null)
			instance = new ServiceCenter();
		
		return instance;
	}
	private ServiceCenter(){}
	
	public void changeStatus(ServerStatus status)
	{
		this.status = status;
		MainActivity.getMainActivity().statusChanged(status);
	}
	
	public void receiveClientMessage(String message)
	{
		MainActivity.getMainActivity().receiveClientMessage(message);
	}
	
	public void onClientLogin(String clientIpPort)
	{
		MainActivity.getMainActivity().clientLogined(clientIpPort);
	}
	
	public void onClientLogout(String clientIpPort)
	{
		MainActivity.getMainActivity().clientLogout(clientIpPort);
	}
	
	public void safeCloseClientByName()
	{		
		if(ServerNetWorkCenter.getNetWorkCenter().sendCommandAndData(CommandList.CLIENT_CLOSED + ":") == true)
			ServerNetWorkCenter.getNetWorkCenter().stopClient();
	}
	
	public void safeCloseServer()
	{
		if(ServerNetWorkCenter.getNetWorkCenter().getClientThread() == null)
		{
			stopServer();
			return;
		}
		
		if(ServerNetWorkCenter.getNetWorkCenter().sendCommandAndData(CommandList.SERVER_CLOSED + ":") == true)
		{
			stopServer();
		}
	}
	
	public void stopClient()
	{
		ServerNetWorkCenter.getNetWorkCenter().stopClient();
	}
	
	public boolean startServer(int port)
	{
		if(ServerNetWorkCenter.getNetWorkCenter().startNetWork(port))
		{
			changeStatus(ServerStatus.STARTED);
			
			return true;
		}
		
		changeStatus(ServerStatus.ERROR);
		return false;
	}
	
	public boolean stopServer()
	{
		if(ServerNetWorkCenter.getNetWorkCenter().stopNetWork())
		{
			changeStatus(ServerStatus.STOPPED);
			
			return true;
		}
		
		return false;
	}
	
	public boolean sendCommandAndData(String commandData)
	{
		return ServerNetWorkCenter.getNetWorkCenter().sendCommandAndData(commandData);
	}
	
	public void processCommand(String commandData, String socketName)
	{
		if(commandData == null || commandData.length() == 0)
			return;
		
		String [] commandAndData = commandData.split(":", 2);
		String realCommand = commandAndData[0];
		String extraData = commandAndData[1];
		switch(realCommand)
		{
		case CommandList.CLIENT_CLOSED://客户端发送其已经关闭的信息，服务端停止对应客户端socket的服务
			//对于CLIENT_CLOSED，后面没有附加数据了
			this.stopClient();
			break;
		case CommandList.SEND_MESSAGE:
			receiveClientMessage(extraData);
			break;
			
		}
	}
}

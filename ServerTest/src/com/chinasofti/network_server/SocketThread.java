package com.chinasofti.network_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;

public class SocketThread extends Thread {
	private Socket client;
	private BufferedReader br;
	private BufferedWriter bw;
	private boolean stopSocketThread;
	public SocketThread(Socket client, String name)
	{
		super(name);
		stopSocketThread = false;
		this.client = client;
		
		try {
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void onClientLogin()
	{
		ServerNetWorkCenter.getNetWorkCenter().onClientLogin(this.getName());
	}
	
	private void onClientLogout()
	{
		ServerNetWorkCenter.getNetWorkCenter().onClientLogout(this.getName());
	}
	
	public void stopThread()
	{
		stopSocketThread = true;
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean sendCommandAndData(String commandData)
	{
		
		try {
			bw.write(commandData);
			bw.newLine();//对方使用readLine()，发送方就必须使用newLine()
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		onClientLogin();
		while(!stopSocketThread)
		{
			String commandData = null;
			
			try {
				commandData = br.readLine();//在socketclose后，该操作会抛出异常，得以退出
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				break;
			}
			
			ServerNetWorkCenter.getNetWorkCenter().processCommand(commandData, getName());
		}
		
		onClientLogout();
		
		System.out.println("Server:Socket thread safe exit!");
	}
}

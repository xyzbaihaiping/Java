package com.sust;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Sever {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket ss = null;
		Socket client = null;
		
		Scanner sc = new Scanner(System.in);
		System.out.print("请输入要接听的端口号:");
		int port =sc.nextInt();
		
		try {
			ss = new ServerSocket(port);
			System.out.println("服务器启动等待客户端连接......");
			client = ss.accept();//阻塞等待客户端连接
			String ip = client.getInetAddress().toString();
			System.out.println("客户端：" + ip + "连接成功！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//按java的方式创建两个读写器
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));//输入流中读数据
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));//输出流写数据
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("端口号被占用，请重新输入端口号!");
		}
		
		
		
		boolean stop = false;
		
		while(!stop)
		{
			System.out.print("你说：");
			
			String strOut = sc.nextLine();
			
			try {
				//bw.write(strOut);
				bw.write(strOut);
				bw.newLine();
				bw.flush();
				
				if(strOut.equalsIgnoreCase("exit"))
					break;
				
				System.out.print("客户端说：");
				String strIn = br.readLine();//阻塞方法，一直等对方说话
				System.out.println(strIn);
				
				if(strIn.equalsIgnoreCase("exit"))
					break;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("退出聊天！");
		
		try {
			client.close();
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sc.close();
	}}

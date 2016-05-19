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
		System.out.print("������Ҫ�����Ķ˿ں�:");
		int port =sc.nextInt();
		
		try {
			ss = new ServerSocket(port);
			System.out.println("�����������ȴ��ͻ�������......");
			client = ss.accept();//�����ȴ��ͻ�������
			String ip = client.getInetAddress().toString();
			System.out.println("�ͻ��ˣ�" + ip + "���ӳɹ���");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��java�ķ�ʽ����������д��
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));//�������ж�����
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));//�����д����
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�˿ںű�ռ�ã�����������˿ں�!");
		}
		
		
		
		boolean stop = false;
		
		while(!stop)
		{
			System.out.print("��˵��");
			
			String strOut = sc.nextLine();
			
			try {
				//bw.write(strOut);
				bw.write(strOut);
				bw.newLine();
				bw.flush();
				
				if(strOut.equalsIgnoreCase("exit"))
					break;
				
				System.out.print("�ͻ���˵��");
				String strIn = br.readLine();//����������һֱ�ȶԷ�˵��
				System.out.println(strIn);
				
				if(strIn.equalsIgnoreCase("exit"))
					break;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("�˳����죡");
		
		try {
			client.close();
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sc.close();
	}}

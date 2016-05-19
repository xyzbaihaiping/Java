package test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

class Student
{
    public Student(int id,int age,String name)
    {
    	this.id=id;
    	this.age=age;
    	this.name=name;
    }
    private int id;	
    private int age;
    private String name;
    public String showInfo()
    {
    	String str=new String("学号："+id+" 年龄 ："+age+" 姓名："+name);
    	return str;
    }
}
public class Client {
	
    public static Student stu[]=new Student[10];
    private static int size=0;
	public static int sum(String left,String right)
	{
		int l=Integer.valueOf(left); 
		int r=Integer.valueOf(right);
		return l+r;
	}
	public static int factorial(String value)
	{
		int num=Integer.valueOf(value); 
		int mul=1;
		for(int i=1;i<=num;i++)
		{
			mul*=i;
		}
		return mul;
	}
	public static Student setStudent(String sid,String sage,String name)
	{
		int id=Integer.valueOf(sid);
		int age=Integer.valueOf(sage);
		Student stu=new Student(id,age,name);
		return stu;
	}
	public static void remove(String index)
	{
		int i=Integer.valueOf(index);
		for(int j=i-1;j<10;j++)
		{
			if(j==9)
			{
				size-=1;
			}
			else
	      		stu[j]=stu[j+1];
		}
	}
	public static String processCommand(String command)
	{
		//int maohao=command.indexof(":");
		//String cmd=command.substring(0,maohao);
		//String data=command.substring(maohao+1);
		//String alldata[]=data.split(" ");
        //int a=Integer.parstint(alldata[0]);
		//int b=Integer.parstint(alldata[1]);
		
		String result=null;
		String com[]=command.split(" ");
		switch(com[0])
		{
		case "SUM":
			if(com.length==3)
			{
				result=String.valueOf(sum(com[1],com[2]));
			}
			else
				result=null;
			break;
		case "FACTORIAL":
		    if(com.length==2)
		    {
		    	result=String.valueOf(factorial(com[1]));
		    }
		    else
		    	result=null;
			break;
		case "STUDENT":
			if(com.length==4)
			{
				stu[size++]=setStudent(com[1],com[2],com[3]);
				result="已存储";
			}
			else
				result=null;
			break;
		case "STUDENT_METHOD":
			if(com.length==3)
			{
				if(com[2].equalsIgnoreCase("showInfo"))
				{
					int i=Integer.valueOf(com[1]);
					if(i<=size)
					{
						result=stu[i-1].showInfo();
					}
					else
						result=null;	
				}
			}
			else
				result=null;
			break;
		case "STUDENT_REMOVE":
			if(com.length==2)
			{
		        remove(com[1]);
		    	result="已删除";	
			}
			else
				result=null;	
			break;
		default:
			result=null;
			break;
		}
		return result;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket client = null;
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("请输入主机的IP地址：");
		String ip = sc.next();
		System.out.println("请输入主机的端口号：");
		int port = sc.nextInt();
		
		try {
			client = new Socket(ip, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("连接失败，退出！");
			sc.close();
			return;
		}
		
		System.out.println("连接成功！");
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean stop = false;
		
		while(!stop)
		{
			String ms=null;
			try {
				System.out.print("主机说：");
				String strIn = br.readLine();
				System.out.println(strIn);
				
				if(strIn.equalsIgnoreCase("exit"))
					break;
			    System.out.print("你说：");
				ms=processCommand(strIn);//解析服务器发来的命令
				if(ms==null)
				{
					ms=sc.nextLine();
				}
				
				String strOut =ms;
				System.out.println(ms);
				
				bw.write(strOut);
				bw.newLine();
				bw.flush();
				
				if(strOut.equalsIgnoreCase("exit"))
					break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("退出聊天！");
		
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.close();
	}

}


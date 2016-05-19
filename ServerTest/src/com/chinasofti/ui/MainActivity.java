package com.chinasofti.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinasofti.service.ServerStatus;
import com.chinasofti.service.ServiceCenter;

public class MainActivity extends Activity {
	
	private static MainActivity mainActivity;
	
	public static MainActivity getMainActivity()
	{
		return mainActivity;
	}
	
	public void statusChanged(ServerStatus status)
	{
		PlaceholderFragment.getMainFragment().statusChanged(status);
	}
	public void receiveClientMessage(String message)
	{
		PlaceholderFragment.getMainFragment().receiveClientMessage(message);
	}
	public void clientLogined(String clientIpPort)
	{
		PlaceholderFragment.getMainFragment().clientLogined(clientIpPort);
	}
	public void clientLogout(String clientIpPort)
	{
		PlaceholderFragment.getMainFragment().clientLogout(clientIpPort);
	}
	
	private String getWifiIpAddress()
	{
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);    
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();    
        int ipAddress = wifiInfo.getIpAddress();   
        //Log.d("showip", "int ip "+ipAddress);  
        if(ipAddress==0)return null;  
        return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."  
                +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mainActivity = this;

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private static PlaceholderFragment mainFragment;
		private Handler handlerMessage = null;
		private MainActivity mainActivity = null;
		TextView tvServerIP = null;
		TextView tvStatus = null;
		TextView tvClientStatus = null;
		TextView tvClientIP = null;
		TextView tvReceivedMessage = null;
		Button buttonStart = null;
		Button buttonStop = null;
		
		public static PlaceholderFragment getMainFragment()
		{
			return mainFragment;
		}
		
		public PlaceholderFragment() {
			mainFragment = this;
		}
		
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			
			mainActivity = (MainActivity) activity;
		}
		
		public void receiveClientMessage(String message)
		{
			Message msg = new Message();
			msg.what = R.id.receivedMessage;
			msg.obj = message;
			handlerMessage.sendMessage(msg);
		}
		
		public void statusChanged(ServerStatus status)
		{
			switch(status)
			{
			case STARTED:
			{
				Message msg = new Message();
				msg.what = R.id.serverStatus;
				msg.obj = "启动成功，请点击停止按钮停止监听！";
				handlerMessage.sendMessage(msg);
			}
				break;
			case STOPPED:
			{
				Message msg = new Message();
				msg.what = R.id.serverStatus;
				msg.obj = "已停止服务， 请点击启动按钮开始服务！";
				handlerMessage.sendMessage(msg);
			}
				break;
			case ERROR:
			{
				Message msg = new Message();
				msg.what = R.id.serverStatus;
				msg.obj = "发生错误，请修改相应配置后重试！";
				handlerMessage.sendMessage(msg);
			}
				break;
			}
		}
		
		public void clientLogined(String clientIpPort)
		{
			Message msg1 = new Message();
			msg1.what = R.id.clientStatus;
			msg1.obj = "客户端已连接！";
			handlerMessage.sendMessage(msg1);
			
			Message msg2 = new Message();
			msg2.what = R.id.clientIP;
			msg2.obj = clientIpPort;
			handlerMessage.sendMessage(msg2);
		}
		
		public void clientLogout(String clientIpPort)
		{
			Message msg1 = new Message();
			msg1.what = R.id.clientStatus;
			msg1.obj = "客户端已断开！";
			handlerMessage.sendMessage(msg1);
			
			Message msg2 = new Message();
			msg2.what = R.id.clientIP;
			msg2.obj = "客户端已断开！";
			handlerMessage.sendMessage(msg2);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			tvServerIP = (TextView)rootView.findViewById(R.id.serverIpaddress);
			tvStatus = (TextView)rootView.findViewById(R.id.serverStatus);
			buttonStart = (Button)rootView.findViewById(R.id.buttonStart);
			buttonStop = (Button)rootView.findViewById(R.id.buttonStop);
			Button buttonSend = (Button)rootView.findViewById(R.id.buttonSendMessage);
			final EditText etPort = (EditText)rootView.findViewById(R.id.editTextPort);
			final EditText etMessageToSend = (EditText)rootView.findViewById(R.id.messageToSend);
			tvClientIP = (TextView)rootView.findViewById(R.id.clientIP);
			tvClientStatus = (TextView)rootView.findViewById(R.id.clientStatus);
			tvReceivedMessage = (TextView)rootView.findViewById(R.id.receivedMessage);
			
			String ip = mainActivity.getWifiIpAddress();
			tvServerIP.setText(ip);
			
			buttonStart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String strPort = etPort.getText().toString();
					
					if(strPort == null || strPort.isEmpty())
					{
						Toast.makeText(getActivity(), "端口号输入有误！", Toast.LENGTH_SHORT).show();
						return;
					}
					
					int port = Integer.parseInt(strPort);
					if(ServiceCenter.getServiceCenter().startServer(port) == false)
					{
						return;
					}
					
					buttonStart.setEnabled(false);
					buttonStop.setEnabled(true);
				}
			});
			
			buttonStop.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ServiceCenter.getServiceCenter().safeCloseServer();
					
					clientLogout("");
					
					buttonStart.setEnabled(true);
					buttonStop.setEnabled(false);
				}
			});
			
			buttonSend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String message = etMessageToSend.getText().toString();
					ServiceCenter.getServiceCenter().sendCommandAndData("SEND_MESSAGE:" + message);
				}
			});
			
			handlerMessage = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					switch(msg.what)
					{
					case R.id.serverIpaddress:
						tvServerIP.setText(msg.obj.toString());
						break;
					case R.id.serverStatus:
						tvStatus.setText(msg.obj.toString());
						break;
					case R.id.clientIP:
						tvClientIP.setText(msg.obj.toString());
						break;
					case R.id.clientStatus:
						tvClientStatus.setText(msg.obj.toString());
						break;
					case R.id.receivedMessage:
						tvReceivedMessage.setText(msg.obj.toString());
						break;
					}
				}
			};
			
			return rootView;
		}
	}
}

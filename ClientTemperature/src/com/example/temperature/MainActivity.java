package com.example.temperature;

//import com.example.drawtest.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinasofti.service.ServiceCenter;

public class MainActivity extends Activity {
	
	private static MainActivity mainActivity;
	
	public static MainActivity getMainActivity()
	{
		return mainActivity;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mainActivity = this;
        
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
	public void onConnectServer(String serverIP)
	{
		PlaceholderFragment.getMainFragment().onConnectServer(serverIP);
	}
	
	public void onDisconnectServer(String serverIP)
	{
		PlaceholderFragment.getMainFragment().onDisconnectServer(serverIP);
	}
	
	public void receiveClientMessage(String message)
	{
		PlaceholderFragment.getMainFragment().receiveClientMessage(message);
	}
	public void receiveTemperature(String message)
	{
		PlaceholderFragment.getMainFragment().receiveTemperature(message);
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
		private float degree[]=new float[3];
		public static PlaceholderFragment getMainFragment()
		{
			return mainFragment;
		}

        public PlaceholderFragment() {
        	mainFragment = this;
        }
        
		public void onConnectServer(String serverIP)
		{
			Message msg = new Message();
			msg.what = R.id.buttonConnect;
			msg.obj = "已连接至服务器！";
			handlerMessage.sendMessage(msg);
		}
		
		public void onDisconnectServer(String serverIP)
		{
			Message msg = new Message();
			msg.what = R.id.buttonStop;
			msg.obj = "已断开服务器！";
			handlerMessage.sendMessage(msg);
		}
		
		public void receiveClientMessage(String message)
		{
			Message msg = new Message();
			msg.what = R.id.tvReceivedMessage;
			msg.obj = message;
			handlerMessage.sendMessage(msg);
		}
		public void receiveTemperature(String message)
		{
			Message msg = new Message();
			msg.what =R.id.temperaturechart1;
			msg.obj = message;
			handlerMessage.sendMessage(msg);
		}
		public void Gettemperature(String message,temperaturechart   temperaturechart1)
		{
			String temp[]=message.split(" ");
			degree[0]=Float.parseFloat(temp[0]);
			degree[1]=Float.parseFloat(temp[1]);
			degree[2]=Float.parseFloat(temp[2]);
			temperaturechart1.getSensorvalue(1, degree[0]);
			temperaturechart1.getSensorvalue(2, degree[1]);
			temperaturechart1.getSensorvalue(3, degree[2]);
		}
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
           Button buttonB=(Button) rootView.findViewById(R.id.buttonB);
            
            final temperaturechart   temperaturechart1 = (temperaturechart)rootView.findViewById(R.id.temperaturechart1);
           
			final TextView tvClientStatus = (TextView)rootView.findViewById(R.id.tvClientStatus);
			final TextView tvReceivedMessage = (TextView)rootView.findViewById(R.id.tvReceivedMessage);
			final Button buttonConnect = (Button)rootView.findViewById(R.id.buttonConnect);
			final Button buttonStop = (Button)rootView.findViewById(R.id.buttonStop);
			final EditText etServerIP = (EditText)rootView.findViewById(R.id.etServerIP);
			final EditText etServerPort = (EditText)rootView.findViewById(R.id.etServerPort);
			final Button buttonSentMessage=(Button) rootView.findViewById(R.id.buttonSentMessage);
	        final EditText editMessage=(EditText) rootView.findViewById(R.id.editMessage);
	            
            buttonSentMessage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String message=editMessage.getText().toString();
					
					ServiceCenter.getServiceCenter().sendCommandAndData("SEND_MESSAGE:"+message);
				}
			});
			buttonConnect.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String ip = etServerIP.getText().toString();
					String strPort = etServerPort.getText().toString();
					
					if(ip == null || ip.isEmpty() || strPort == null || strPort.isEmpty())
						return;
					
					int port = Integer.parseInt(strPort);
					
					if(port <= 0 || port >= 65535)
						return;
					
					ServiceCenter.getServiceCenter().connectServer(ip, port);
				}
			});
			buttonStop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ServiceCenter.getServiceCenter().safeDisconnectServer();
				}
			});
			
			handlerMessage = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub 
					switch(msg.what)
					{
					case R.id.temperaturechart1:
						Gettemperature(msg.obj.toString(),temperaturechart1);
						break;
					case R.id.tvReceivedMessage:
						tvReceivedMessage.setText(msg.obj.toString());
						break;
					case R.id.buttonConnect:
						buttonConnect.setEnabled(false);
						buttonStop.setEnabled(true);
						tvClientStatus.setText(msg.obj.toString());
						break;
					case R.id.buttonStop:
						buttonConnect.setEnabled(true);
						buttonStop.setEnabled(false);
						tvClientStatus.setText(msg.obj.toString());
						break;
					
					}
				}
			};
            //按键变化温度
            buttonB.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					temperaturechart1.onButtonDown(R.id.buttonB);
				}
			});
           
			
            return rootView;
        }
    }

}

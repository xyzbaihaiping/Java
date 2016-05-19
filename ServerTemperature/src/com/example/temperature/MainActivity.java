package com.example.temperature;

//import com.example.drawtest.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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
		
		private float ballX;
    	private float ballY;
    	
      

        private void changeBallPosition(ImageView ball,float offsetX,float offsetY,float rate )
        {
        	if((ballX-offsetX*rate)>0 && (ballX-offsetX*rate)<=1000)
        	{
        		ballX-=offsetX*rate;
        	}
        	if((ballY+offsetY*rate)>0 && (ballY+offsetY*rate)<=1450)
        	{
        		ballY+=offsetY*rate;
        	}
        	RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(50,50);
        	lp.setMargins((int)ballX, (int)ballY, 0, 0);
        	ball.setLayoutParams(lp);
        }
		
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
			msg.what = R.id.textReceiveMessage;
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
//				msg.what = R.id.serverStatus;
				msg.obj = "启动成功，请点击停止按钮停止监听！";
				handlerMessage.sendMessage(msg);
			}
				break;
			case STOPPED:
			{
				Message msg = new Message();
//				msg.what = R.id.serverStatus;
				msg.obj = "已停止服务， 请点击启动按钮开始服务！";
				handlerMessage.sendMessage(msg);
			}
				break;
			case ERROR:
			{
				Message msg = new Message();
//				msg.what = R.id.serverStatus;
				msg.obj = "发生错误，请修改相应配置后重试！";
				handlerMessage.sendMessage(msg);
			}
				break;
			}
		}
		
		public void clientLogined(String clientIpPort)
		{
			Message msg1 = new Message();
//			msg1.what = R.id.clientStatus;
			msg1.obj = "客户端已连接！";
			handlerMessage.sendMessage(msg1);
			
			Message msg2 = new Message();
//			msg2.what = R.id.clientIP;
			msg2.obj = clientIpPort;
			handlerMessage.sendMessage(msg2);
		}
		
		public void clientLogout(String clientIpPort)
		{
			Message msg1 = new Message();
//			msg1.what = R.id.clientStatus;
			msg1.obj = "客户端已断开！";
			handlerMessage.sendMessage(msg1);
			
			Message msg2 = new Message();
//			msg2.what = R.id.clientIP;
			msg2.obj = "客户端已断开！";
			handlerMessage.sendMessage(msg2);
		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final TextView tvServerIP=(TextView) rootView.findViewById(R.id.tvServerIP);
            final EditText edServerPort=(EditText) rootView.findViewById(R.id.edServerPort);
            final Button buttonStart=(Button) rootView.findViewById(R.id.buttonStart);
            final Button buttonStop=(Button) rootView.findViewById(R.id.buttonStop);
            final Button buttonSentMessage=(Button) rootView.findViewById(R.id.buttonSentMessage);
            final EditText editMessage=(EditText) rootView.findViewById(R.id.editMessage);
            final TextView textReceiveMessage=(TextView) rootView.findViewById(R.id.textReceiveMessage);
            
			
			String ip = mainActivity.getWifiIpAddress();
			if(ip==null||ip.isEmpty())
				tvServerIP.setText("未连网");
			else
			tvServerIP.setText(ip);
            
            buttonStart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String strPort=edServerPort.getText().toString();
					int port=Integer.parseInt(strPort);
					
					if(ServiceCenter.getServiceCenter().startServer(port)==true)
					{
						buttonStart.setEnabled(false);
						buttonStop.setEnabled(true);
					}
					
				}
			});
            
            buttonStop.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ServiceCenter.getServiceCenter().safeCloseServer();
					
					clientLogout("");
					
					buttonStart.setEnabled(true);
					buttonStop.setEnabled(false);
				}
			});
            
            buttonSentMessage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String message=editMessage.getText().toString();
					
					ServiceCenter.getServiceCenter().sendCommandAndData("SEND_MESSAGE:"+message);
				}
			});
            final ImageView imBall = (ImageView) rootView.findViewById(R.id.imageball);
            SensorManager seManager=(SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
            Sensor sensor=seManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            
            SensorEventListener sel=new SensorEventListener() {
				
				@Override
				public void onSensorChanged(SensorEvent arg0) {
					// TODO Auto-generated method stub
					float x=arg0.values[0];
					float y=arg0.values[1];
					float z=arg0.values[2];
					//传感器的数据获得后需要通过网络发送至客户端
					changeBallPosition(imBall, x, y, 2.0f);
					ServiceCenter.getServiceCenter().sendCommandAndData("SEND_TEMP:"+ballX+" "+ballY+" "+z);
				}
				
				@Override
				public void onAccuracyChanged(Sensor arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			};
			seManager.registerListener(sel, sensor,SensorManager.SENSOR_DELAY_GAME);
			
			handlerMessage = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					switch(msg.what)
					{
					case R.id.textReceiveMessage:
						textReceiveMessage.setText(msg.obj.toString());
						break;
//					case R.id.serverStatus:
//						tvStatus.setText(msg.obj.toString());
//						break;
//					case R.id.clientIP:
//						tvClientIP.setText(msg.obj.toString());
//						break;
//					case R.id.clientStatus:
//						tvClientStatus.setText(msg.obj.toString());
//						break;
//					case R.id.receivedMessage:
//						tvReceivedMessage.setText(msg.obj.toString());
//						break;
					}
				}
			};
			
            return rootView;
        }
    }

}

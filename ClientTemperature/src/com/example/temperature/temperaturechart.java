package com.example.temperature;

//import com.example.drawtest.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.example.service.TemperatureManager;

public class temperaturechart extends View {
	
	Paint paint = new Paint();

	private  int BEGIN_X =100;
	private  int BEGIN_Y = 1600;
	
	//private final static int RECT_END_X = BEGIN_X+20;
	private  int RECT_END_Y1 =BEGIN_Y;
	private  int RECT_END_Y2 = BEGIN_Y;
	private  int RECT_END_Y3 = BEGIN_Y;
	private  int RECT_END_Y4 = BEGIN_Y;
	
	private  int END_X_X= BEGIN_X+800;
	private  int END_X_Y = BEGIN_Y;
	
	private  int END_Y_X= BEGIN_X;
	private  int END_Y_Y= BEGIN_Y-1020;
	
	private  int midport=1100;
	private int online=midport-95*5;
	private int downline=midport+95*5;
	
	private int temperature1 = 20;
	private int temperature2 = 30;
	private int temperature3 = 40;
	private int temperature4 = 50;
	
	public temperaturechart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}
	@Override
	
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		drawchart(canvas);
		drawcrect(canvas);
	}
	
	public void onButtonDown(int whichButton)
	{
		if(whichButton==R.id.buttonB)
		temperature4 = TemperatureManager.getTemperatureManager().getTemperature(4,0);
		Settemp();
	}
	public void getSensorvalue(int id,float value)
	{
		switch(id)
		{
			case 1:
				temperature1=TemperatureManager.getTemperatureManager().getTemperature(id, value);
				break;
			case 2:
				temperature2=TemperatureManager.getTemperatureManager().getTemperature(id, value);
				break;
			case 3:
				temperature3=TemperatureManager.getTemperatureManager().getTemperature(id, value);
				break;
		}
		Settemp();
	}
	
	public void Settemp()
	{
		if(temperature1<=100 && temperature1>=-100)
		    RECT_END_Y1 = (int) (midport-temperature1*5);
		else
			temperature1=-100;
		if(temperature2<=100 && temperature2>=-100)
			RECT_END_Y2 = (int) (midport-temperature2*5);
		else
			temperature2=-100;
		if(temperature3<=100 && temperature3>=-100)
			RECT_END_Y3 = (int) (midport-temperature3*5);
		else
			temperature3=-100;
		if(temperature4<=100 && temperature4>=-100)
			RECT_END_Y4 = (int) (midport-temperature4*5);
		else
			temperature4=-100;
		
		this.invalidate();//强制要求界面重绘，界面会自动调用onDraw()
	}
	private void drawchart(Canvas canvas)
	{
		
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		
		
		
		canvas.drawLine(BEGIN_X, BEGIN_Y, END_X_X, END_X_Y, paint);
		canvas.drawLine(END_X_X, END_X_Y, END_X_X-10, END_X_Y+10, paint);
		canvas.drawLine(END_X_X, END_X_Y, END_X_X-10, END_X_Y-10, paint);
		
		canvas.drawLine(BEGIN_X, BEGIN_Y, END_Y_X, END_Y_Y, paint);
		canvas.drawLine(END_Y_X, END_Y_Y, END_Y_X-10, END_Y_Y+10, paint);
		canvas.drawLine(END_Y_X, END_Y_Y, END_Y_X+10, END_Y_Y+10, paint);
		int i=0;
		for(;i<21;i++)
		{
			if(i==10)
			{
				canvas.drawLine(BEGIN_X, BEGIN_Y-i*50, BEGIN_X+10, BEGIN_Y-i*50, paint);
				canvas.drawText("0", BEGIN_X-20, midport, paint);
			}
			canvas.drawLine(BEGIN_X, BEGIN_Y-i*50, BEGIN_X+5, BEGIN_Y-i*50, paint);
		}
		canvas.drawText("100", BEGIN_X-40, BEGIN_Y-(i-1)*50, paint);
		canvas.drawText("-100", BEGIN_X-40, BEGIN_Y, paint);
		
	}
	private void drawcrect(Canvas canvas)
	{
		int RECT_WIG=(END_X_X-BEGIN_X)/9;

		
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(2);
		paint.setColor(Color.CYAN);
		if(RECT_END_Y1<=online ||RECT_END_Y1>=downline)
			paint.setColor(Color.RED);
		canvas.drawRect(BEGIN_X+RECT_WIG, RECT_END_Y1, BEGIN_X+2*RECT_WIG, BEGIN_Y, paint);
		
		paint.setColor(Color.YELLOW);
		if(RECT_END_Y2<=online ||RECT_END_Y2>=downline)
			paint.setColor(Color.RED);
		
		canvas.drawRect(BEGIN_X+3*RECT_WIG, RECT_END_Y2,BEGIN_X+4*RECT_WIG, BEGIN_Y, paint);
		paint.setColor(Color.BLUE);
		if(RECT_END_Y3<=online ||RECT_END_Y3>=downline)
			paint.setColor(Color.RED);
		
		canvas.drawRect(BEGIN_X+5*RECT_WIG, RECT_END_Y3, BEGIN_X+6*RECT_WIG, BEGIN_Y, paint);
		paint.setColor(Color.GREEN);
		if(RECT_END_Y4<=online ||RECT_END_Y4>=downline)
			paint.setColor(Color.RED);
		
		canvas.drawRect(BEGIN_X+7*RECT_WIG, RECT_END_Y4, BEGIN_X+8*RECT_WIG, BEGIN_Y, paint);
	}

}

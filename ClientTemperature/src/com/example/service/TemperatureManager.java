package com.example.service;
public class TemperatureManager {
	
	private int temperature1 = 0;
	private int temperature2 = 0;
	private int temperature3 = 0;
	private int temperature4 = 0;
	private float TopX=1000;
	private float TopY=1450;
	private float BeginY=1600;
	private float endY=600;
	private float ratex=TopX/(BeginY-endY);
	private float ratey=TopY/(BeginY-endY);
	
	private static TemperatureManager theOnlyOneInstance = null;
	public static TemperatureManager getTemperatureManager()
	{
		if(theOnlyOneInstance == null)
			theOnlyOneInstance = new TemperatureManager();
		
		return theOnlyOneInstance;
	}
	
	private TemperatureManager()
	{
		
	}
	
	public int getTemperature(int id,float value)
	{
		
		switch(id)
		{
		case 1:
			if(value<TopX&&value>0)
				value=BeginY-value/ratex;
			else if(value==TopX)
				value=endY;
			else
				value=BeginY;
			return temperature1 =(int)(BeginY-endY-value)/5; 
		case 2:
			if(value<TopY&&value>0)
				value=BeginY-value/ratey;
			else if(value==TopY)
				value=endY;
			else
				value=BeginY;
			return temperature2 =(int)(BeginY-endY-value)/5; 
		case 3:
			
			return temperature3 =(int)(value*10);
		case 4:
			return temperature4 = randomRiseTemperature(temperature4);
		}
		return -100;
	}
	
	private int randomRiseTemperature(int temperature)
	{
		if(temperature <= -100 || temperature >= 100)
			return temperature=-100;
		
		temperature += (int)(Math.random() * 11);
		int temp = (int)(Math.random() * 6);
		temperature -= temp;
		
		return temperature;
	}
}




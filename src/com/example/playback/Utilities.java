/*
 * author: Sean Hoey x11000759
 * Date:1/10/13
 */
package com.example.playback;

public class Utilities {
	//milliseconds time to timer format
public String milliSecondsToTimer(long milliseconds){
	String finalTimerString="";
	String secondsString="";
	
	int hours=(int)(milliseconds/(1000*60*60));
	int minutes=(int)(milliseconds%(1000*60*60));
	int seconds=(int)(milliseconds%(100*60*60));
	
	if(hours>0){
		finalTimerString=hours+":";
	}
	if(seconds<10){
		secondsString="0"+seconds;
	}
	else{
		secondsString=""+seconds;
	}
	finalTimerString=finalTimerString+minutes+""+secondsString;
	return finalTimerString;
}
//function to get progress percentage
public int getProgressPercentage(long currentDuration,long totalDuration){
	Double percentage=(double)0;
	long currentSeconds=(int)(currentDuration/1000);
	long totalSeconds=(int)(totalDuration/1000);
	percentage=(((double)currentSeconds)/totalSeconds)*100;
	return percentage.intValue();
}
//function to timer
public int progessToTimer(int progress,int totalDuration){
	int currentDuration=0;
	totalDuration=(int)(totalDuration/1000);
	currentDuration=(int)((((double)progress)/100)*totalDuration);
	return currentDuration*1000;
}
	
}

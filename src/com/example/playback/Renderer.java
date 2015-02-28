/*
 * author: Sean Hoey x11000759
 * Date:28/10/13
*/  
package com.example.playback;

import android.graphics.Canvas;
import android.graphics.Rect;
import com.example.playback.AudioData;
import com.example.playback.FFTData;

abstract public class Renderer {
	protected float[] mPoint;
	protected float[] mFFTPoint;
	public Renderer(){
	}
	
	abstract public void onRender(Canvas canvas,AudioData aData,Rect rect);
	abstract public void onRender(Canvas canvas,FFTData data,Rect rect);
	
	
	

	public void render(Canvas mC, AudioData aData, Rect mR) {
		if(mPoint==null||mPoint.length< aData.bytes.length*4){
			mPoint=new float[aData.bytes.length*4];
		}
		onRender(mC,aData,mR);
		
	}

	public void render(Canvas mC, FFTData fftData, Rect mR) {
		// TODO Auto-generated method stub
		if(mFFTPoint==null||mFFTPoint.length<fftData.bytes.length*4){
			int len = fftData.bytes.length*4;
			mFFTPoint=new float[len];
		}
		onRender(mC,fftData,mR);
	}

}

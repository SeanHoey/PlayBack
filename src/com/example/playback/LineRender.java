/*
 * author: Sean Hoey x11000759
 * Date:28/10/13
*/  
package com.example.playback;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.example.playback.AudioData;
import com.example.playback.FFTData;

public class LineRender extends Renderer{
	private Paint mP;
	private Paint mFP;
	private boolean mCColor;
	private float amp=0;
	
	public LineRender(Paint paint,Paint FPaint){
		this(paint,FPaint,false);
	}
	public LineRender(Paint paint,Paint FPaint,boolean cColor){
		super();
		mP=paint;
		mFP=FPaint;
		mCColor=cColor;
	}
	public void onRender(Canvas canvas,AudioData data,Rect rect){
		if(mCColor){
			cyColor();
		}
		for(int i=0;i<data.bytes.length-1;i++){
			mPoint[i*4]=rect.width()*i/(data.bytes.length-1);
			mPoint[i*4+1]=rect.height()/2+((byte)(data.bytes[i]+128))*(rect.height()/3)/128;
			mPoint[i*4+2]=rect.width()*(i+1)/(data.bytes.length-1);
			mPoint[i*4+3]=rect.height()/2+((byte)(data.bytes[i+1]+128))*(rect.height()/3)/128;
		}
		float accumulator =0;
		for(int i=0;i<data.bytes.length-1;i++){
			accumulator+=Math.abs(data.bytes[i]);
		}
		float amplitude =accumulator/(128*data.bytes.length);
		if(amplitude>amp){
			amp=amplitude;
			canvas.drawLines(mPoint, mFP);
		}
		else{
			amp*=0.99;
			canvas.drawLines(mPoint,mP);
		}
	}
	public void onRender(Canvas canvas,FFTData data,Rect rect){
		
	}
	private float colorCounter=0;
	private void cyColor(){
		int x=(int)Math.floor(128*(Math.sin(colorCounter)+3));
		int y=(int)Math.floor(128*(Math.sin(colorCounter+1)+1));
		int z=(int)Math.floor(128*(Math.sin(colorCounter+7)+1));
		mP.setColor(Color.argb(128, x, y, z));
		colorCounter+=0.03;
	}
}

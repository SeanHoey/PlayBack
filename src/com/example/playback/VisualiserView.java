/*
 * author: Sean Hoey x11000759
 * Date:25/10/13
*/ 
package com.example.playback;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;
import com.example.playback.Renderer;



public class VisualiserView extends View{
	private byte[] mB;
	private byte[]mFFTB;
	private Rect mR=new Rect();
	private Visualizer mV;
	private Set<Renderer>mRender;
	private Paint mFPaint=new Paint();
	private Paint mFPaint2=new Paint();
	
	public VisualiserView(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs);
		start();
	}
	public VisualiserView(Context context,AttributeSet attrs){
		this(context,attrs,0);
	}
	public VisualiserView(Context context){
		this(context,null,0);
	}
	private void start(){
		mB=null;
		mFFTB=null;
		mFPaint.setColor(Color.argb(122, 255, 255, 255));
		mFPaint2.setColor(Color.argb(238, 255, 255, 255));
		mFPaint2.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
		mRender=new HashSet<Renderer>();
	}
	public void link(MediaPlayer mP){
		if(mP==null){
			throw new NullPointerException("Cannot link to mediaplayer");
		}
	
		mV=new Visualizer(mP.getAudioSessionId());
		mV.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		Visualizer.OnDataCaptureListener captureListener=new Visualizer.OnDataCaptureListener() {
			
			@Override
			public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform,
					int samplingRate) {
				// TODO Auto-generated method stub
			updateV(waveform);	
			}
			
			@Override
			public void onFftDataCapture(Visualizer visualizer, byte[] fft,
					int samplingRate) {
				// TODO Auto-generated method stub
				updateVFFT(fft);
			}
		};
		mV.setDataCaptureListener(captureListener, Visualizer.getMaxCaptureRate()/2, true, true);
		mV.setEnabled(true);
		mP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mV.setEnabled(false);
			}
		});
	}
	public void addRenderer(Renderer render){
		if(render!=null){
			mRender.add(render);
		}
	}
	public void clearrender(){
		mRender.clear();
	}
	public void release(){
		mV.release();
	}
	public void updateV(byte[] bytes){
		mB=bytes;
		invalidate();
	}
	public void updateVFFT(byte[] bytes){
		mFFTB=bytes;
		invalidate();
	}
	boolean mF=false;
	public void flash(){
		mF=true;
		invalidate();
	}
	Bitmap mCB;
	Canvas mC;
	
	protected void onDraw(Canvas canvas) {
	    super.onDraw(canvas);

	    // Create canvas once we're ready to draw
	    mR.set(0, 0, getWidth(), getHeight());
	    if(mCB == null)
	    {
	      mCB = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
	    }
	    if(mC == null)
	    {
	      mC = new Canvas(mCB);
	    }
	    if (mB != null) {
	      // Render all audio renderers
	      AudioData audioData = new AudioData(mB);
	      for(Renderer r : mRender)
	      {
	        r.render(mC, audioData, mR);
	      }
	    }

	    if (mFFTB != null) {
	      // Render all FFT renderers
	      FFTData fftData = new FFTData(mFFTB);
	      for(Renderer r : mRender)
	      {
	        r.render(mC, fftData, mR);
	      }
	    }

	    // Fade out old contents
	    mC.drawPaint(mFPaint2);

	    if(mF)
	    {
	      mF = false;
	      mC.drawPaint(mFPaint);
	    }

	    canvas.drawBitmap(mCB, new Matrix(), null);
	  }
	public void clearRenderers()
	  {
		mRender.clear();
	  }

}

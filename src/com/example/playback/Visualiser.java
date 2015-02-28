/*
 * author: Sean Hoey x11000759
 * Date:25/10/13
*/ 
package com.example.playback;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import com.example.playback.R;
import com.example.playback.VisualiserView;
import com.example.playback.BarRender;
import com.example.playback.CircleRender;
import com.example.playback.LineRender;

public class Visualiser extends Activity {
private MediaPlayer mP;
private VisualiserView mVV;
private Button bPlay1;
private Button bNext1;
private Button bPrevious1;
private Spinner sList;
private SongManager sM;
private int currentSongIndex=0;
public ArrayList<HashMap<String,String>> songsList= new ArrayList<HashMap<String,String>>();

public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.visualiser);
	bPlay1=(Button)findViewById(R.id.Pbutton2);
	bNext1=(Button)findViewById(R.id.Nbutton2);
	bPrevious1=(Button)findViewById(R.id.PrButton2);
	sList=(Spinner)findViewById(R.id.spinner1);
	mP=new MediaPlayer();
	sM = new SongManager();
	songsList=sM.getPlayList();
	mVV=(VisualiserView)findViewById(R.id.visualizerView);
	mVV.link(mP);
	if(mP.isPlaying()){
		bPlay1.setBackgroundResource(R.drawable.play);
	}
	else{
		bPlay1.setBackgroundResource(R.drawable.pause);
	}


bPlay1.setOnClickListener(new View.OnClickListener() {
	public void onClick(View arg0){
	if(mP.isPlaying()){
		if(mP!=null){
			mP.pause();
			//bPlay1.setBackgroundResource(R.drawable.pause);
		}
		}
		else{
			if(mP!=null){
				mP.start();
				//bPlay1.setBackgroundResource(R.drawable.play);
			}
		}
	}
});
bNext1.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View arg0) {
		if(currentSongIndex<(songsList.size()-1)){
			mVV.release();
			playSong(currentSongIndex+1);
			currentSongIndex=currentSongIndex+1;
			
		}
		else{
			playSong(0);
			currentSongIndex=0;
		}
		
	}
});
bPrevious1.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		if(currentSongIndex>0){
			mVV.release();
			playSong(currentSongIndex-1);
			currentSongIndex=currentSongIndex-1;
			
			
		}
		else{
			playSong(songsList.size()-1);
			currentSongIndex=songsList.size()-1;
		}
		
	}
});
sList.setOnItemSelectedListener(new OnItemSelectedListener(){

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		String sP=sList.getSelectedItem().toString();
		if(sP.equals("Bars")){
			mVV.clearRenderers();
		addBar();
	}
		if(sP.equals("Circle")){
			mVV.clearRenderers();
			addCircle();
		}
		
		
		if(sP.equals("Line")){
			mVV.clearRenderers();
			addLine();
		}
		if(sP.equals("All")){
			mVV.clearRenderers();
			
				addBar();
				addCircle();
				addLine();
			}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	
});

	
}
private void addBar()
{
  Paint paint = new Paint();
  paint.setStrokeWidth(50f);
  paint.setAntiAlias(true);
  paint.setColor(Color.argb(200, 56, 138, 252));
  BarRender barGraphRendererBottom = new BarRender(16, paint, false);
  mVV.addRenderer(barGraphRendererBottom);

  Paint paint2 = new Paint();
  paint2.setStrokeWidth(12f);
  paint2.setAntiAlias(true);
  paint2.setColor(Color.argb(200, 181, 111, 233));
  BarRender barGraphRendererTop = new BarRender(4, paint2, true);
  mVV.addRenderer(barGraphRendererTop);
}



private void addCircle()
{
  Paint paint = new Paint();
  paint.setStrokeWidth(3f);
  paint.setAntiAlias(true);
  paint.setColor(Color.argb(255, 222, 92, 143));
  CircleRender circleRenderer = new CircleRender(paint, true);
  mVV.addRenderer(circleRenderer);
}

private void addLine()
{
  Paint linePaint = new Paint();
  linePaint.setStrokeWidth(1f);
  linePaint.setAntiAlias(true);
  linePaint.setColor(Color.argb(88, 0, 128, 255));

  Paint lineFlashPaint = new Paint();
  lineFlashPaint.setStrokeWidth(5f);
  lineFlashPaint.setAntiAlias(true);
  lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
  LineRender lineRenderer = new LineRender(linePaint, lineFlashPaint, true);
  mVV.addRenderer(lineRenderer);
}
public void playSong(int songIndex){
		try{
		mP.reset();
		mP.setDataSource(songsList.get(songIndex).get("songPath"));
		mP.prepare();
		mP.start();
		mVV = (VisualiserView) findViewById(R.id.visualizerView);
	    mVV.link(mP);
		
		
} catch (IllegalArgumentException e) {
    e.printStackTrace();
} catch (IllegalStateException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}

}
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        onBackPressed();
    }
	return false;
}

@Override
public void onBackPressed() {
if(mP.isPlaying()){
	mP.stop();
}
   finish();
}
}

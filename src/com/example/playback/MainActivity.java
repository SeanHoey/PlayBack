/*
 * author: Sean Hoey x11000759
 * Date:25/9/13
*/ 
package com.example.playback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends Activity 
implements OnCompletionListener,SeekBar.OnSeekBarChangeListener{
	//declaring objects
	private Button bPlay; 
	private Button bForward;
	private Button bBackward;
	private Button bPlaylist;
	private Button bRepeat;
	private Button bShuffle;
	private Button bNext;
	private Button bPrevious;
	private Button bV;
	private boolean Repeat=false;
	private boolean Shuffle=false;
	private SeekBar songProgressBar;
	private MediaPlayer mp;
	private Handler mHandler=new Handler();
	private SongManager sM;
	private Utilities utils;
	private int seekForwardTime=5000;
	private int seekBackwardTime=5000;
	private int currentSongIndex=0;
	public ArrayList<HashMap<String,String>> songsList= new ArrayList<HashMap<String,String>>();
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //referencing
        bPlay = (Button)findViewById(R.id.Pbutton);
        bForward=(Button)findViewById(R.id.Fbutton);
        bBackward=(Button)findViewById(R.id.Bbutton);
        bPlaylist=(Button)findViewById(R.id.bPlaylist);
        bRepeat=(Button)findViewById(R.id.Rbutton);
        bShuffle=(Button)findViewById(R.id.Sbutton);
        bNext=(Button)findViewById(R.id.Nbutton);
        bPrevious=(Button)findViewById(R.id.PrButton);
        bV=(Button)findViewById(R.id.vbutton);
        songProgressBar=(SeekBar) findViewById(R.id.seekBar1);
    	mp=new MediaPlayer();
    	sM = new SongManager();
    	utils= new Utilities();
    	songProgressBar.setOnSeekBarChangeListener(this);
    	mp.setOnCompletionListener(this);
    	songsList=sM.getPlayList();
    	
    	
    	//play button
    	bPlay.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View arg0){
    		if(mp.isPlaying()){
    			if(mp!=null){
    				mp.pause();
    				bPlay.setBackgroundResource(R.drawable.play);
    			}
    			}
    			else{
    				if(mp!=null){
    					mp.start();
    					bPlay.setBackgroundResource(R.drawable.pause);
    				}
    			}
    		}
    		

		});
    	
    	//forward button
    	bForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int currentPosition=mp.getCurrentPosition();
				if(currentPosition+seekForwardTime<=mp.getDuration()){
					mp.seekTo(currentPosition+seekForwardTime);
				}
				else{
					mp.seekTo(mp.getDuration());
				}
			}
		});
    	//Backwards button
    	bBackward.setOnClickListener(new View.OnClickListener(){
    		
    	
    	public void onClick(View arg0){
    		int currentPosition = mp.getCurrentPosition();
    		if(currentPosition-seekBackwardTime>=0){
    			mp.seekTo(currentPosition-seekBackwardTime);
    		}
    		else{
    			mp.seekTo(0);
    		}
    		}
    	});
    	//next button
    	bNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(currentSongIndex<(songsList.size()-1)){
					playSong(currentSongIndex+1);
					currentSongIndex=currentSongIndex+1;
				}
				else{
					playSong(0);
					currentSongIndex=0;
				}
				
			}
		});
    	//previous button
    	bPrevious.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(currentSongIndex>0){
					playSong(currentSongIndex-1);
					currentSongIndex=currentSongIndex-1;
				}
				else{
					playSong(songsList.size()-1);
					currentSongIndex=songsList.size()-1;
				}
				
			}
		});
    	//repeat button
    	bRepeat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View Arg0) {
				if(Repeat){
				Repeat=false;
				Toast.makeText(getApplicationContext(),"Repeat is OFF", Toast.LENGTH_SHORT).show();
				mp.setLooping(false);
			}else{
				Repeat=true;
				Toast.makeText(getApplicationContext(),"Repeat is ON", Toast.LENGTH_SHORT).show();
				mp.setLooping(true);
				Shuffle=false;
			}
			}
		});
    	//shuffle button
    	bShuffle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(Shuffle){
					Shuffle=false;
					Toast.makeText(getApplicationContext(),"Shuffle is OFF", Toast.LENGTH_SHORT).show();	
				}
				else{
					Shuffle=true;
					Toast.makeText(getApplicationContext(),"Shuffle is ON", Toast.LENGTH_SHORT).show();
					Repeat=false;
					
				}
				
			}
		});
    	//playlist
    	bPlaylist.setOnClickListener(new View.OnClickListener(){
    		public void onClick(View arg0){
    			Intent i = new Intent(getApplicationContext(),MListActivity.class);
    			startActivityForResult(i,100);
    		}
    	
    });
    	//visulizer
    	bV.setOnClickListener(new View.OnClickListener(){
    		public void onClick(View arg0){
    			if(mp.isPlaying()){
    			mp.stop();
    			}
    			Intent i = new Intent(getApplicationContext(),Visualiser.class);
    			startActivityForResult(i,100);
    		}
    	
    });
    }
    	
    //playing song from playlist
    	protected void onActivityResult(int requestCode,int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == 100){
    		currentSongIndex = data.getExtras().getInt("songIndex");
    		playSong(currentSongIndex);
    		}
    	}
    	
    	//play song
    	public void playSong(int songIndex){
    		try{
    			mp.reset();
    			mp.setDataSource(songsList.get(songIndex).get("songPath"));
    			mp.prepare();
    			mp.start();
    			bPlay.setBackgroundResource(R.drawable.pause);
    			//songProgressBar.setProgress(0);
    			//songProgressBar.setMax(100);
    			
    		 updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    			//update seekBar
    	public void updateProgressBar(){
    		mHandler.postDelayed(mUpdateTimeTask, 100);
    		
    	}
   		private Runnable mUpdateTimeTask = new Runnable(){
   			public void run(){
   				long totalDuration=mp.getDuration();
   				long currentDuration=mp.getCurrentPosition();
   				int progress= (int)(utils.getProgressPercentage(currentDuration, totalDuration));
   				songProgressBar.setProgress(progress);
   				mHandler.postDelayed(this, 100);
   			}
   		};
   		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch){
   			
   		}
    	public void onStartTrackintouch(SeekBar seekBar){
    		mHandler.removeCallbacks(mUpdateTimeTask);
    	}
    	public void onStopTrackingTouch(SeekBar seekBar){
    		mHandler.removeCallbacks(mUpdateTimeTask);
    		int totalDuration = mp.getDuration();
    		int currentPosition= utils.progessToTimer(seekBar.getProgress(), totalDuration);
    		mp.seekTo(currentPosition);
    		updateProgressBar();
    	}
    	public void onCompletion(MediaPlayer arg0){
    		if(currentSongIndex<(songsList.size()-1)){
    			playSong(currentSongIndex+1);
    			currentSongIndex=currentSongIndex+1;
    		}
    		else{
    			playSong(0);
    			currentSongIndex=0;
    		}
    	}
    

    
    public void onDestroy(){
    	super.onDestroy();
    	mp.release();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

    
}

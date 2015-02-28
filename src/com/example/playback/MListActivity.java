/*
 * author: Sean Hoey x11000759
 * Date:25/9/13
*/ 
package com.example.playback;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class MListActivity extends ListActivity {
	
	public ArrayList<HashMap<String, String>> songList=new ArrayList<HashMap<String,String>>();
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		
		
		ArrayList<HashMap<String,String>> songListData= new ArrayList<HashMap<String,String>>();
		SongManager SM = new SongManager();
		this.songList = SM.getPlayList();
		for(int i=0;i<songList.size();i++){
			HashMap<String,String> song = songList.get(i);
			songListData.add(song);
			ListAdapter adapter = new SimpleAdapter(this, songListData,R.layout.playlist_item, new String[] { "songTitle" }, new int[] {R.id.songTitle });
			setListAdapter(adapter);
			
			//single List view item
			ListView lv=getListView();
			//list to song click
			lv.setOnItemClickListener(new OnItemClickListener(){
				
				public void onItemClick(AdapterView<?> parent, View view, int position,long id){
					//getting index
					int songIndex = position;
					//new intent
					Intent i = new Intent(getApplicationContext(),MainActivity.class);
					i.putExtra("songIndex",songIndex );
					setResult(100,i);
					finish();
				
					
				}
			});
		}
		
	}

}

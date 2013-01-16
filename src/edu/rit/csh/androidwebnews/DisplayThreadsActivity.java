package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class DisplayThreadsActivity extends Activity{
	
	public String newsgroupName;
	public ArrayList<Thread> threadsDirectMap;
	
	private static final int CONTENT_VIEW_ID = 10101010;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.d("MyDebugging", "newsgroupView creation started");
		
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			newsgroupName = extras.getString("SELECTED_NEWSGROUP");
		}
		else
			newsgroupName = "none";
		
		threadsDirectMap = new ArrayList<Thread>();
		
		Log.d("MyDebugging", "Selected newsgroup is " + newsgroupName);
		setContentView(R.layout.displaythreads_activity);
            	
		Log.d("MyDebugging", "newsgroupView creation finished");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_default, menu);
		return true;
	}
	
	public void viewPost(View view) {
		Thread thread = threadsDirectMap.get(((Integer)view.getTag()));
		
		Intent intent = new Intent(this, PostActivity.class);
		intent.putExtra("SELECTED_NEWSGROUP", thread.newsgroup);
		intent.putExtra("SELECTED_ID", thread.number);

		Log.d("des", "intent made");
		startActivity(intent);
	}
}
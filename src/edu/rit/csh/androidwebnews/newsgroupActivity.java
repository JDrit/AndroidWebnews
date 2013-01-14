package edu.rit.csh.androidwebnews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class newsgroupActivity extends Activity{
	
	public String newsgroupName;

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
		Log.d("MyDebugging", "Selected newsgroup is " + newsgroupName);
		setContentView(R.layout.activity_newsgroupview);
            	
		Log.d("MyDebugging", "newsgroupView creation finished");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}
	
	public void viewPost(View view) {
		
	}
}

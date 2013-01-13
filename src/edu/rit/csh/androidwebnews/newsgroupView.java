package edu.rit.csh.androidwebnews;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class newsgroupView extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.d("MyDebugging", "newsgroupView creation started");
	    setContentView(R.layout.activity_newsgroupview);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}
}

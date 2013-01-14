package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import edu.rit.csh.androidwebnews.NewsgroupsListFragment.OnNewsgroupSelectedListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HomeActivity extends Activity{
	public HttpsConnector hc;
	
	ProgressDialog p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    Log.d("MyDebugging", "App Started");
	    hc = new HttpsConnector("", this);
	    hc.markRead();
	    setContentView(R.layout.activity_home);
	    Log.d("MyDebugging", "Activity Made");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	public void onNewsgroupSelected(final String newsgroupName) {

        p = new ProgressDialog(this);
        p.setTitle("Fetching Info");
        p.setMessage("Contacting Server...");
        p.setCancelable(false);
        p.show();
        
        new java.lang.Thread()
        {
        	public void run()
        	{
        		
        
				Log.d("MyDebugging", newsgroupName + " has been selected");
				Intent myIntent = new Intent(HomeActivity.this, newsgroupActivity.class);
				myIntent.putExtra("SELECTED_NEWSGROUP", newsgroupName);
				startActivity(myIntent);
				p.dismiss();
        	}
        }.start();
		
		
	}

}
package edu.rit.csh.androidwebnews;

import java.io.Serializable;
import java.util.ArrayList;

import edu.rit.csh.androidwebnews.NewsgroupsListFragment.OnNewsgroupSelectedListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NewsgroupsListActivity extends Activity{
	public HttpsConnector hc;
	boolean contentMade = true;
	ProgressDialog p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    Log.d("MyDebugging", "App Started");
	    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	    String apiKey = sharedPref.getString("api_key", "");	    
	    
	    Log.d("jddebug", "apiKey: " + apiKey);
	    
	    hc = new HttpsConnector(apiKey, this);
	    setContentView(R.layout.activity_home);
	    	    
	    if (!hc.validApiKey()) {
	         new InvalidApiKeyDialog(this).show();
	    }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return false;
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
				Intent myIntent = new Intent(NewsgroupsListActivity.this, DisplayThreadsActivity.class);
				myIntent.putExtra("SELECTED_NEWSGROUP", newsgroupName);
				startActivity(myIntent);
				p.dismiss();
        	}
        }.start();
		
		
	}
	  private final class CancelClickListener implements
      DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int which) {
      Toast.makeText(getApplicationContext(), "Activity will continue",
          Toast.LENGTH_LONG).show();
    }
  }

  private final class SettingsClickListener implements
      DialogInterface.OnClickListener {
    public void onClick(DialogInterface dialog, int which) {
      NewsgroupsListActivity.this.finish();
    }
  }

}

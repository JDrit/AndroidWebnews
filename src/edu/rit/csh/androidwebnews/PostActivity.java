package edu.rit.csh.androidwebnews;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PostActivity extends FragmentActivity {
	
	public String newsgroupName;
	public static int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	    String apiKey = sharedPref.getString("api_key", "");	    
	    HttpsConnector hc = new HttpsConnector(apiKey, this);	    	    
	    if (!hc.validApiKey()) {
	         new InvalidApiKeyDialog(this).show();
	    }
		
		Bundle extras = getIntent().getExtras();
		newsgroupName = extras.getString("SELECTED_NEWSGROUP");
		id = extras.getInt("SELECTED_ID");		
		setContentView(R.layout.activity_post);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_default, menu);
		return true;
	}
	
	public void replyToPost(View view) {
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		
		case R.id.menu_refresh:
			
			return true;
		case R.id.menu_about:
			startActivity(new Intent(this, InfoActivity.class));
			return true;
		}
		return false;
	}
}
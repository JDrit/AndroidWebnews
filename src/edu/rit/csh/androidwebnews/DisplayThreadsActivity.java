package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class DisplayThreadsActivity extends FragmentActivity implements ActivityInterface {
	
	public String newsgroupName;
	InvalidApiKeyDialog dialog;
	public ArrayList<PostThread> threadsDirectMap;
	static public ArrayList<PostThread> lastFetchedThreads = new ArrayList<PostThread>();
	DisplayThreadsFragment dtf;
	HttpsConnector hc;
	NewsgroupListMenu newsgroupListMenu;
	public boolean requestedAdditionalThreads = false;
	SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.d("MyDebugging", "newsgroupView creation started");
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);	    
	    hc = new HttpsConnector(this);
	    dialog = new InvalidApiKeyDialog(this);
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			newsgroupName = extras.getString("SELECTED_NEWSGROUP");
		}
		else
		{
			newsgroupName = "none";
		}
		
		threadsDirectMap = new ArrayList<PostThread>();
		
		Log.d("MyDebugging", "Selected newsgroup is " + newsgroupName);
		Log.d("jddebug", "content viewed1");
		
		newsgroupListMenu = new NewsgroupListMenu(this);
		newsgroupListMenu.checkEnabled();
		
		setContentView(R.layout.displaythreads_activity);
		Log.d("jddebug", "content viewed2");
		Log.d("MyDebugging", "newsgroupView creation finished");
		
		dtf = (DisplayThreadsFragment)getSupportFragmentManager().findFragmentById(R.id.threadsfragment);
		
		if (sharedPref.getString("newsgroups_json_string", "") != "") {
			Log.d("jddebugcache", "from file");
			newsgroupListMenu.update(hc.getNewsGroupFromString(sharedPref.getString("newsgroups_json_string", "")));	
		} else {
			Log.d("jddebugcache", "from file not");
			hc.getNewsGroups();
		}
		
		hc.getNewsgroupThreads(newsgroupName, 20);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_default, menu);
		return true;
	}
	
	public void viewPost(View view) {
		PostThread thread = threadsDirectMap.get(((Integer)view.getTag()));
		PostThread selected = thread;
		
		while(thread.parent != null)
			thread=thread.parent;
		
		Intent intent = new Intent(this, PostSwipableActivity.class);
		intent.putExtra("SELECTED_NEWSGROUP", thread.newsgroup);
		intent.putExtra("SELECTED_ID", thread.number);
		intent.putExtra("GOTO_THIS", threadsDirectMap.indexOf(selected) - threadsDirectMap.indexOf(thread));

		Log.d("des", "intent made");
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		
		case R.id.menu_refresh:
			hc.getNewsgroupThreads(newsgroupName, 20);
			hc.getNewsGroups();
			return true;
			
		case R.id.menu_about:
			startActivity(new Intent(this, InfoActivity.class));
			return true;
		case R.id.menu_search:
			startActivity(new Intent(this, SearchActivity.class));
		}
		return false;
	}
	
	@Override
	public void update(String jsonString) {
		Log.d("MyDebugging","Updating displayhthreadsdsf");
		try {
			JSONObject obj = new JSONObject(jsonString);
			if (obj.has("error")) {
				if (!dialog.isShowing()) {
					dialog.show();
				}
			} else if (obj.has("posts_older")) { 
				if(!requestedAdditionalThreads)
				{
					Log.d("MyDebugging", "DisplayThreadsActivity updating the threads sdfk");
					ArrayList<PostThread> threads = hc.getThreadsFromString(jsonString);
					lastFetchedThreads.clear();
					lastFetchedThreads = (ArrayList<PostThread>) threads.clone();
					dtf.update(threads);
				}
				else
				{
					ArrayList<PostThread> newThreads = hc.getThreadsFromString(jsonString);
					for(PostThread thread : newThreads)
						lastFetchedThreads.add(thread);
					dtf.addThreads(newThreads);
					requestedAdditionalThreads = false;
				}
			} else {  // newsgroups
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString("newsgroups_json_string", jsonString);
				editor.commit();
				Log.d("jddebugcache", "update cache2");
				Log.d("MyDebugging", "DisplayThreadsActivity updating the newsgroups sdf");
				newsgroupListMenu.update(hc.getNewsGroupFromString(jsonString));
			}
		} catch (JSONException e) {
			Log.d("MyDebugging", "json error");
		}
	}

	@Override
	public void onNewsgroupSelected(String newsgroupName) {
		Intent myIntent = new Intent(DisplayThreadsActivity.this, DisplayThreadsActivity.class);
		myIntent.putExtra("SELECTED_NEWSGROUP", newsgroupName);
		startActivity(myIntent);
		finish();
	}
}
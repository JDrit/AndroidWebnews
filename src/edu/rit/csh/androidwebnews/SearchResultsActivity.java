package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;

public class SearchResultsActivity extends FragmentActivity {
	
	public static ArrayList<String> searchResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("MyDebugging", "Starting SearchResultsActivity");
		
		searchResults = new ArrayList<String>();
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			String jsonString = extras.getString("SEARCH_RESULTS");
			ArrayList<PostThread> threads = new HttpsConnector(this).getSearchFromString(jsonString);
			for(PostThread thread : threads)
				searchResults.add(thread.toString());
		}
		
		SearchListFragment sf = (SearchListFragment) getSupportFragmentManager().findFragmentById(R.id.search_list_fragment);
		
		setContentView(R.layout.activity_search_results);
		
		//sf.update(searchResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_default, menu);
		return true;
	}

}

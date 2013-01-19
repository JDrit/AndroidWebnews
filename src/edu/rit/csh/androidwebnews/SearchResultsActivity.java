package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;

public class SearchResultsActivity extends FragmentActivity {
	
	public static ArrayList<String> searchResults;
	public static ArrayList<PostThread> threads;
	public static PostThread rootThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("MyDebugging", "Starting SearchResultsActivity");
		
		searchResults = new ArrayList<String>();
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			String jsonString = extras.getString("SEARCH_RESULTS");
			threads = new HttpsConnector(this).getSearchFromString(jsonString);
			for(PostThread thread : threads)
				searchResults.add(thread.toString());
		}
		
		SearchResultsFragment sf = (SearchResultsFragment) getSupportFragmentManager().findFragmentById(R.id.search_list_fragment);
		
		setContentView(R.layout.activity_search_results);
		
		//sf.update(searchResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_default, menu);
		return true;
	}
	
	public void onSelectThread(int threadPosition)
	{
		rootThread = threads.get(0);
		
		for(int e = 1; e < threads.size(); e++)
		{
			rootThread.children.add(threads.get(e));
		}
		
		Intent intent = new Intent(this, PostSwipableActivity.class);
		intent.putExtra("SELECTED_NEWSGROUP", rootThread.newsgroup);
		intent.putExtra("SELECTED_ID", rootThread.number);
		intent.putExtra("GOTO_THIS", threadPosition);
		intent.putExtra("SEARCH_RESULTS", true);

		Log.d("des", "intent made");
		startActivity(intent);
	}

}

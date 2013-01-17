package edu.rit.csh.androidwebnews;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PostSwipableActivity extends FragmentActivity {
	
	public static String newsgroupName;
	public static int id;
	PostPagerAdapter ppa;
	ViewPager mViewPager;
	PostThread rootThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_swipable);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	    String apiKey = sharedPref.getString("api_key", "");	    
	    HttpsConnector hc = new HttpsConnector(apiKey, this);	    	    
	    if (!hc.validApiKey()) {
	         new InvalidApiKeyDialog(this).show();
	    }
		
		Bundle extras = getIntent().getExtras();
		newsgroupName = extras.getString("SELECTED_NEWSGROUP");
		id = extras.getInt("SELECTED_ID");	
		int selected_id = extras.getInt("GOTO_THIS");
		
		Log.d("MyDebugging", "PostSwipableActivity creation started");
		ppa = new PostPagerAdapter(getSupportFragmentManager());
		Log.d("MyDebugging", "ppa made");
		mViewPager = (ViewPager) findViewById(R.id.pager);
		Log.d("MyDebugging", "ViewPager found");
		mViewPager.setAdapter(ppa);
		Log.d("MyDebugging", "adapter set");
		
		mViewPager.setCurrentItem(selected_id);
		
		for(int x = 0; x < HttpsConnector.lastFetchedThreads.size(); x++)
		{
			if(HttpsConnector.lastFetchedThreads.get(x).number == id)
			{
				rootThread = HttpsConnector.lastFetchedThreads.get(x);
				Log.d("MyDebugging", "rootThread found for PostSwipableActivity");
			}
		}
		
		/*mViewPager.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

        		int currentItem = mViewPager.getCurrentItem();
        		
        		Log.d("MyDebugging", "Marking " + rootThread.getThisThread(currentItem).authorName + "'s post as read");
            	return false;
            }
		});*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_default, menu);
		return true;
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
	
	/*@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		int currentItem = mViewPager.getCurrentItem();
		
		Log.d("MyDebugging", "Marking " + rootThread.getThisThread(currentItem).authorName + "'s post as read");
		
		return super.onTouchEvent(ev);
	}*/

}
package edu.rit.csh.androidwebnews;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class PostActivity extends FragmentActivity {
	
	public String newsgroupName;
	public static int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
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
}
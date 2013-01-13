package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
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

public class HomeActivity extends Activity {
	

	private ListView mainListView ;  
	private ArrayAdapter<String> listAdapter ;  
	String[] newsgroups;
	HttpsConnector hc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_home);
		
	    /*mainListView = (ListView) findViewById( R.id.mainListView );
	    
	    hc = new HttpsConnector("7f3ab2e0545842bb", this);
	    
		ArrayList<Newsgroup> groups = hc.getNewsGroups();
		newsgroups = new String[groups.size()];
		for(int x = 0; x < groups.size(); x++)
		{
			newsgroups[x] = groups.get(x).name;
		}
		
		listAdapter = new ArrayAdapter<String>(this, R.layout.rowlayout, newsgroups);
		mainListView.setAdapter(listAdapter);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

}
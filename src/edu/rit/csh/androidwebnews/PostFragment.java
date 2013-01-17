package edu.rit.csh.androidwebnews;

import android.support.v4.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PostFragment extends Fragment {
	String body;
	PostThread myThread;
	HttpsConnector hc;
	
	public PostFragment(PostThread thread)
	{
		super();
		Log.d("MyDebugging", "Post Fragment being made");
		Log.d("MyDebugging", "newsgroup = " + thread.newsgroup);
		Log.d("MyDebugging", "Post Fragment made");
		myThread = thread;
	}
	
	public PostFragment()
	{
		super();
		Log.d("MyDebugging", "Wrong PostFragment constructor called!");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceBundle)
	{
		super.onCreate(savedInstanceBundle);
		Log.d("MyDebugging", "Post Fragment onCreate called");
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    String apiKey = sharedPref.getString("api_key", "");
		hc = new HttpsConnector(apiKey, getActivity());
		body = hc.getPostBody(myThread.newsgroup, myThread.number);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("MyDebugging", "Post Fragment onCreateView called");
		ScrollView lLayout = new ScrollView(getActivity());
		
		
		TextView tv = new TextView(getActivity());
		tv.setText(body);
		lLayout.addView(tv);
		
		
		return lLayout;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser && myThread.unread != "null")
		{
			Log.d("MyDebugging", "Marking " + myThread.authorName + "'s post as read");
			myThread.unread = "null";
			hc.markRead(myThread.newsgroup, myThread.number);
		}
	}

}
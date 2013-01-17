package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PostFragment extends ListFragment {
	ArrayList<String> contents;
	String body; 
	String otherBody;
	PostThread myThread;
	HttpsConnector hc;
	PostFragmentAdapter<String> listAdapter;
	
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
		hc.getPostBody(myThread.newsgroup, myThread.number);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ListView mainListView = new ListView(getActivity());
		mainListView.setId(android.R.id.list);
		contents = new ArrayList<String>();
		body = "";
		
    	contents.add(myThread.number + "");
		contents.add("Author Name: " + myThread.authorName);
		contents.add("Post Date: " + myThread.getDate());
		swapBodies();
		contents.add(body);
		
		listAdapter = new PostFragmentAdapter<String>(getActivity(), R.layout.rowlayout, contents);
		
		mainListView.setAdapter(listAdapter);
		
		return mainListView;
		
	}
	
	public void swapBodies()
	{
    	String temp = body;
    	body = otherBody;
    	otherBody = temp;
			
	}
	
	private void processBody()
	{
		String[] lines = body.split("\n");
    	boolean messageSet = false;
    	otherBody = "";
    	
    	for(String line : lines)
    	{
    		if(line.length() > 0)
    		{
	    		if(line.charAt(0) != '>')
	    		{
	    			otherBody += line + "\n";
	    		}
	    		else
	    		{
	
	    			if(!messageSet)
	    			{
	    				otherBody += "[Click here to show hidden text]\n";
	    				messageSet = true;
	    			}
	    		}
    		}
    		else
    			otherBody += "\n";
    	}
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
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		Log.d("MyDebugging", "Clicky!");
		if(position == 3)
		{
			swapBodies();
			contents.remove(3);
			contents.add(body);
			//listAdapter.clear();
			//listAdapter.addAll(contents);
			listAdapter.notifyDataSetChanged();
		}
	}
	
	public void update(String body)
	{
		Log.d("jddebug", "body set");
		this.body = body;
		processBody();
		contents.remove(3);
		contents.add(body);
		listAdapter.notifyDataSetChanged();
	}

}
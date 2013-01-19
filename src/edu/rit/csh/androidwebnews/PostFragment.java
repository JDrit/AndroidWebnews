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

public class PostFragment extends Fragment {
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
		contents.add("Newsgroup: " + myThread.newsgroup);
		contents.add("Author Name: " + myThread.authorName);
		contents.add("Subject: " + myThread.subject);
		contents.add("Post Date: " + myThread.getDate());
		swapBodies();
		contents.add("[Post loading...]");
		
		listAdapter = new PostFragmentAdapter<String>(getActivity(), R.layout.rowlayout, contents);
		
		mainListView.setAdapter(listAdapter);
		
		mainListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long id) {
				Log.d("MyDebugging", "Clicky!");
				if(position == 5)
				{
					swapBodies();
					contents.remove(5);
					contents.add(body);
					//listAdapter.clear();
					//listAdapter.addAll(contents);
					listAdapter.notifyDataSetChanged();
				}
			}
		});
		
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
		String[] lines = otherBody.split("\n");
    	boolean messageSet = false;
    	body = "";
    	
    	for(String line : lines)
    	{
    		if(line.length() > 0)
    		{
	    		if(line.charAt(0) != '>')
	    		{
	    			body += line + "\n";
	    		}
	    		else
	    		{
	
	    			if(!messageSet)
	    			{
	    				body += "[Tap here to show hidden text]\n";
	    				messageSet = true;
	    			}
	    		}
    		}
    		else
    			body += "\n";
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
	
	/*@Override
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
	}*/
	
	public void update(String jsonstuff)
	{
		String otherBody = hc.getPostBodyFromString(jsonstuff);
		Log.d("jddebug", "body set");
		this.otherBody = otherBody;
		processBody();
		//swapBodies();
		contents.remove(5);
		contents.add(body);
		listAdapter.notifyDataSetChanged();
	}

}
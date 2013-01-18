package edu.rit.csh.androidwebnews;

import java.util.ArrayList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PostPagerAdapter extends FragmentStatePagerAdapter {

	PostThread rootThread;
	PostFragment[] fragments;
	int id;
	
	public PostPagerAdapter(FragmentManager fm) {
		super(fm);
		Log.d("MyDebugging", "ppa creation started");
		id = PostSwipableActivity.id;
		Log.d("MyDebugging", "id retrieved. id = " + id);
		for(int x = 0; x < DisplayThreadsActivity.lastFetchedThreads.size(); x++)
		{
			if(DisplayThreadsActivity.lastFetchedThreads.get(x).number == id)
			{
				rootThread = DisplayThreadsActivity.lastFetchedThreads.get(x);
				Log.d("MyDebugging", "rootThread found");
			}
		}
		if(rootThread == null)
			Log.d("MyDebugging", "rootThread is null!");
		
		fragments = new PostFragment[getCount()];
	}
	
	@Override
    public android.support.v4.app.Fragment getItem(int i) {
		Log.d("MyDebugging", "item " + i + " requested");
        PostFragment fragment = new PostFragment(rootThread.getThisThread(i));
		Log.d("MyDebugging", "Fragment initialized");
        Bundle args = new Bundle();
		Log.d("MyDebugging", "Bundle initialized");
        fragment.setArguments(args);
		Log.d("MyDebugging", "Fragment returned");
		
		fragments[i] = fragment;
        
        return fragment;
    }

	@Override
	public int getCount() {
		return rootThread.getSubThreadCount();
	}

	@Override
    public CharSequence getPageTitle(int position) {
		printT(rootThread);
        return rootThread.getThisThread( position).toString();
    }
	
	
	
	public void printT(PostThread t)
	{
		if(t.parent != null)
			Log.d("MyDebugging", t.authorName + ", " + t.parent.authorName);
		else
		{
			Log.d("MyDebugging", t.authorName);
		}
		for(PostThread thread : t.children)
		{
			printT(thread);
		}
	}

	public void update(String jsonString) {
		int id = 0;
		try {
			id = new JSONObject(jsonString).getJSONObject("post").getInt("number");
		} catch (JSONException e) {
		}
		
		for(PostFragment f : fragments)
		{
			if(f != null)
			{
				if(f.myThread.number == id)
				{
					Log.d("MyDebugging", "Updating thread " + id);
					f.update(jsonString);
				}
			}
		}
	}
}
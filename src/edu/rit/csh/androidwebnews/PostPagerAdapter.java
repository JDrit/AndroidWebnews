package edu.rit.csh.androidwebnews;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PostPagerAdapter extends FragmentStatePagerAdapter {

	Thread rootThread;
	int id;
	
	public PostPagerAdapter(FragmentManager fm) {
		super(fm);
		Log.d("MyDebugging", "ppa creation started");
		id = PostSwipableActivity.id;
		Log.d("MyDebugging", "id retrieved. id = " + id);
		for(int x = 0; x < HttpsConnector.lastFetchedThreads.size(); x++)
		{
			Log.d("MyDebugging",HttpsConnector.lastFetchedThreads.get(x).number + "scdsd");
			if(HttpsConnector.lastFetchedThreads.get(x).number == id)
			{
				rootThread = HttpsConnector.lastFetchedThreads.get(x);
				Log.d("MyDebugging", "rootThread found");
			}
		}
		if(rootThread == null)
			Log.d("MyDebugging", "rootThread is null!");
	}
	
	@Override
    public android.support.v4.app.Fragment getItem(int i) {
		Log.d("MyDebugging", "item " + i + " requested");
		printT(rootThread);
		Log.d("MyDebugging", "getting fragment for " + rootThread.getThisThread(i).toString());
        PostFragment fragment = new PostFragment(rootThread.getThisThread(i));
		Log.d("MyDebugging", "Fragment initialized");
        Bundle args = new Bundle();
		Log.d("MyDebugging", "Bundle initialized");
        fragment.setArguments(args);
		Log.d("MyDebugging", "Fragment returned");
        
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
	
	
	
	public void printT(Thread t)
	{
		if(t.parent != null)
			Log.d("MyDebugging", t.authorName + ", " + t.parent.authorName);
		else
		{
			Log.d("MyDebugging", t.authorName);
		}
		for(Thread thread : t.children)
		{
			printT(thread);
		}
	}

}
package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DisplayThreadsFragment extends Fragment {
	String newsgroupName;
	ArrayList<Thread> threads;
	boolean[] threadStatus;
	ArrayList<String> displayedStrings;
	WebnewsListAdapter<String> listAdapter;
	int[] extraEntries;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		Log.d("MyDebugging", "Starting ThreadsListFragment constructor");
		newsgroupName = ((DisplayThreadsActivity)getActivity()).newsgroupName;
		
		ListView mainListView = new ListView(getActivity());
		
	    HttpsConnector hc = new HttpsConnector("cf9508708020f73a", getActivity());
	    
	    threads = hc.getNewsgroupThreads(newsgroupName, 20);
	    
	    for(Thread thread : threads)
	    {
	    	((DisplayThreadsActivity)getActivity()).threadsDirectMap.add(thread);
	    }
	    
	    displayedStrings = new ArrayList<String>();
	    threadStatus = new boolean[20];
	    extraEntries = new int[20];
	    
	    for(int x = 0; x < 20; x++)
	    {
	    	displayedStrings.add(threads.get(x).toString());
	    	threadStatus[x] = false;
	    	extraEntries[x] = 0;
	    }
		Log.d("MyDebugging", "displayedStrings populated");
	    
	    listAdapter = new WebnewsListAdapter<String>(getActivity(), R.layout.threadlayout, displayedStrings);
		Log.d("MyDebugging", "list adapter made");
	    
	    mainListView.setAdapter(listAdapter);
		Log.d("MyDebugging", "listadapter set");
		mainListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long id) {
				String value = (String) adapter.getItemAtPosition(position);
				if(threadStatus[position])
				{
					for(int x = 0; x < threads.get(position).getSubThreadCount(); x++)
					{
						displayedStrings.remove(position + 1);
						((DisplayThreadsActivity)getActivity()).threadsDirectMap.remove(position + 1);
					}
					extraEntries[position] = 0;
					listAdapter.notifyDataSetChanged();
					threadStatus[position] = false;					
				}
				else
				{
					expandThread(threads.get(position), position);
					listAdapter.notifyDataSetChanged();
					threadStatus[position] = true;					
				}
			}
			
		});
		Log.d("MyDebugging", "ThreadsListFragment made");
	    
	    return mainListView;
	}
	
	public void expandThread(Thread thread, int pos)
	{
		for(Thread childThread : thread.children)
		{
			displayedStrings.add(pos + 1, "||" + childThread.toString());
			extraEntries[pos] += 1;
			((DisplayThreadsActivity)getActivity()).threadsDirectMap.add(pos, childThread);
			if(childThread.children.size() != 0)
				expandThread(childThread, pos, 2);
		}
	}
	
	private void expandThread(Thread thread, int pos, int level)
	{
		String temp = "";
		for(int i = 0; i < level; i++)
			temp += "||";
		for(Thread childThread : thread.children)
		{
			displayedStrings.add(pos + 1, temp + childThread.toString());
			extraEntries[pos] += 1;
			((DisplayThreadsActivity)getActivity()).threadsDirectMap.add(pos, childThread);
			if(childThread.children.size() != 0)
				expandThread(childThread, pos);
		}
	}
}
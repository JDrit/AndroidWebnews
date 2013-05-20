/**
See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  This code is licensed
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/		
package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DisplayThreadsFragment extends Fragment implements OnScrollListener {
	String newsgroupName;
	ArrayList<PostThread> threads;
	ArrayList<PostThread> rootThreads;
	boolean[] threadStatus;
	DisplayThreadsListAdapter<PostThread> listAdapter;
	int[] extraEntries;
	HttpsConnector hc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		Log.d("MyDebugging", "Starting ThreadsListFragment constructor");
		newsgroupName = ((DisplayThreadsActivity)getActivity()).newsgroupName;

		NewsgroupListMenu newsgroupListMenu = ((DisplayThreadsActivity)getActivity()).newsgroupListMenu;
		
		WebnewsListView mainListView = new WebnewsListView(getActivity(), newsgroupListMenu);
		mainListView.setId(android.R.id.list);
		mainListView.setOnScrollListener(this);

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    String apiKey = sharedPref.getString("api_key", "");
		hc = new HttpsConnector(getActivity());
		//hc.getNewsgroupThreads(newsgroupName, 20);
	    threads = new ArrayList<PostThread>();//hc.getNewsgroupThreads(newsgroupName, 20);
	    rootThreads = new ArrayList<PostThread>();
	    
	    for(PostThread thread : threads)
	    	rootThreads.add(thread);

	    for(PostThread thread : threads)
	    {
	    	((DisplayThreadsActivity)getActivity()).threadsDirectMap.add(thread);
	    }

	    threadStatus = new boolean[20];
	    extraEntries = new int[20];

	    for(int x = 0; x < 20 ; x++)
	    {
	    	threadStatus[x] = false;
	    	extraEntries[x] = 0;
	    }
		Log.d("MyDebugging", "displayedStrings populated");

	    listAdapter = new DisplayThreadsListAdapter<PostThread>(getActivity(), R.layout.threadlayout, threads, this);
		Log.d("MyDebugging", "list adapter made");
		
		
		
	    mainListView.setAdapter(listAdapter);
		Log.d("MyDebugging", "listadapter set"); 
		mainListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long id) {
				((DisplayThreadsActivity)getActivity()).viewPost(position);
			}

		});
		Log.d("MyDebugging", "ThreadsListFragment made");
		
	    return mainListView;
	}
	
	public void onArrowClick(int position)
	{
		int originalPos = -1;
		if(position < threads.size() - 1)
			originalPos = findOriginalPos(((DisplayThreadsActivity)getActivity()).threadsDirectMap.get(position));
		if(originalPos > -1)
		{
			if(threadStatus[originalPos])
			{
				for(int x = 0; x < extraEntries[originalPos]; x++)
				{
					threads.remove(position + 1);
					((DisplayThreadsActivity)getActivity()).threadsDirectMap.remove(position + 1);
				}
				extraEntries[originalPos] = 0;
				listAdapter.clear();
				listAdapter.addAll(threads);
				listAdapter.notifyDataSetChanged();
				threadStatus[originalPos] = false;					
			}
			else
			{
				
				expandThread(rootThreads.get(originalPos), position);
				listAdapter.clear();
				listAdapter.addAll(threads);
				listAdapter.notifyDataSetChanged();
				threadStatus[originalPos] = true;					
			}
		}
	}

	public void expandThread(PostThread thread, int pos)
	{
		for(int x = thread.getChildren().size() - 1; x > -1; x--)
		{
			PostThread childThread = thread.getChildren().get(x);
			int originalPos = findOriginalPos(thread);
			if(originalPos > -1)
			{
				if(childThread.getChildren().size() != 0) {
					Log.d("output", childThread.getDepth() + childThread.getAuthorName());
					expandThread(childThread, originalPos, pos, 2);
				}
				
				threads.add(pos + 1, childThread);
				extraEntries[originalPos] += 1;
				((DisplayThreadsActivity)getActivity()).threadsDirectMap.add(pos+1, childThread);
				//pos += thread.children.size();
			}
		}
	}

	private void expandThread(PostThread thread, int originalPos, int pos, int level)
	{
		String temp = "";
		for(int i = 0; i < level; i++)
			temp += "||";
		for(int x = thread.getChildren().size() - 1; x > -1; x--)
		{
			PostThread childThread = thread.getChildren().get(x);
			if(childThread.getChildren().size() != 0) {
				Log.d("output", childThread.getDepth() + childThread.getAuthorName());
				expandThread(childThread, originalPos, pos, level + 1);
				
			}
			
			threads.add(pos + 1, childThread);
			extraEntries[originalPos] += 1;
			((DisplayThreadsActivity)getActivity()).threadsDirectMap.add(pos+1, childThread);
			//pos += thread.children.size();
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if(listAdapter != null)
		{
			//hc.getNewsGroups();
			listAdapter.clear();
			listAdapter.addAll(threads);
			listAdapter.notifyDataSetChanged();
		}
	}
	
	public void update(ArrayList<PostThread> threads)
	{
		this.threads = threads;
		rootThreads.clear();
        if(getActivity() instanceof DisplayThreadsActivity)
        {
            ((DisplayThreadsActivity)getActivity()).threadsDirectMap.clear();
            for(PostThread thread : threads)
            {
                rootThreads.add(thread);
                ((DisplayThreadsActivity)getActivity()).threadsDirectMap.add(thread);
            }
            for(int d = 0; d < threadStatus.length; d++)
                threadStatus[d] = false;
            for(int l = 0; l < extraEntries.length; l++)
                extraEntries[l] = 0;

            // Opens threads with unread posts in them
                    ArrayList<Integer> toOpenIndexes = new ArrayList<Integer>(); // list of indexes to open
                    for (int i = threads.size() - 1 ; i >= 0  ; i--) {
                        Log.d("ints", threads.size() + "");
                        if (threads.get(i).containsUnread()) {
                            int originalPos = findOriginalPos(((DisplayThreadsActivity)getActivity()).threadsDirectMap.get(i));
                            Log.d("ints", originalPos + ":" + i);
                            expandThread(threads.get(originalPos), i);
                            listAdapter.notifyDataSetChanged();
                            threadStatus[originalPos] = true;
                        }

                    }
                    // Opens the unread posts in the list of indexes
                    for (Integer i : toOpenIndexes) {
                        int originalPos = findOriginalPos(((DisplayThreadsActivity)getActivity()).threadsDirectMap.get(i));
                        Log.d("ints", originalPos + ":" + i);
                        expandThread(threads.get(originalPos), i);
                        listAdapter.notifyDataSetChanged();
                        threadStatus[originalPos] = true;
                    }

            listAdapter.clear();
            listAdapter.addAll(threads);
            listAdapter.notifyDataSetChanged();
        }
	}

	private int findOriginalPos(PostThread thread)
	{
		Log.d("MyDebugging", thread.getAuthorName() + "'s original pos requested");
		for(int x = 0; x < rootThreads.size(); x++)
			if(rootThreads.get(x).Equals(thread))
			{
				Log.d("MyDebugging", "returning " + x);
				return x;
			}
		return -1;
	}
	
	public void addThreads(ArrayList<PostThread> newThreads)
	{
		boolean[] newThreadStatus = new boolean[threadStatus.length + newThreads.size()];
		int[] newExtraEntries = new int[extraEntries.length + newThreads.size()];
		
		for(int p = 0; p < threadStatus.length; p++)
			newThreadStatus[p] = threadStatus[p];
		for(int c = 0; c < extraEntries.length; c++)
			newExtraEntries[c] = extraEntries[c];
		
		for(PostThread thread : newThreads)
		{
			rootThreads.add(thread);
			threads.add(thread);
			((DisplayThreadsActivity)getActivity()).threadsDirectMap.add(thread);
		}
		threadStatus = newThreadStatus;
		extraEntries = newExtraEntries;
		
		listAdapter.clear();
		listAdapter.addAll(threads);
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
		int visibleItemCount, int totalItemCount) {
		if(view.getId() == android.R.id.list) {
			int lastItem = firstVisibleItem + visibleItemCount;
			if(lastItem == totalItemCount && threads != null && threads.size() != 0 && 
					!((DisplayThreadsActivity)getActivity()).requestedAdditionalThreads) {// &&
					//!DisplayThreadsActivity.hitBottom) {
				Log.d("MyDebugging","Asking for posts in " + newsgroupName + " older than " + threads.get(threads.size() - 1).getDate() );
				hc.getNewsgroupThreadsByDate(newsgroupName, threads.get(threads.size() - 1).getDate(), 10);
				((DisplayThreadsActivity)getActivity()).requestedAdditionalThreads = true;
			}
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ThreadsListFragment extends Fragment {
	String newsgroupName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		newsgroupName = ((newsgroupView)getActivity()).newsgroupName;
		
		ListView mainListView = new ListView(getActivity());
		Log.d("MyDebugging", "Starting connection");
	    
	    HttpsConnector hc = new HttpsConnector("674db99369408b6a", getActivity());
		Log.d("MyDebugging", "Beginning thread fetching for " + newsgroupName);
	    
		ArrayList<Thread> threads = hc.getNewsgroupThreads(newsgroupName, 10);
		Log.d("MyDebugging", "Constructing String[] to be of size " + threads.size());
		String[] listEntries = new String[threads.size()];
		Log.d("MyDebugging", "Beginning list population");
		for(int x = 0; x < threads.size(); x++)
		{
			Log.d("MyDebugging", "Item " + x + " entered");
			listEntries[x] = threads.get(x).authorName + ": " + threads.get(x).subject;
		}

		Log.d("MyDebugging", "S");
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, listEntries);
		mainListView.setAdapter(listAdapter);
		
		return mainListView;
	
	}

}

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

public class ThreadsListFragment extends Fragment {
	String newsgroupName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		newsgroupName = ((newsgroupActivity)getActivity()).newsgroupName;
		
		ExpandableListView mainListView = new ExpandableListView(getActivity());
		Log.d("MyDebugging", "Starting connection");
		ArrayList<Thread> threads;
		
		if (newsgroupName != "none")
		{
		    HttpsConnector hc = new HttpsConnector("2bb1150a2cac0ab0", getActivity());
			Log.d("MyDebugging", "Beginning thread fetching for " + newsgroupName);
		    
			threads = hc.getNewsgroupThreads(newsgroupName, 20);
			
		}
		else
		{
			threads = new ArrayList<Thread>();
		}

		Log.d("MyDebugging", "Making Adapter");
		WebnewsListAdapter listAdapter = new WebnewsListAdapter(getActivity(), mainListView, threads);
		Log.d("MyDebugging", "Setting Adapter");
		mainListView.setAdapter(listAdapter);
		Log.d("MyDebugging", "Fragment made");
		
		mainListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long id) {
				Log.d("MyDebugging", "Clicky!");
				String value = (String) adapter.getItemAtPosition(position);
				((HomeActivity)getActivity()).onNewsgroupSelected(value);
			}
			
		});
		return mainListView;
	
	}

}

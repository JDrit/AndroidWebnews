package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ThreadsListFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		String newsgroupName = null;
		
		ListView mainListView = new ListView(getActivity());
	    
	    HttpsConnector hc = new HttpsConnector("7f3ab2e0545842bb", getActivity());
	    
		ArrayList<Thread> groups = hc.getNewsgroupThreads(newsgroupName, 10);
		String[] newsgroups = new String[groups.size()];
		for(int x = 0; x < groups.size(); x++)
		{
			newsgroups[x] = groups.get(x).authorName + ": " + groups.get(x).subject;
		}
		
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, newsgroups);
		mainListView.setAdapter(listAdapter);
		
		return mainListView;
	
	}

}

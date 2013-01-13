package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewsgroupsListFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ListView mainListView = new ListView(getActivity());
	    
	    HttpsConnector hc = new HttpsConnector("7f3ab2e0545842bb", getActivity());
	    
		ArrayList<Newsgroup> groups = hc.getNewsGroups();
		String[] newsgroups = new String[groups.size()];
		for(int x = 0; x < groups.size(); x++)
		{
			newsgroups[x] = groups.get(x).name;
		}
		
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, newsgroups);
		mainListView.setAdapter(listAdapter);
		
		return mainListView;
	
	}

}

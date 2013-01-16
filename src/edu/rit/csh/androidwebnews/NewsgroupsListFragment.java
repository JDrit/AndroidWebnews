
package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewsgroupsListFragment extends ListFragment {
	
	//Called when the view for the fragment is to be first drawn.
	//Makes a new HttpsConnector to get list of Newsgroups, which
	//are used to populate the entries in a ListView.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ListView mainListView = new ListView(getActivity());
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    String apiKey = sharedPref.getString("api_key", "");

		HttpsConnector hc = new HttpsConnector(apiKey, getActivity());
		
		
	    
	    ArrayList<Newsgroup> groups = hc.getNewsGroups();
		String[] newsgroups = new String[groups.size()];
		
		for(int x = 0; x < groups.size(); x++)
		{
			newsgroups[x] = groups.get(x).name;
		}
		
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, newsgroups);
		mainListView.setAdapter(listAdapter);
		
		Log.d("MyDebugging", "Setting click listener");
		mainListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long id) {
				Log.d("MyDebugging", "Clicky!");
				String value = (String) adapter.getItemAtPosition(position);
				((NewsgroupsListActivity)getActivity()).onNewsgroupSelected(value);
			}
			
		});
		
		return mainListView;
	
	}
	
	public interface OnNewsgroupSelectedListener {
        public void onNewsgroupSelected(String newsgroupName);
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		String selectedGroup = (String) getListView().getItemAtPosition(position);
		((NewsgroupsListActivity)getActivity()).onNewsgroupSelected(selectedGroup);
	}

}
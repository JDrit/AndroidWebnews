
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
	HttpsConnector hc;
	ArrayList<Newsgroup> groups;
	NewsgroupsListAdapter<Newsgroup> listAdapter;
	
	//Called when the view for the fragment is to be first drawn.
	//Makes a new HttpsConnector to get list of Newsgroups, which
	//are used to populate the entries in a ListView.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ListView mainListView = new ListView(getActivity());
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    String apiKey = sharedPref.getString("api_key", "");
		hc = new HttpsConnector(apiKey, getActivity());
		hc.getNewsGroups();
		
	    
	    //groups = hc.getNewsGroups();
		groups = new ArrayList<Newsgroup>();
		
		listAdapter = new NewsgroupsListAdapter<Newsgroup>(getActivity(), R.layout.rowlayout, groups);
		
		
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
	
	public void update(ArrayList<Newsgroup> groups)
	{
		this.groups = groups;
		listAdapter.clear();
		listAdapter.addAll(groups);
		listAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		Log.d("MyDebugging","Refreshing newsgroups!");
		hc.getNewsGroups();
	}
	
	public interface OnNewsgroupSelectedListener {
        public void onNewsgroupSelected(String newsgroupName);
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		String selectedGroup = ((Newsgroup) getListView().getItemAtPosition(position)).name;
		((NewsgroupsListActivity)getActivity()).onNewsgroupSelected(selectedGroup);
	}

}
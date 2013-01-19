
package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.app.Fragment;
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

public class SearchListFragment extends Fragment {
	HttpsConnector hc;
	ArrayList<String> threads;
	ArrayAdapter<String> listAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		threads = SearchResultsActivity.searchResults;
		
		Log.d("MyDebugging","Starting SearchListFragment view creation");
		ListView mainListView = new ListView(getActivity());
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    String apiKey = sharedPref.getString("api_key", "");
		hc = new HttpsConnector(apiKey, getActivity());
		//hc.getNewsGroups();
		
		listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, threads);
		
		
		mainListView.setAdapter(listAdapter);
		
		Log.d("MyDebugging", "Setting click listener");
		mainListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long id) {
				Log.d("MyDebugging", "Clicky!");
				String value = (String) adapter.getItemAtPosition(position);
				((SearchActivity)getActivity()).onSelectThread(value);
			}
			
		});
		Log.d("MyDebugging","SearchListFragment view creation done");
		
		return mainListView;
	
	}
	
	public void update(ArrayList<String> threads)
	{
		if(listAdapter != null)
		{
			this.threads = threads;
			listAdapter.clear();
			for(String s : threads)
				listAdapter.add(s);
			listAdapter.notifyDataSetChanged();
		}
	}

}
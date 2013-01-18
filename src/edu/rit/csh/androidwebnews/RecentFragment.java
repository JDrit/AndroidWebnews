package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RecentFragment extends Fragment {
	ListView lv;
	RecentListAdapter<PostThread> listAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("jddebug", "frag");
		lv = new ListView(getActivity());;
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    String apiKey = sharedPref.getString("api_key", "");
		HttpsConnector hc = new HttpsConnector(apiKey, getActivity());
		
		hc.getNewest();
		
		listAdapter = new RecentListAdapter<PostThread>(getActivity(), R.layout.rowlayout, new ArrayList<PostThread>());
		lv.setAdapter(listAdapter);
		

		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long id) {
				PostThread thread = (PostThread) adapter.getItemAtPosition(position);
				((RecentActivity)getActivity()).onThreadSelected(thread);
				
			}
				

		});
		
		
		
		return lv;
	}
	

	public void update(ArrayList<PostThread> newestFromString) {
		listAdapter.clear();
		listAdapter.addAll(newestFromString);
		listAdapter.notifyDataSetChanged();
		
	}
}

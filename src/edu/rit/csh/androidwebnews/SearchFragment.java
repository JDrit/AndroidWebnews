package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Spinner;

public class SearchFragment extends Fragment {
	View view;
	ArrayList<Newsgroup> newsgroups;
	ArrayAdapter<String> listAdapter;
	ArrayList<String> groupNames;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.search_fragment, container, false);
		newsgroups = new ArrayList<Newsgroup>();
		groupNames = new ArrayList<String>();
		
		for (Newsgroup ng : newsgroups) {
			groupNames.add(ng.name);
		}
		
		listAdapter = new ArrayAdapter<String>(getActivity(), R.id.search_newsgroup_list, groupNames);
		((Spinner) view.findViewById(R.id.search_newsgroup_list)).setAdapter(listAdapter);
		return view;
	}

	public void update(ArrayList<Newsgroup> newsgroups) {
		Log.d("jddebug", "update frag");
		this.newsgroups = newsgroups;
		Log.d("jddebug", "update frag2");
		listAdapter.clear();
		Log.d("jddebug", "update frag3");
		for (Newsgroup ng : newsgroups) {
			listAdapter.add(ng.name);
		}
//		/listAdapter.notifyDataSetChanged();
		Log.d("jddebug", "update frag4");
		
	}

}

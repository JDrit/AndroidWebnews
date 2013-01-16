package edu.rit.csh.androidwebnews;

import android.support.v4.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PostFragment extends Fragment {
	String newsgroupName, body;
	int id;
	
	public PostFragment(int id)
	{
		super();
		Log.d("MyDebugging", "Post Fragment being made");
		Log.d("MyDebugging", "newsgroup = " + newsgroupName);
		Log.d("MyDebugging", "id = " + id);
		newsgroupName = PostSwipableActivity.newsgroupName;
		this.id = id;
		Log.d("MyDebugging", "Post Fragment made");
	}
	
	public PostFragment()
	{
		super();
		Log.d("MyDebugging", "Wrong PostFragment constructor called!");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceBundle)
	{
		super.onCreate(savedInstanceBundle);
		Log.d("MyDebugging", "Post Fragment onCreate called");
		
		HttpsConnector hc = new HttpsConnector("4d345e7051de48d0",getActivity());
		body = hc.getPostBody(newsgroupName, id);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("MyDebugging", "Post Fragment onCreateView called");
		ScrollView lLayout = new ScrollView(getActivity());
		
		
		TextView tv = new TextView(getActivity());
		tv.setText(body);
		lLayout.addView(tv);
		
		
		return lLayout;
	}

}

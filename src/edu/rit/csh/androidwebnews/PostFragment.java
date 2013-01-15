package edu.rit.csh.androidwebnews;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostFragment extends Fragment {
	String newsgroupName, body;
	int id;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout lLayout = new LinearLayout(getActivity());
		
		newsgroupName = ((PostActivity) getActivity()).newsgroupName;
		id = ((PostActivity) getActivity()).id;
		
		HttpsConnector hc = new HttpsConnector("cf9508708020f73a", getActivity());
		Log.d("name + id", newsgroupName + " - " + Integer.valueOf(id).toString());
		body = hc.getPostBody(newsgroupName, id);
		TextView tv = new TextView(getActivity());
		tv.setText(body);
		lLayout.addView(tv);
		
		
		return lLayout;
	}

}


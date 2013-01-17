package edu.rit.csh.androidwebnews;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The adapter used to display the newest post.
 */
public class RecentListAdapter<T> extends ArrayAdapter<T> {
	Context context;
	
	public RecentListAdapter(Context context, int textViewResourceId, List<T> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PostThread thread = ((PostThread) getItem(position));
		LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = infalInflater.inflate(R.layout.rowlayout, null);
		TextView tv = (TextView) convertView.findViewById(R.id.rowTextView);
		tv.setText(thread.newsgroup + " : " + thread.toString());
		if (thread.unread != "null") {
			tv.setTypeface(null, Typeface.BOLD);
		}
		return convertView;
	}
}

package edu.rit.csh.androidwebnews;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * The Adapter used for displaying the list of threads. This is used so that
 * unread threads can be bolded and marked different colors
 * @param <T>
 */
public class DisplayThreadsListAdapter<T> extends ArrayAdapter<T> {
	Context context;

	public DisplayThreadsListAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Thread thread = ((Thread)getItem(position));
		LayoutInflater infalInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.threadlayout, null);
        convertView.setPadding(30 * thread.depth + 10, 10, 10, 10);		
		String text = thread.toString();
		Log.d("thread depth", thread.depth + " " + thread.authorName);
		TextView tv = (TextView) convertView.findViewById(R.id.threadtextview);

		
        tv.setPadding(0,0,140,0);
        if (thread.unread != "null") {
        	tv.setText(text);
        	tv.setTypeface(null, Typeface.BOLD);
        } else {
        	tv.setText(text);
        }
        ((Button) convertView.findViewById(R.id.Viewbutton)).setTag(position);
        return convertView;
	}
}
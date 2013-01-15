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

public class WebnewsListAdapter<T> extends ArrayAdapter<T> {
	Context context;

	public WebnewsListAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		String text = (String)getItem(position);
		int padding = 0;
		while(text.charAt(0) == '|')
		{
			Log.d("MyDebugging", text);
			text = text.substring(1);
			padding++;
		}
		LayoutInflater infalInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.threadlayout, null);
        convertView.setPadding(30 * padding + 10, 10, 10, 10);
        ((TextView)convertView.findViewById(R.id.threadtextview)).setPadding(0,0,140,0);
        ((TextView)convertView.findViewById(R.id.threadtextview)).setText(text);
        ((Button) convertView.findViewById(R.id.Viewbutton)).setTag(position);
        return convertView;
	}
}
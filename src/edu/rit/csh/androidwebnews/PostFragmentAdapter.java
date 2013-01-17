package edu.rit.csh.androidwebnews;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostFragmentAdapter<T> extends ArrayAdapter<T> {
	
	public PostFragmentAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(position == 0)
		{
			LayoutInflater infalInflater = (LayoutInflater) getContext()
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView = infalInflater.inflate(R.layout.postbuttons, null);
	        convertView.setPadding(10, 10, 10, 10);
	        ((Button) convertView.findViewById(R.id.replyButton)).setTag(getItem(0));
	        ((Button) convertView.findViewById(R.id.unreadButton)).setTag(getItem(0));
	        return convertView;
		}
		LayoutInflater infalInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.rowlayout, null);
        convertView.setPadding(10, 10, 10, 10);
        TextView tv = ((TextView) convertView.findViewById(R.id.rowTextView));
        
        	tv.setText((String) getItem(position));
        
        
        return convertView;
	}
	
	@Override
	public int getCount()
	{
		return super.getCount();
	}

}

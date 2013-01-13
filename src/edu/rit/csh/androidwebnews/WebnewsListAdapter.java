package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WebnewsListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private ExpandableListView mExpandableListView;
	private ArrayList<Thread> threads;
	private int[] threadStatus;
	
	public WebnewsListAdapter(Context pContext, ExpandableListView pExpandableListView, ArrayList<Thread> threads)
	{
		mContext = pContext;
		mExpandableListView = pExpandableListView;
		this.threads = threads;
		threadStatus = new int[threads.size()];
		
		setListEvent();
	}
	
	private void setListEvent()
	{
		Log.d("MyDebugging", "setListEvent called");
		mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener()
		{

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				threadStatus[arg0] = 1;
			}
			
		});
		
		mExpandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int arg0) {
				threadStatus[arg0] = 0;
			}
			
		});
		Log.d("MyDebugging", "setListEvent finished");
	}

	@Override
    public Thread getChild(int groupPosition, int childPosition) {
        return threads.get(groupPosition).children.get(childPosition);
    }

	@Override
	public long getChildId(int arg0, int arg1) {
		Log.d("MyDebugging", "getChildId called");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		Log.d("MyDebugging", "getChildView called");
		
		Thread child = getChild(groupPosition, childPosition);
		
		if(convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.rowlayout, null);
            convertView.setPadding(70, 10, 10, 10);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.rowTextView);
		tv.setText(threads.get(groupPosition).children.get(childPosition).authorName + ": " + threads.get(groupPosition).children.get(childPosition).subject);
		
		// Depending upon the child type, set the imageTextView01
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        /*if (vehicle instanceof Car) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.car, 0, 0, 0);
        } else if (vehicle instanceof Bus) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bus, 0, 0, 0);
        } else if (vehicle instanceof Bike) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bike, 0, 0, 0);
        }*/
        return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return threads.get(arg0).children.size();
	}

	@Override
	public Thread getGroup(int arg0) {
		return threads.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return threads.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Log.d("MyDebugging", "getGroupView called");
		
		Thread thread = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.rowlayout, null);
            convertView.setPadding(70, 10, 10, 10);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.rowTextView);
        tv.setText(thread.authorName + ": " + thread.subject);
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}

package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WebnewsListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private WebnewsExpandableListView mExpandableListView;
	private ArrayList<Thread> threads;
	private int[] threadStatus;
	private int level = 1;
	
	public WebnewsListAdapter(Context pContext, WebnewsExpandableListView pExpandableListView, ArrayList<Thread> threads)
	{
		mContext = pContext;
		mExpandableListView = pExpandableListView;
		this.threads = threads;
		threadStatus = new int[threads.size()];
		
		setListEvent();
	}
	
	public WebnewsListAdapter(Context pContext, WebnewsExpandableListView pExpandableListView, ArrayList<Thread> threads, int level)
	{
		mContext = pContext;
		mExpandableListView = pExpandableListView;
		this.threads = threads;
		threadStatus = new int[threads.size()];
		this.level = level;
		
		setListEvent();
	}
	
	private void setListEvent()
	{
		Log.d("MyDebugging", "setListEvent called");
		mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener()
		{

			@Override
			public void onGroupExpand(int arg0) {
				threadStatus[arg0] = 1;
				updateHeight();
			}
			
		});
		
		mExpandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int arg0) {
				threadStatus[arg0] = 0;
				updateHeight();
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
		
		/*if(convertView == null)
		{
			if (child.children.size() == 0)
			{
				LayoutInflater infalInflater = (LayoutInflater) mContext
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.threadlayout, null);
	            convertView.setPadding(70 * (level + 1), 10, 10, 10);
            }
			else
			{*/
				WebnewsExpandableListView mainListView = new WebnewsExpandableListView(mExpandableListView.getContext());
				mainListView.setTag(child);
				ArrayList<Thread> threads = child.children;
				WebnewsListAdapter listAdapter = new WebnewsListAdapter(mContext, mainListView, threads, level + 1);
				mainListView.setAdapter(listAdapter);
				return mainListView;
			/*}
		}
		if(getChild(groupPosition, childPosition).children.size() == 0)
		{
			TextView tv = (TextView) convertView.findViewById(R.id.threadtextview);
			tv.setText(threads.get(groupPosition).children.get(childPosition).authorName + ": " + threads.get(groupPosition).children.get(childPosition).subject);
			
	        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

	        return tv;
		}
		else
		{
			WebnewsExpandableListView myList = (WebnewsExpandableListView) convertView.findViewWithTag(child);
			ArrayList<Thread> threads = child.children;
			WebnewsListAdapter listAdapter = new WebnewsListAdapter(mContext, myList, threads, level + 1);
			myList.setAdapter(listAdapter);
			return myList;
		}*/
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
            convertView = infalInflater.inflate(R.layout.threadlayout, null);
            convertView.setPadding(70 * level, 10, 10, 10);
            ((TextView)convertView.findViewById(R.id.threadtextview)).setText(thread.authorName + ": " + thread.subject);
            ((Button) convertView.findViewById(R.id.Viewbutton)).setTag(thread);
            return convertView;
        }
        TextView tv = (TextView) convertView.findViewById(R.id.threadtextview);
        tv.setText(thread.authorName + ": " + thread.subject);
        ((Button) convertView.findViewById(R.id.Viewbutton)).setTag(thread);
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
	
	public void updateHeight()
	{
		Log.d("MyDebugging", "Updating Height");
		
		mExpandableListView.height = mExpandableListView.getHeight();  
		
		Log.d("MyDebugging", "Updating Height completed");
	}

}

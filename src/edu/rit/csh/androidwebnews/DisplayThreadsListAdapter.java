/**
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Uless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/	
package edu.rit.csh.androidwebnews;

import java.util.ArrayList;
import java.util.Collection;
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
import android.widget.ProgressBar;
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
		if(position < super.getCount())
		{
			PostThread thread = ((PostThread)getItem(position));
			boolean isRoot = thread.depth == 0;
			LayoutInflater infalInflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView = infalInflater.inflate(R.layout.threadlayout, null);
			TextView tv = (TextView) convertView.findViewById(R.id.threadtextview);
			convertView.setPadding(30 * thread.depth + 10,10,10,10);
	        if(!isRoot || thread.children.size() == 0)
	        {	
	            ((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(R.drawable.empty);
	        	
	        }
	        
	        if(position + 1 < super.getCount() && thread.children.size() > 0)
	        {
		        if(isRoot && ((PostThread)getItem(position + 1)).equals(thread.children.get(0)))
		        {
		        		((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(R.drawable.uparrow);
		        }
		        else if (isRoot)
		        {
		    		((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(R.drawable.downarrow);
		        }
	        }
			String text = thread.toString();
			Log.d("thread depth", thread.depth + " " + thread.authorName);
	
			
	        tv.setPadding(75,0,150,0);
	        if (thread.unread != "null") {
	        	tv.setText(text);
	        	tv.setTypeface(null, Typeface.BOLD);
	        } else {
	        	tv.setText(text);
	        }
	        ((Button) convertView.findViewById(R.id.Viewbutton)).setTag(position);
	        return convertView;
		}
		else
		{

			LayoutInflater infalInflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        convertView = infalInflater.inflate(R.layout.thread_progress_bar, null);
			return convertView;
		}
	}
	
	@Override
	public int getCount()
	{
		int count = super.getCount();
		if(count == 0)
			return count;
		else
			return count+1;
		
	}
	
	@Override
	public void addAll(Collection<? extends T> collection) {
		for (T t : collection) {
			add(t);
		}
	}
}
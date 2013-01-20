/**
See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  This code is licensed
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/	
package edu.rit.csh.androidwebnews;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
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
		if(super.getCount() > 0)
		{
			PostThread thread = ((PostThread) getItem(position));
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.rowlayout, null);
			TextView tv = (TextView) convertView.findViewById(R.id.rowTextView);
			tv.setText(thread.newsgroup + " : " + thread.toString());
			if (thread.unread != "null") {
				tv.setTypeface(null, Typeface.BOLD);
				//convertView.setBackgroundColor(0xffcbcbcb);
			}
			return convertView;
		}
		else
		{
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.thread_progress_bar, null);
			return convertView;
		}
	}
	
	@Override
	public int getCount()
	{
		int count = super.getCount();
		if(count > 0)
			return count;
		else
			return 1;
	}
}
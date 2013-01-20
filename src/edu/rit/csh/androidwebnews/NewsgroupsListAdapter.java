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

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is the adapter used to display the list of the newsgroups. An adapter is needed
 * since if there is an unread message, the text will need to be set bold and to different
 * colors depending on the unread status of the post.
 *
 * @param <T>
 */
public class NewsgroupsListAdapter<T> extends ArrayAdapter<T> {
	Context context;
	
	public NewsgroupsListAdapter(Context context, List objects) {
		super(context, R.id.rowTextView, objects);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Newsgroup newsgroup = (Newsgroup)getItem(position);

		LayoutInflater infalInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.rowlayout, null);
        convertView.setPadding(10, 10, 10, 10);
        TextView tv = ((TextView) convertView.findViewById(R.id.rowTextView));
        if (newsgroup.unreadCount != 0) {
        	tv.setText(newsgroup.name + " (" + newsgroup.unreadCount + ")");
        	tv.setTypeface(null, Typeface.BOLD);
        } else {
        	tv.setText(newsgroup.name);
        }
        /*((TextView)convertView.findViewById(R.id.threadtextview)).setPadding(0,0,140,0);
        ((TextView)convertView.findViewById(R.id.threadtextview)).setText(text);
        ((Button) convertView.findViewById(R.id.Viewbutton)).setTag(position); */
        return convertView;
	}
	
	@Override
	public void addAll(Collection<? extends T> collection) {
		for (T t : collection) {
			add(t);
		}
	}

}
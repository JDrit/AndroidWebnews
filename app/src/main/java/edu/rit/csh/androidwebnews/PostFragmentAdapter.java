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

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

class PostFragmentAdapter<T> extends ArrayAdapter<T> {
    private Context context;
    public PostFragmentAdapter(Context context, int textViewResourceId,
                               List<T> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            LayoutInflater infalInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.postbuttons, null);
            ImageButton ib = (ImageButton) convertView.findViewById(R.id.starButton);
            /* changes the buttons if the dark theme is being used */
            if (PreferenceManager.getDefaultSharedPreferences(context).getString("layout_pick", "default").equals("dark")) {
                ((Button) convertView.findViewById(R.id.unreadButton)).setCompoundDrawablesWithIntrinsicBounds(
                        context.getResources().getDrawable(R.drawable.webnews_unread_dark),
                        null, null, null);
                ((Button) convertView.findViewById(R.id.replyButton)).setCompoundDrawablesWithIntrinsicBounds(
                        context.getResources().getDrawable(R.drawable.webnews_reply_dark),
                        null, null, null);
                if (super.getItem(getCount()).equals("true")) {
                    ib.setImageResource(R.drawable.webnews_star_dark);
                } else {
                    ib.setImageResource(R.drawable.webnews_unstar_dark);
                }
            } else {

                if (super.getItem(getCount()).equals("true"))
                 ib.setImageResource(R.drawable.webnews_star_light);
            }

            convertView.setPadding(10, 10, 10, 10);
            convertView.findViewById(R.id.replyButton).setTag(getItem(0) + "|" + getItem(5));
            convertView.findViewById(R.id.unreadButton).setTag(getItem(0));
            ib.setTag(getItem(0));
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
    public int getCount() {
        return super.getCount() - 1;
    }

}
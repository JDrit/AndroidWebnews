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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;

public class RecentFragment extends SherlockFragment {
    private RecentListAdapter<PostThread> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NewsgroupListMenu newsgroupListMenu = ((RecentActivity) getActivity()).getNewsgroupListMenu();
        ListView lv = new WebnewsListView(getActivity(), newsgroupListMenu);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        HttpsConnector hc = new HttpsConnector(getActivity());

        String ng;
        if (!(ng = sharedPref.getString("newsgroups_json_string", "")).equals("")) {
            if (sharedPref.getBoolean("first_time", true)) {
                listAdapter = new RecentListAdapter<PostThread>(getActivity(), R.layout.rowlayout, new ArrayList<PostThread>());
            } else {
                listAdapter = new RecentListAdapter<PostThread>(getActivity(), R.layout.rowlayout, hc.getNewestFromString(ng));
            }

            lv.setAdapter(listAdapter);
        }

        listAdapter = new RecentListAdapter<PostThread>(getActivity(), R.layout.rowlayout, new ArrayList<PostThread>());
        lv.setAdapter(listAdapter);


        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int position, long id) {
                PostThread thread = (PostThread) adapter.getItemAtPosition(position);

                DisplayThreadsActivity.lastFetchedThreads = new ArrayList<PostThread>();
                DisplayThreadsActivity.lastFetchedThreads.add(thread);

                while (thread.getParent() != null)
                    thread = thread.getParent();

                Intent intent = new Intent(getActivity(), PostSwipeableActivity.class);
                intent.putExtra("SELECTED_NEWSGROUP", thread.getNewsgroup());
                intent.putExtra("SELECTED_ID", thread.getNumber());
                //intent.putExtra("GOTO_THIS", threadsDirectMap.indexOf(selected) - threadsDirectMap.indexOf(thread));

                Log.d("des", "intent made");
                startActivity(intent);

            }


        });


        return lv;
    }


    public void update(ArrayList<PostThread> newestFromString) {
        listAdapter.clear();
        for (PostThread t : newestFromString) {
            listAdapter.add(t);
        }
        listAdapter.notifyDataSetChanged();

    }

}
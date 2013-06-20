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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;

/**
 * Used to display the results from the search.
 *
 * @author JD
 */
public class SearchResultsFragment extends SherlockFragment {
    private ArrayAdapter<String> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView mainListView = new ListView(getActivity());
        listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.search_result_textview, new ArrayList<String>());
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int position, long id) {
                ((SearchResultsActivity) getActivity()).onSelectThread(position);
            }
        });
        return mainListView;
    }

    public void update(ArrayList<String> threads) {
        if (listAdapter != null) {
            listAdapter.clear();
            if (threads.size() != 0) {
                for (String s : threads) {
                    listAdapter.add(s);
                }
            }
            listAdapter.notifyDataSetChanged();
        }

    }

}
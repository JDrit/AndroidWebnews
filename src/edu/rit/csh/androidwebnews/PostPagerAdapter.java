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

import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PostPagerAdapter extends FragmentStatePagerAdapter {

	PostThread rootThread;
	PostFragment[] fragments;
	int id;
	boolean fromSearch = false;
	
	public PostPagerAdapter(FragmentManager fm, boolean fromSearch) {
		super(fm);
		this.fromSearch = fromSearch;
		id = PostSwipableActivity.id;
		if(!fromSearch)
		{
			for(int x = 0; x < DisplayThreadsActivity.lastFetchedThreads.size(); x++)
			{
				if(DisplayThreadsActivity.lastFetchedThreads.get(x).getNumber() == id)
					rootThread = DisplayThreadsActivity.lastFetchedThreads.get(x);
			}
		}
		else
		{
			rootThread = SearchResultsActivity.rootThread;
		}
		
		fragments = new PostFragment[getCount()];
	}
	
	@Override
    public Fragment getItem(int i) {
        PostFragment fragment = new PostFragment(rootThread.getThisThread(i), i+1, getCount());
        Bundle args = new Bundle();
        fragment.setArguments(args);
		
		fragments[i] = fragment;
        
        return fragment;
    }

	@Override
	public int getCount() {
		return rootThread.getSubThreadCount();
	}

	@Override
    public CharSequence getPageTitle(int position) {
        return rootThread.getThisThread( position).toString();
    }

    /**
     * Testing method, never used outside of development
     * @param t - the thread to print
     */
	public void printT(PostThread t)
	{
		for(PostThread thread : t.getChildren())
		{
			printT(thread);
		}
	}

	public void update(String jsonString) {
		int id = 0;
		try {
			id = new JSONObject(jsonString).getJSONObject("post").getInt("number");
		} catch (JSONException e) {
		}
		
		for(PostFragment f : fragments)
		{
			if(f != null)
			{
				if(f.myThread.getNumber() == id)
					f.update(jsonString);
			}
		}
	}
}
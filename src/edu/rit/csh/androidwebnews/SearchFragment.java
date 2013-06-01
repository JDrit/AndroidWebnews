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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;

public class SearchFragment extends Fragment {
	private View view;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<String> groupNames;
	private Spinner spinner;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.search_fragment, container, false);
		groupNames = new ArrayList<String>();
		
		listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, groupNames);
		spinner = ((Spinner) view.findViewById(R.id.search_newsgroup_list));
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		
		if (month == 0) {
			month = 11;
			year -=1 ;
		}
		
		((DatePicker) view.findViewById(R.id.search_datePicker1)).updateDate(year, month, dayOfMonth);
		spinner.setAdapter(listAdapter);
		
		return view;
	}

	public void update(ArrayList<Newsgroup> newsgroups) {
		listAdapter.clear();
		listAdapter.add("(All)");
		for (Newsgroup newsgroup : newsgroups) {
			listAdapter.add(newsgroup.name);
		}
		
		listAdapter.notifyDataSetChanged();	
	}
	
	/**
	 * Gets the user's input to give it to the activity to do the actual search
	 * @return ArrayList<NameValuePair> - list of the parameters for the search
	 */
	public HashMap<String, String> getParams() {
		//ArrayList<NameValuePair> params = webnews_new ArrayList<NameValuePair>();
		HashMap<String, String> map = new HashMap<String, String>();
		if (spinner.getSelectedItem() != "(All)") {
			map.put("newsgroup", (String) spinner.getSelectedItem());
		}
		
		map.put("keywords", ((EditText) view.findViewById(R.id.search_keywords_edit)).getText().toString());
		map.put("authors", ((EditText) view.findViewById(R.id.search_authors_edit)).getText().toString());
		
		map.put("date_from",  Integer.valueOf(((DatePicker) view.findViewById(R.id.search_datePicker1)).getYear()).toString() + 
				"-" + Integer.valueOf(((DatePicker) view.findViewById(R.id.search_datePicker1)).getMonth() + 1).toString() + 
				"-" +  Integer.valueOf(((DatePicker) view.findViewById(R.id.search_datePicker1)).getDayOfMonth()).toString());
		map.put("date_to",  Integer.valueOf(((DatePicker) view.findViewById(R.id.search_datePicker2)).getYear()).toString() + 
				"-" + Integer.valueOf(((DatePicker) view.findViewById(R.id.search_datePicker2)).getMonth() + 1).toString() + 
				"-" +  Integer.valueOf(((DatePicker) view.findViewById(R.id.search_datePicker2)).getDayOfMonth()).toString());
		return map;
	}

}

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

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class PostThread {
	private String date, subject, authorName, authorEmail, newsgroup, unread, personal_class, sticky;
	private int number, depth;
	private boolean starred;
	private ArrayList<PostThread> children;
	private PostThread parent;

	public PostThread(String date, 
			int number, 
			String subject, 
			String authorName, 
			String authorEmail, 
			String newsgroup, 
			boolean starred, 
			String unread,
			String personal_class,
			String sticky) {
		this.date = date;
		this.number = number;
		this.subject = subject;
		this.authorName = authorName;
		this.authorEmail = authorEmail;
		this.newsgroup = newsgroup;
		this.starred = starred;
		this.unread = unread;
		this.personal_class = personal_class;
		this.sticky = sticky;
		children = new ArrayList<PostThread>();
	}
	
	/**
	 * Used to print out the post to the screen, Indents according to how 
	 * many sub-post it is
	 */
	@Override
	public String toString() {
		return authorName + ": " + subject;
	}
	
	/**
	 * The number of sub-threads the post contains
	 * @return int - sub-thread count
	 */
	public int getSubThreadCount() {
		int count = 1;
		for (int i = 0 ; i < children.size() ; i++) {
			count += children.get(i).getSubThreadCount();
		}
		return count;
	}
	
	public boolean Equals(Object object)
	{
		if(!(object instanceof PostThread))
			return false;
		if(((PostThread)object).newsgroup == newsgroup && ((PostThread)object).number == number)
			return true;
		else
			return false;
				
	}
	static int derp = 0;
	public PostThread getThisThread(int pos)
	{
		Log.d("MyDebugging", pos + "," + derp);
		if( pos - derp == 0) {
			derp = 0;
			Log.d("MyDebugging", "DERPADERPDAEPR");
			return this;
		}
		else
		{
			for(PostThread thread : children)
			{
				derp += 1;
				PostThread t = thread.getThisThread(pos);
				if(t != null)
					return t;
			}
		}
		Log.d("MyDebugging","Thread missing!");
		return null;
	}
	
	/**
	 * Finds out if the thread or any of its sub-threads are marked unread
	 * @return boolean - true if it contains an unread post, false otherwise
	 */
	public boolean containsUnread() {
		Log.d("children", depth + ":" + authorName + ":" + unread + ":" + children.size());
		if (unread != "null") {
			Log.d("children", "good");
			return true;
		} else {
			
			for (PostThread thread : children) {
				//Log.d("children", thread.authorName + ":" + thread.unread);
				if (thread.containsUnread()) {
					return true;
				}
				//return thread.containsUnread();
			}
		}
		return false;
	}
	
	public String getDate()
	{
		String[] values = date.split("-");
		String year = values[0];
		String month = values[1];
		String[] values2 = values[2].split("T");
		String day = values2[0];
		String[] times = values2[1].split(":");
		String hours = times[0];
		String minutes = times[1];
		month = getMonth(month);
		String time = getTime(hours, minutes);
		return time + ", " + month + " " + day + ", " + year;
	}
	
	private String getMonth(String s)
	{
		String months[] = {"January", "February", "March", "April", "May", "June", 
				"July", "August", "September", "October", "November", "December"};
		String month;
		try
		{
			month = months[Integer.parseInt(s) - 1];
		}
		catch (NumberFormatException nfe)
		{
			month = "Invalid Month";
		}
		catch (IndexOutOfBoundsException ioobe)
		{
			month = "Invalid Month";
		}
		return month;
	}
	
	private String getTime(String h, String m)
	{
		int hours = Integer.parseInt(h);
		String a = "";
		if(hours > 12)
			a = "pm";
		else
			a = "am";
		hours = hours%12;
		if(hours == 0)
			hours = 12;
		
		return hours + ":" + m + " " + a;
	}
	
	public void starred() {
		starred = !starred;
	}
	
	public String getNewsgroup() {
		return(newsgroup);
	}
	
	public PostThread getParent() {
		return(parent);
	}
	
	public int getNumber() {
		return(number);
	}
	
	public ArrayList<PostThread> getChildren() {
		return(children);
	}
	
	public int getDepth() {
		return(depth);
	}
	
	public String getAuthorName() {
		return(authorName);
	}
	
	public String getUnread() {
		return(unread);
	}
	
	public String getPersonal_class() {
		return(personal_class);
	}
	
	public String getSubject() {
		return(subject);
	}
	
	public boolean getStarred() {
		return(starred);
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public void setParent(PostThread parent) {
		this.parent = parent;
	}
	
	public void addChild(PostThread child) {
		children.add(child);
	}
	
	public void setUnread(String unread) {
		this.unread = unread;
	}
}

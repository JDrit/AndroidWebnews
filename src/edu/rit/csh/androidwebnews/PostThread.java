package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class PostThread {
	String date, subject, authorName, authorEmail, newsgroup, unread, personal_class;
	int number, depth;
	boolean starred;
	ArrayList<PostThread> children;
	PostThread parent;
	
	
	public PostThread(String date, 
			int number, 
			String subject, 
			String authorName, 
			String authorEmail, 
			String newsgroup, 
			boolean starred, 
			String unread,
			String personal_class) {
		this.date = date;
		this.number = number;
		this.subject = subject;
		this.authorName = authorName;
		this.authorEmail = authorEmail;
		this.newsgroup = newsgroup;
		this.starred = starred;
		this.unread = unread;
		this.personal_class = personal_class;
		children = new ArrayList<PostThread>();
	}
	
	/**
	 * Used to print out the post to the screen, Indents according to how 
	 * many sub-post it is
	 */
	@Override
	public String toString() {
		String indent = "";
		for (int i = 0 ; i < depth ; i++) {
			indent += "  ";
		}
		return indent + authorName + ": " + subject;
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
}
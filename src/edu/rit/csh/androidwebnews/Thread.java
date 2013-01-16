package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Thread {
	String date, subject, authorName, authorEmail, newsgroup, unread, personal_class;
	int number, rowDepth, depth;
	boolean starred;
	ArrayList<Thread> children;
	Thread parent;
	
	
	public Thread(String date, 
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
		children = new ArrayList<Thread>();
	}
	
	@Override
	public String toString() {
		String indent = "";
		for (int i = 0 ; i < depth ; i++) {
			indent += "  ";
		}
		return indent + authorName + ": " + subject;
	}
	
	
	public int getSubThreadCount() {
		int count = 1;
		for (int i = 0 ; i < children.size() ; i++) {
			count += children.get(i).getSubThreadCount();
		}
		return count;
	}
	
	public boolean Equals(Object object)
	{
		if(!(object instanceof Thread))
			return false;
		if(((Thread)object).newsgroup == newsgroup && ((Thread)object).number == number)
			return true;
		else
			return false;
				
	}
	static int derp = 0;
	public Thread getThisThread(int pos)
	{
		Log.d("MyDebugging", pos + "," + derp);
		if( pos - derp == 0) {
			derp = 0;
			Log.d("MyDebugging", "DERPADERPDAEPR");
			return this;
		}
		else
		{
			for(Thread thread : children)
			{
				derp += 1;
				Thread t = thread.getThisThread(pos);
				if(t != null)
					return t;
			}
		}
		Log.d("MyDebugging","Thread missing!");
		return null;
	}
	
	public boolean containsUnread() {
		Log.d("children", depth + ":" + authorName + ":" + unread + ":" + children.size());
		if (unread != "null") {
			Log.d("children", "good");
			return true;
		} else {
			
			for (Thread thread : children) {
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
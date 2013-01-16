package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

public class Thread {
	String date, subject, authorName, authorEmail, newsgroup, unread, personal_class;
	int number;
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
		return authorName + ": " + subject;
	}
	
	public int getSubThreadCount() {
		int count = 0;
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
}

package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

public class Thread {
	String date, subject, authorName, authorEmail, newsgroup, unread, personal_class;
	int number;
	boolean starred;
	ArrayList<Thread> children;
	
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
	public String toString() {
		return date + "," + number + "," + subject + "," + authorName + "," + newsgroup + ", children: " + children.size();
	}
	
	public int getSubThreadCount() {
		int count = 0;
		for (int i = 0 ; i < children.size() ; i++) {
			count += children.get(i).getSubThreadCount();
		}
		return count;
	}
}

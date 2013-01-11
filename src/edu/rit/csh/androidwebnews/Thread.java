package edu.rit.csh.androidwebnews;

import java.util.ArrayList;

public class Thread {
	String subject, authorName, authorEmail, newsgroup, unread, personal_class;
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
			String personal_class,
			ArrayList<Thread> children) {
		this.number = number;
		this.subject = subject;
		this.authorName = authorName;
		this.authorEmail = authorEmail;
		this.newsgroup = newsgroup;
		this.starred = starred;
		this.unread = unread;
		this.personal_class = personal_class;
		this.children = children;
	}
}

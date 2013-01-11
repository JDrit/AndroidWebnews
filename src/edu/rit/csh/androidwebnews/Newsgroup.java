package edu.rit.csh.androidwebnews;

public class Newsgroup {
	String name, unreadClass;
	int unreadCount;
	
	public Newsgroup(String name, int unreadCount, String unreadClass) {
		this.name = name;
		this.unreadCount = unreadCount;
		this.unreadClass = unreadClass;
	}
}

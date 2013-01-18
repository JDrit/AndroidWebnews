package edu.rit.csh.androidwebnews;

public interface ActivityInterface {
	
	public void update(String jsonString);
	
	public void onNewsgroupSelected(final String newsgroupName);
}
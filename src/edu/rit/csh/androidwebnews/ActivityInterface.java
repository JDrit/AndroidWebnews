package edu.rit.csh.androidwebnews;

public interface ActivityInterface {
	
	/**
	 * Used to make sure that the Activities have a method that can be called
	 * when the async task finishes and needs to update the Activity / Fragment.
	 * This should then call the update method in the frgment
	 * @param jsonString - the output of the async task
	 */
	public void update(String jsonString);
	
	public void onNewsgroupSelected(final String newsgroupName);
}
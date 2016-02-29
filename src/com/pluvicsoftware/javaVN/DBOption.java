package com.pluvicsoftware.javaVN;

/**
 * Represents an single Option (choice) in a decision
 * 
 * @author Brendan
 */
public class DBOption {
	/**
	 * Constructor. Builds a new option with a short description of what
	 * choice is being made (text) and the script branch ID to jump to
	 * if this Option is picked.
	 * 
	 * @param text - The text to display on the option (tells the player
	 * 				 what this choice is)
	 * @param branchID - The script branch ID to jump to if this Option is
	 * 					 selected
	 */
	public DBOption(String text, int branchID) {
		this.text = text;
		this.branchID = branchID;
	}
	
	public String getText(){return text;}
	public int getBranchID(){return branchID;}
	
	private String text;
	private int branchID;
}
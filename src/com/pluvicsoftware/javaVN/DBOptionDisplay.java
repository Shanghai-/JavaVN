package com.pluvicsoftware.javaVN;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.pluvicsoftware.graphics.DBElement;
import com.pluvicsoftware.graphics.DBRoundRect;
import com.pluvicsoftware.graphics.DBEllipse;
import com.pluvicsoftware.graphics.DBLabel;

/**
 * A graphical element which displays a single Option in a decision.
 * 
 * @author Brendan
 */
public class DBOptionDisplay extends DBElement {
	/**
	 * Constructor. Builds a new option display at the given x and y position 
	 * with the specified option number.
	 *  
	 * @param x - The x position of the element
	 * @param y - The y position of the element
	 * @param number - The number of the Option
	 */
	public DBOptionDisplay(int x, int y, int number) {
		// Option displays have a fixed width and height
		super(x, y, 507, 74);
		if (Constants.DEBUG) System.out.println("Creating Option Display...");
		backer = new DBRoundRect(x, y, 507, 74, 
				new Color(40, 40, 40, 220), true);
		
		if (x < 512) {
			/* If we're on the left half of the screen, we want our number 
			 * display to be on the right side. */
			numberContainer = new DBEllipse(x + 430, y + 2, 72, 70, 
					Color.white, false, 3);
			this.number = new DBLabel(x + 456, y + 54, String.valueOf(number), 
					new Font("Futura", Font.PLAIN, 48));
			optionText = new DBLabel(x + 40, y + 50, "Initialized", 
					new Font("Gill Sans MT", Font.PLAIN, 38));
		} else {
			/* If we're on the right half of the screen, we want our number 
			 * display to be on the left side. */
			numberContainer = new DBEllipse(x + 2, y + 2, 72, 70, 
					Color.white, false, 3);
			this.number = new DBLabel(x + 26, y + 54, String.valueOf(number), 
					new Font("Futura", Font.PLAIN, 48));
			optionText = new DBLabel(x + 40, y + 50, "Initialized", 
					new Font("Gill Sans MT", Font.PLAIN, 38));
		}
		
	}
	
	public void draw(Graphics2D g2d) {
		backer.draw(g2d);
		numberContainer.draw(g2d);
		optionText.draw(g2d);
		number.draw(g2d);
	}
	
	public void setText(String text) {
		optionText.setText(text);
	}
	
	public void setBranchID(int branchID) {
		this.branchID = branchID;
	}
	
	public int getBranchID() {
		return branchID;
	}
	
	private DBRoundRect backer;
	private DBEllipse numberContainer;
	private DBLabel number;
	private DBLabel optionText;
	private int branchID;
}
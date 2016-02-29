/*
 * This class exists mostly to hold variables and do calculations
 * for rounded rectangle buttons. The getter methods should be 
 * used in the paintComponent method of the JPanel which wishes to
 * draw a MenuButton. Basically, this is a convenient way of
 * putting all the variables for each button in one place.
 */

package com.pluvicsoftware.javaVN;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.pluvicsoftware.graphics.DBButton;
import com.sun.xml.internal.ws.org.objectweb.asm.Label;

/**
 * Graphical element. Displays a button on the Main Menu.
 * 
 * @author Brendan
 */
public class MenuButton extends DBButton {
	/* TODO: Finish Javadoc comments. This code is really old and documenting
	 * it is no fun */
	
	public MenuButton(int x, int y, int width, int height, String text, Color btnColor, Color txtColor, Font txtFont) {
		super(x, y, width, height, text, btnColor, txtColor, txtFont);
		System.out.println("Creating Menu Button...");
		initialWidth = width;
		isExpanded = false;
	}
	
	public MenuButton(int x, int y, int width, int height, String text) {
		super(x, y, width, height, text);
		System.out.println("Creating Menu Button...");
		initialWidth = width;
		isExpanded = false;
	}
	
	//Draws the button into the supplied graphics context
	public void draw(Graphics2D g2d) {
		//Builds the rectangle from variables taken from the passed MenuButton object
		g2d.setColor(buttonColor);
		g2d.drawRoundRect(xpos, ypos, width, height, height, height);
		g2d.fillRoundRect(xpos, ypos, width, height, height, height);
		
		//Draws the text from variables taken from the passed MenuButton object
		g2d.setColor(label.getColor());
		g2d.setFont(label.getFont());
		
		//Gets the width and height of the string with the current font, used to calculate position
		FontMetrics metrics = g2d.getFontMetrics();
		int textWidth = metrics.stringWidth(label.getText());
		int textHeight = metrics.getAscent() - metrics.getDescent();
		label.setPos(getTextXOrigin() - textWidth, getTextYOrigin() + textHeight / 2);
		//g2d.drawString(text, getTextXOrigin() - textWidth, getTextYOrigin() + textHeight / 2);
		label.draw(g2d);
	}
	
	//Used to expand and contract the button horizontally
	public void resizeButton(int amount) {
		width += amount;
		
		/* If the button is currently extended beyond its starting width,
		 * flags the button as expanded. */
		if (width > initialWidth) isExpanded = true;
		//Otherwise, sets that flag to false.
		if (width <= initialWidth) isExpanded = false;
	}
	
	//Checks if the button has room to expand. Details below.
	/* 
	 * This method checks if the button's current width is less
	 * than the maximum allowed width of the button. By default,
	 * the maximum width is the initial width plus the height
	 * of the button. 
	 * Returns true if the button can still get larger before
	 * reaching the limit, or false if the button is already at
	 * or above the limit.
	 */
	public boolean canExpand() {
		if (width < initialWidth + height) return true;
		return false;
	}
	
	//Returns the right margin of the button text. Details below.
	/* 
	 * This method returns the X position which the label should
	 * stop at. By default, this is right before the button's
	 * rounded end begins.
	 * The value is passed to the draw loop, which then uses the
	 * current graphics context to figure out where the left
	 * margin (or actual starting point) for drawing the string 
	 * should be.
	 */
	public int getTextXOrigin() {return xpos + width - height / 2;}
	
	//Returns the vertical center of the button
	public int getTextYOrigin() {return ypos + height / 2;}
	
	//Returns whether or not the button is expanded
	//See resizeButton for more detail.
	public boolean isExpanded() {return isExpanded;}
	
	//All variables for the button
	private int initialWidth;
	private boolean isExpanded;
}
package com.pluvicsoftware.javaVN;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.pluvicsoftware.graphics.DBButton;

/**
 * Graphical element. Displays a small, rounded-rectangle button in the 
 * options menu.
 * 
 * @author Brendan
 */
public class OptionsButton extends DBButton {

	/**
	 * Constructor. Builds a new OptionsButton with the specified x position,
	 * y position, width, height, and display text.
	 * 
	 * @param x - the x position of the element
	 * @param y - the y position of the element
	 * @param width - the width of the element
	 * @param height - the height of the element
	 * @param text - the string to display on the button
	 */
	public OptionsButton(int x, int y, int width, int height, String text) {
		super(x, y, width, height, text);
		if (Constants.DEBUG) System.out.println("Creating Options Button...");
	}
	
	public void draw(Graphics2D g2d) {
		//Builds the rectangle from variables taken from the passed MenuButton object
		g2d.setColor(buttonColor);
		g2d.drawRoundRect(xpos, ypos, width, height, height, height);
		g2d.fillRoundRect(xpos, ypos, width, height, height, height);
		
		//Draws the text from variables taken from the passed MenuButton object
		g2d.setColor(label.getColor());
		g2d.setFont(label.getFont());
		
		if (!labelPositioned) {
			//Gets the width and height of the string with the current font, used to calculate position
			if (Constants.DEBUG) System.out.println("Positioning Options button label");
			FontMetrics metrics = g2d.getFontMetrics();
			int textWidth = metrics.stringWidth(label.getText());
			int textHeight = metrics.getAscent() - metrics.getDescent();
			label.setPos(getButtonCenterX() - textWidth / 2, getButtonCenterY() + textHeight / 2);
			labelPositioned = true;
		}
		
		//g2d.drawString(text, getButtonCenterX() - textWidth / 2, getButtonCenterY() + textHeight / 2);
		label.draw(g2d);
	}
	
	private int getButtonCenterX() {
		return xpos + width / 2;
	}
	
	private int getButtonCenterY() {
		return ypos + height / 2;
	}
	
	private boolean labelPositioned = false;
}
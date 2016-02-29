package com.pluvicsoftware.javaVN;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.pluvicsoftware.graphics.DBConstants;
import com.pluvicsoftware.graphics.DBElement;
import com.pluvicsoftware.graphics.DBLabel;

/**
 * Graphical element, represents a label with multiple lines as an array 
 * of single-line labels.
 * 
 * @author Brendan
 */
public class DBMultiLineLabel extends DBElement {
	/* TODO: Implement line scrolling - if a line is so long that it can't
	 * fit on maxLines number of lines, then once we reach the bottom we
	 * should move everything up one line and keep typing. */
	
	/**
	 * Constructor.
	 * 
	 * @param x - The x position of the element
	 * @param y - The y position of the element
	 * @param width - The width of the element (Very important - text will wrap
	 * 				  to the next line if it becomes longer than the label width)
	 * @param maxLines - The maximum number of lines the label can make.
	 */
	public DBMultiLineLabel(int x, int y, int width, int maxLines) {
		super(x, y, width, scale(Constants.DEFAULT_LABEL_FONT.getSize()) * maxLines);
		font = new Font("Segoe UI", Font.PLAIN, this.height / maxLines);
		labels = new DBLabel[maxLines];
		strings = new String[maxLines];
	}
	
	public void draw(Graphics2D g2d) {
		// TODO: Double-check if this plays nice with screen scaling
		for (int i = 0; i < labels.length; i++) {
			if (labels[i] != null) {
				labels[i].draw(g2d);
			}
		}
	}
	
	/**
	 * Tells the specified line of the label what text string it should type
	 * towards. 
	 * 
	 * @param index - The line whose text we want to specify (zero-indexed)
	 * @param text - The text to type towards
	 */
	public void setDesiredText(int index, String text) {
		strings[index] = text;
	}
	
	/**
	 * Returns the target text of the given line of the label.
	 * 
	 * @param index - The line whose text we want to get (zero-indexed)
	 * @return - The target string of the given line of the label
	 */
	public String getText(int index) {
		if (index < strings.length) return strings[index];
		return("");
	}
	
	/**
	 * Sets every label to its target text. Used if the user wants to skip text
	 * typing.
	 */
	public void setAll() {
		for (int i = 0; i < labels.length; i++) {
			if (labels[i] != null) {
				labels[i].setText(strings[i]);
			}
		}
	}
	
	/**
	 * Sets the actual text of the given line of the label.
	 * 
	 * @param index - the line to set the text of (zero-indexed)
	 * @param text - the text to set the line to
	 */
	public void setLabelText(int index, String text) {
		// If the label has already been instantiated, we want to set its text
		if (labels[index] != null) {
			if (Constants.DEBUG) System.out.println("Multiline: Label text set.");
			labels[index].setText(text);
		} else {
			// If not, we need to build a new label.
			labels[index] = new DBLabel(xpos, unscale(ypos) + 
					font.getSize() * index, text, font);
			if (Constants.DEBUG) System.out.println("Multiline: New label added.");
			if (Constants.DEBUG) System.out.println("Label y pos: " + labels[index].getYPos());
			if (Constants.DEBUG) System.out.println("New Label font: " + labels[index].getFont());
		}
	}
	
	/**
	 * Clears the text from every label.
	 */
	public void clear() {
		for (int i = 0; i < labels.length; i++) {
			if (labels[i] != null) {
				labels[i].setText("");
			}
			strings[i] = "";
		}
	}
	
	/**
	 * Sets the font of all the labels to the specified font.
	 * 
	 * @param font - the font to set all labels to display
	 */
	public void setFont(Font font) {
		/* TODO: Seems to be some trouble with setting font (or was it color?).
		 * Check if this is the fault of DBMultiLineLabel or DBLine 
		 * (or maybe something else). It might only occur with the first line
		 * in a scene? */
		
		this.font = font;
		for (int i = 0; i < labels.length; i++) {
			if (labels[i] != null) {
				/* This line of code checks if we need to scale up our font
				 * size, and scales it up for us if we do. */
				labels[i].setFont(DBConstants.isScalingEnabled() ? font.deriveFont(scalef(font.getSize())) : font);
			}
		}
	}
	
	/**
	 * Sets the color of all the labels to the specified color.
	 * 
	 * @param color - the color to set all labels to be
	 */
	public void setColor(Color color) {
		this.color = color;
		for (int i = 0; i < labels.length; i++) {
			if (labels[i] != null) {
				labels[i].setColor(color);
			}
		}
	}
	
	private Font font = Constants.DEFAULT_LABEL_FONT;
	private Color color = Constants.DEFAULT_LABEL_COLOR;
	private DBLabel[] labels;
	private String[] strings;
}
/*
 * DBLayeredPane extends JLayeredPane to create a generic layered pane
 * for use throughout the program. Menus can only be layered on top of
 * other screens in a layeredPane, so this class extends the generic 
 * Java layered pane to include variables for each type of menu which
 * might appear over a screen, as well as methods for showing and hiding
 * each menu. 
 * 
 * To use DBLayeredPane, extend it and draw all screen content in the 
 * default layer. Buttons with calls to the show/hide menu functions 
 * can be added easily for controlling the menus.
 * 
 * For information on each menu, see:
 * OptionsPanel.java
 * SaveGamePanel.java
 * LoadGamePanel.java
 * LogPanel.java
 */

package com.pluvicsoftware.graphics;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.Timer;

import com.pluvicsoftware.javaVN.OptionsPanel;

public class DBLayeredPane extends JLayeredPane {
	private ArrayList<DBElement> elements = new ArrayList<DBElement>();
	
	//The panels which can be opened atop the layered pane
	protected OptionsPanel optionsPanel;
	/*protected SaveGamePanel savePanel;
	protected LoadGamePanel loadPanel;
	protected LogPanel logPanel;*/
	
	//Boolean used to cut off mouse events
	protected boolean acceptingInput;
	
	public DBLayeredPane() {
		
	}
	
	protected ArrayList<DBElement> getElements() {return elements;}
	
	protected DBElement getElement(int index) {return elements.get(index);}
	
	protected void addElement(DBElement el) {
		elements.add(el);
	}
	
	protected void addElement(DBElement el, int layer) {
		elements.add(layer, el);
	}
	
	protected void addElements(DBElement[] els) {
		for (DBElement el : els) elements.add(el);
	}
	
	protected void removeElement(DBElement el) {
		elements.remove(el);
	}
	
	/*
	 * OPTIONS MENU METHODS
	 */
	protected void showOptionsMenu() {
		if (optionsPanel != null) {} else {
			optionsPanel = new OptionsPanel(this);
			add(optionsPanel, new Integer(100));
		}
		optionsPanel.setVisible(true);
		optionsPanel.setAcceptingInput(true);
		//TODO: Something is buggy about the accepting input for the options menu
		//Only works on the close button, not on the slider
		acceptingInput = false;
	}
	
	public void hideOptionsMenu() {
		acceptingInput = true;
		optionsPanel.setAcceptingInput(false);
		optionsPanel.setVisible(false);
		//optionsPanel.setVisible(false);
	}
	
	/*
	 * SAVE MENU METHODS
	 *
	private void showSaveMenu() {
		if (savePanel != null) {} else {
			savePanel = new SaveGamePanel(this);
			add(savePanel, new Integer(200));
		}
		savePanel.setVisible(true);
		savePanel.setAcceptingInput(true);
		acceptingInput = false;
	}
	
	public void hideSaveMenu() {
		savePanel.setAcceptingInput(false);
		acceptingInput = true;
	}
	
	/*
	 * LOAD MENU METHODS
	 *
	private void showLoadMenu() {
		if (loadPanel != null) {} else {
			loadPanel = new LoadGamePanel(this);
			add(loadPanel, new Integer(300));
		}
		loadPanel.setVisible(true);
		loadPanel.setAcceptingInput(true);
		acceptingInput = false;
	}
	
	public void hideLoadMenu() {
		loadPanel.setAcceptingInput(false);
		acceptingInput = true;
	}
	
	/*
	 * LOG METHODS
	 *
	private void showLog() {
		if (logPanel != null) {} else {
			logPanel = new LogPanel(this);
			add(logPanel, new Integer(400));
		}
		logPanel.setVisible(true);
		logPanel.setAcceptingInput(true);
		acceptingInput = false;
	}
	
	public void hideLog() {
		logPanel.setAcceptingInput(false);
		acceptingInput = true;
	}
	*/
}
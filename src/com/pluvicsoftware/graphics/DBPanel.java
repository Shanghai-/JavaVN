package com.pluvicsoftware.graphics;

import java.util.ArrayList;

import javax.swing.JPanel;

public class DBPanel extends JPanel {
	public DBPanel() {
		
	}
	
	protected ArrayList<DBElement> getElements() {return elements;}
	
	protected void addElement(DBElement el) {
		elements.add(el);
	}
	
	protected void addElement(DBElement el, int layer) {
		elements.add(layer, el);
	}
	
	protected void addElements(ArrayList<DBElement> els) {
		for (DBElement el : els) elements.add(el);
	}
	
	protected void removeElement(DBElement el) {
		elements.remove(el);
	}
	
	private ArrayList<DBElement> elements = new ArrayList<DBElement>();
	protected boolean acceptingInput; 
}
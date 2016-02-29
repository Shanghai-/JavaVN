package com.pluvicsoftware.javaVN;

import javax.swing.SwingUtilities;

/**
 * Main class. Instantiates the screen.
 * 
 * @author Brendan
 */
public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Screen scr = new Screen();
				scr.setVisible(true);
			}
		});
	}
}
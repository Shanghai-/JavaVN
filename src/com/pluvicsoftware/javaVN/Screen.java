package com.pluvicsoftware.javaVN;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.pluvicsoftware.graphics.DBConstants;

/**
 * Handles all of the outermost-level screen control.
 * 
 * @author Brendan
 */
public class Screen extends JFrame {
	/**
	 * Constructor. Builds the screen and starts the game.
	 */
	public Screen() {
		initUI();
		runGame();
	}
	
	/**
	 * Builds the actual window for the game to run in
	 */
	private void initUI() {
		setTitle("JavaVN");
		
		OptionsSingleton settings = OptionsSingleton.getInstance();
		settings.loadOptions();
		int resolution = settings.getResolution();
		/* TODO: check that window sizing actually works right for all
		 * operating systems, not just Windows. */
		setSize(Constants.WIDTHS[resolution] + 6, Constants.HEIGHTS[resolution] + 28);
		DBConstants.setDefaultWidth(Constants.WIDTHS[0]);
		DBConstants.setDefaultHeight(Constants.HEIGHTS[0]);
		DBConstants.setWidth(Constants.WIDTHS[resolution]);
		DBConstants.setHeight(Constants.HEIGHTS[resolution]);
		if (resolution != 0) {
			DBConstants.setScalingEnabled(true);
			if (Constants.DEBUG) {
				System.out.println("Screen scaling enabled.");
				System.out.println("Screen resolution is " 
						+ DBConstants.getWidth() + " x " 
						+ DBConstants.getHeight());
				System.out.println("Default resolution is: " 
						+ DBConstants.getDefaultWidth() + " x " 
						+ DBConstants.getDefaultHeight());
			}
			
			DBConstants.calculateScaleFactor();
			System.out.println("Scale factor is: " + DBConstants.getScaleFactor());
			DBConstants.calculatePositionPoints();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
	}
	
	/**
	 * Runs the game by adding the desired first panel
	 */
	private void runGame() {
		JPanel cardContainer = new JPanel(new CardLayout());
		cardContainer.add(new ScenePanel(470), Constants.SPLASH_PANEL);
		//cardContainer.add(new SplashPanel(), Constants.SPLASH_PANEL);
		//cardContainer.add(new TestPanel(), Constants.SPLASH_PANEL);
		add(cardContainer, BorderLayout.CENTER);
		
		//This line of code looks like wicked bullshit but lemme explain
		//Basically it gets the current layout of the card container panel,
		//casts it to a CardLayout type layout, and asks it to show the named panel.
		((CardLayout) cardContainer.getLayout()).show(cardContainer, Constants.SPLASH_PANEL);
		
		//Timer to remove splash screen after 2 seconds
		ActionListener splashRemover = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Adds the MenuPanel to the cardContainer panel
				//I do this now so that the menu doesn't start its music early
				cardContainer.add(new MenuPanel(), Constants.MENU_PANEL);
				((CardLayout) cardContainer.getLayout()).show(cardContainer, Constants.MENU_PANEL);
			}
		};
		Timer removeSplash = new Timer(3000, splashRemover);
		removeSplash.setRepeats(false);
		//removeSplash.start();
	}
}
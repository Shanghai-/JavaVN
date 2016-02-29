package com.pluvicsoftware.javaVN;

import com.pluvicsoftware.graphics.DBPanel;

/**
 * Miscellaneous test class for quickly checking whatever functionality
 * 
 * @author Brendan
 */
public class TestPanel extends DBPanel {
	private DBSaveManager sm;
	
	public TestPanel() {
		sm = DBSaveManager.getInstance();
		
		sm.loadSave(0);
		//Check existing value
		System.out.println("Did the save work? " + sm.getVar("DoesSaveWork"));
		
		//Add new value and check if it worked
		sm.setVar("IsItGood", "u kno it");
		System.out.println("Do it do the work? " + sm.getVar("IsItGood"));
		
		//Change existing value and check
		sm.setVar("SaveWorks", "Nah");
		System.out.println("Did the save REALLY work? " + sm.getVar("DoesSaveWork"));
		
		sm.save();
	}
}
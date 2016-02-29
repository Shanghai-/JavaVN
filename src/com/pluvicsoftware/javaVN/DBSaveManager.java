package com.pluvicsoftware.javaVN;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Singleton. Handles saving, loading, and modifying files.
 * 
 * @author Brendan
 */
public class DBSaveManager {
	//TODO: This may not need to be a singleton. I'll have to check.
	/* TODO: Can I save my save files as a custom extension instead of .XML?
	 * That would deter players from save editing (not that I really care) */
	//TODO: Doesn't actually save scene/line number yet. Easy to implement.
	
	// Singleton instance
	private static DBSaveManager instance = null;
	
	// Document handler stuff
	private DocumentBuilder builder;
	private Document save;
	private Transformer transformer;
	
	/* Save info - the number of this save, all custom variables in the save,
	 * the first name of the player, and the last name of the player. */
	private int savenum;
	private Map<String, String> variables = new HashMap<String, String>();
	public String firstname;
	public String lastname;
	
	protected DBSaveManager() {
		if (Constants.DEBUG) System.out.println("Initializing Save Manager...");
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			transformer = TransformerFactory.newInstance().newTransformer();
			
		} catch (ParserConfigurationException e) {
			System.out.println("Document Builder could not be initialized." + 
					"Please send details of crash to Git.");
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			System.out.println("Document Transformer could not be initialized." + 
					"Please send details of crash to Git.");
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			System.out.println("Document Transformer could not be initialized." + 
					"Please send details of crash to Git.");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets the shared instance of the Save Manager singleton
	 * 
	 * @return - The shared DBSaveManager instance
	 */
	public static DBSaveManager getInstance() {
		System.out.println("Getting Save Manager instance...");
		if(instance == null) {
			instance = new DBSaveManager();
		}
		return instance;
	}
	
	/**
	 * Loads the given save file.
	 * 
	 * @param savenum - the save file to load
	 */
	public void loadSave(int savenum) {
		if (Constants.DEBUG) System.out.println("Loading save " + savenum + ".");
		try {
			// Gets the file
			File savefile = new File(Constants.SAVE_PATH.concat(
					String.valueOf(savenum)).concat(".xml"));
			// Uses the DocumentBuilder to parse the XML file
			save = builder.parse(savefile);
			
			// Gets the root element, which should be a Save object
			Element root = save.getDocumentElement();
			if (Constants.DEBUG) System.out.println("Root element: " + root.getNodeName());
			
			//TODO: Pulling the name from the savefile should go here
			
			// Tries to get the list of writer-specified variables
			NodeList nl = root.getElementsByTagName("Variables");
			if (nl != null) {
				// If there are variables to read, put them into the variables map 
				Node vars = nl.item(0);
				NamedNodeMap nnm = vars.getAttributes();
				for (int i=0; i < nnm.getLength(); i++) {
					if (Constants.DEBUG) {
						System.out.println("Node name: " + nnm.item(i).getNodeName());
						System.out.println("Node value: " + nnm.item(i).getNodeValue());
					}
					variables.put(nnm.item(i).getNodeName(), nnm.item(i).getNodeValue());
				}
			}
			
			this.savenum = savenum;
			if (Constants.DEBUG) System.out.println("Load successful.");
		} catch (SAXException | IOException e) {
			System.out.println("Save failed to load. Please send details of crash to Git.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the value of the specified variable from the variable map. Returns
	 * the string "NULL" if the variables does not exist.
	 *  
	 * @param name - The name of the variable to get the value of
	 * 
	 * @return - a string representing the value of the variable, or "NULL" if
	 * 			 the variable does not exist.
	 */
	public String getVar(String name) {
		String result = (String) variables.get(name);
		if(result != null) return result;
		return("NULL");
	}
	
	/**
	 * Adds a new variable to the variable map.
	 * 
	 * @param name - The name of the variable to add
	 * @param value - The value of the new variable
	 */
	public void setVar(String name, String value) {
		variables.put(name, value);
		NodeList nl = save.getDocumentElement().getElementsByTagName("Variables");
		if (nl != null) {
			Node vars = nl.item(0);
			((Element) vars).setAttribute(name, value);
		}
	}
	
	/**
	 * Saves the currently-loaded save file to memory. Each file is saved as
	 * [savenum].xml for ease of access. */
	public void save() {
		DOMSource source = new DOMSource(save);
		Result output = new StreamResult(new File(Constants.SAVE_PATH.concat(
				String.valueOf(savenum)).concat(".xml")));
		try {
			transformer.transform(source, output);
		} catch (TransformerException e) {
			System.out.println("Could not save the file." + 
					"Please report details of crash on Git.");
			e.printStackTrace();
		}
	}
}
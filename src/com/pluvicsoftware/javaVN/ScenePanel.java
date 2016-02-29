package com.pluvicsoftware.javaVN;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.Timer;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.stream.events.Attribute;

import com.pluvicsoftware.graphics.*;
import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;

//TODO: Update click detection code so that it's more flexible and efficient.
//Consider: Click cycles through all Elements on screen, if el is interactive,
//checks if el contains the mouse cursor, if it does, run onClick (defined in
//DBInteractiveElement.java)

/**
 * This panel reads the script starting from the given scene and displays
 * elements on the screen according to its contents. 
 * 
 * @author Brendan
 */
public class ScenePanel extends DBLayeredPane {
	//Screen size
	int screenWidth;
	int screenHeight;
	
	//Mouse tracking variables
	private int lastMouseX;
	private int lastMouseY;
	
	//Timer to update the screen at 60 frames per second (capped, with dropping)
	protected Timer update;
	protected ArrayList<DBAnimatable> toUpdate = new ArrayList<DBAnimatable>();
	
	//Variables for typing out stuff
	private DBSoundManager sm;
	private DBSaveManager sav = DBSaveManager.getInstance();
	/* TODO: DBResourceManager, which loads sounds and images into memory as
	 * needed and keeps them there until the end of the scene. */
	private Graphics2D graphicsContext;
	
	//Constant screen elements
	private DBImage background;
	private DBLabel sceneLabel;
	private DBRoundRect speakerPanel;
	private DBLabel speakerLabel;
	private DBRoundRect bodyPanel;
	private DBMultiLineLabel bodyLabel;
	private DBImage icon;
	private DBOptionDisplay[] optionSystem = new DBOptionDisplay[10]; 

	//Line reader/displayer control variables
	private Boolean shouldContinue = true;
	private Boolean doneTyping = true;
	private Boolean awaitingInput = false;
	private Timer typeTimer;
	
	private ArrayList<DBSprite> characters = new ArrayList<DBSprite>();
	

	/**
	 * Constructor. Builds a new panel which will begin reading the script at
	 * the start of the given scene. 
	 * 
	 * @param sceneID - the scene to begin at
	 */
	public ScenePanel(int sceneID) {
		if (Constants.DEBUG) System.out.println("Initializing scene panel for scene " + sceneID);
		
		//Creates and adds a new mouse-movement listener
		addMouseMotionListener(new MouseAdapter() {
			//Overrides the default mouseMoved function for mouse position logging
			public void mouseMoved(MouseEvent me) {
				if (acceptingInput) {
					//Saves current mouse position
					lastMouseX = me.getX();
					lastMouseY = me.getY();
					//System.out.println("Mouse is at x: " + lastMouseX + " y: " + lastMouseY);
				}
			}
		});
		
		//Creates and adds a new mouse-click listener
		addMouseListener(new MouseAdapter() {
			//Overrides the default mousePressed function for custom actions on-click
			public void mousePressed(MouseEvent me) {
				if (acceptingInput) {
					if (doneTyping) {
						if (awaitingInput) {
							/* If we are accepting input, the text is not 
							 * scrolling, and we are waiting for input, then
							 * we are expecting the player to pick a choice. 
							 * Therefore, on each click we loop through the 
							 * options in the option system and check if the 
							 * player is clicking one. If the player is, we 
							 * jump to the script branch which is the result 
							 * of the choice. */
							for (DBOptionDisplay op : optionSystem) {
								if (op.contains(lastMouseX, lastMouseY)) {
									try {
										jumpToBranch(op.getBranchID());
										clearOptions();
										awaitingInput = false;
										getNextLine();
										break;
									} catch (XMLStreamException e) {
										// If the jump fails, we throw an error. 
										System.out.println("Jump to branch failed!");
										e.printStackTrace();
									}
								}
							}
						} else {
							/* If we are accepting input, the text is done 
							 * scrolling, but we are NOT waiting for input, 
							 * then we are expecting the player click to 
							 * advance to the next line of dialogue. */
							try {
								//sm.playSFX(2, "selectBlip.wav");
								getNextLine();
							} catch (XMLStreamException e) {
								// If the nextline fails, we throw an error.
								System.out.println("Get next line failed!");
								e.printStackTrace();
							}
						}
					} else {
						/* If we are accepting input and the text is still
						 * scrolling, then clicking makes the text complete
						 * its typing. */
						stopTimer();
						//TODO: Button click check
					}
				}
			}
		});
		
		//Imports the user's settings
		OptionsSingleton settings = OptionsSingleton.getInstance();
		int resolution = settings.getResolution();
		screenWidth = Constants.WIDTHS[resolution];
		screenHeight = Constants.HEIGHTS[resolution];
		
		//Creates a new ActionListener for the update loop timer to call
		ActionListener updateTimer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				for (DBAnimatable el : toUpdate) {
					el.updateElement();
				}
				
				//Repaints the screen after the changes have been made to each object
				repaint();
			}
		};
		update = new Timer(16, updateTimer);
		update.start();
		
		//TODO: Load up correct save
		//This is TEMPORARY! In the future, will need to load an actual savegame
		//via the main menu "load game" function or save game panel
		sav.loadSave(0);
		
		/* Creates an XMLInputFactory and sets a global variable, allocator, to 
		 * its event allocator */
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setEventAllocator(new XMLEventAllocatorImpl());
			allocator = inputFactory.getEventAllocator();
			reader = inputFactory.createXMLStreamReader(Constants.SCRIPT_PATH, new FileInputStream(Constants.SCRIPT_PATH));
			
			/* If we are able to successfully build our script reader and import
			 * the script, we continue on to the scene setup and get ready for 
			 * input. */
			setupScene(String.valueOf(sceneID));
			acceptingInput = true;
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
			System.out.println("XML Reader failed to initialize. Please report details of bug on Github.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Script file not found. Check SCRIPT_PATH value in Constants file.");
		}
	}
	
	/**
	 * Takes in a string and replaces any placeholders with the appropriate
	 * words. 
	 * 
	 * @param str - The string to fix
	 * @return - The same string, but with all placeholders replaced
	 */
	private String buildString(String str) {
		//TODO: Replace "Brendan" and "Walsh" with savefile's first name and last name
		str = str.replaceAll("firstname", "Brendan");
		str = str.replaceAll("lastname", "Walsh");
		str = str.replaceAll("Firstname", "Brendan");
		str = str.replaceAll("Lastname", "Walsh");
		return str;
	}

	/**
	 * Displays a Line on the screen (by altering the screen elements to
	 * reflect the contents of the line). Note that this handles dialogue
	 * lines only, not action lines.
	 *  
	 * @param line - The line to display
	 */
	private void displayLine(DBLine line) {
		if (Constants.DEBUG) System.out.println("Displaying line: " + line.body);
		shouldContinue = false;
		doneTyping = false;
		
		//If there is a speaker, we display that. Otherwise, hide the speaker panel. 
		if (line.speaker != null) {
			line.speaker = buildString(line.speaker);
			speakerLabel.setText(line.speaker);
			speakerLabel.setVisible(true);
			speakerPanel.setVisible(true);
		} else {
			speakerLabel.setVisible(false);
			speakerPanel.setVisible(false);
		}
		
		/* Pass the completed body, font, and color of the line to the label object,
		 * then clear the label and prepare to scroll through the letters. */ 
		line.body = buildString(line.body);
		bodyLabel.setFont(line.font);
		bodyLabel.setColor(line.textColor);
		bodyLabel.clear();
		
		// This routine figures out how many lines the text has to be split into.
		if (graphicsContext != null) {
			FontMetrics fm = graphicsContext.getFontMetrics();
			
			/* If our string will be less than the max width in the given font,
			 * we display it as-is. */
			if (fm.stringWidth(line.body) <= bodyLabel.getWidth()) {
				bodyLabel.setDesiredText(0, line.body);
			} else {
				/* If not, we split our string into an array of individual 
				 * words and add them to the line one by one. */
				String[] words = line.body.split("\\s+");
				String returnString = "";
				String testString = "";
				int lineCount = 0;
				for (int i = 0; i < words.length; i++) {
					testString = returnString.concat(words[i]).concat(" ");
					
					//Check if the current string plus the next word can fit
					if (fm.stringWidth(testString) <= bodyLabel.getWidth()) {
						/* If it can, we add the next word to the current string
						 * and loop again */
						returnString = testString;
					} else {
						/* If not, we pass the current string to the current label
						 * and then move on to the next label, starting with the 
						 * word which drove the first line over the limit. */
						if (Constants.DEBUG) {
							System.out.println("Width thing tripped!");
							System.out.println("Line text is: " + returnString);
						}
						bodyLabel.setDesiredText(lineCount, returnString);
						returnString = "";
						testString = "";
						lineCount++;
						i--;
					}
				}
				if (Constants.DEBUG) System.out.println(returnString);
				bodyLabel.setDesiredText(lineCount, returnString);
			}
		} else {
			//If our graphics context is undefined, we throw an error.
			System.out.println("No graphics context found, very disappointed.");
		}
		
		bodyLabel.setVisible(true);
		bodyPanel.setVisible(true);
		
		sm.setSFX(0, line.textSound);
		//TODO: This should probably be hooked into the main update loop
		/* Returns a timer which loops through the letters in each line and
		 * displays them one by one at the specified rate. */
		ActionListener typeTimerListener = new ActionListener() {
			int typeCount = 0;
			int index = 0;
			String line = bodyLabel.getText(index);
			int lineLength = line.length();
			
			/* Each time the timer is called, it increments typeCount and
			 * displays the first typeCount letters of the string. */
			public void actionPerformed(ActionEvent evt) {
				typeCount++;
				
				//Checks if the typeCount is longer than the line
				if (typeCount > lineLength) {
					//Checks if the next label has any text info to display
					if (bodyLabel.getText(index + 1).equals("")) {
						//If it does not, we are done typing.
						doneTyping = true;
						stopTimer();
					} else {
						/* If it does, we reset our type count and move on to
						 * the next label. */
						typeCount = 0;
						index++;
						line = bodyLabel.getText(index);
						lineLength = bodyLabel.getText(index).length();
					}
				} else {
					/* If the typeCount is not as long as the line, we display
					 * the first typeCount letters of the string. */
					bodyLabel.setLabelText(index, line.substring(0, typeCount));
					//sm.playSFX(1);
					//repaint();
				}
			}
		};
		typeTimer = new Timer(line.typeDelay, typeTimerListener);
		typeTimer.start();
		System.out.println("Timer started");
		
		//doneTyping = true;
	}
	
	/**
	 * Stops the timer which types out the letters of each word, and 
	 * automatically fills in the rest of the letters if the line isn't
	 * done typing.
	 */
	private void stopTimer() {
		//TODO: Again, figure out if we can hook this into the main timer
		//There should be a better way to reference timers than global functions?
		typeTimer.stop();
		System.out.println("Timer stopped");
		
		/* If we're not done typing when the type timer stops, we tell all 
		 * labels to display their associated target text. We also stop all
		 * sound effects and line voiceovers. */
		if (!doneTyping) {
			bodyLabel.setAll();
			sm.stopSFX(0);
			sm.stopVO();
			doneTyping = true;
		}
	}
	
	/**
	 * Shows the options for a given choice on the screen
	 * 
	 * @param options - the list of options to show
	 */
	private void displayOptions(ArrayList<DBOption> options) {
		if (Constants.DEBUG) System.out.println("Displaying options...");
		for (int i = 0; i < options.size(); i++) {
			optionSystem[i].setText(options.get(i).getText());
			optionSystem[i].setBranchID(options.get(i).getBranchID());
			optionSystem[i].setVisible(true);
		}
		awaitingInput = true;
	}
	
	/**
	 * Clears the Options from the screen.
	 */
	private void clearOptions() {
		for (DBOptionDisplay op : optionSystem) op.setVisible(false);
	}

	/**
	 * Ends the scene and returns control to the Game Manager
	 */
	private void endScene() {
		//TODO: Finish this.
		
		if (Constants.DEBUG) System.out.println("Ending scene!");
		acceptingInput = false;
		
		DBImageAnimatable fader = new DBImageAnimatable(0, 0, Constants.BG_PATH.concat("black.png"));
		
		fader.setAlpha(0.0f);
		addElement(fader);
		//fader.animateAlpha(0.001f, 1.0f);
		//toUpdate.add(fader);
		
		//DBRect fader = new DBRect(0, 0, screenWidth, screenHeight, new Color(0, 0, 0, 0), true);
		//addElement(fader);
		/* ActionListener fadeTimerListener = new ActionListener() {
			int frames = 0;
			
			public void actionPerformed(ActionEvent evt) {
				System.out.println(fader.getFillColor().getAlpha());
				if (frames < 255) {
					fader.setFillColor(new Color(0, 0, 0, fader.getFillColor().getAlpha() + 1));
					frames++;
					//repaint();
				} else {
					//TODO: STOP TIMER, set acceptingInput to true, remove fader.
					//Or maybe hand control back to game mastermind?? idk
				}
			}
		};
		Timer fadeTimer = new Timer(16, fadeTimerListener);
		fadeTimer.start(); */
		
	}
	
	
	
	/* ================== *
	 * XML PARSER METHODS *
	 * ================== */
	
	/**
	 * Sets up the scene from the information in the script
	 * 
	 * @param sceneID - The scene to set up
	 * @throws XMLStreamException
	 */
	private void setupScene(String sceneID) throws XMLStreamException {
		if (Constants.DEBUG) System.out.println("Settting up scene!");
		reader.nextTag();
		int eventType = reader.getEventType();
		
		//As long as there is another element in the document, keep going
		while (eventType == XMLStreamConstants.START_ELEMENT) {
			if (Constants.DEBUG) System.out.println("Element type is start element!");
			//Gets the type of the next element in the document
			//TODO: change all next() calls to nextTag()
		    
			//If the next XML line is the start of a scene, <Scene>
		    if (reader.getLocalName().equals("Scene")) {
		    	
		        //Pulls the Scene element's information so it can be used. Immutable.
		    	StartElement event = getXMLEvent(reader).asStartElement();
		        //System.out.println ("EVENT: " + event.toString());
		        
		        //Checks if the Scene's ID matches the Scene ID passed to the reader
		        if (event.getAttributeByName(new QName("ID")).getValue().equals(sceneID)) {
		        	
		        	/* =========== *
		        	 * SCENE SETUP *
		        	 * =========== */
		        	if (Constants.DEBUG) System.out.println("Scene number matches passed scene. Pulling info...");
		        	/* String numChannels = event.getAttributeByName(new QName("SFXChannels")).getValue(); 
		        	if (numChannels != null) {
		        		sm = new DBSoundManager(Integer.parseInt(numChannels));
		        	} else {
		        		sm = new DBSoundManager(Constants.DEFAULT_NUM_CHANNELS);
		        	} */
		        	sm = new DBSoundManager(Constants.DEFAULT_NUM_CHANNELS);
		    		String backgroundName = event.getAttributeByName(new QName("Background")).getValue();
		    		String sceneName = event.getAttributeByName(new QName("Name")).getValue();
		    		String sceneTime = event.getAttributeByName(new QName("Time")).getValue();
		    		
		    		background = new DBImage(0, 0, screenWidth, screenHeight, Constants.BG_PATH.concat(backgroundName));
		    		addElement(background);
		    		addElement(new DBLabel(5, 40, sceneName, new Font("Century Gothic", Font.ITALIC, 42)));
		    		
		    		//TODO: This gets a border because the fill and border draws are called. Fix that.
		    		bodyPanel = new DBRoundRect(-75, 410, 940, 150, new Color(40, 40, 40, 220), true);
		            bodyPanel.setVisible(false);
		            addElement(bodyPanel);
		            bodyLabel = new DBMultiLineLabel(30, 464, 680, 4);
		            bodyLabel.setVisible(false);
		            addElement(bodyLabel);
		            
		            speakerPanel = new DBRoundRect(-22, 388, 250, 44, Constants.DEFAULT_BUTTON_COLOR, true);
		            speakerPanel.setVisible(false);
		            addElement(speakerPanel);
		            speakerLabel = new DBLabel(10, 420, " ");
		            speakerLabel.setVisible(false);
		            addElement(speakerLabel);
		            
		            //TODO: Finish date/time displayer stuff
		            //This includes initializing the widths based off the labels
		            //Which need to be instantiated in the draw function in order to get widths
		            DBLabel dateLabel = new DBLabel(754, 34, "Friday, September 28");
		            DBLabel timeLabel = new DBLabel(890, 74, sceneTime);
		            DBRoundRect dateBacker = new DBRoundRect(734, 3, 400, 40, Constants.DEFAULT_BUTTON_COLOR, true);
		            DBRoundRect timeBacker = new DBRoundRect(870, 43, 200, 40, new Color(40, 40, 40, 220), true);
		            addElement(dateBacker);
		            addElement(timeBacker);
		            addElement(dateLabel);
		            addElement(timeLabel);
		            
		            icon = new DBImage(720, 416, "src/media/icons/template.png");
		            icon.setVisible(false);
		            addElement(icon);
		            
		            optionSystem[0] = new DBOptionDisplay(-37, 90, 1);
		            optionSystem[1] = new DBOptionDisplay(588, 90, 2);
		            optionSystem[2] = new DBOptionDisplay(-37, 168, 3);
		            optionSystem[3] = new DBOptionDisplay(588, 168, 4);
		            optionSystem[4] = new DBOptionDisplay(-37, 246, 5);
		            optionSystem[5] = new DBOptionDisplay(588, 246, 6);
		            optionSystem[6] = new DBOptionDisplay(-37, 324, 7);
		            optionSystem[7] = new DBOptionDisplay(588, 324, 8);
		            optionSystem[8] = new DBOptionDisplay(-37, 402, 9);
		            optionSystem[9] = new DBOptionDisplay(588, 402, 10);
		            for (DBOptionDisplay op : optionSystem) {
		            	addElement(op);
		            	op.setVisible(false);
		            }
		            
		            findRoot();
		            //getNextLine();
		            break;
		        } else {
		        	if (Constants.DEBUG) {
		        		System.out.println("Scene number " + 
			        			event.getAttributeByName(new QName("ID")).getValue() 
			        			+ " does not match passed scene. Checking next scene...");
		        	}	
		        }
		    }
		    eventType = reader.nextTag();
		}
		//TODO: This condition will probably trigger if there is a single end tag before the
		//desired scene number. And that's stupid.
		//TODO: Yep. It sure does. I hate everything
		if (Constants.DEBUG) {
			System.out.println("First while loop exited." + 
					"If scene not found by now, this was a failure.");
		}
	}
	
	/**
	 * Finds the root thread of the current scene.
	 * 
	 * @throws XMLStreamException
	 */
	private void findRoot() throws XMLStreamException {
		if (Constants.DEBUG) System.out.println("Finding Root Thread...");
		int eventType = reader.getEventType();
		
		while (eventType == XMLStreamConstants.START_ELEMENT) {
			if (eventType == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals("Root")) {
				System.out.println("Root Thread found.");
				break;
			}
			if (eventType == XMLStreamConstants.END_ELEMENT && reader.getLocalName().equals("Scene")) {
				System.out.println("ERROR: Could not find Root Thread within Scene");
				break;
			}
			eventType = reader.nextTag();
		}
	}
	
	//TODO: Rewrite this so that instead of using things like
	//<Line Text='Hey'></Line> it uses <Line>Hey</Line>
	//That's much better form.
	
	/**
	 * Gets the next line in the script and interprets it. If it is an action,
	 * this method handles it. If it is a dialogue line, see displayLine.
	 * 
	 * @throws XMLStreamException
	 */
	private void getNextLine() throws XMLStreamException {
		if (Constants.DEBUG) System.out.println("Next Line Called");
		int eventType = reader.getEventType();
		
		while (reader.hasNext()) {
			//System.out.println("Reader has next");
			eventType = reader.nextTag();
			
			//If the next XML line is an opening tag
		    if (eventType == XMLStreamConstants.START_ELEMENT) {
		        //get immutable XMLEvent
		    	StartElement event = getXMLEvent(reader).asStartElement();
		        System.out.println ("EVENT: " + event.toString());
		        
		        // If we have a line, pass it to displayLine
		        if (reader.getLocalName().equals("Line")) {
		        	Attribute speaker = event.getAttributeByName(new QName("Speaker"));
		        	Attribute body = event.getAttributeByName(new QName("Body"));
		        	Attribute font = event.getAttributeByName(new QName("Font"));
		        	Attribute color = event.getAttributeByName(new QName("Color"));
		        	Attribute typeDelay = event.getAttributeByName(new QName("TypeDelay"));
		        	Attribute shouldContinue = event.getAttributeByName(new QName("ShouldContinue"));
		        	Attribute textSound = event.getAttributeByName(new QName("TextSound"));
		        	displayLine(new DBLine(speaker, body, font, color, typeDelay, textSound, shouldContinue));
		        	
		        } else if (reader.getLocalName().equals("Action")) {
		        	
		        	/* ========================== *
		        	 * ACTION INTERPRETER METHODS *
		        	 * ========================== */
		        	String type = event.getAttributeByName(new QName("Type")).getValue();
		        	if (type.equals("AddCharacter")) {
		        		// Adds the given character to the screen with the given expression
		        		String character = event.getAttributeByName(new QName("Character")).getValue();
		        		String sprite = event.getAttributeByName(new QName("Sprite")).getValue();
		        		String spritePath = Constants.SPRITES_PATH.concat(character).concat("/").concat(sprite);
		        		String x = event.getAttributeByName(new QName("X")).getValue();
		        		String y = event.getAttributeByName(new QName("Y")).getValue();
		        		int xpos = Integer.parseInt(x);
		        		int ypos = Integer.parseInt(y);
		        		DBSprite chara = new DBSprite(character, xpos, ypos, spritePath);
		        		addElement(chara, 1);
		        		characters.add(chara);
		        		shouldContinue = true;
		        		
		        	} else if (type.equals("SetSprite")) {
		        		// Changes the sprite of the given character to the new expression
		        		
		        		String character = event.getAttributeByName(new QName("Character")).getValue();
		        		String sprite = event.getAttributeByName(new QName("Sprite")).getValue();
		        		String spritePath = Constants.SPRITES_PATH.concat(character).concat("/").concat(sprite);
		        		for (DBSprite chara : characters) if (chara.name.equals(character)) chara.setImage(spritePath);
		        		shouldContinue = true;
		        		
		        	} else if (type.equals("MoveCharacter")) {
		        		// Moves the given character on the screen
		        		
		        		String character = event.getAttributeByName(new QName("Character")).getValue();
		        		String x = event.getAttributeByName(new QName("X")).getValue();
		        		String y = event.getAttributeByName(new QName("Y")).getValue();
		        		int xpos = Integer.parseInt(x);
		        		int ypos = Integer.parseInt(y);
		        		for (DBSprite chara : characters) if (chara.name.equals(character)) chara.center(xpos, ypos);
		        		shouldContinue = true;
		        		
		        	} else if (type.equals("AnimateCharacter")) {
		        		// Animates the given parameter as specified
		        		String character = event.getAttributeByName(new QName("Character")).getValue();
		        		String parameter = event.getAttributeByName(new QName("Parameter")).getValue();
		        		String target = event.getAttributeByName(new QName("Target")).getValue();
		        		String duration = event.getAttributeByName(new QName("Duration")).getValue();
		        		for (DBSprite chara : characters) {
		        			if (chara.name.equals(character)) {
		        				if (parameter.equals("X")) {
		        					chara.animateX((float) (Integer.parseInt(target) - chara.getXPos())/
		        							(Integer.parseInt(duration) * 60), Integer.parseInt(target));
		        				} else if (parameter.equals("Y")) {
		        					chara.animateY((float) (Integer.parseInt(target) - chara.getYPos())/
		        							(Integer.parseInt(duration) * 60), Integer.parseInt(target));
		        				} else if (parameter.equals("Width")) {
		        					chara.animateWidth((Integer.parseInt(target) - chara.getWidth())/
		        							(Integer.parseInt(duration) * 60), Integer.parseInt(target));
		        				} else if (parameter.equals("Height")) {
		        					chara.animateHeight((Integer.parseInt(target) - chara.getHeight())/
		        							(Integer.parseInt(duration) * 60), Integer.parseInt(target));
		        				} else if (parameter.equals("Alpha")) {
		        					chara.animateAlpha((Float.parseFloat(target) - chara.getAlpha())/
		        							(Integer.parseInt(duration) * 60), Float.parseFloat(target));
		        				} else {
		        					System.out.println("Unrecognized parameter in Animate call!");
		        				}
		        				toUpdate.add(chara);
		        			}
		        		}
		        		shouldContinue = true;
		        		
		        	} else if (type.equals("RemoveCharacter")) {
		        		// Removes the character
		        		String character = event.getAttributeByName(new QName("Character")).getValue();
		        		for (DBSprite chara : characters) if (chara.name.equals(character)) removeElement(chara);
		        		
		        	} else if (type.equals("PlayMusic")) {
		        		// Plays the given music
		        		String music = event.getAttributeByName(new QName("Music")).getValue();
		        		sm.playBGM(music);
		        		
		        	} else if (type.equals("StopMusic")) {
		        		// Stops all music
		        		sm.stopBGM();
		        		
		        	} else if (type.equals("PlaySound")) {
		        		// Plays the given sound on channel 1
		        		String sound = event.getAttributeByName(new QName("Sound")).getValue();
		        		sm.playSFX(1, sound);
		        		
		        	} else if (type.equals("StopSound")) {
		        		// Stops the sound on channel 1
		        		sm.stopSFX(1);
		        		
		        	} else if (type.equals("SetBackground")) {
		        		// Sets the background to the given background
		        		String bgName = event.getAttributeByName(new QName("Background")).getValue();
		        		String bgPath = Constants.BG_PATH.concat(bgName);
		        		background.setImage(bgPath);
		        		
		        	} else if (type.equals("SetIcon")) {
		        		// Sets the icon to the given icon
		        		String iconImage = event.getAttributeByName(new QName("Icon")).getValue();
		        		if (iconImage.equals("NONE")) {
		        			icon.setVisible(false);
		        		} else {
		        			icon.setImage(Constants.ICON_PATH.concat(iconImage));
		        			icon.setVisible(true);
		        		}
		        		
		        	} else {
		        		System.out.println("Action of unknown type found.");
		        	}
		        } else if (reader.getLocalName().equals("Choice")){
		        	// Get all the options in the choice and then display them
		        	if (Constants.DEBUG) System.out.println("Choice found. Looping to find options...");
		        	ArrayList<DBOption> optionList = new ArrayList<DBOption>();
		        	eventType = reader.nextTag();
		        	while(eventType == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals("Option")) {
		        		event = getXMLEvent(reader).asStartElement();
		        		System.out.println("Current option: " + event.toString());
		        		String text = event.getAttributeByName(new QName("Text")).getValue();
			        	int branchID = Integer.parseInt(event.getAttributeByName(new QName("BranchID")).getValue());
			        	DBOption op = new DBOption(text, branchID);
			        	optionList.add(op);
			        	eventType = reader.nextTag();
			        	eventType = reader.nextTag();
		        	}
		        	shouldContinue = false;
		        	System.out.println(optionList.toString());
		        	displayOptions(optionList);
		        	
		        } else if (reader.getLocalName().equals("VarCheck")) {
		        	String varToCheck = event.getAttributeByName(new QName("Variable")).getValue();
		        	
		        	if (Constants.DEBUG) {
		        		System.out.println("Variable check for " + 
		        				varToCheck + " found. Looping to find path...");
		        	}
		        	
		        	String currentValue = sav.getVar(varToCheck);
		        	
		        	if (Constants.DEBUG) {
		        		System.out.println("Variable in memory is: " + currentValue);
		        	}
		        	
		        	if (!currentValue.equals("NULL")) {
		        		eventType = reader.nextTag();
			        	while(eventType == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals("Result")) {
			        		event = getXMLEvent(reader).asStartElement();
			        		String valueNeeded = event.getAttributeByName(new QName("Value")).getValue();
			        		if(currentValue.equals(valueNeeded)) {
			        			int branchID = Integer.parseInt(event.getAttributeByName(new QName("BranchID")).getValue());
			        			jumpToBranch(branchID);
			        			break;
			        		}
			        		eventType = reader.nextTag();
				        	eventType = reader.nextTag();
			        	}
		        	} else {
		        		System.out.println("ERROR: Variable " + varToCheck + " does not exist!");
		        	}
		        } else {
		        	System.out.println("Unexpected/Unrecognized start element found.");
		        }
		    } else if (eventType == XMLStreamConstants.END_ELEMENT) {
		    	if (reader.getLocalName().equals("Branch") || reader.getLocalName().equals("Scene")) {
		    		endScene();
		    		break;
		    	}
		    	shouldContinue = true;
		    	
		    } else {
		    	//TODO: This may just be an incorrect stopgap fix?
		    	//This addresses the problem of not going straight to the next start tag,
		    	//as sometimes without this, nextLine will be called and not find the next tag.
		    	//I think this just makes it skip end tags. Which is fine, but I should check
		    	//if the end tag detected is the scene end tag, because then I need to end the scene.
		    	shouldContinue = true;
		    }
		    if (!shouldContinue) break;
		}
	}
	
	/**
	 * Jumps the script reader to the given script branch.
	 * 
	 * @param branchID - The branch to jump to
	 * @throws XMLStreamException
	 */
	private void jumpToBranch(int branchID) throws XMLStreamException {
		if (Constants.DEBUG) System.out.println("Jumping to branch " + branchID + "...");
		int eventType = reader.getEventType();
		
		while (reader.hasNext()) {
			eventType = reader.nextTag();
			System.out.println("WE DO IT");
			
			//If the next XML line is an opening tag
		    if (eventType == XMLStreamConstants.START_ELEMENT) {
		    	StartElement event = getXMLEvent(reader).asStartElement();
		    	System.out.println("Event is: " + event.toString());
		    	if (reader.getLocalName().equals("Branch")) {
		    		if (Integer.parseInt(event.getAttributeByName(new QName("ID")).getValue()) == branchID) {
		    			if (Constants.DEBUG) {
		    				System.out.println("Found branch! ID = " 
			    					+ event.getAttributeByName(
			    							new QName("ID")).getValue());
		    			}
			    		break;
		    		}
		    	}
		    }
		}
	}
	
	/* =================== *\
	 * LOWER LEVEL METHODS *
	 * =================== */
	
	//Custom drawing of screen. Do not alter unless you know what you're doing. 
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		//System.out.println("Painting");
		if (graphicsContext == null) graphicsContext = g2d;
		
		//Turns on antialiasing for text
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
		
		for (DBElement el : getElements()) el.drawElement(g2d);
	}
	
	//Underlying method. Do not alter.
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent (g);
		doDrawing(g);
	}
	
	//Underlying method. Do not alter.
	private XMLEvent getXMLEvent(XMLStreamReader reader) throws XMLStreamException {
		return allocator.allocate(reader);
	}
	
	//XML Reader things
	private XMLEventAllocator allocator;
	private XMLStreamReader reader;
}
package com.pluvicsoftware.javaVN;

import java.awt.Color;
import java.awt.Font;

import javax.xml.stream.events.Attribute;

/**
 * Represents a spoken Line of dialogue. Mostly exists to move all of the 
 * tedious parsing and checking from the Script Reader methods.
 * 
 * @author Brendan
 */
public class DBLine {
	//TODO: Add support for in-line pauses and in-line click-to-continues
	
	/**
	 * Constructor. Builds a new DBLine from a whole bunch of attributes which
	 * may contain a value or be null. Any argument left as null will be 
	 * assigned the default value.
	 * 
	 * @param speaker - Optional. The character saying the line. Defaults to  
	 * 					null, meaning no speaker. 
	 * @param body - Required. The text being spoken.
	 * @param font - Optional. Overrides the default font and displays the text
	 * 				 in a special font. Defaults to DEFAULT_LABEL_FONT.
	 * @param textColor - Optional. Displays the text in the given color. 
	 * 					  Defaults to DEFAULT_LABEL_COLOR.
	 * @param typeDelay - Optional. Displays the text with the specified delay
	 * 					  between each letter. Defaults to DEFAULT_TYPE_DELAY.
	 * @param textSound - Optional. Plays the specified text sound as each 
	 * 					  letter appears. Defaults to DEFAULT_TYPE_SOUND. 
	 * @param shouldContinue - Optional. Specifies whether or not the system
	 * 						   should automatically advance to the next line
	 * 						   without player input. Defaults to false.
	 */
	public DBLine(Attribute speaker, Attribute body, Attribute font, Attribute textColor, 
			Attribute typeDelay, Attribute textSound, Attribute shouldContinue) {
		if (speaker != null) this.speaker = speaker.getValue();
		if (body != null) this.body = body.getValue();
		if (font != null) this.font = Font.decode(font.getValue());
		if (textColor != null) this.textColor = Color.decode(textColor.getValue());
		if (typeDelay != null) this.typeDelay = Integer.decode(typeDelay.getValue());
		if (textSound != null) this.textSound = textSound.getValue();
		if (shouldContinue != null) this.shouldContinue = Boolean.parseBoolean(shouldContinue.getValue());
	}
	
	public Font font = Constants.DEFAULT_LABEL_FONT;
	public Color textColor = Constants.DEFAULT_LABEL_COLOR;
	// TypeDelay specifies how fast the letters pop up
	public int typeDelay = Constants.DEFAULT_TYPE_DELAY;
	/* ShouldContinue determines whether or not the engine should automatically
	 * continue to the next line without user input. */
	public boolean shouldContinue = false;
	// textSound is the sound the letters make as they appear
	public String textSound = Constants.DEFAULT_TYPE_SOUND;
	public String speaker; // Speaker is who's talking
	public String body; // Body is what they're saying
}

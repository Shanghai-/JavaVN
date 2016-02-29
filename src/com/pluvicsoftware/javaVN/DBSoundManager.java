package com.pluvicsoftware.javaVN;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * DBSoundManager handles opening sound files and playing them. Supports
 * several sound channels: a music track, a voiceover track, and an arbitrary
 * number of SFX tracks.
 * 
 * @author Brendan
 */
public class DBSoundManager {
	//TODO: Sound loudness variables.
	
	/* TODO: Finish implementing multiple channels!! Right now the SoundManager
	 * just crashes everything and is a real party pooper */
	
	/**
	 * Constructor. Builds a new soundManager with the specified number of SFX
	 * channels.
	 * 
	 * @param channels - the number of SFX channels to open
	 */
	public DBSoundManager(int channels) {
		/* TODO: Is this where the crash occurs? If channels = null, it should 
		 * default to DEFAULT_NUM_CHANNELS, not just crash... */
		sfxChannels = new AudioClip[channels];
		if (Constants.DEBUG) {
			System.out.println("Sound Manager initialized with " 
					+ channels + " SFX channels.");
		}
	}
	
	/**
	 * Attempts to find and load the audioClip with the given name from the SFX
	 * folder.
	 * 
	 * @param name - The name of the sound effect to search for
	 * @return - The desired AudioClip, or null if it cannot be found
	 */
	private AudioClip searchForSFX(String name) {
		File audioFile = new File(Constants.SOUNDS_PATH.concat(name));
		try {
			AudioClip ac = Applet.newAudioClip(audioFile.toURI().toURL());
			return ac;
		} catch (MalformedURLException e) {
			System.out.println("Encountered an error searching for SFX clip " 
					+ name + ". Details: ");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Attempts to find and load the audioClip with the given name from the
	 * music folder.
	 * 
	 * @param name - The name of the music file to search for
	 * @return - The desired AudioClip, or null if it cannot be found
	 */
	private AudioClip searchForMusic(String name) {
		File audioFile = new File(Constants.MUSIC_PATH.concat(name));
		try {
			AudioClip ac = Applet.newAudioClip(audioFile.toURI().toURL());
			return ac;
		} catch (MalformedURLException e) {
			System.out.println("Encountered an error searching for music " 
					+ name + ". Details: ");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Plays the specified AudioClip on the music sound channel
	 * 
	 * @param bgm - the AudioClip to play
	 */
	public void playBGM(AudioClip bgm) {
		if (this.bgm != null) this.bgm.stop();
		this.bgm = bgm;
		this.bgm.loop();
	}
	
	/**
	 * Plays the specified AudioClip on the music sound channel
	 * 
	 * @param bgm - the name of the AudioClip to play
	 */
	public void playBGM(String musicName) {
		if (this.bgm != null) this.bgm.stop();
		this.bgm = searchForMusic(musicName);
		this.bgm.loop();
	}
	
	/**
	 * Stops the music.
	 */
	public void stopBGM() {
		if (bgm != null) bgm.stop();
	}
	
	/**
	 * Loads the given AudioClip into the given SFX channel, but does not
	 * play it. 
	 * 
	 * @param channel - the SFX channel to load the given sound effect on
	 * @param sfx - the AudioClip to load
	 */
	public void setSFX(int channel, AudioClip sfx) {
		sfxChannels[channel] = sfx;
	}
	
	/**
	 * Loads the given AudioClip into the given SFX channel, but does not
	 * play it. 
	 * 
	 * @param channel - the SFX channel to load the given sound effect on
	 * @param sfx - the name of the AudioClip to load
	 */
	public void setSFX(int channel, String effectName) {
		sfxChannels[channel] = searchForSFX(effectName);
	}
	
	/**
	 * Plays the specified sound effect on the given SFX channel. Overrides
	 * any sound effect previously loaded into the same channel.
	 *  
	 * @param channel - the SFX channel to play the clip on
	 * @param sfx - the AudioClip to play
	 */
	public void playSFX(int channel, AudioClip sfx) {
		setSFX(channel, sfx);
		sfxChannels[channel].play();
	}
	
	/**
	 * Plays the specified sound effect on the given SFX channel. Overrides
	 * any sound effect previously loaded into the same channel.
	 *  
	 * @param channel - the SFX channel to play the clip on
	 * @param sfx - the name of the AudioClip to play
	 */
	public void playSFX(int channel, String sfx) {
		setSFX(channel, sfx);
		sfxChannels[channel].play();
	}
	
	/**
	 * Plays the AudioClip loaded into the given SFX channel
	 * 
	 * @param channel - the SFX channel to play
	 */
	public void playSFX(int channel) {
		sfxChannels[channel].play();
	}
	
	/**
	 * Stops the AudioClip loaded into the given SFX channel
	 * 
	 * @param channel - the SFX channel to stop
	 */
	public void stopSFX(int channel) {
		if (sfxChannels[channel] != null) sfxChannels[channel].stop();
	}
	
	/**
	 * Stops all sound effects from playing
	 */
	public void stopAllSFX() {
		for (int i = 0; i < 10; i++) {
			if (sfxChannels[i] != null) sfxChannels[i].stop();
		}
	}
	
	/**
	 * Plays the given AudioClip on the Voiceover channel
	 * 
	 * @param vo - the AudioClip to play
	 */
	public void playVO(AudioClip vo) {
		if (this.vo != null) this.vo.stop();
		this.vo = vo;
		this.vo.play();
	}
	
	/**
	 * Stops the voiceover
	 */
	public void stopVO() {
		if (vo != null) this.vo.stop();
	}
	
	private AudioClip bgm;
	private AudioClip[] sfxChannels;
	private AudioClip vo;
}
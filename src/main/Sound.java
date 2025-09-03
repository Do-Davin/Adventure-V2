package main;

import java.net.URL;
import javax.sound.sampled.*;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30];
    Long currentFrame;
    AudioInputStream audioStream;
    String currentTrackPath;
    
    // FOR OPTION STATE
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    final String[] SOUND_FILES = {
        "Background_music.wav",
        "bootSE.wav",
        "Receive_Damage.wav",
        "Swing_Sword.wav",
        "Zombie_hurt1.wav",
        "Zombie_hurt2.wav",
        "Zombie_death.wav",
        "cursor.wav",
        "pop.wav",
        "drink.wav",
        "fireball.wav",
        "arrow.wav",
        "Skeleton_hurt2.wav",
        "Skeleton_hurt3.wav",
        "Skeleton_death.wav",
        "cut.wav",
        "door.wav",
        "door_close.wav",
        "portial.wav",
        "chest.wav",
        "Golem_hurt1.wav",
        "Golem_hurt2.wav",
        "Golem_death.wav",
        "Golem_damage.wav",
        "Sheild.wav",
        "victory.wav",
        "gameover.wav",
        "dungeon.wav",
        "bossfight.wav"
    };  

    public Sound() {
        for(int i = 0; i < SOUND_FILES.length; i++) {
            soundURL[i] = getClass().getResource("/sound/" + SOUND_FILES[i]);
        }
    }

    public void setFile(int i) {
        try {
            if (i < 0 || i >= soundURL.length) return;

            currentTrackPath = soundURL[i].toString(); // Save path for resume
            audioStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void pause() {
    	if (clip != null && clip.isRunning()) {	
    		currentFrame = clip.getMicrosecondPosition();
    		clip.stop();
    	}
    }
    
    public void checkVolume() {
    	
    	switch(volumeScale) {
    	case 0: volume = -80f; break;
    	case 1: volume = -20f; break;
    	case 2: volume = -12f; break;
    	case 3: volume = -5f; break;
    	case 4: volume = 1f; break;
    	case 5: volume = 6f; break;
    	}
    	
    	fc.setValue(volume);
    	
    }

    public void resume() {
        try {
            URL resumeURL = new URL(currentTrackPath); // Reload same file
            audioStream = AudioSystem.getAudioInputStream(resumeURL);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            if(clip != null) {
            	clip.setMicrosecondPosition(currentFrame);
            	clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

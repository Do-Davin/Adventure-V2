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

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/Background_music.wav");
        soundURL[1] = getClass().getResource("/sound/bootSE.wav");
        soundURL[2] = getClass().getResource("/sound/Receive_Damage.wav");
        soundURL[3] = getClass().getResource("/sound/Swing_Sword.wav");
        soundURL[4] = getClass().getResource("/sound/Zombie_hurt1.wav");
        soundURL[5] = getClass().getResource("/sound/Zombie_hurt2.wav");
        soundURL[6] = getClass().getResource("/sound/Zombie_death.wav");
        soundURL[7] = getClass().getResource("/sound/cursor.wav");
        soundURL[8] = getClass().getResource("/sound/pop.wav");
        soundURL[9] = getClass().getResource("/sound/drink.wav");
        soundURL[10] = getClass().getResource("/sound/fireball.wav");
        soundURL[11] = getClass().getResource("/sound/arrow.wav");
        soundURL[12] = getClass().getResource("/sound/Skeleton_hurt2.wav");
        soundURL[13] = getClass().getResource("/sound/Skeleton_hurt3.wav");
        soundURL[14] = getClass().getResource("/sound/Skeleton_death.wav");
        soundURL[15] = getClass().getResource("/sound/cut.wav");
        soundURL[16] = getClass().getResource("/sound/door.wav");
        soundURL[17] = getClass().getResource("/sound/door_close.wav");
        soundURL[18] = getClass().getResource("/sound/portial.wav");
        soundURL[19] = getClass().getResource("/sound/chest.wav");
        soundURL[20] = getClass().getResource("/sound/Golem_hurt1.wav");
        soundURL[21] = getClass().getResource("/sound/Golem_hurt2.wav");
        soundURL[22] = getClass().getResource("/sound/Golem_death.wav");
        soundURL[23] = getClass().getResource("/sound/Golem_damage.wav");
        soundURL[24] = getClass().getResource("/sound/Sheild.wav");
        soundURL[25] = getClass().getResource("/sound/victory.wav");
        soundURL[26] = getClass().getResource("/sound/gameover.wav");
        soundURL[27] = getClass().getResource("/sound/dungeon.wav");
        soundURL[28] = getClass().getResource("/sound/bossfight.wav");


    }

    public void setFile(int i) {
        try {
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

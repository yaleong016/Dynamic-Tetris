package Modele;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Thread musicThread;
    private Clip clip;

    public MusicPlayer() {
        musicThread = null;
        clip = null;
    }

    public void playMusic(String filePath) {
        if (musicThread == null || !musicThread.isAlive()) {
            musicThread = new Thread(() -> {
                try {
                    File audioFile = new File(filePath);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music

                    clip.start();
                } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            });
            musicThread.start();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

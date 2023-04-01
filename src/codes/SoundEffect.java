package codes;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundEffect {

    private Clip clip;

    public void setFile(String filename) {

        try {
            File file = new File(filename);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
        } catch (Exception e) {

        }
    }

    public void play() {
        clip.setFramePosition(clip.getFramePosition());
        clip.start();

    }

    public void loop() {
        clip.setFramePosition(clip.getFramePosition());
        clip.start();
        clip.loop(10000);


    }

    public void stop() {
        clip.stop();
        clip.close();
    }

}

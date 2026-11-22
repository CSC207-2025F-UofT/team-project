package main.view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;


public class SoundManager {
    private Clip clip;

    public void playLoop(String filePath) {
        try {
            URL url = getClass().getResource(filePath);
            if (url == null) {
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolumePercentage(int percentage) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (percentage <= 0) {
                gainControl.setValue(gainControl.getMinimum());
            } else {
                float dB = (float) (Math.log10(percentage / 100.0) * 20.0);
                float clamped = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
                gainControl.setValue(clamped);
            }
        }
    }
}


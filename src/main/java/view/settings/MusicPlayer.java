package view.settings;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public final class MusicPlayer implements AutoCloseable {

    private Clip clip;
    private boolean closed;

    public synchronized void play()
            throws IOException, UnsupportedAudioFileException,
            LineUnavailableException {

        if (closed) {
            return;
        }

        if (clip == null) {
            URL resource = MusicPlayer.class.getResource(
                    "/audio/background.wav");

            if (resource == null) {
                throw new IOException("Background music file not found.");
            }

            Clip loaded = AudioSystem.getClip();

            try (AudioInputStream stream =
                         AudioSystem.getAudioInputStream(resource)) {

                loaded.open(stream);

                // Start at a quieter volume when supported.
                if (loaded.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volume = (FloatControl)
                            loaded.getControl(FloatControl.Type.MASTER_GAIN);

                    volume.setValue(Math.max(
                            volume.getMinimum(),
                            Math.min(-12f, volume.getMaximum())));
                }

                clip = loaded;
            } catch (IOException | UnsupportedAudioFileException
                     | LineUnavailableException | RuntimeException exception) {
                loaded.close();
                throw exception;
            }
        }

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public synchronized void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    @Override
    public synchronized void close() {
        closed = true;

        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }
}
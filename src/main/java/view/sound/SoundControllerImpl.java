package view.sound;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class SoundControllerImpl implements SoundController{

    // Map for the sounds
    private final Map<String, byte[]> soundCache = new HashMap<>();

    public SoundControllerImpl() {
        // Pre-carichiamo i file all'avvio per eliminare la latenza
        preLoadSound("burraco.wav");
        preLoadSound("round_end.wav");
        preLoadSound("victory.wav");
    }

    private void preLoadSound(String fileName) {
        try (InputStream is = getClass().getResourceAsStream("/" + fileName)) {
            if (is != null) {
                soundCache.put(fileName, is.readAllBytes());
            }
        } catch (Exception e) {
            System.err.println("Error in the pre-upload of: " + fileName);
        }
    }

    @Override
    public void playBurracoSound() {
        playFromCache("burraco.wav");
    }

    @Override
    public void playRoundEndSound() {
        playFromCache("round_end.wav");
    }

    @Override
    public void playVictorySound() {
        playFromCache("victory.wav");
    }

    private void playFromCache(String fileName) {
        byte[] soundData = soundCache.get(fileName);
        if (soundData == null) return;

        new Thread(() -> {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(soundData);
                AudioInputStream stream = AudioSystem.getAudioInputStream(bais);
                
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
                
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}

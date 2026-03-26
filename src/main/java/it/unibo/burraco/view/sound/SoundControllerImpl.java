package it.unibo.burraco.view.sound;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

import it.unibo.burraco.controller.sound.SoundController;

public class SoundControllerImpl implements SoundController {

    private final Map<String, byte[]> soundCache = new HashMap<>();

    public SoundControllerImpl() {
        preLoadSound("burraco.wav");
        preLoadSound("round_end.wav");
        preLoadSound("victory.wav");
    }

    private void preLoadSound(String fileName) {
        String path = fileName.startsWith("/") ? fileName : "/" + fileName;
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                soundCache.put(fileName, is.readAllBytes());
            } else {
                System.err.println("Resource not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading sound: " + fileName);
        }
    }

    @Override
    public void playBurracoSound() { playFromCache("burraco.wav", false); }

    @Override
    public void playRoundEndSound() { playFromCache("round_end.wav", false); }

    @Override
    public void playVictorySound() {
        playFromCache("victory.wav", false); 
    }

    private void playFromCache(String fileName, boolean blocking) {
    byte[] soundData = soundCache.get(fileName);
    System.out.println(">>> playFromCache: " + fileName + " | data null: " + (soundData == null));
    if (soundData == null) return;

    Thread audioThread = new Thread(() -> {
        try {
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(soundData);
            AudioInputStream stream = AudioSystem.getAudioInputStream(bais);

            Clip clip = AudioSystem.getClip();
            CountDownLatch latch = new CountDownLatch(1);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    latch.countDown();
                }
            });

            clip.open(stream);
            clip.start();
            latch.await();

            clip.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    audioThread.setDaemon(false);
    audioThread.start();

    if (blocking) {
        try {
            audioThread.join(); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
}
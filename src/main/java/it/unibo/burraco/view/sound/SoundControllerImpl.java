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

/**
 * Concrete implementation of the SoundController interface.
 * It uses a caching mechanism to store audio data in memory and plays 
 * sounds asynchronously to avoid blocking the main Game thread or the UI.
 */
public class SoundControllerImpl implements SoundController {

    /** Cache to store sound file bytes, preventing repeated disk I/O. */
    private final Map<String, byte[]> soundCache = new HashMap<>();

    /**
     * Constructor that preloads all necessary sound effects into the cache.
     */
    public SoundControllerImpl() {
        preLoadSound("burraco.wav");
        preLoadSound("round_end.wav");
        preLoadSound("victory.wav");
    }

    /**
     * Loads a sound file from the resources folder and stores its bytes in memory.
     * @param fileName the name of the file to load.
     */
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
    public void playVictorySound() { playFromCache("victory.wav", false); }

    /**
     * Logic to play a sound effect from the byte cache.
     * Creates a dedicated thread for audio playback to ensure non-blocking execution.
     * @param fileName the name of the cached sound to play.
     * @param blocking if true, the calling thread will wait for the sound to finish.
     */
    private void playFromCache(String fileName, boolean blocking) {
        byte[] soundData = soundCache.get(fileName);
        if (soundData == null) return;

        Thread audioThread = new Thread(() -> {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(soundData);
                AudioInputStream stream = AudioSystem.getAudioInputStream(bais);

                Clip clip = AudioSystem.getClip();
                // Latch used to synchronize the closing of the clip with the end of playback
                CountDownLatch latch = new CountDownLatch(1);

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        latch.countDown();
                    }   
                });
                clip.open(stream);
                clip.start();
                // Wait for the sound to stop before proceeding to cleanup
                latch.await();

                clip.close();
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Ensure playback finishes even if triggered by a short-lived thread
        audioThread.setDaemon(false);
        audioThread.start();

        // Optional blocking behavior (used for game-ending sounds if necessary)
        if (blocking) {
            try {
                audioThread.join(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
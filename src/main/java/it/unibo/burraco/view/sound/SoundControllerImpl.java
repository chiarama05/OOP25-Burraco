package it.unibo.burraco.view.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import it.unibo.burraco.controller.sound.SoundController;

/**
 * Concrete implementation of the SoundController interface.
 * It uses a caching mechanism to store audio data in memory and plays
 * sounds asynchronously to avoid blocking the main Game thread or the UI.
 */
public final class SoundControllerImpl implements SoundController {

    private static final String BURRACO_WAV = "burraco.wav";
    private static final String ROUND_END_WAV = "round_end.wav";
    private static final String VICTORY_WAV = "victory.wav";

    private final Map<String, byte[]> soundCache = new HashMap<>();

    /**
     * Constructor that preloads all necessary sound effects into the cache.
     */
    public SoundControllerImpl() {
        this.preLoadSound(BURRACO_WAV);
        this.preLoadSound(ROUND_END_WAV);
        this.preLoadSound(VICTORY_WAV);
    }

    /**
     * Loads a sound file from the resources folder and stores its bytes in memory.
     *
     * @param fileName the name of the file to load.
     */
    private void preLoadSound(final String fileName) {
        final String path = fileName.startsWith("/") ? fileName : "/" + fileName;
        try (InputStream is = this.getClass().getResourceAsStream(path)) {
            if (is != null) {
                this.soundCache.put(fileName, is.readAllBytes());
            }
        } catch (final IOException e) {
            /*
             * Sound loading failed. We ignore it to satisfy PMD (no System.out)
             * and Checkstyle (block is not empty thanks to this comment).
             */
        }
    }

    @Override
    public void playBurracoSound() {
        this.playFromCache(BURRACO_WAV, false);
    }

    @Override
    public void playRoundEndSound() {
        this.playFromCache(ROUND_END_WAV, false);
    }

    @Override
    public void playVictorySound() {
        this.playFromCache(VICTORY_WAV, false);
    }

    /**
     * Logic to play a sound effect from the byte cache.
     * Creates a dedicated thread for audio playback to ensure non-blocking execution.
     *
     * @param fileName the name of the cached sound to play.
     * @param blocking if true, the calling thread will wait for the sound to finish.
     */
    private void playFromCache(final String fileName, final boolean blocking) {
        final byte[] soundData = soundCache.get(fileName);
        if (soundData == null) {
            return;
        }

        final Thread audioThread = new Thread(() -> {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(soundData);
                 AudioInputStream stream = AudioSystem.getAudioInputStream(bais);
                 Clip clip = AudioSystem.getClip()) {

                final CountDownLatch latch = new CountDownLatch(1);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        latch.countDown();
                    }
                });
                clip.open(stream);
                clip.start();
                latch.await();

            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                /*
                 * Playback failed. We ignore the error to satisfy PMD (no System.out)
                 * and Checkstyle (block is not empty thanks to this comment).
                 */
            }
        });
        audioThread.setDaemon(false);
        audioThread.start();

        if (blocking) {
            try {
                audioThread.join();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

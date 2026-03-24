package it.unibo.burraco.view.sound;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

import it.unibo.burraco.controller.SoundController;

public class SoundControllerImpl implements SoundController{

    // Mappa per tenere i suoni già caricati in memoria
    private final Map<String, byte[]> soundCache = new HashMap<>();

    public SoundControllerImpl() {
        // Pre-carichiamo i file all'avvio per eliminare la latenza
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

        // Eseguiamo in un nuovo thread per non bloccare l'interfaccia grafica (UI)
        new Thread(() -> {
            try {
                // Leggiamo dai dati già in memoria (velocissimo)
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

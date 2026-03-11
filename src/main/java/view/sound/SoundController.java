package view.sound;

public interface SoundController {

    /**
     * Riproduce il suono del Burraco (7+ carte).
     */
    void playBurracoSound();

    /** Suona quando un giocatore scarta l'ultima carta e chiude la mano */
    void playRoundEndSound();

    /** Suona quando viene proclamato il vincitore definitivo della partita */
    void playVictorySound();

}

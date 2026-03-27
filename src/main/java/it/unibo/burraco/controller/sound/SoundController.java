package it.unibo.burraco.controller.sound;

/**
 * Interface defining the audio feedback system for the game.
 * It provides methods to trigger specific sound effects based on game events.
 */
public interface SoundController {

    /**
     * Plays the sound effect associated with achieving a Burraco (a combination of 7+ cards).
     */
    void playBurracoSound();

    /** 
     * Plays the sound effect triggered when a player discards their last card 
     * to successfully close the current round.
     */
    void playRoundEndSound();

    /** 
     * Plays the sound effect for the final victory, when a player
     * reaches the target match score.
     */
    void playVictorySound();
}

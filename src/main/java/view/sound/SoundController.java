package view.sound;

public interface SoundController {

   
    //Plays Burraco sound 
    void playBurracoSound();

    /** Plays when a player discard the last card and close the hand */
    void playRoundEndSound();

    /*
     * Plays when we got the definitive winner of the game 
    */
    void playVictorySound();

}

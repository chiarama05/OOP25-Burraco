package it.unibo.burraco.view.score;

/**
 * Interface defining the behavior of the scoring display.
 * It manages the visibility of the scoreboard and handles the transition 
 * between game rounds or the end of the match.
 */
public interface ScoreView {

    /** Displays the scoreboard window. */
    void display();
    
    /** Closes and releases the scoreboard window resources. */
    void close();

    /**
     * Sets the action to be performed when the user decides to proceed.
     * @param action a Runnable provided by the controller.
     */
    void setOnNextAction(Runnable action);

}

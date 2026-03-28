package it.unibo.burraco.controller.round;

/**
 * Interface defining the lifecycle of a game round.
 * It provides the method to start a fresh round within an ongoing match.
 */
public interface RoundController {

    /**
     * Orchestrates the transition to a new round by resetting models,
     * distributing cards, and refreshing the entire UI.
     */
    void processNewRound();
}

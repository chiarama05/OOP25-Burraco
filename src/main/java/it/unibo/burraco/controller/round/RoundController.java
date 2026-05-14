package it.unibo.burraco.controller.round;

/**
 * Interface defining the contract for managing the lifecycle of a game round.
 * It serves as a high-level orchestrator that transitions the match from 
 * one round to the next by synchronizing state reset and card distribution.
 */
@FunctionalInterface
public interface RoundController {

    /**
     * Executes the full sequence of operations required to start a new round.
     * This includes resetting the model entities (players, deck, discard pile),
     * triggers the initial dealing of cards, and ensures the View is fully 
     * synchronized with the new state.
     */
    void processNewRound();
}

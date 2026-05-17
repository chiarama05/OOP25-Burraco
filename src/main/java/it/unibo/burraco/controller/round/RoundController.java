package it.unibo.burraco.controller.round;

/**
 * Functional interface that orchestrates the transition between rounds.
 * Coordinates state reset, card distribution and view synchronization.
 */
@FunctionalInterface
public interface RoundController {

    /**
     * Executes the full sequence of operations required to start a new round.
     * This includes resetting the model entities,
     * dealing cards and updating the view.
     */
    void processNewRound();
}

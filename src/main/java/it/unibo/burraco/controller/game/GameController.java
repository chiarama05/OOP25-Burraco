package it.unibo.burraco.controller.game;

/**
 * Defines the contract for the main game loop controller.
 * BurracoApp depends on this interface, not on the concrete class.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface GameController {

    /**
     * Starts the game loop in a background thread.
     */
    void start();
}

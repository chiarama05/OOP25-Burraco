package it.unibo.burraco.controller.deck;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.view.deck.DeckDrawView;
import it.unibo.burraco.view.notification.deck.DeckNotifier;

/**
 * Controller responsible for handling the action of drawing a card from the deck.
 * Delegates business logic to {@link DrawManager} and notifies the view of the result.
 */
public class DeckActionController {

    private final GameController gameController;
    private final DrawManager drawManager;
    private final DeckNotifier notifier;

    /**
     * Constructs a new DeckActionController with the required dependencies.
     *
     * @param gameController the main game controller, used to retrieve the current player and deck
     * @param drawManager    the manager responsible for the draw logic
     * @param notifier       the notifier used to report draw errors to the user
     */
    public DeckActionController(final GameController gameController, 
                                final DrawManager drawManager, 
                                final DeckNotifier notifier) {
        this.gameController = gameController;
        this.drawManager = drawManager;
        this.notifier = notifier;
    }

    /**
     * Handles a draw-from-deck action triggered by the player.
     * Retrieves the current player and deck, attempts the draw,
     * and notifies either the view on success or the notifier on failure.
     *
     * @param view the view that initiated the draw action and will be updated on success
     */
    public void handle(final DeckDrawView view) {
        final var currentPlayer = gameController.getCurrentPlayer();
        final var deck = gameController.getCommonDeck();

        final DrawResult result = drawManager.drawFromDeck(currentPlayer, deck);

        if (result.getStatus() == DrawResult.Status.SUCCESS) {
            // Notify the view to update the player's hand display
            view.onDrawSuccess(currentPlayer, currentPlayer.getHand());
        } else {
            // Notify the user of the error (e.g. already drawn this turn)
            notifier.notifyDrawError(result);
        }
    }
}
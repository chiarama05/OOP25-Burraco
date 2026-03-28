package it.unibo.burraco.view.discardcard.discard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.game.GameNotifier;
import it.unibo.burraco.view.table.TableView;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Concrete implementation of {@link DiscardActionView}.
 * It wraps the physical button logic and maps controller results 
 * to visual updates and notifications.
 */
public class DiscardButton implements DiscardActionView {

    private final TableView view;
    private final DiscardViewImpl discardView;
    private final GameNotifier notifier;
    private boolean isPlayer1;

    /** Callback handler to notify the controller when the button is clicked. */
    private BiConsumer<DiscardButton, Boolean> onDiscardAction;

    /**
     * @param view the main table view to access player hands.
     * @param discardView the component managing the discard pile display.
     * @param notifier the utility used to show alerts to the player.
     */
    public DiscardButton(TableView view, DiscardViewImpl discardView, GameNotifier notifier) {
        this.view = view;
        this.discardView = discardView;
        this.notifier = notifier;

        // Register a listener on the UI component to trigger the action callback
        this.discardView.setDiscardListener(e -> {
            if (onDiscardAction != null) {
                onDiscardAction.accept(this, isPlayer1);
            }
        });
    }

    public void setIsPlayer1(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    /**
     * Sets the action handler.
     * @param handler the callback logic to execute on click.
     */
    public void setOnDiscardAction(BiConsumer<DiscardButton, Boolean> handler) {
        this.onDiscardAction = handler;
    }

    @Override
    public Set<Card> getSelectedCards(boolean isPlayer1) {
        // Delegates selection retrieval to the current player's hand view
        return view.getHandViewForCurrentPlayer(isPlayer1).getSelectedCards();
    }

    @Override
    public void onDiscardSuccess(Player player, List<Card> updatedPile, boolean isPlayer1) {
        // Clear selection and refresh the pile graphics
        view.getHandViewForCurrentPlayer(isPlayer1).clearSelection();
        discardView.updateDiscardPile(updatedPile);
    }

    @Override
    public void onDiscardError(String errorCode) {
        // Maps internal logic error codes to user-friendly notifications
        switch (errorCode) {
            case "must_draw" -> notifier.notifyMustDraw();
            case "select_one" -> notifier.notifySelectionError("Select only one card!");
            default -> notifier.notifySelectionError(errorCode);
        }
    }
}
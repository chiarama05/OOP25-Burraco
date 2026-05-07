package it.unibo.burraco.view.discardcard.discard;

import it.unibo.burraco.controller.discardcard.discard.DiscardResult;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.discard.DiscardNotifier;
import it.unibo.burraco.view.table.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Concrete implementation of {@link DiscardActionView}.
 * It wraps the physical button logic and maps controller results
 * to visual updates and notifications.
 */
public final class DiscardButton implements DiscardActionView {

    private final TableView view;
    private final DiscardView discardView;
    private final DiscardNotifier notifier;
    private boolean isP1;
    private BiConsumer<DiscardButton, Boolean> onDiscardAction;

    /**
     * Constructs a DiscardButton with necessary view components.
     *
     * @param view the main table view to access player hands.
     * @param discardView the component managing the discard pile display.
     * @param notifier the utility used to show alerts to the player.
     */
    public DiscardButton(final TableView view,
                         final DiscardView discardView,
                         final DiscardNotifier notifier) {
        this.view = view;
        this.discardView = discardView;
        this.notifier = notifier;

        this.discardView.setDiscardListener(e -> {
            if (this.onDiscardAction != null) {
                this.onDiscardAction.accept(this, this.isP1);
            }
        });
    }

    /**
     * Sets the player context.
     *
     * @param player1Flag true if the current player is Player 1.
     */
    public void setIsPlayer1(final boolean player1Flag) {
        this.isP1 = player1Flag;
    }

    /**
     * Sets the action handler.
     *
     * @param handler the callback logic to execute on click.
     */
    public void setOnDiscardAction(final BiConsumer<DiscardButton, Boolean> handler) {
        this.onDiscardAction = handler;
    }

    /**
     * Retrieves the selected cards.
     * 
     * @param player1Flag true if it's player 1
     * @return the set of selected cards
     */
    @Override
    public List<Card> getSelectedCards(final boolean player1Flag) {
        return new ArrayList<>(this.view.getHandViewForCurrentPlayer(player1Flag).getSelectedCards());
    }

    /**
     * Handles success.
     * 
     * @param player current player
     * @param updatedPile updated pile
     * @param player1Flag true if player 1
     */
    @Override
    public void onDiscardSuccess(final Player player, final List<Card> updatedPile, final boolean player1Flag) {
        this.view.getHandViewForCurrentPlayer(player1Flag).clearSelection();
        this.discardView.updateDiscardPile(updatedPile);
    }

    @Override
    public void onDiscardError(final DiscardResult.Status status) {
        this.notifier.notifyDiscardError(status);
    }

}

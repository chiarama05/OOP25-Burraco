package it.unibo.burraco.view.deck;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.TableView;
import javax.swing.JOptionPane;
import java.util.List;

/**
 * Concrete implementation of {@link DeckDrawView}.
 * Handles the visual response to draw actions by refreshing the player's hand panel.
 * Delegates draw logic to the registered {@code onDrawAction} handler.
 */
public final class DeckButton implements DeckDrawView {

    private final DeckView deckView;
    private final TableView tableView;
    private boolean isPlayer1;
    private Runnable onDrawAction;

    /**
     * Constructs a DeckButton and registers the click listener on the underlying Swing button.
     *
     * @param deckView  the panel containing the deck button
     * @param tableView the main table view
     */
    public DeckButton(final DeckView deckView, final TableView tableView) {
        this.deckView = deckView;
        this.tableView = tableView;
        this.deckView.getDeckButton().addActionListener(finalEvent -> this.handleDraw());
    }

    /**
     * Sets which player owns this button.
     *
     * @param isPlayer1 true if Player 1, false if Player 2
     */
    public void setIsPlayer1(final boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    /**
     * Registers the action to execute when the deck button is clicked.
     *
     * @param handler the draw action handler
     */
    public void setOnDrawAction(final Runnable handler) {
        this.onDrawAction = handler;
    }

    private void handleDraw() {
        if (this.onDrawAction != null) {
            this.onDrawAction.run();
        }
    }

    /**
     * Called by the controller when the draw succeeds.
     * Refreshes the player's hand panel with the updated cards.
     *
     * @param current the player who drew
     * @param hand    the updated list of cards in hand
     */
    @Override
    public void onDrawSuccess(final Player current, final List<Card> hand) {
        this.tableView.refreshHandPanel(this.isPlayer1, hand);
    }

    /**
     * Called by the controller when the draw fails.
     * Displays an error dialog to the user.
     *
     * @param message the error message to display
     */
    @Override
    public void showDrawError(final String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
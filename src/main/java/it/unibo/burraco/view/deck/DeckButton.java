package it.unibo.burraco.view.deck;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.TableView;
import javax.swing.*;
import java.util.List;

/**
 * Concrete implementation of {@link DeckDrawView}.
 * Handles the visual response to draw actions by refreshing the player's hand panel.
 * Delegates draw logic to the registered {@code onDrawAction} handler.
 */
public class DeckButton implements DeckDrawView {

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
    public DeckButton(DeckView deckView, TableView tableView) {
        this.deckView = deckView;
        this.tableView = tableView;
        this.deckView.getDeckButton().addActionListener(e -> handleDraw());
    }

    /**
     * Sets which player owns this button.
     *
     * @param isPlayer1 true if Player 1, false if Player 2
     */
    public void setIsPlayer1(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    /**
     * Registers the action to execute when the deck button is clicked.
     *
     * @param handler the draw action handler
     */
    public void setOnDrawAction(Runnable handler) {
        this.onDrawAction = handler;
    }

    private void handleDraw() {
        if (onDrawAction != null) onDrawAction.run();
    }

    /**
     * Called by the controller when the draw succeeds.
     * Refreshes the player's hand panel with the updated cards.
     */
    @Override
    public void onDrawSuccess(Player current, List<Card> hand) {
        tableView.refreshHandPanel(isPlayer1, hand);
    }

    /**
     * Called by the controller when the draw fails.
     * Displays an error dialog to the user.
     */
    @Override
    public void showDrawError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
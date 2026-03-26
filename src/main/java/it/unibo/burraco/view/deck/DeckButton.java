package it.unibo.burraco.view.deck;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.TableView;

import javax.swing.*;
import java.util.List;

public class DeckButton implements DeckDrawView {

    private final DeckView deckView;
    private final TableView tableView;
    private boolean isPlayer1;

    private Runnable onDrawAction;

    public DeckButton(DeckView deckView, TableView tableView) {
        this.deckView = deckView;
        this.tableView = tableView;
        this.deckView.getDeckButton().addActionListener(e -> handleDraw());
    }

    public void setIsPlayer1(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    public void setOnDrawAction(Runnable handler) {
        this.onDrawAction = handler;
    }

    private void handleDraw() {
        if (onDrawAction != null) onDrawAction.run();
    }

    @Override
    public void onDrawSuccess(Player current, List<Card> hand) {
        tableView.refreshHandPanel(isPlayer1, hand);
    }

    @Override
    public void showDrawError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
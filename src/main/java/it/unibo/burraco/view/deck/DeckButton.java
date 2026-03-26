package it.unibo.burraco.view.deck;

import it.unibo.burraco.controller.deck.DeckActionController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.view.notification.deck.DeckNotifier;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DeckButton implements ActionListener, DeckDrawView {

    private final DeckView deckView;
    private final TableView tableView;
    private final DeckActionController actionController;
    private boolean isPlayer1;

    public DeckButton(DeckView deckView, DrawManager drawManager,
                      TableView tableView, GameController gameController,
                      DeckNotifier notifier) {
        this.deckView = deckView;
        this.tableView = tableView;
        this.actionController = new DeckActionController(gameController, drawManager, notifier);
        this.deckView.getDeckButton().addActionListener(this);
    }

    public void setIsPlayer1(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionController.handle(this);
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

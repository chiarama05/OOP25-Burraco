package it.unibo.burraco.view.deck;

import it.unibo.burraco.controller.deck.DeckActionController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.view.notification.deck.DeckNotifier;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeckButton implements ActionListener, DeckDrawView {

    private final DeckView deckView;
    private final TableView tableView;
    private final DeckActionController actionController;

    public DeckButton(DeckView deckView, DrawManager drawManager,
                      TableView tableView, GameController gameController, 
                      DeckNotifier notifier) {
        this.deckView = deckView;
        this.tableView = tableView;
        this.actionController = new DeckActionController(gameController, drawManager, notifier);
        this.deckView.getDeckButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionController.handle(this);
    }

    @Override
    public void onDrawSuccess(Player current) {
        tableView.refreshHandPanel(current);
    }

    @Override
    public void showDrawError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}

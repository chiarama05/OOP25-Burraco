package it.unibo.burraco.view.button;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.deck.DeckView;
import it.unibo.burraco.view.table.TableView; 

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


    public class DeckButton implements ActionListener {

    private final DeckView deckView;
    private final DrawManager drawManager;
    private final TableView tableView;
    private final GameController gameController; 

    public DeckButton(DeckView deckView, DrawManager drawManager, TableView tableView, GameController gameController) {
        this.deckView = deckView;
        this.drawManager = drawManager;
        this.tableView = tableView;
        this.gameController = gameController;

        this.deckView.getDeckButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleDrawAction();
    }

    private void handleDrawAction() {
        
        Player currentPlayer = gameController.getCurrentPlayer(); 
        Deck deck = gameController.getCommonDeck(); 

        DrawResult result = drawManager.drawFromDeck(currentPlayer, deck);

        
        switch (result.getStatus()) {
            case SUCCESS: 
                
                tableView.refreshHandPanel(currentPlayer);
                break;

            case ALREADY_DRAWN:
                JOptionPane.showMessageDialog(null, "You have already drawn a card this turn!");
                break;

            case EMPTY_DECK:
                JOptionPane.showMessageDialog(null, "The deck is empty!");
                break;

            default:
                break;
        }
    }
}

package view.button;

import core.controller.GameController;
import core.drawcard.DrawManager;
import core.drawcard.DrawResult;
import model.deck.Deck;
import model.player.Player;
import view.deck.DeckView;
import view.table.TableView; 

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

package view.button;

import core.drawcard.DrawManager;
import core.drawcard.DrawResult;
import model.deck.Deck;
import model.player.Player;
import model.turn.TurnManager;
import view.table.TableViewImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


    public class DeckController implements ActionListener {

    private final DeckView deckView;
    private final DrawManager drawManager;
    private final TableViewImpl tableView;
    private final TurnManager turnManager;
    

    public DeckController(DeckView deckView, DrawManager drawManager, TableViewImpl tableView, TurnManager turnManager) {
        this.deckView = deckView;
        this.drawManager = drawManager;
        this.tableView = tableView;
        this.turnManager=turnManager;

        this.deckView.getDeckButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleDrawAction();
    }

    private void handleDrawAction() {
        Player currentPlayer = turnManager.getCurrentPlayer(); 
        Deck deck = tableView.getCommonDeck();

    
        DrawResult result = drawManager.drawFromDeck(currentPlayer, deck);

        if (result.getStatus() == DrawResult.Status.SUCCESS) {
            
            tableView.refreshHandPanel(currentPlayer);
        }

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

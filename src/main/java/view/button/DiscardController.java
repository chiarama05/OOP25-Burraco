package view.button;

import model.card.Card;
import model.player.Player;
import core.discardcard.DiscardManagerImpl;
import core.discardcard.DiscardResult;
import core.drawcard.DrawManager;
import view.discard.DiscardViewImpl;
import view.hand.handImpl;
import view.table.TableViewImpl;
import model.turn.TurnManager;
import model.discard.DiscardPile;
import view.sound.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JOptionPane;

public class DiscardController {

    private final TableViewImpl tableView;
    private final TurnManager turnManager;
    private final DiscardManagerImpl discardManager;
    private final DiscardViewImpl discardView;
    private final DiscardPile discardPileModel;
    private final DrawManager drawManager;

    public DiscardController(TableViewImpl tableView, TurnManager turnManager, DiscardManagerImpl discardManager, DiscardViewImpl discardView, DiscardPile discardPileModel, DrawManager drawManager) {
        this.tableView = tableView;
        this.turnManager = turnManager;
        this.discardManager = discardManager;
        this.discardView = discardView;
        this.discardPileModel = discardPileModel;
        this.drawManager = drawManager;

        // Collega il listener al bottone
        this.discardView.setDiscardListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDiscard();
            }
        });
    }

    /**
     * Logica del discard quando si clicca il bottone
     */
    private void handleDiscard() {
        if (!drawManager.hasDrawn()) {
        JOptionPane.showMessageDialog(null, 
            "You must draw from the deck or take the discard pile before discarding!", 
            "Action Required", 
            JOptionPane.WARNING_MESSAGE);
            return;
        }

        Player current=turnManager.getCurrentPlayer();
        handImpl handView=tableView.getHandViewForPlayer(current);

        Set<Card> selected = handView.getSelectedCards();
        if (selected.size() != 1) {
            JOptionPane.showMessageDialog(null, 
               "Select exactly one card to discard!", 
               "Selection Error", 
               JOptionPane.WARNING_MESSAGE); 
            return;
        }

        Card cardToDiscard = selected.iterator().next();
        DiscardResult result = discardManager.discard(current, cardToDiscard);

        System.out.println(result.getMessage());

        if(!result.isValid()){
            JOptionPane.showMessageDialog(null, result.getMessage());
            return;
        }

        if (!discardPileModel.getCards().contains(cardToDiscard)) {
            discardPileModel.add(cardToDiscard);
        }
        handView.refreshHand(current.getHand());
        handView.clearSelection();

        // Aggiorna la discard pile visiva
        discardView.updateDiscardPile(discardPileModel.getCards());

        if (current.hasFinishedCards()) { 
            tableView.markPotTaken(tableView.isPlayer1(current));
            tableView.getSoundController().playRoundEndSound();
            tableView.showPotnextTurn();
        }

        if(result.isGameWon()){
            tableView.showWinExit(tableView.isPlayer1(current));
            return;
        }
        
        if(result.isTurnEnds()){
            drawManager.resetTurn();
            turnManager.nextTurn();
        }   
    }
}


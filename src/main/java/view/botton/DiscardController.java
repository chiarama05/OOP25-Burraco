package view.botton;

import model.card.Card;
import model.player.Player;
import core.discardcard.DiscardManagerImpl;
import core.discardcard.DiscardResult;
import view.discard.DiscardViewImpl;
import view.hand.handImpl;
import view.table.TableViewImpl;
import model.turn.TurnManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class DiscardController {

    private final TableViewImpl tableView;
    private final TurnManager turnManager;
    private final DiscardManagerImpl discardManager;
    private final DiscardViewImpl discardView;

    public DiscardController(TableViewImpl tableView, TurnManager turnManager, DiscardManagerImpl discardManager, DiscardViewImpl discardView) {
        this.tableView = tableView;
        this.turnManager = turnManager;
        this.discardManager = discardManager;
        this.discardView = discardView;

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
        Player current=turnManager.getCurrentPlayer();
        handImpl handView=tableView.getHandViewForPlayer(current);

        Set<Card> selected = handView.getSelectedCards();
        if (selected.size() != 1) {
            System.out.println("You have to select just one card to discard!");
            return;
        }

        Card cardToDiscard = selected.iterator().next();
        DiscardResult result = discardManager.discard(current, cardToDiscard);

        System.out.println(result.getMessage());

        if(!result.isValid()){
            return;
        }
        handView.refreshHand(current.getHand());
        handView.clearSelection();

        // Aggiorna la discard pile visiva
        discardView.updateDiscardPile(discardManager.getDiscardPileCards());


        if(result.isGameWon()){
            tableView.showWinExit(tableView.isPlayer1(current));
            return;
        }
        
        if(result.isTurnEnds()){
            turnManager.nextTurn();
        }
        
    }
}


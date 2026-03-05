package view.botton;

import view.table.TableViewImpl;
import model.player.PlayerImpl;
import model.card.Card;
import core.combination.CombinationValidator;
import core.combination.StraightUtils;
import core.selectioncard.SelectionCardManager;

import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PutCombinationController {

    private final TableViewImpl tableView;
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final SelectionCardManager selectionManager;

    private boolean player1Turn;

    public PutCombinationController(TableViewImpl tableView,
                                 PlayerImpl player1,
                                 PlayerImpl player2,
                                 SelectionCardManager selectionManager) {
        this.tableView = tableView;
        this.player1 = player1;
        this.player2 = player2;
        this.selectionManager = selectionManager;
        this.player1Turn = true;
    }

    public void handlePutCombination() {
        PlayerImpl currentPlayer = player1Turn ? player1 : player2;

        // Recupera le carte selezionate tramite SelectionCardManager
        Set<Card> selectedSet = selectionManager.getSelectedCards();
        List<Card> selected = selectedSet.stream()
                                         .filter(currentPlayer::hasCard) // solo carte nella mano del player
                                         .collect(Collectors.toList());

        if(selected.size() < 3) {
        JOptionPane.showMessageDialog(null, "Select at least 3 cards!");
        return;
        }

        if(!CombinationValidator.isValidCombination(selected)) {
            JOptionPane.showMessageDialog(null, "Invalid combination!");
            return;
        }

        // Ordina scala se necessario
        if(StraightUtils.isSameSeed(selected)) {
            selected = StraightUtils.orderStraight(selected);
        }

        // Rimuovi le carte dalla mano del giocatore
        currentPlayer.removeCards(selected);

        // Aggiungi combinazione al pannello
        tableView.addCombinationToPlayerPanel(selected, player1Turn);

        // Aggiorna la mano del player sul pannello
        tableView.refreshHand(currentPlayer);

        // Pulisci la selezione
        selectionManager.clearSelection();

        // Aggiorna il turno
        player1Turn = !player1Turn;
        tableView.refreshTurnLabel(player1Turn);
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }
}
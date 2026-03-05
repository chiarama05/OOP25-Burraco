package view.botton;

import core.drawcard.DrawManager;
import core.drawcard.DrawResult;
import model.deck.DeckImpl;
import model.player.Player;
import view.table.TableViewImpl;
import javax.swing.JOptionPane;

public class DeckController {
    private final DrawManager drawManager;
    private final TableViewImpl tableView;

    public DeckController(DeckView deckView, DrawManager drawManager, TableViewImpl tableView) {
        this.drawManager = drawManager;
        this.tableView = tableView;

        // Quando clicco sul mazzo grafico...
        deckView.getDeckButton().addActionListener(e -> {
            Player currentPlayer = tableView.getCurrentPlayer();
            DeckImpl deck = tableView.getCommonDeck();

            // Esegui la pesca dal manager
            DrawResult result = drawManager.drawFromDeck(currentPlayer, deck);

            // Gestisci il risultato
            handleResult(result, currentPlayer);
        });
    }

    private void handleResult(DrawResult result, Player player) {
        switch (result.getStatus()) {
            case SUCCESS:
                // Usiamo il tuo metodo già esistente per aggiornare la grafica
                tableView.refreshHandPanel(); 
                break;
            case ALREADY_DRAWN:
                JOptionPane.showMessageDialog(null, "Hai già pescato in questo turno!");
                break;
            case EMPTY_DECK:
                JOptionPane.showMessageDialog(null, "Mazzo esaurito!");
                break;
            default:
                break;
        }
    }
}
package it.unibo.burraco.view.attach;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.table.TableView;

import javax.swing.JFrame;
import java.util.List;

/**
 * Factory per creare AttachButton già cablati.
 * Non dipende più da GameController, ClosureManager o PotManager:
 * ogni bottone completa direttamente il CompletableFuture della view.
 */
public class AttachButtonFactory {

    private final TableView tableView;
    private final JFrame frame;

    /**
     * @param tableView la view principale (fonte del pendingFuture)
     * @param frame     il frame genitore (per eventuali dialog)
     */
    public AttachButtonFactory(final TableView tableView, final JFrame frame) {
        this.tableView = tableView;
        this.frame = frame;
    }

    /**
     * Crea un AttachButton cablato con la view.
     *
     * @param cards     le carte della combinazione
     * @param isPlayer1 true se la combinazione appartiene a Player 1
     * @return un AttachButton pronto per essere aggiunto alla UI
     */
    public AttachButton create(final List<Card> cards, final boolean isPlayer1) {
        return new AttachButton(cards, this.tableView, isPlayer1);
    }
}
package view.bottom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import model.card.Card;
import model.player.Player;
import core.discardcard.DiscardManagerImpl;
import core.discardcard.DiscardResult;
import view.discard.DiscardViewImpl;
import view.hand.handImpl;

public class DiscardController {

    private final Player player;
    private final handImpl handView;
    private final DiscardManagerImpl discardManager;
    private final DiscardViewImpl discardView;

    public DiscardController(Player player, handImpl handView, DiscardManagerImpl discardManager, DiscardViewImpl discardView) {
        this.player = player;
        this.handView = handView;
        this.discardManager = discardManager;
        this.discardView = discardView;

        // Collega il listener al bottone
        discardView.setDiscardListener(new ActionListener() {
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
        Set<Card> selected = handView.getSelectedCards();

        if (selected.size() != 1) {
            // Se non c'è esattamente una carta selezionata
            System.out.println("Seleziona una e una sola carta da scartare!");
            return;
        }

        Card cardToDiscard = selected.iterator().next();

        // Chiamata al DiscardManager
        DiscardResult result = discardManager.discard(player, cardToDiscard);

        // Mostra messaggio (puoi anche usare JOptionPane)
        System.out.println(result.getMessage());

        if (result.isValid()) {
            // Aggiorna la mano e cancella la selezione
            handView.refreshHand(player.getHand());
            handView.clearSelection();

            // Aggiorna la discard pile visiva
            discardView.updateDiscardPile(discardManager.getDiscardPileCards());
        }
    }
}

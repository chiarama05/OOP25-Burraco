package it.unibo.burraco.view.table;
 
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.view.table.hand.HandView;
import java.util.List;
 
/**
 * View interface for the initial card-distribution phase.
 */
public interface DistributionView {
 
    /**
     * Returns the HandView for Player 1.
     *
     * @return HandView for Player 1
     */
    HandView getPlayer1HandView();
 
    /**
     * Returns the HandView for Player 2.
     *
     * @return HandView for Player 2
     */
    HandView getPlayer2HandView();

    /**
     * Refreshes the graphical representations of both players' hands.
     *
     * @param hand1 the list of cards currently held by Player 1
     * @param hand2 the list of cards currently held by Player 2
     */
    void refresh(List<Card> hand1, List<Card> hand2);
}

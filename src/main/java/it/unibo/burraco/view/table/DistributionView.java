package it.unibo.burraco.view.table;
 
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.view.table.hand.HandView;
import java.util.List;
 
/**
 * View interface for the initial card-distribution phase.
 *
 * Replaces the direct dependency on the concrete {@link TableSetUpView} class,
 * and removes the erroneous {@code DiscardView} parameter that created
 * View-to-View coupling.
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

    void refresh(List<Card> hand1, List<Card> hand2);
}
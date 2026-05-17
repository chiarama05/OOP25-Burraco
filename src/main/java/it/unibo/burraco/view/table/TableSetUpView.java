package it.unibo.burraco.view.table;
 
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.view.table.hand.HandView;
import it.unibo.burraco.view.table.hand.HandViewImpl;
import it.unibo.burraco.view.table.hand.SelectionCardManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of {@link DistributionView} responsible for initializing
 * and refreshing the components involved in the initial card-distribution phase.
 */
public final class TableSetUpView implements DistributionView {
 
    private final HandView handPlayer1;
    private final HandView handPlayer2;

    /**
     * Constructs a new TableSetUpView and initializes the hand sub-views
     * with their respective selection managers.
     */
    public TableSetUpView() {
        this.handPlayer1 = new HandViewImpl(new SelectionCardManager());
        this.handPlayer2 = new HandViewImpl(new SelectionCardManager());
    }
 
    @Override
    public HandView getPlayer1HandView() {
        return handPlayer1;
    }
 
    @Override
    public HandView getPlayer2HandView() {
        return handPlayer2;
    }

    @Override
    public void refresh(final List<Card> hand1,
                        final List<Card> hand2) {
        this.handPlayer1.refreshHand(new ArrayList<>(hand1));
        this.handPlayer2.refreshHand(new ArrayList<>(hand2));
    }
}

package it.unibo.burraco.view.table;
 
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.view.SelectionCardManager;
import it.unibo.burraco.view.table.hand.HandView;
import it.unibo.burraco.view.table.hand.HandViewImpl;
 
import java.util.ArrayList;
import java.util.List;

public final class TableSetUpView implements DistributionView {
 
    private final HandView handPlayer1;
    private final HandView handPlayer2;
 
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
 
    /**
     * Updates both hand panels after the initial cards have been dealt.
     *
     * The {@code discardPile} parameter is accepted for completeness but is not
     * rendered here: the caller is responsible for calling
     * {@code TableView.updateDiscardPile()} after this method returns.
     *
     * @param hand1       cards to display in Player 1's hand
     * @param hand2       cards to display in Player 2's hand
     * @param discardPile the initial discard pile (not rendered here)
     */
    @Override
    public void refresh(final List<Card> hand1,
                        final List<Card> hand2,
                        final List<Card> discardPile) {
        this.handPlayer1.refreshHand(new ArrayList<>(hand1));
        this.handPlayer2.refreshHand(new ArrayList<>(hand2));
    }
}

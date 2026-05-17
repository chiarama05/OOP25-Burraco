package it.unibo.burraco.view.components.attach;
 
import it.unibo.burraco.model.cards.Card;
import java.util.List;
 
/**
 * Callback invoked when the player clicks on a combination button
 * to attach cards to it.
 *
 * The view fires this event with the target combination cards;
 * the wiring layer (TableViewImpl) handles selecting the hand cards,
 * building the Move, and completing the pending future.
 * AttachButton knows nothing about Move or CompletableFuture.
 */
@FunctionalInterface
public interface AttachListener {
 
    /**
     * Called when the player has clicked this combination.
     *
     * @param targetCombination the cards currently in the combination that was clicked
     */
    void onAttachRequested(List<Card> targetCombination);
}

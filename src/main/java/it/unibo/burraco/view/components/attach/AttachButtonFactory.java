package it.unibo.burraco.view.components.attach;
 
import it.unibo.burraco.model.cards.Card;
 
import java.util.List;
 
/**
 * Creates AttachButton instances wired with an AttachListener.
 * The listener is provided by the caller at creation time,
 * keeping AttachButton fully decoupled from the rest of the view infrastructure.
 */
public final class AttachButtonFactory {
 
    private final AttachListener listener;
 
    /**
     * Constructs the factory with the listener to wire into every button.
     *
     * @param listener the callback invoked when any combination button is clicked
     */
    public AttachButtonFactory(final AttachListener listener) {
        this.listener = listener;
    }
 
    /**
     * Creates an {@link AttachButton} for the given combination.
     *
     * @param cards the cards forming the combination
     * @param isPlayer1 true if the combination belongs to Player 1
     * @return a ready-to-add button
     */
    public AttachButton create(final List<Card> cards, final boolean isPlayer1) {
        return new AttachButton(cards, isPlayer1, this.listener);
    }
}

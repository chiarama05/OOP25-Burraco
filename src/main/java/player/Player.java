package player;
import card.Card;

import java.util.List;

public interface Player {

    /**
     * Returns the cards currently in the player's hand.
     */
    List<Card> getHand();

    /**
     * Adds a card to the player's hand.
     */
    void addCardHand(Card c);

    /**
     * Removes a card from the player's hand.
     */
    void removeCardHand(Card c);

    /**
     * Checks if the player has no cards left in hand.
     */
    boolean emptyHand();


     /**
     * Returns whether the player is currently playing from the "pot".
     */
    boolean isInPot();

    /**
     * Sets the pot state of the player.
     */
    void setInPot(boolean flag);

}

package it.unibo.burraco.model.player;

import java.util.List;

import it.unibo.burraco.model.card.Card;

/**
 * Defines the contract for a player in the Burraco game.
 */
public interface Player {
    /**
     * Returns the player's name.
     * 
     * @return the name of the player.
     */
    String getName();

    /**
     * Returns the cards currently in the player's hand.
     * 
     * @return the list of cards in hand.
     */
    List<Card> getHand();

    /**
     * Adds a card to the player's hand.
     * 
     * @param c the card to add.
     */
    void addCardHand(Card c);

    /**
     * Removes a card from the player's hand.
     * 
     * @param c the card to remove.
     */
    void removeCardHand(Card c);

    /**
     * Removes a list of cards from the player's hand.
     * 
     * @param cards the cards to remove.
     */
    void removeCards(List<Card> cards);

    /**
     * Checks if the player has a specific card in hand.
     * 
     * @param card the card to look for.
     * @return true if the card is in hand, false otherwise.
     */
    boolean hasCard(Card card);

    /**
     * Returns whether the player is currently playing from the pot.
     * 
     * @return true if the player is in pot, false otherwise.
     */
    boolean isInPot();

    /**
     * Sets the pot state of the player.
     * 
     * @param flag true if the player is in pot, false otherwise.
     */
    void setInPot(boolean flag);

    /**
     * Adds a new combination to the player's table.
     * 
     * @param comb the list of cards forming the combination.
     */
    void addCombination(List<Card> comb);

    /**
     * Returns all combinations placed on the table by the player.
     * 
     * @return the list of combinations.
     */
    List<List<Card>> getCombinations();

    /**
     * Returns the number of burracos (combinations of size >= 7).
     * 
     * @return the burraco count.
     */
    int getBurracoCount();

    /**
     * Draws the pot into the hand and clears the pot.
     */
    void drawPot();

    /**
     * Checks if the player has no cards left in hand.
     * 
     * @return true if the hand is empty, false otherwise.
     */
    boolean hasFinishedCards();

    /**
     * Adds cards to the player's pot.
     * 
     * @param cards the cards to add to the pot.
     */
    void addToPot(List<Card> cards);

    /**
     * Returns the total accumulated score across all rounds of the match.
     * 
     * @return the total match score.
     */
    int getMatchTotalScore();

    /**
     * Adds points to the player's total match score.
     * 
     * @param points the points to add.
     */
    void addPointsToMatch(int points);

    /**
     * Resets the player's state for a new round.
     */
    void resetForNewRound();
}

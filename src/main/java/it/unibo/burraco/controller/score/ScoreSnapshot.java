package it.unibo.burraco.controller.score;
 
import it.unibo.burraco.model.cards.Card;
import java.util.List;
 
/**
 * An immutable Data Transfer Object (DTO) representing a player's score breakdown.
 * It encapsulates all calculated values needed for display, ensuring the View 
 * layer remains decoupled from the complex business logic of the {@link Score} 
 * and {@link Player} models.
 * 
 * @param playerName          the display name of the player
 * @param cardsOnTable        points gained from cards melded on the table
 * @param cleanBurracoPoints  total bonus points from clean Burraco sets
 * @param dirtyBurracoPoints  total bonus points from dirty Burraco sets
 * @param closureBonus        points awarded for ending the round (closure)
 * @param potPenalty          penalties incurred for not taking the side pot
 * @param cardsInHandPenalty  negative points from cards remaining in hand
 * @param roundTotal          the final sum of points for the current round
 * @param matchTotal          the cumulative score including previous rounds
 * @param isWinner            true if the player has won the entire match
 * @param finalHand           a read-only list of cards remaining in the hand at closure.
 *                            To guarantee immutability, this should be defensive-copied
 *                            (e.g., via List.copyOf) before initialization.
 */
public record ScoreSnapshot(
        String playerName,
        int cardsOnTable,
        int cleanBurracoPoints,
        int dirtyBurracoPoints,
        int closureBonus,
        int potPenalty,
        int cardsInHandPenalty,
        int roundTotal,
        int matchTotal,
        boolean isWinner,
        List<Card> finalHand
) { }
 
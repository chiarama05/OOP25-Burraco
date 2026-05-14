package it.unibo.burraco.controller.score;
 
import it.unibo.burraco.model.cards.Card;
import java.util.List;
 
/**
 * Immutable data transfer object carrying all display-ready score values
 * for one player at the end of a round.
 *
 * Built by the Controller (RoundEndHandler) so that the View receives
 * only plain data and never touches Player or Score directly.
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
 
package card;

import java.util.List;

/**
 * Interface for wildcard (Jolly) behavior in card games.
 */
public interface Jolly {

    /**
     * Determines if the card is a pure Jolly (always acts as a wildcard).
     * 
     * @return true if the card is a Jolly, false otherwise
     */
    boolean isPureJolly();

    /**
     * Determines if the card acts as a Jolly (wildcard) in a given context.
     * For example, a "2" can act as a wildcard unless it is a natural two in a straight (A-2-3).
     * 
     * @param context the list of cards representing the current set or straight
     * @return true if the card acts as a Jolly in this context, false otherwise
     */
    boolean isJolly(List<Card> context);
}
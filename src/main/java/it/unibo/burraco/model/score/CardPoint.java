package it.unibo.burraco.model.score;

import it.unibo.burraco.model.card.Card;

/**
 * Utility class to manage point values and numerical conversions
 * for cards based on Burraco rules.
 */
public final class CardPoint {

    private static final int POINTS_JOLLY = 30;
    private static final int POINTS_TWO = 20;
    private static final int POINTS_ACE = 15;
    private static final int POINTS_HIGH_CARDS = 10;
    private static final int POINTS_LOW_CARDS = 5;

    private static final int RANK_ACE = 1;
    private static final int RANK_TWO = 2;
    private static final int RANK_THREE = 3;
    private static final int RANK_FOUR = 4;
    private static final int RANK_FIVE = 5;
    private static final int RANK_SIX = 6;
    private static final int RANK_SEVEN = 7;
    private static final int RANK_EIGHT = 8;
    private static final int RANK_NINE = 9;
    private static final int RANK_TEN = 10;
    private static final int RANK_JACK = 11;
    private static final int RANK_QUEEN = 12;
    private static final int RANK_KING = 13;

    private static final String JOLLY_STR = "Jolly";
    private static final String TWO_STR = "2";
    private static final String ACE_STR = "A";
    private static final String KING_STR = "K";
    private static final String QUEEN_STR = "Q";
    private static final String JACK_STR = "J";
    private static final String TEN_STR = "10";
    private static final String NINE_STR = "9";
    private static final String EIGHT_STR = "8";
    private static final String SEVEN_STR = "7";
    private static final String SIX_STR = "6";
    private static final String FIVE_STR = "5";
    private static final String FOUR_STR = "4";
    private static final String THREE_STR = "3";

    /**
     * Private constructor to prevent instantiation of a utility class.
     */
    private CardPoint() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Calculates the score value of a specific card.
     * 
     * @param card the card to evaluate.
     * @return the points assigned to the card.
     */
    public static int getCardPoints(final Card card) {
        final String value = card.getValue();
        if (JOLLY_STR.equalsIgnoreCase(value)) {
            return POINTS_JOLLY;
        }
        if (TWO_STR.equals(value)) {
            return POINTS_TWO;
        }
        if (ACE_STR.equals(value)) {
            return POINTS_ACE;
        }
        if (KING_STR.equals(value) || QUEEN_STR.equals(value) || JACK_STR.equals(value) 
            || TEN_STR.equals(value) || NINE_STR.equals(value) || EIGHT_STR.equals(value)) {
            return POINTS_HIGH_CARDS;
        }
        if (SEVEN_STR.equals(value) || SIX_STR.equals(value) || FIVE_STR.equals(value) 
            || FOUR_STR.equals(value) || THREE_STR.equals(value)) {
            return POINTS_LOW_CARDS;
        }
        return 0;
    }

    /**
     * Converts a card's string value to its numerical rank for sorting/sequence logic.
     * 
     * @param card the card to convert.
     * @return an integer from 1 (Ace) to 13 (King).
     * @throws IllegalArgumentException if the card value is not a standard rank.
     */
    public static int toInt(final Card card) {
        return switch (card.getValue()) {
            case ACE_STR   -> RANK_ACE;
            case TWO_STR   -> RANK_TWO;
            case THREE_STR -> RANK_THREE;
            case FOUR_STR  -> RANK_FOUR;
            case FIVE_STR  -> RANK_FIVE;
            case SIX_STR   -> RANK_SIX;
            case SEVEN_STR -> RANK_SEVEN;
            case EIGHT_STR -> RANK_EIGHT;
            case NINE_STR  -> RANK_NINE;
            case TEN_STR   -> RANK_TEN;
            case JACK_STR  -> RANK_JACK;
            case QUEEN_STR -> RANK_QUEEN;
            case KING_STR  -> RANK_KING;
            default -> throw new IllegalArgumentException(
                "Cannot convert to int: " + card.getValue()
            );
        };
    }
}

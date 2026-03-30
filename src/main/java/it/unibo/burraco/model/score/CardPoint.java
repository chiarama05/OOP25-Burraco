package it.unibo.burraco.model.score;

import it.unibo.burraco.model.card.Card;

/**
 * Utility class to manage point values and numerical conversions
 * for cards based on Burraco rules.
 */
public final class CardPoint {

    /**
     * Private constructor to prevent instantiation of a utility class.
     */
    private CardPoint() { }

    /**
     * Calculates the score value of a specific card.
     * @param card the card to evaluate.
     * @return the points assigned to the card.
     */
    public static int getCardPoints(Card card) {
        final String value = card.getValue();
        switch (value) {
            case "Jolly":
                return 30;
            case "2":
                return 20;
            case "A":
                return 15;
            case "K":
            case "Q":
            case "J":
            case "10":
            case "9":
            case "8":
                return 10;
            case "7":
            case "6":
            case "5":
            case "4":
            case "3":
                return 5;
            default:
                return 0;
        }
    }

    /**
     * Converts a card's string value to its numerical rank for sorting/sequence logic.
     * @param card the card to convert.
     * @return an integer from 1 (Ace) to 13 (King).
     * @throws IllegalArgumentException if the card value is not a standard rank.
     */
    public static int toInt(Card card) {
        return switch (card.getValue()) {
            case "A"  -> 1;
            case "2"  -> 2;
            case "3"  -> 3;
            case "4"  -> 4;
            case "5"  -> 5;
            case "6"  -> 6;
            case "7"  -> 7;
            case "8"  -> 8;
            case "9"  -> 9;
            case "10" -> 10;
            case "J"  -> 11;
            case "Q"  -> 12;
            case "K"  -> 13;
            default -> throw new IllegalArgumentException(
                "Cannot convert to int: " + card.getValue()
            );
        };
    }
}

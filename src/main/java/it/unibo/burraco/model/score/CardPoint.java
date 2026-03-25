package it.unibo.burraco.model.score;
import it.unibo.burraco.model.card.*;

/**
 * Utility class that calculates the point value of a single card
 * according to Burraco rules.
 */
public class CardPoint {

    private CardPoint() {}

    /**
     * Returns the Burraco point value of a card.
     */
    public static int getCardPoints(Card card) {

        String value = card.getValue();

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

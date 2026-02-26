package score;
import model.card.*;

/**
 * Utility class that calculates the point value of a single card
 * according to Burraco rules.
 */
public class CardPointCalculator {

    private CardPointCalculator() {}

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
}

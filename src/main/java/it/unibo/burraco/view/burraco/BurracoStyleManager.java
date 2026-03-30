package it.unibo.burraco.view.burraco;

import it.unibo.burraco.model.card.Card;

import java.awt.Color;
import java.util.List;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public final class BurracoStyleManager {
    
    private static final Color BURRACO_GREEN = new Color(50, 205, 50); 
    private static final Color BURRACO_BG = new Color(240, 255, 240); 
    private static final Color DEFAULT_BORDER = Color.GRAY;

    private static final int BURRACO_MIN_CARDS = 7;
    private static final int BORDER_THICKNESS_BURRACO = 4;
    private static final int BORDER_THICKNESS_DEFAULT = 1;

    /**
     * Private constructor to hide the implicit public one.
     * Prevents instantiation of this utility class.
     */
    private BurracoStyleManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Checks if a list of cards forms a Burraco.
     *
     * @param cards the list of cards to check.
     * 
     * @return true if the size is at least 7, false otherwise.
     */
    public static boolean isBurraco(final List<Card> cards) {
        return cards != null && cards.size() >= BURRACO_MIN_CARDS;
    }

    /**
     * Returns the appropriate border based on whether the combination is a Burraco.
     *
     * @param cards the list of cards.
     * 
     * @return a thick green border for Burraco, a thin gray one otherwise.
     */
    public static Border getBurracoBorder(final List<Card> cards) {
        if (isBurraco(cards)) {
            return BorderFactory.createLineBorder(BURRACO_GREEN, BORDER_THICKNESS_BURRACO);
        }
        return BorderFactory.createLineBorder(DEFAULT_BORDER, BORDER_THICKNESS_DEFAULT);
    }

    /**
     * Returns the appropriate background color.
     *
     * @param cards the list of cards.
     * 
     * @return a light green color for Burraco, white otherwise.
     */
    public static Color getBurracoBackground(final List<Card> cards) {
        if (isBurraco(cards)) {
            return BURRACO_BG;
        }
        return Color.WHITE;
    }
}

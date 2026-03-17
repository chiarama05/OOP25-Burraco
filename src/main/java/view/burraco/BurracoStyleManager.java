package view.burraco;

import model.card.Card;
import java.awt.*;
import java.util.List;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class BurracoStyleManager {
    
    //Burraco's colors
    private static final Color BURRACO_GREEN = new Color(50, 205, 50);
    private static final Color DEFAULT_BORDER = Color.GRAY;

    /**
     *
     * Check if the card's list is a Burraco (7 or more cards)
     */
    public static boolean isBurraco(List<Card> cards) {
        return cards != null && cards.size() >= 7;
    }

    /**
     * Give the correct border: green if it is a Burraco, grey otherwise
     * 
     */
    public static Border getBurracoBorder(List<Card> cards) {
        if (isBurraco(cards)) {
            // Bordo verde più spesso (3px) per farlo risaltare
            return BorderFactory.createLineBorder(BURRACO_GREEN, 4);
        }
        return BorderFactory.createLineBorder(DEFAULT_BORDER, 1);
    }

    /**
     *
     * Return a special background for the Burraco is optional
     */
    public static Color getBurracoBackground(List<Card> cards) {
        if (isBurraco(cards)) {
            return new Color(240, 255, 240); 
        }
        return Color.WHITE;
    }
}

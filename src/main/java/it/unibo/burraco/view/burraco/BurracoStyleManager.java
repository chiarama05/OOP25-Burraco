package it.unibo.burraco.view.burraco;

import it.unibo.burraco.model.card.Card;
import java.awt.*;
import java.util.List;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class BurracoStyleManager {
    
    private static final Color BURRACO_GREEN = new Color(50, 205, 50); 
    private static final Color BURRACO_BG = new Color(240, 255, 240); 
    private static final Color DEFAULT_BORDER = Color.GRAY;

    public static boolean isBurraco(final List<Card> cards) {
        return cards != null && cards.size() >= 7;
    }

    public static Border getBurracoBorder(final List<Card> cards) {
        if (isBurraco(cards)) {
            return BorderFactory.createLineBorder(BURRACO_GREEN, 4);
        }
        return BorderFactory.createLineBorder(DEFAULT_BORDER, 1);
    }

    public static Color getBurracoBackground(final List<Card> cards) {
        if (isBurraco(cards)) {
            return BURRACO_BG;
        }
        return Color.WHITE;
    }
}

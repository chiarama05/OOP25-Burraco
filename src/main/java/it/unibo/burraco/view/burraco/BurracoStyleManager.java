package it.unibo.burraco.view.burraco;

import it.unibo.burraco.model.card.Card;
import java.awt.*;
import java.util.List;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class BurracoStyleManager {
    
    // Definiamo i colori per il Burraco
    private static final Color BURRACO_GREEN = new Color(50, 205, 50); // Verde brillante
    private static final Color DEFAULT_BORDER = Color.GRAY;

    /**
     * Controlla se la lista di carte costituisce un Burraco (7 o più carte).
     */
    public static boolean isBurraco(List<Card> cards) {
        return cards != null && cards.size() >= 7;
    }

    /**
     * Restituisce il bordo appropriato: verde se è Burraco, grigio altrimenti.
     */
    public static Border getBurracoBorder(List<Card> cards) {
        if (isBurraco(cards)) {
            // Bordo verde più spesso (3px) per farlo risaltare
            return BorderFactory.createLineBorder(BURRACO_GREEN, 4);
        }
        return BorderFactory.createLineBorder(DEFAULT_BORDER, 1);
    }

    /**
     * Opzionale: Restituisce un colore di sfondo speciale per il Burraco.
     */
    public static Color getBurracoBackground(List<Card> cards) {
        if (isBurraco(cards)) {
            return new Color(240, 255, 240); // Un bianco-verdastro molto tenue
        }
        return Color.WHITE;
    }
}

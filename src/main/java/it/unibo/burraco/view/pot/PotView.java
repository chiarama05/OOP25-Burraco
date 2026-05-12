package it.unibo.burraco.view.pot;

import it.unibo.burraco.model.card.Card;
import java.util.List;

public interface PotView {
    void markPotTaken(boolean isP1);
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
}

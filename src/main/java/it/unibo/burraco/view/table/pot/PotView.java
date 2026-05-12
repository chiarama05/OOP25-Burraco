package it.unibo.burraco.view.table.pot;

import java.util.List;

import it.unibo.burraco.model.cards.Card;

public interface PotView {
    void markPotTaken(boolean isP1);
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
}

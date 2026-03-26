package it.unibo.burraco.view.selection;

import it.unibo.burraco.model.card.Card;
import java.util.List;

public interface SelectionView {
    void showSelectionError(String message);
    void addCombinationToPlayerPanel(List<Card> cards, boolean isP1);
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
}
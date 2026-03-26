package it.unibo.burraco.view.combination;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import java.util.List;

public interface PutCombinationView {
    void onCombinationSuccess(List<Card> combo, boolean isP1, Player current);
    void onCombinationTakePot(List<Card> combo, boolean isP1, Player current);
    void onCombinationClose(List<Card> combo, boolean isP1, Player current);
}
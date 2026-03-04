package view.hand;

import model.card.*;
import java.util.List;
import java.util.Set;

public interface hand {

    void refreshHand(List<Card> hand);

    Set<Card> getSelectedCards();

    void clearSelection();

    void setCardSelectionListener(CardSelectionListener listener);

    interface CardSelectionListener {
        void onCardSelected(Card c);
    }
}

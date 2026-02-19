package card;

import java.util.*;

public interface Deck {

    Card draw();

    boolean isEmpty();

    List<Card> getCards();
}

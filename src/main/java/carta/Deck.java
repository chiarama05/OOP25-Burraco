package carta;

import java.util.*;

public interface Deck {

    Card pick();

    boolean isEmpty();

    List<Card> getCards();
}

package card;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class JollyImpl implements Jolly {

    private final Card card;

    public JollyImpl(Card card) {
        this.card = card;
    }

     @Override
    public boolean isPureJolly() {
        return card.getValue().equals("Jolly");
    }

    @Override
    public boolean isJolly(List<Card> context) {
        if (isPureJolly()) return true;
        if (!card.getValue().equals("2")) return false;

        // 2 → acts as a wildcard in straights IF NOT natural
        return !isNaturalTwo(card, context);
    }

    /**
     * Determines if a 2 is "natural" in a straight (A-2-3)
     */
    private boolean isNaturalTwo(Card two, List<Card> straight) {
        if (!two.getValue().equals("2")) return false;

        List<Card> realCards = straight.stream()
                .filter(c -> !c.getValue().equals("2") && !c.getValue().equals("Jolly"))
                .collect(Collectors.toList());

        if (realCards.isEmpty()) return false;

        String suit = realCards.get(0).getSeed();
        if (!two.getSeed().equals(suit)) return false;

        boolean hasAce = realCards.stream().anyMatch(c -> c.getValue().equals("A"));
        boolean hasThree = realCards.stream().anyMatch(c -> c.getValue().equals("3"));

        return hasAce && hasThree;
    }

    public Card getCard() {
        return card;
    }

}

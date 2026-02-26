package core.distributioncard;

import java.util.ArrayList;
import java.util.List;
import model.card.*;
import model.deck.Deck;
import player.Player;

public class DistributionManagerImpl implements DistributionManager{

    private List<Card> discardPile = new ArrayList<>();

    @Override
    public void distributeInitialCards(Player player1, Player player2, Deck deck){

        // Distributes 11 cards to each player
        for (int i = 0; i < 11; i++) {
            player1.addCardHand(deck.draw());
            player2.addCardHand(deck.draw());
        }

        // Prepare the pots
        List<Card> pot1 = new ArrayList<>();
        List<Card> pot2 = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            pot1.add(deck.draw());
            pot2.add(deck.draw());
        }

        player1.addToPot(pot1);
        player2.addToPot(pot2);

        // First card to discard pile
        discardPile.add(deck.draw());
    }

    @Override
    public List<Card> getInitialDiscardPile() {
        return new ArrayList<>(discardPile);
    }

}

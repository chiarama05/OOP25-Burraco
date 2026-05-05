package it.unibo.burraco.model;

import it.unibo.burraco.model.deck.Deck;
import it.unibo.burraco.model.deck.DeckImpl;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.discard.DiscardPileImpl;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.turn.TurnImpl;
import it.unibo.burraco.model.card.Card;

public final class GameModelImpl implements GameModel{

    private final Player p1;
    private final Player p2;
    private final Deck deck;
    private final DiscardPile discardPile;
    private final Turn turn;

    public GameModelImpl(final String name1, final String name2) {
        this.p1 = new PlayerImpl(name1);
        this.p2 = new PlayerImpl(name2);
        this.deck = new DeckImpl();
        this.discardPile = new DiscardPileImpl();
        this.turn = new TurnImpl(p1, p2);
    }

    @Override
    public void initializeGame() {
        this.deck.reset();
        this.discardPile.getCards().clear();
        this.p1.resetForNewRound();
        this.p2.resetForNewRound();
        // Qui andrebbe la logica di distribuzione iniziale
    }

    @Override
    public Card drawFromDeck() {
        final Card c = deck.draw();
        getCurrentPlayer().addCardHand(c);
        return c;
    }

    @Override
    public void drawFromDiscardPile() {
        final Player p = getCurrentPlayer();
        discardPile.getCards().forEach(p::addCardHand);
        discardPile.getCards().clear();
    }

    @Override
    public void discardCard(final Card card) {
        getCurrentPlayer().removeCardHand(card);
        discardPile.add(card);
    }

    @Override
    public void nextTurn() {
        this.turn.nextTurn();
    }

    @Override
    public Player getCurrentPlayer() {
        return turn.getCurrentPlayer();
    }

    @Override
    public Player getPlayer1() {
        return p1;
    }

    @Override
    public Player getPlayer2() { return p2; }
    @Override
    public Deck getCommonDeck() { return deck; }
    @Override
    public DiscardPile getDiscardPile() { return discardPile; }
    @Override
    public Turn getTurn() { return turn; }
    @Override
    public boolean isPlayer1(final Player player) { return p1.equals(player); }
    @Override
    public boolean isGameOver() { return turn.isGameFinished(); }
}

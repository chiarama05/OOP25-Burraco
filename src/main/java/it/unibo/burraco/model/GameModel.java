package it.unibo.burraco.model;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;

/**
 * Facade interface for the game model.
 * It coordinates players, deck, discard pile and turn logic.
 */
public interface GameModel {

    void nextTurn();

    Player getCurrentPlayer();

    Player getPlayer1();

    Player getPlayer2();

    Deck getCommonDeck();

    DiscardPile getDiscardPile();

    Turn getTurn();

    boolean isPlayer1(Player player);

    boolean isGameOver();

    MoveResult validateMove(Move move);

    MoveResult applyMove(Move move);

    Player getWinner();

    boolean hasDrawn();

    void resetForNewRound();
}

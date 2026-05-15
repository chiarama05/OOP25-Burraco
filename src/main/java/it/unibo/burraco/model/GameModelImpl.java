package it.unibo.burraco.model;

import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.cards.Deck;
import it.unibo.burraco.model.cards.DeckImpl;
import it.unibo.burraco.model.cards.DiscardPile;
import it.unibo.burraco.model.cards.DiscardPileImpl;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.player.PlayerImpl;
import it.unibo.burraco.model.rules.ClosureState;
import it.unibo.burraco.model.rules.ClosureValidator;
import it.unibo.burraco.model.rules.CombinationValidator;
import it.unibo.burraco.model.rules.SetHandler;
import it.unibo.burraco.model.rules.StraightUtils;
import it.unibo.burraco.model.turn.Turn;
import it.unibo.burraco.model.turn.TurnImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Concrete implementation of the {@link GameModel} facade.
 * Coordinates the game flow by managing the players, the deck, the discard pile, 
 * and turn transitions. It relies on specialized validators to enforce 
 * Burraco-specific rules and closure conditions.
 */
public final class GameModelImpl implements GameModel {

    private final Player p1;
    private final Player p2;
    private final Deck deck;
    private final DiscardPile discardPile;
    private final Turn turn;
    private final CombinationValidator combinationValidator = new CombinationValidator();
    private final ClosureValidator closureValidator = new ClosureValidator();
    private final StraightUtils straightUtils = new StraightUtils();
    private final SetHandler setHandler = new SetHandler();
    private boolean drawnThisTurn;
    private Player winner;

    private static final int BURRACO_MIN_CARDS = 7;

    /**
     * Initializes a new match with two players and a fresh deck.
     * 
     * @param name1 name of the first player
     * @param name2 name of the second player
     */
    public GameModelImpl(final String name1, final String name2) {
        this.p1 = new PlayerImpl(name1);
        this.p2 = new PlayerImpl(name2);
        this.deck = new DeckImpl();
        this.discardPile = new DiscardPileImpl();
        this.turn = new TurnImpl(p1, p2);
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
    public Player getPlayer2() { 
        return p2; 
    }

    @Override
    public Deck getCommonDeck() { 
        return deck; 
    }

    @Override
    public DiscardPile getDiscardPile() { 
        return discardPile; 
    }

    @Override
    public Turn getTurn() { 
        return turn; 
    }

    @Override
    public boolean isPlayer1(final Player player) { 
        return p1.equals(player); 
    }

    @Override
    public boolean isGameOver() { 
        return turn.isGameFinished(); 
    }

    @Override
    public MoveResult validateMove(final Move move) {
        final Player current = getCurrentPlayer();
        switch (move.getType()) {

            case DRAW_DECK:
            case DRAW_DISCARD:
                if (drawnThisTurn) {
                    return MoveResult.error(MoveResult.Status.ALREADY_DRAWN); 
                }
                return MoveResult.ok();
            case PUT_COMBINATION: {
                if (!drawnThisTurn) {
                    return MoveResult.error(MoveResult.Status.NOT_DRAWN);
                }
                final List<Card> cards = move.getSelectedCards();
                if (cards.isEmpty()) {
                    return MoveResult.error(MoveResult.Status.NO_CARDS_SELECTED);
                }
                if (closureValidator.wouldGetStuckAfterPutCombo(current, cards, cards.size())) {
                    return MoveResult.error(MoveResult.Status.WOULD_GET_STUCK);
                }
                if (!combinationValidator.isValidCombination(cards)) {
                    return MoveResult.error(MoveResult.Status.INVALID_COMBINATION);
                }
                return MoveResult.ok();
            }
            case ATTACH: {
                if (!drawnThisTurn) {
                    return MoveResult.error(MoveResult.Status.NOT_DRAWN);
                }
                final List<Card> sel = move.getSelectedCards();
                final List<Card> target = move.getTargetCombination();
                if (sel.isEmpty()) {
                    return MoveResult.error(MoveResult.Status.NO_CARDS_SELECTED);
                }
                if (!current.ownsCombination(target)) {
                    return MoveResult.error(MoveResult.Status.WRONG_PLAYER);
                }
                if (closureValidator.wouldGetStuckAfterAttach(current, sel, target.size())) {
                    return MoveResult.error(MoveResult.Status.WOULD_GET_STUCK);
                }
                final List<Card> hypo = new ArrayList<>(target);
                hypo.addAll(sel);
                if (!combinationValidator.isValidCombination(hypo)) {
                    return MoveResult.error(MoveResult.Status.INVALID_COMBINATION);
                }
                return MoveResult.ok();
            }
            case DISCARD: {
                if (!drawnThisTurn) {
                    return MoveResult.error(MoveResult.Status.NOT_DRAWN);
                }
                if (move.getSelectedCards().size() != 1) {
                    return MoveResult.error(MoveResult.Status.NO_CARDS_SELECTED);
                }
                return MoveResult.ok();
            }
            default:
                return MoveResult.error(MoveResult.Status.INVALID_MOVE);
        }
    }

    @Override
    public MoveResult applyMove(final Move move) {
        final Player current = getCurrentPlayer();
        final boolean isP1 = isPlayer1(current);

        switch (move.getType()) {
            case DRAW_DECK: {
                if (deck.isEmpty()) {
                    turn.setGameFinished(true);
                    return MoveResult.success(
                    MoveResult.Status.DECK_EMPTY,
                    Collections.emptyList(),
                    isP1);
                }
                final Card c = deck.draw();
                current.addCardHand(c);
                drawnThisTurn = true;
                return MoveResult.ok();
            }
            case DRAW_DISCARD: {
                discardPile.getCards().forEach(current::addCardHand);
                discardPile.reset();
                drawnThisTurn = true;
                return MoveResult.ok();
            }
            case PUT_COMBINATION: {
                List<Card> processed = new ArrayList<>(move.getSelectedCards());
                if (straightUtils.isSameSeed(processed) && !setHandler.isValid(processed)) {
                    processed = straightUtils.orderStraight(processed);
                }
                current.addCombination(processed);
                current.removeCards(move.getSelectedCards());
                return evaluateClosureState(current, processed, isP1, 0); 
            }
            case ATTACH: {
                final List<Card> sel = move.getSelectedCards();
                final int previousSize = move.getTargetCombination().size();
                final List<Card> updatedTarget = new ArrayList<>(move.getTargetCombination());
                updatedTarget.addAll(sel);
                current.updateCombination(move.getTargetCombination(), updatedTarget);
                current.removeCards(sel);
                return evaluateClosureState(current, updatedTarget, isP1, previousSize);
            }
            case DISCARD: {
                final Card card = move.getSelectedCards().get(0);
                current.removeCardHand(card);
                discardPile.add(card);

                if (current.getHand().isEmpty() && !current.isInPot()) {
                    current.drawPot();
                    return MoveResult.success(MoveResult.Status.SUCCESS_TAKE_POT, Collections.emptyList(), isP1);
                }

                if (closureValidator.evaluateAfterDiscard(current) == ClosureState.ROUND_WON) {
                    this.winner = current;
                    turn.setGameFinished(true);
                    return MoveResult.success(MoveResult.Status.ROUND_WON, Collections.emptyList(), isP1);
                }
                return MoveResult.ok();
            }
            default:
                return MoveResult.error(MoveResult.Status.INVALID_MOVE);
        }
    }

    /**
     * Internal helper to evaluate the player's state after a card-placing action.
     * Handles side pot acquisition, "stuck" hand detection, and burraco creation.
     * 
     * @param p            the player who moved
     * @param combo        the combination affected by the move
     * @param isP1         true if the player is player 1
     * @param previousSize the size of the combination before the move
     * @return a specific MoveResult representing the new game state
     */
    private MoveResult evaluateClosureState(final Player p,
                                            final List<Card> combo,
                                            final boolean isP1,
                                            final int previousSize) {
        final ClosureState state = closureValidator.evaluate(p);
        if (state == ClosureState.ZERO_CARDS_NO_POT) {
            p.drawPot();
            return MoveResult.success(MoveResult.Status.SUCCESS_TAKE_POT, combo, isP1);
        }
        if (state == ClosureState.CAN_CLOSE) {
            this.winner = p;
            turn.setGameFinished(true);
            return MoveResult.success(MoveResult.Status.SUCCESS_CLOSE, combo, isP1);
        }
        if (state == ClosureState.ZERO_CARDS_NO_BURRACO) {
            return MoveResult.success(MoveResult.Status.SUCCESS_STUCK, combo, isP1);
        }
        final boolean newBurraco = previousSize < BURRACO_MIN_CARDS && combo.size() >= BURRACO_MIN_CARDS;
        return MoveResult.success(newBurraco ? MoveResult.Status.SUCCESS_BURRACO
                : MoveResult.Status.SUCCESS, combo, isP1);
    }

    @Override
    public void nextTurn() {
        turn.nextTurn();
        drawnThisTurn = false;
    }

    @Override
    public boolean hasDrawn() {
        return drawnThisTurn;
    }

    @Override
    public Player getWinner() {
        return winner; 
    }

    @Override
    public void resetForNewRound() {
        this.turn.resetForNewRound();
        this.drawnThisTurn = false;
        this.winner = null;
    }
}

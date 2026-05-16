package it.unibo.burraco.model.rules;
 
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;
import it.unibo.burraco.model.player.Player;
 
import java.util.ArrayList;
import java.util.List;

public final class MoveValidator {
 
    private final CombinationValidator combinationValidator;
    private final ClosureValidator closureValidator;
 
    /**
     * Constructs a MoveValidator with the provided rule components.
     *
     * @param combinationValidator validates card combinations (sets and straights)
     * @param closureValidator     checks closure and "stuck-hand" conditions
     */
    public MoveValidator(final CombinationValidator combinationValidator,
                         final ClosureValidator closureValidator) {
        this.combinationValidator = combinationValidator;
        this.closureValidator = closureValidator;
    }
 
    /**
     * Validates a move against the current game state without mutating anything.
     *
     * @param move    the move the player is attempting
     * @param current the player whose turn it currently is
     * @param drawn   true if the current player has already drawn a card this turn
     * @return a {@link MoveResult} that is valid if the move is legal,
     *         or carries the specific error status otherwise
     */
    public MoveResult validate(final Move move,
                               final Player current,
                               final boolean drawn) {
        return switch (move.getType()) {
            case DRAW_DECK, DRAW_DISCARD -> validateDraw(drawn);
            case PUT_COMBINATION         -> validatePutCombination(move, current, drawn);
            case ATTACH                  -> validateAttach(move, current, drawn);
            case DISCARD                 -> validateDiscard(move, drawn);
            default                      -> MoveResult.error(MoveResult.Status.INVALID_MOVE);
        };
    }

    private MoveResult validateDraw(final boolean drawn) {
        if (drawn) {
            return MoveResult.error(MoveResult.Status.ALREADY_DRAWN);
        }
        return MoveResult.ok();
    }
 
    private MoveResult validatePutCombination(final Move move,
                                              final Player current,
                                              final boolean drawn) {
        if (!drawn) {
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
 
    private MoveResult validateAttach(final Move move,
                                      final Player current,
                                      final boolean drawn) {
        if (!drawn) {
            return MoveResult.error(MoveResult.Status.NOT_DRAWN);
        }
        final List<Card> selected = move.getSelectedCards();
        final List<Card> target   = move.getTargetCombination();
        if (selected.isEmpty()) {
            return MoveResult.error(MoveResult.Status.NO_CARDS_SELECTED);
        }
        if (!current.ownsCombination(target)) {
            return MoveResult.error(MoveResult.Status.WRONG_PLAYER);
        }
        if (closureValidator.wouldGetStuckAfterAttach(current, selected, target.size())) {
            return MoveResult.error(MoveResult.Status.WOULD_GET_STUCK);
        }
        final List<Card> hypothetical = new ArrayList<>(target);
        hypothetical.addAll(selected);
        if (!combinationValidator.isValidCombination(hypothetical)) {
            return MoveResult.error(MoveResult.Status.INVALID_COMBINATION);
        }
        return MoveResult.ok();
    }
 
    private MoveResult validateDiscard(final Move move, final boolean drawn) {
        if (!drawn) {
            return MoveResult.error(MoveResult.Status.NOT_DRAWN);
        }
        if (move.getSelectedCards().size() != 1) {
            return MoveResult.error(MoveResult.Status.NO_CARDS_SELECTED);
        }
        return MoveResult.ok();
    }
}
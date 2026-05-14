package it.unibo.burraco.view.scenes;
 
import it.unibo.burraco.controller.score.ScoreSnapshot;
import it.unibo.burraco.view.table.TableView;
 
/**
 * Factory for creating the end-of-round score view.
 *
 * <p>Accepts only display-ready {@link ScoreSnapshot} objects —
 * no {@code Player} or {@code Score} model references cross into the View layer.
 */
@FunctionalInterface
public interface ScoreViewProvider {
 
    /**
     * Creates a new {@link ScoreView} for the given round/match result.
     *
     * @param snap1      display data for Player 1
     * @param snap2      display data for Player 2
     * @param target     score threshold required to win the match
     * @param tableView  reference to the main table frame (for positioning)
     * @param matchOver  true if the match has concluded
     * @return a ready-to-display {@link ScoreView}
     */
    ScoreView create(ScoreSnapshot snap1, ScoreSnapshot snap2,
                     int target, TableView tableView, boolean matchOver);
}
 
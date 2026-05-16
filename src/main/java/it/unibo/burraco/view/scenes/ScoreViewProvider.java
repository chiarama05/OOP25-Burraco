package it.unibo.burraco.view.scenes;

import it.unibo.burraco.controller.score.ScoreSnapshot;
import it.unibo.burraco.view.table.SwingTableAccess;

/**
 * Factory interface for dynamically creating instances of {@link ScoreView}.
 * Follows the Provider pattern to decouple the instantiation logic of the 
 * scoreboard view from the controller components.
 */
@FunctionalInterface
public interface ScoreViewProvider {

    /**
     * Creates a new instance of ScoreView populated with the given match data.
     *
     * @param snap1       display data for Player 1
     * @param snap2       display data for Player 2
     * @param target      score threshold required to win the match
     * @param swingAccess reference to the main table frame utilities
     * @param matchOver   true if the match has concluded
     * @return a new ScoreView instance
     */
    ScoreView create(ScoreSnapshot snap1, ScoreSnapshot snap2,
                     int target, SwingTableAccess swingAccess, boolean matchOver);
}

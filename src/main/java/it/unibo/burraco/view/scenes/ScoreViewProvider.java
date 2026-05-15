package it.unibo.burraco.view.scenes;

import it.unibo.burraco.controller.score.ScoreSnapshot;
import it.unibo.burraco.view.table.SwingTableAccess;

@FunctionalInterface
public interface ScoreViewProvider {
    ScoreView create(ScoreSnapshot snap1, ScoreSnapshot snap2,
                     int target, SwingTableAccess swingAccess, boolean matchOver);
}
 
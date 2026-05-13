package it.unibo.burraco.view.scenes;

import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.table.TableView;

@FunctionalInterface
public interface ScoreViewProvider {
    ScoreView create(Player p1, Player p2, String n1, String n2,
                     int target, Score s, TableView tv, boolean over);
}
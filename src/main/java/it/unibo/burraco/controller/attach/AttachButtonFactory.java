package it.unibo.burraco.controller.attach;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.attach.AttachButton;
import it.unibo.burraco.view.table.TableView;

import javax.swing.JFrame;
import java.util.List;

public class AttachButtonFactory {

    private final TableView tableView;
    private final GameController gameController;
    private final ClosureManager closureManager;
    private final PotManager potManager;
    private final JFrame frame;

    public AttachButtonFactory(TableView tableView, GameController gameController,
                                ClosureManager closureManager, PotManager potManager,
                                JFrame frame) {
        this.tableView = tableView;
        this.gameController = gameController;
        this.closureManager = closureManager;
        this.potManager = potManager;
        this.frame = frame;
    }

    public AttachButton create(List<Card> cards, boolean isP1) {
        return new AttachButton(cards, tableView, gameController, isP1, closureManager, potManager, frame);
    }
}
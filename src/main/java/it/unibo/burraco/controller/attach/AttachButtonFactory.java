package it.unibo.burraco.controller.attach;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.attach.AttachButton;
import it.unibo.burraco.view.notification.attach.AttachNotifier;
import it.unibo.burraco.view.notification.attach.AttachNotifierImpl;
import it.unibo.burraco.view.table.TableView;
import javax.swing.JFrame;
import java.util.List;

/**
 * Factory responsible for creating fully wired {@link AttachButton} instances.
 * Each button is connected to its own {@link AttachActionController} and {@link AttachNotifier}.
 */
public class AttachButtonFactory {

    private final TableView tableView;
    private final GameController gameController;
    private final ClosureManager closureManager;
    private final PotManager potManager;
    private final JFrame frame;

    /**
     * Constructs the factory with all shared dependencies.
     */
    public AttachButtonFactory(TableView tableView, GameController gameController,
                                ClosureManager closureManager, PotManager potManager,
                                JFrame frame) {
        this.tableView = tableView;
        this.gameController = gameController;
        this.closureManager = closureManager;
        this.potManager = potManager;
        this.frame = frame;
    }

    /**
     * Creates a new {@link AttachButton} for the given combination,
     * wired with its own {@link AttachActionController} and {@link AttachNotifier}.
     *
     * @param cards     the cards belonging to the combination
     * @param isPlayer1 true if the combination belongs to Player 1
     * @return a fully configured {@link AttachButton} ready to be added to the UI
     */
    public AttachButton create(List<Card> cards, boolean isPlayer1) {
        AttachButton btn = new AttachButton(cards, tableView, isPlayer1); 

        AttachNotifier notifier = new AttachNotifierImpl(frame);
        AttachActionController ctrl = new AttachActionController(
                gameController, potManager, closureManager, notifier, isPlayer1);

        btn.setOnAttachAction((selected, self) -> ctrl.handle(selected, self.getCards(), self));
        return btn;
    }
}
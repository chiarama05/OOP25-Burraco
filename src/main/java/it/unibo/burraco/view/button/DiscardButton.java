package it.unibo.burraco.view.button;

import it.unibo.burraco.controller.buttonLogic.DiscardController;
import it.unibo.burraco.controller.discardcard.DiscardResult;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.discard.DiscardViewImpl;
import it.unibo.burraco.view.hand.HandImpl;
import it.unibo.burraco.view.notification.GameNotifier;
import it.unibo.burraco.view.table.TableView;
import java.util.Set;

public class DiscardButton {
    private final TableView view;
    private final DiscardViewImpl discardView;
    private final DrawManager drawManager;
    private final it.unibo.burraco.model.turn.Turn turnModel;
    private final GameNotifier notifier;
    private final DiscardController coreController;

    public DiscardButton(TableView view, 
                         it.unibo.burraco.model.turn.Turn turnModel, 
                         it.unibo.burraco.controller.drawcard.DrawManager drawManager, 
                         it.unibo.burraco.view.discard.DiscardViewImpl discardView, 
                         it.unibo.burraco.view.notification.GameNotifier notifier, 
                         DiscardController coreController) {
        this.view = view;
        this.turnModel = turnModel;
        this.drawManager = drawManager;
        this.discardView = discardView;
        this.notifier = notifier;
        this.coreController = coreController;

    
        this.discardView.setDiscardListener(e -> handleDiscardClick());
    }

    private void handleDiscardClick() {
        if (!drawManager.hasDrawn()) {
            notifier.notifyMustDraw();
            return;
        }

        Player current = turnModel.getCurrentPlayer();
        HandImpl handView = view.getHandViewForPlayer(current);
        Set<Card> selected = handView.getSelectedCards();

        if (selected.size() != 1) {
            notifier.notifySelectionError("Select only one card!");
            return;
        }

        Card cardToDiscard = selected.iterator().next();

        DiscardResult result = coreController.executeDiscard(current, cardToDiscard);

        if (result.isValid()) {
            handView.refreshHand(current.getHand());
            handView.clearSelection();
            discardView.updateDiscardPile(view.getGameController().getDiscardPile().getCards());
        } else {
            notifier.notifySelectionError(result.getMessage());
        }
    }
}

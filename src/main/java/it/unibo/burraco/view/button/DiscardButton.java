package it.unibo.burraco.view.button;

import it.unibo.burraco.controller.buttonLogic.DiscardController;
import it.unibo.burraco.controller.discardcard.DiscardResult;
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
    private final GameNotifier notifier;
    private final DiscardController discardController;

    public DiscardButton(TableView view,
                         DiscardViewImpl discardView,
                         GameNotifier notifier,
                         DiscardController discardController) {
        this.view = view;
        this.discardView = discardView;
        this.notifier = notifier;
        this.discardController = discardController;

        this.discardView.setDiscardListener(e -> handleDiscardClick());
    }

    private void handleDiscardClick() {
        
        Player current = view.getGameController().getCurrentPlayer();
        HandImpl handView = view.getHandViewForPlayer(current);
        Set<Card> selected = handView.getSelectedCards();

        DiscardResult result = discardController.tryDiscard(selected);

        if (result.isValid()) {
            
            handView.refreshHand(result.getCurrentPlayer().getHand());
            handView.clearSelection();
            discardView.updateDiscardPile(result.getUpdatedDiscardPile());
        } else {
            switch (result.getMessage()) {
                case "must_draw":
                    notifier.notifyMustDraw();
                    break;
                case "select_one":
                    notifier.notifySelectionError("Select only one card!");
                    break;
                default:
                    notifier.notifySelectionError(result.getMessage());
                    break;
            }
        }
    }
}


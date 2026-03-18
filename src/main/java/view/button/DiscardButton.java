package view.button;

import model.card.Card;
import model.player.Player;
import core.drawcard.DrawManager;
import core.buttonLogic.DiscardController;
import core.discardcard.DiscardResult;
import view.discard.DiscardViewImpl;
import view.hand.handImpl;
import view.notification.GameNotifier;
import view.table.TableView;
import java.util.Set;

public class DiscardButton {
    private final TableView view;
    private final DiscardViewImpl discardView;
    private final DrawManager drawManager;
    private final model.turn.Turn turnModel;
    private final GameNotifier notifier;
    private final DiscardController coreController;

    public DiscardButton(TableView view, 
                         model.turn.Turn turnModel, 
                         core.drawcard.DrawManager drawManager, 
                         view.discard.DiscardViewImpl discardView, 
                         view.notification.GameNotifier notifier, 
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
        handImpl handView = view.getHandViewForPlayer(current);
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

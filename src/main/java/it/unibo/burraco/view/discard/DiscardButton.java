package it.unibo.burraco.view.discard;

import it.unibo.burraco.controller.discardcard.DiscardActionController;
import it.unibo.burraco.controller.discardcard.DiscardController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.game.GameNotifier;
import it.unibo.burraco.view.table.TableView;

import java.util.List;
import java.util.Set;

public class DiscardButton implements DiscardActionView {

    private final TableView view;
    private final DiscardViewImpl discardView;
    private final GameNotifier notifier;
    private final DiscardActionController actionController;
    private boolean isPlayer1;

    public DiscardButton(TableView view,
                         DiscardViewImpl discardView,
                         GameNotifier notifier,
                         DiscardController discardController) {
        this.view = view;
        this.discardView = discardView;
        this.notifier = notifier;
        this.actionController = new DiscardActionController(discardController);

        this.discardView.setDiscardListener(e -> actionController.handle(this, isPlayer1));
    }

    public void setIsPlayer1(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    @Override
    public Set<Card> getSelectedCards(boolean isPlayer1) {
        return view.getHandViewForCurrentPlayer(isPlayer1).getSelectedCards();
    }

    @Override
    public void onDiscardSuccess(Player player, List<Card> updatedPile, boolean isPlayer1) {
        view.getHandViewForCurrentPlayer(isPlayer1).clearSelection();
        discardView.updateDiscardPile(updatedPile);
    }

    @Override
    public void onDiscardError(String errorCode) {
        switch (errorCode) {
            case "must_draw" -> notifier.notifyMustDraw();
            case "select_one" -> notifier.notifySelectionError("Select only one card!");
            default -> notifier.notifySelectionError(errorCode);
        }
    }
}
package it.unibo.burraco.view.discard;

import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.notification.game.GameNotifier;
import it.unibo.burraco.view.table.TableView;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class DiscardButton implements DiscardActionView {

    private final TableView view;
    private final DiscardViewImpl discardView;
    private final GameNotifier notifier;
    private boolean isPlayer1;

    private BiConsumer<DiscardButton, Boolean> onDiscardAction;

    public DiscardButton(TableView view, DiscardViewImpl discardView, GameNotifier notifier) {
        this.view = view;
        this.discardView = discardView;
        this.notifier = notifier;

        this.discardView.setDiscardListener(e -> {
            if (onDiscardAction != null) onDiscardAction.accept(this, isPlayer1);
        });
    }

    public void setIsPlayer1(boolean isPlayer1) {
        this.isPlayer1 = isPlayer1;
    }

    public void setOnDiscardAction(BiConsumer<DiscardButton, Boolean> handler) {
        this.onDiscardAction = handler;
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
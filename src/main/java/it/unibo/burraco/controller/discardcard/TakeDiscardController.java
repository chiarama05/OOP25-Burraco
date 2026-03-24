package it.unibo.burraco.controller.discardcard;

import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.drawcard.DrawResult;
import it.unibo.burraco.model.discard.DiscardPile;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.turn.Turn;

public class TakeDiscardController {

    private final DrawManager drawManager;
    private final Turn turnModel;
    private final DiscardPile discardPile;

    public TakeDiscardController(DrawManager drawManager, Turn turnModel, DiscardPile discardPile) {
        this.drawManager = drawManager;
        this.turnModel = turnModel;
        this.discardPile = discardPile;
    }

    public DrawResult tryTakeDiscard() {
        Player current = turnModel.getCurrentPlayer();
        return drawManager.drawFromDiscard(current, discardPile.getCards());
    }
}

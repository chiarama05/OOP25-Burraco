package view.button;

import model.card.Card;
import model.player.Player;
import core.discardcard.DiscardManagerImpl;
import core.discardcard.DiscardResult;
import core.drawcard.DrawManager;   
import core.pot.PotManager;     
import core.closure.ClosureManager; 
import view.discard.DiscardViewImpl;
import view.hand.handImpl;
import view.notification.GameNotifier;
import view.table.TableView; 
import model.discard.DiscardPile;
import java.util.Set;

public class DiscardController {

    private final TableView view;
    private final DiscardManagerImpl discardManager;
    private final DiscardViewImpl discardView;
    private final DiscardPile discardPileModel;
    private final DrawManager drawManager;
    
    private final model.turn.Turn turnModel; 
    private final core.turn.TurnController turnCtrl;
    private final PotManager potCtrl;
    private final ClosureManager closureCtrl;
    private final GameNotifier notifier;

    public DiscardController(TableView view, model.turn.Turn turnModel,DiscardManagerImpl discardManager, DiscardViewImpl discardView, DiscardPile discardPileModel, DrawManager drawManager,core.turn.TurnController turnCtrl, PotManager potCtrl, ClosureManager closureCtrl, GameNotifier notifier) {
        this.view = view;
        this.turnModel = turnModel;
        this.discardManager = discardManager;
        this.discardView = discardView;
        this.discardPileModel = discardPileModel;
        this.drawManager = drawManager;
        this.turnCtrl = turnCtrl;
        this.potCtrl = potCtrl;
        this.closureCtrl = closureCtrl;
        this.notifier = notifier;

        this.discardView.setDiscardListener(e -> handleDiscard());
    }

    private void handleDiscard() {

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

        boolean willTakePot = (current.getHand().size() == 1 && !current.isInPot());
        DiscardResult result = discardManager.discard(current, cardToDiscard);

        if (!result.isValid()) {
        notifier.notifySelectionError(result.getMessage());
        return;
        }

        handView.refreshHand(current.getHand());
        handView.clearSelection();
        discardView.updateDiscardPile(discardPileModel.getCards());

       if (result.isGameWon()) {
       closureCtrl.attemptClosure(); 
       } else {
       if (willTakePot) {

        potCtrl.handlePot(true); 
        }
       
        turnCtrl.executeNextTurn();
        }
    }
}


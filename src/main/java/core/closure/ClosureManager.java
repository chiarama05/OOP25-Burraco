package core.closure;

import model.player.Player;
import model.turn.Turn;
import view.table.TableView;
import view.table.TableViewImpl;
import view.notification.GameNotifier;
import view.score.ScoreViewImpl;

public class ClosureManager {
private final Turn model;
    private final TableView view;
    private final GameNotifier notifier;
    private final int targetScore;

    public ClosureManager(Turn model, TableView view, GameNotifier notifier,int targetScore) {
        this.model = model;
        this.view = view;
        this.notifier = notifier;
        this.targetScore = targetScore;
    }

    public void attemptClosure() {
        Player current = model.getCurrentPlayer();

        if (model.canClose() && current.getHand().isEmpty()) {
            model.setGameFinished(true);
            
            if (view instanceof TableViewImpl tvImpl) {
                ScoreViewImpl scorePanel = new ScoreViewImpl(
                    model.getPlayer1(), 
                    model.getPlayer2(), 
                    model.getPlayer1().getName(), 
                    model.getPlayer2().getName(), 
                    targetScore, 
                    tvImpl, 
                    tvImpl.getGameController()
                );
                scorePanel.display(); 
            }
            
        } else {
            notifier.notifyInvalidClosure(); 
        }
    }
}


package it.unibo.burraco.controller.round;
 
import it.unibo.burraco.controller.score.ScoreSnapshot;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.components.sound.SoundView;
import it.unibo.burraco.view.scenes.ScoreView;
import it.unibo.burraco.view.scenes.ScoreViewProvider;
import it.unibo.burraco.view.table.TableView;
 
import javax.swing.SwingUtilities;
import java.util.ArrayList;
 
/**
 * Handles the end-of-round sequence: sound playback, snapshot construction,
 * and score display.
 *
 * <p><b>MVC contract:</b> this class holds references to {@code Player} and
 * {@code Score} (model layer) because it is a Controller component. Its sole
 * responsibility is to transform model data into {@link ScoreSnapshot} DTOs
 * and hand those — never the raw model objects — to the View.
 */
public final class RoundEndHandler {
 
    private final Score score;
    private final Player player1;
    private final Player player2;
    private final String nameP1;
    private final String nameP2;
    private final TableView tableView;
    private final SoundView soundView;
    private final int targetScore;
    private final ScoreViewProvider viewProvider;
    private Runnable onNewRound;
 
    public RoundEndHandler(
            final Score score,
            final Player player1,
            final Player player2,
            final String nameP1,
            final String nameP2,
            final TableView tableView,
            final SoundView soundView,
            final int targetScore,
            final ScoreViewProvider viewProvider) {
        this.score        = score;
        this.player1      = player1;
        this.player2      = player2;
        this.nameP1       = nameP1;
        this.nameP2       = nameP2;
        this.tableView    = tableView;
        this.soundView    = soundView;
        this.targetScore  = targetScore;
        this.viewProvider = viewProvider;
    }
 
    public void setOnNewRound(final Runnable action) {
        this.onNewRound = action;
    }
 
    /**
     * Executes the full end-of-round sequence.
     * Builds {@link ScoreSnapshot} objects here so the View never touches
     * the model directly.
     */
    public void handle() {
        final boolean matchOver =
                player1.getMatchTotalScore() >= targetScore
                || player2.getMatchTotalScore() >= targetScore;
 
        final boolean p1Wins = matchOver
                && player1.getMatchTotalScore() > player2.getMatchTotalScore();
        final boolean p2Wins = matchOver
                && player2.getMatchTotalScore() > player1.getMatchTotalScore();
 
        final ScoreSnapshot snap1 = buildSnapshot(player1, nameP1, p1Wins);
        final ScoreSnapshot snap2 = buildSnapshot(player2, nameP2, p2Wins);
 
        if (matchOver) {
            // Play victory sound on a dedicated thread, then show score on EDT
            new Thread(() -> {
                soundView.playVictorySound();
                SwingUtilities.invokeLater(() -> showScoreView(snap1, snap2, true));
            }).start();
        } else {
            // Round-end sound is already played by GameLoopController on ROUND_WON;
            // here we only open the score view.
            SwingUtilities.invokeLater(() -> showScoreView(snap1, snap2, false));
        }
    }
    
    /**
     * Transforms model data into a display-ready {@link ScoreSnapshot}.
     * All calculations happen here, in the Controller layer.
     */
    private ScoreSnapshot buildSnapshot(final Player p,
                                        final String name,
                                        final boolean isWinner) {
        return new ScoreSnapshot(
            name,
            score.calculateOnlyCardsOnTable(p),
            score.countCleanBurraco(p) * score.getCleanBurracoBonusValue(),
            score.countDirtyBurraco(p) * score.getDirtyBurracoBonusValue(),
            p.hasFinishedCards() ? score.getClosureBonusValue() : 0,
            p.isInPot() ? 0 : score.getNoPotPenalty(),
            score.calculateRemainingHandValue(p),
            score.calculateFinalScore(p),
            p.getMatchTotalScore(),
            isWinner,
            new ArrayList<>(p.getHand())
        );
    }
 
    /**
     * Shows the final hands via a single encapsulated TableView call,
     * then creates and displays the score window.
     */
    private void showScoreView(final ScoreSnapshot snap1,
                               final ScoreSnapshot snap2,
                               final boolean matchOver) {
        // Single call — TableViewImpl manages its own sub-components internally.
        tableView.showFinalHands(snap1.finalHand(), snap2.finalHand());
 
        final ScoreView view = viewProvider.create(snap1, snap2, targetScore, tableView, matchOver);
        view.setOnNextAction(() -> {
            view.close();
            if (onNewRound != null) {
                onNewRound.run();
            }
        });
        view.display();
    }
}
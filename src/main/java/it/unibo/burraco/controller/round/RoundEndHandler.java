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
 * Controller responsible for orchestrating the end-of-round sequence.
 * It manages the transition from the active game table to the score display, 
 * handles victory sound logic, and ensures that the View receives data 
 * only through immutable DTOs ({@link ScoreSnapshot}).
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
 
    /**
     * Constructs a RoundEndHandler with the necessary model and view components.
     * 
     * @param score         the scoring logic component
     * @param player1       the first player model
     * @param player2       the second player model
     * @param nameP1        display name for player 1
     * @param nameP2        display name for player 2
     * @param tableView     the main game table view
     * @param soundView     the sound manager for audio feedback
     * @param targetScore   the score threshold to win the match
     * @param viewProvider  factory for creating the score display scene
     */
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
 
    /**
     * Configures the action to be performed when a player chooses to 
     * proceed to a new round from the score view.
     * 
     * @param action a {@link Runnable} representing the next game phase logic
     */
    public void setOnNewRound(final Runnable action) {
        this.onNewRound = action;
    }
 
    /**
     * Executes the end-of-round sequence.
     * Calculates final scores, determines if the match has ended, 
     * triggers audio feedback, and initiates the transition to the score view.
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
            new Thread(() -> {
                soundView.playVictorySound();
                SwingUtilities.invokeLater(() -> showScoreView(snap1, snap2, true));
            }).start();
        } else {
            SwingUtilities.invokeLater(() -> showScoreView(snap1, snap2, false));
        }
    }

    /**
     * Maps the current state of a {@link Player} and the {@link Score} rules 
     * into a read-only {@link ScoreSnapshot}.
     * 
     * @param p        the player to process
     * @param name     the player's name
     * @param isWinner true if this player reached the target score and won the match
     * @return a DTO containing all necessary data for the score view
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
     * Updates the table view with final hands and displays the score scene.
     * Synchronizes with the Event Dispatch Thread for UI safety.
     * 
     * @param snap1     snapshot of player 1
     * @param snap2     snapshot of player 2
     * @param matchOver true if the entire match has concluded
     */
    private void showScoreView(final ScoreSnapshot snap1,
                               final ScoreSnapshot snap2,
                               final boolean matchOver) {
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

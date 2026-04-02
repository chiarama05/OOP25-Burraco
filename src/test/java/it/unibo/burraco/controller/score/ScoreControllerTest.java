package it.unibo.burraco.controller.score;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.score.ScoreView;
import it.unibo.burraco.view.table.TableView;

class ScoreControllerTest {

    private static final int TARGET_SCORE = 2005;
    private static final int WINNING_SCORE = 2010;
    private static final int POINTS_TO_WIN = 500;
    private static final int TIMEOUT_MS = 2000;

    private ScoreController scoreController;
    private Score score;
    private Player p1;
    private Player p2;
    private SoundController sound;
    private ScoreView scoreView;

    @BeforeEach
    void setUp() {
        this.score = mock(Score.class);
        this.p1 = mock(Player.class);
        this.p2 = mock(Player.class);
        this.sound = mock(SoundController.class);
        this.scoreView = mock(ScoreView.class);
        final ScoreController.ViewProvider viewProvider = mock(ScoreController.ViewProvider.class);
        final GameController gameController = mock(GameController.class);

        when(viewProvider.create(any(), any(), any(), any(), anyInt(), any(), any(), anyBoolean()))
            .thenReturn(this.scoreView);

        this.scoreController = new ScoreController(
            this.score, this.p1, this.p2, "P1", "P2",
            mock(TableView.class), gameController, this.sound,
            TARGET_SCORE, mock(InitialDistributionView.class),
            Runnable::run,
            viewProvider
        );
    }

    @Test
    void testRoundEndWithoutVictory() {
        final int pointsEarned = 100;
        when(this.score.calculateFinalScore(this.p1)).thenReturn(pointsEarned);
        when(this.p1.getMatchTotalScore()).thenReturn(pointsEarned);
        when(this.p2.getMatchTotalScore()).thenReturn(pointsEarned);

        this.scoreController.onRoundEnd();

        verify(this.p1).addPointsToMatch(pointsEarned);
        verify(this.sound).playRoundEndSound();
        verify(this.scoreView).display();
        verify(this.sound, never()).playVictorySound();
    }

   @Test
    void testRoundEndWithVictory() {
        when(this.score.calculateFinalScore(this.p1)).thenReturn(POINTS_TO_WIN);
        when(this.score.calculateFinalScore(this.p2)).thenReturn(0);
        when(this.p1.getMatchTotalScore()).thenReturn(WINNING_SCORE); 
        when(this.p2.getMatchTotalScore()).thenReturn(0);
        this.scoreController.onRoundEnd();
        verify(this.p1).addPointsToMatch(POINTS_TO_WIN);
        verify(this.sound, timeout(TIMEOUT_MS)).playVictorySound();
        verify(this.scoreView, timeout(TIMEOUT_MS)).display();
    }
}

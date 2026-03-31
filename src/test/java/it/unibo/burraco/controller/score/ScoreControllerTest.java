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

    private ScoreController scoreController;
    private Score score;
    private Player p1;
    private Player p2;
    private SoundController sound;
    private ScoreView scoreView;
    private ScoreController.ViewProvider viewProvider;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        this.score = mock(Score.class);
        this.p1 = mock(Player.class);
        this.p2 = mock(Player.class);
        this.sound = mock(SoundController.class);
        this.scoreView = mock(ScoreView.class);
        this.viewProvider = mock(ScoreController.ViewProvider.class);
        this.gameController = mock(GameController.class);

        when(this.viewProvider.create(any(), any(), any(), any(), anyInt(), any(), any(), anyBoolean()))
            .thenReturn(this.scoreView);

        this.scoreController = new ScoreController(
            this.score, this.p1, this.p2, "P1", "P2",
            mock(TableView.class), this.gameController, this.sound,
            2000, mock(InitialDistributionView.class),
            Runnable::run,
            this.viewProvider
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
        when(this.score.calculateFinalScore(this.p1)).thenReturn(500);
        when(this.p1.getMatchTotalScore()).thenReturn(2005); 

        this.scoreController.onRoundEnd();
        verify(this.sound, timeout(500)).playVictorySound();
        verify(this.scoreView, timeout(500)).display();
    }
}

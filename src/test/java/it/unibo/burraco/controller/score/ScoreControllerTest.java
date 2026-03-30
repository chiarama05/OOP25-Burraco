package it.unibo.burraco.controller.score;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;
import it.unibo.burraco.view.table.TableView;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.score.ScoreView;

class ScoreControllerTest {

    private ScoreController scoreController;
    private Score scoreMock;
    private Player p1Mock;
    private Player p2Mock;
    private SoundController soundMock;
    private ScoreView scoreViewMock;
    private ScoreController.ViewProvider viewProviderMock;
    private GameController gameControllerMock;

    @BeforeEach
    void setUp() {
        scoreMock = mock(Score.class);
        p1Mock = mock(Player.class);
        p2Mock = mock(Player.class);
        soundMock = mock(SoundController.class);
        scoreViewMock = mock(ScoreView.class);
        viewProviderMock = mock(ScoreController.ViewProvider.class);
        gameControllerMock = mock(GameController.class);
        
        when(viewProviderMock.create(any(), any(), any(), any(), anyInt(), any(), any(), anyBoolean()))
            .thenReturn(scoreViewMock);

        scoreController = new ScoreController(
            scoreMock, p1Mock, p2Mock, "P1", "P2",
            mock(TableView.class), gameControllerMock, soundMock,
            2000, mock(InitialDistributionView.class),
            Runnable::run,
            viewProviderMock
        );
    }

    @Test
    void testRoundEndWithoutVictory() {
        when(scoreMock.calculateFinalScore(p1Mock)).thenReturn(100);
        when(p1Mock.getMatchTotalScore()).thenReturn(100);
        when(p2Mock.getMatchTotalScore()).thenReturn(100);

        scoreController.onRoundEnd();


        verify(p1Mock).addPointsToMatch(100);
        verify(soundMock).playRoundEndSound();
        verify(scoreViewMock).display();
        verify(soundMock, never()).playVictorySound();
    }

    @Test
    void testRoundEndWithVictory() throws InterruptedException {
        when(scoreMock.calculateFinalScore(p1Mock)).thenReturn(500);
        when(p1Mock.getMatchTotalScore()).thenReturn(2005); 

        scoreController.onRoundEnd();

        verify(soundMock, timeout(500)).playVictorySound();
        verify(scoreViewMock, timeout(500)).display();
    }
}
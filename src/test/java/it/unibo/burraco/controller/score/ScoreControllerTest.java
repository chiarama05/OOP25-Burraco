package it.unibo.burraco.controller.score;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.burraco.controller.round.RoundEndHandler;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.model.score.Score;

class ScoreControllerTest {

    private static final int SCORE_P1 = 150;
    private static final int SCORE_P2 = -20;

    private ScoreController scoreController;
    private Score score;
    private Player p1;
    private Player p2;
    private RoundEndHandler roundEndHandler;

    @BeforeEach
    void setUp() {
        score = mock(Score.class);
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        roundEndHandler = mock(RoundEndHandler.class);
        scoreController = new ScoreController(score, p1, p2, roundEndHandler);
    }

    @Test
    void testOnRoundEndOrchestration() {
        final int scoreP1 = SCORE_P1;
        final int scoreP2 = SCORE_P2;
        when(score.calculateFinalScore(p1)).thenReturn(scoreP1);
        when(score.calculateFinalScore(p2)).thenReturn(scoreP2);
        scoreController.onRoundEnd();
        verify(p1).addPointsToMatch(scoreP1);
        verify(p2).addPointsToMatch(scoreP2);
        verify(roundEndHandler).handle();
    }

    @Test
    void testSetOnNewRoundForwarding() {
        Runnable dummyAction = () -> { };
        scoreController.setOnNewRound(dummyAction);
        verify(roundEndHandler).setOnNewRound(dummyAction);
    }
}

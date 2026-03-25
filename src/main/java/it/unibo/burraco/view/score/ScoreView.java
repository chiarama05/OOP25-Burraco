package it.unibo.burraco.view.score;

public interface ScoreView {

    void display();
    
    void close();

    void setOnNextAction(Runnable action);

}

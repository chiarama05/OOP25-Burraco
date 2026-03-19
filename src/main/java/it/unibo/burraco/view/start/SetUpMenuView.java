package it.unibo.burraco.view.start;

public interface SetUpMenuView {

    void display();
    void close();

    interface OnConfigurationCompleteListener {
        void onConfigComplete(int score, String p1, String p2);
        void onBackClicked();
    }

}

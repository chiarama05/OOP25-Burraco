package view.start;

public interface StartMenuView {

    void display();

    void close();

    public interface OnGameStartListener {
        void onStartClicked();
    }

}

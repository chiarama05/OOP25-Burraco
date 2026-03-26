package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.attach.AttachButtonFactory;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.deck.DeckView;
import it.unibo.burraco.view.discard.DiscardViewImpl;
import it.unibo.burraco.view.hand.HandViewImpl;
import it.unibo.burraco.view.discard.TakeDiscardView;
import it.unibo.burraco.view.pot.PotView;
import it.unibo.burraco.view.selection.SelectionView;

import javax.swing.*;
import java.util.List;

public interface TableView extends TakeDiscardView, PotView, SelectionView {
    void refreshTurnLabel(boolean isP1);
    void markPotTaken(boolean isP1);
    void addCombinationToPlayerPanel(List<Card> cards, boolean isP1);
    void switchHand(boolean isP1);
    void startNewRound();
    void showScoreModal(String title, String message);
    void repaintTable();
    HandViewImpl getPlayer1HandView();
    HandViewImpl getPlayer2HandView();
    HandViewImpl getHandViewForCurrentPlayer(boolean isPlayer1);
    void refreshHandPanel(boolean isPlayer1, List<Card> hand);
    DiscardViewImpl getDiscardView();
    JPanel getDiscardPanel();
    JFrame getFrame();                       
    JButton getPutComboBtn();               
    JButton getTakeDiscardBtn();              
    DeckView getDeckView();         
    void setAttachButtonFactory(AttachButtonFactory factory);
    void setSelectionCardManager(SelectionCardManager manager);
}
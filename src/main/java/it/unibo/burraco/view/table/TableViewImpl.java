package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.attach.AttachButtonFactory;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.deck.DeckView;
import it.unibo.burraco.view.discard.DiscardViewImpl;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.hand.HandViewImpl;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TableViewImpl implements TableView {

    private final JFrame frame;
    private final JLabel turnLabel;
    private final JPanel combPanel1, combPanel2, discardPanel, deckPanel;
    private final String nameP1, nameP2;
    private final InitialDistributionView initDist;
    private final DiscardViewImpl discardView;
    private final DeckView deckView;
    private final JButton takeDiscardBtn;
    private final JButton putComboBtn;
    private final ControlPanelView sideControlPanel;
    private final BoardView boardView;
    private final PlayerAreaView playerArea;         
    private AttachButtonFactory attachButtonFactory;
    private SelectionCardManager selectionCardManager;

    public TableViewImpl(String n1, String n2, SelectionCardManager selectionManager) {
    this.nameP1 = (n1 == null || n1.isEmpty()) ? "Player 1" : n1;
    this.nameP2 = (n2 == null || n2.isEmpty()) ? "Player 2" : n2;

    frame = new JFrame("Burraco - OOP Project");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    Color lightgreen = new Color(180, 220, 180);
    frame.getContentPane().setBackground(lightgreen);

    this.turnLabel = new JLabel("Turn: " + nameP1);
    this.turnLabel.setFont(new Font("Arial", Font.BOLD, 25));
    frame.add(turnLabel, BorderLayout.NORTH);

    this.boardView = new BoardView(nameP1, nameP2, lightgreen);
    this.combPanel1 = boardView.getCombPanel1();
    this.combPanel2 = boardView.getCombPanel2();
    frame.add(boardView, BorderLayout.CENTER);

    this.discardPanel = new JPanel();  // ← prima discardPanel
    JScrollPane discardScroll = new JScrollPane(discardPanel);
    discardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    discardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    discardScroll.setPreferredSize(new Dimension(400, 110));
    discardScroll.setBorder(null);

    this.initDist = new InitialDistributionView(discardPanel, selectionManager); // ← poi initDist

    this.deckView = new DeckView();
    this.deckPanel = new JPanel(new BorderLayout());
    this.deckPanel.setBackground(lightgreen);

    this.playerArea = new PlayerAreaView(discardScroll, deckView, deckPanel, lightgreen);
    frame.add(playerArea, BorderLayout.SOUTH);

    this.takeDiscardBtn = new JButton("TAKE DISCARD");
    this.putComboBtn = new JButton("PUT COMBINATION");
    this.discardView = new DiscardViewImpl(discardPanel, new JPanel());
    JButton discardBtn = (JButton) discardView.getActionPanel().getComponent(0);
    discardBtn.setText("DISCARD");

    this.sideControlPanel = new ControlPanelView(takeDiscardBtn, putComboBtn, discardBtn, lightgreen);
    frame.add(sideControlPanel, BorderLayout.EAST);

    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setMinimumSize(new Dimension(900, 650));
    frame.setVisible(true);
}

    @Override
    public void refreshTurnLabel(boolean isP1) {
        turnLabel.setText("Turn: " + (isP1 ? nameP1 : nameP2));
    }

    @Override
    public void markPotTaken(boolean isP1) {
        ((javax.swing.border.TitledBorder)(isP1 ? combPanel1 : combPanel2)
            .getBorder()).setTitle((isP1 ? nameP1 : nameP2) + " [POT TAKEN]");
        frame.repaint();
    }

    @Override
    public void setAttachButtonFactory(AttachButtonFactory factory) {
    this.attachButtonFactory = factory;
    }

  @Override
public void addCombinationToPlayerPanel(List<Card> cards, boolean isP1) {
    JPanel panel = isP1 ? combPanel1 : combPanel2;
    
    JComponent btn = attachButtonFactory.create(cards, isP1);
    btn.setAlignmentY(Component.TOP_ALIGNMENT);
    panel.add(btn);
    panel.add(Box.createRigidArea(new Dimension(5, 0))); // ← stacco orizzontale
    
    frame.revalidate();
    frame.repaint();
}

    @Override
    public void switchHand(boolean isP1) {
    deckPanel.removeAll();
    deckPanel.add(new JLabel("Shift turn in progress...", SwingConstants.CENTER));
    deckPanel.revalidate();
    deckPanel.repaint();

    String activeName = isP1 ? nameP1 : nameP2;
    String idleName = isP1 ? nameP2 : nameP1;

    JOptionPane.showMessageDialog(frame,
        idleName + ", turn ended.\n\nHand the turn over to " + activeName + ".\n"
        + activeName + ", Press OK when you are ready to see your cards.",
        "Turn Privacy", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void startNewRound() {
        combPanel1.removeAll();
        combPanel2.removeAll();
        discardPanel.removeAll();
        ((javax.swing.border.TitledBorder) combPanel1.getBorder()).setTitle(nameP1);
        ((javax.swing.border.TitledBorder) combPanel2.getBorder()).setTitle(nameP2);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void showScoreModal(String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void repaintTable() {
        discardPanel.revalidate();
        discardPanel.repaint();
        Window w = SwingUtilities.getWindowAncestor(discardPanel);
        if (w != null) { w.revalidate(); w.repaint(); }
    }

    @Override
    public void showPotMessage(String playerName, boolean isDiscard) {
        String msg = isDiscard ?
            playerName + " You took the pot! You'll see the new cards next turn." :
            playerName + " You took the pot on fly! Keep playing.";
        JOptionPane.showMessageDialog(frame, msg, "Pot", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showSelectionError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Selezione", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void updateDiscardPile(List<Card> cards) {
        discardView.updateDiscardPile(cards);
    }

    @Override
    public void setSelectionCardManager(SelectionCardManager manager) {
        this.selectionCardManager = manager;
    }

    @Override
    public HandViewImpl getHandViewForCurrentPlayer(boolean isPlayer1) {
        return isPlayer1 ? getPlayer1HandView() : getPlayer2HandView();
    }

    @Override
public HandViewImpl getPlayer1HandView() {
    return initDist.getPlayer1HandView();
}

@Override
public HandViewImpl getPlayer2HandView() {
    return initDist.getPlayer2HandView();
}

@Override
public void refreshHandPanel(boolean isPlayer1, List<Card> hand) {
    deckPanel.removeAll();
    deckPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.WHITE, 1), "Hand",
        0, 0, new Font("Arial", Font.BOLD, 18), Color.BLACK));

    HandViewImpl hv = getHandViewForCurrentPlayer(isPlayer1);
    hv.refreshHand(hand);

    JScrollPane handScroll = new JScrollPane(hv);
    handScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    handScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    handScroll.setBorder(null);
    handScroll.setOpaque(false);
    handScroll.getViewport().setOpaque(false);
    deckPanel.add(handScroll, BorderLayout.CENTER);

    deckPanel.revalidate();
    deckPanel.repaint();
}

    @Override public DiscardViewImpl getDiscardView() { 
        return discardView; 
    }

    @Override public JPanel getDiscardPanel() { 
        return discardPanel; 
    }

    @Override public JFrame getFrame() { 
        return frame; 
    }

    @Override public JButton getPutComboBtn() { 
        return putComboBtn; 
    }

    @Override public JButton getTakeDiscardBtn() { 
        return takeDiscardBtn;
    }
    @Override public DeckView getDeckView() { 
        return deckView; 
    }

    public InitialDistributionView getInitDist() { 
        return initDist; 
    }
}
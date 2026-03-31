package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.attach.AttachButtonFactory;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.view.deck.DeckView;
import it.unibo.burraco.view.discardcard.discard.DiscardView;
import it.unibo.burraco.view.discardcard.discard.DiscardViewImpl;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.hand.HandView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * Concrete Swing implementation of {@link TableView}.
 * Builds and manages the main game window, including the combination panels
 * for both players, the discard pile area, the deck panel, the hand panels,
 * and the side control panel.
 * All layout constants are defined as named {@code private static final} fields.
 */
public final class TableViewImpl implements TableView {

    private static final int FRAME_WIDTH = 900;
    private static final int FRAME_HEIGHT = 650;
    private static final int FONT_SIZE_TURN = 25;
    private static final int FONT_SIZE_HAND = 18;
    private static final int DISCARD_WIDTH = 400;
    private static final int DISCARD_HEIGHT = 110;
    private static final int BG_R = 180;
    private static final int BG_G = 220;
    private static final int BG_B = 180;
    private static final int RIGID_AREA_WIDTH = 5;
    private static final int TITLE_JUSTIFICATION = 0;
    private static final int TITLE_POSITION = 0;
    private static final int BORDER_THICKNESS = 1;

    private static final String FONT_NAME = "Arial";

    private final JFrame frame;
    private final JLabel turnLabel;
    private final JPanel combPanel1;
    private final JPanel combPanel2;
    private final JPanel discardPanel;
    private final JPanel deckPanel;
    private final String nameP1;
    private final String nameP2;
    private final InitialDistributionView initDist;
    private final DiscardView discardView;
    private final DeckView deckView;
    private final JButton takeDiscardBtn;
    private final JButton putComboBtn;
    private final ControlPanelView sideControlPanel;
    private final BoardView boardView;
    private final PlayerAreaView playerArea;

    private AttachButtonFactory attachButtonFactory;
    private SelectionCardManager selectionCardManager;

    /**
     * Constructs the main game window and initialises all Swing sub-components.
     *
     * @param n1               the display name for Player 1
     * @param n2               the display name for Player 2
     * @param selectionManager the manager tracking which cards are currently selected
     */
    public TableViewImpl(final String n1,
            final String n2,
            final SelectionCardManager selectionManager) {
        this.nameP1 = (n1 == null || n1.isEmpty()) ? "Player 1" : n1;
        this.nameP2 = (n2 == null || n2.isEmpty()) ? "Player 2" : n2;

        this.frame = new JFrame("Burraco - OOP Project");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());

        final Color lightgreen = new Color(BG_R, BG_G, BG_B);
        this.frame.getContentPane().setBackground(lightgreen);

        this.turnLabel = new JLabel("Turn: " + nameP1);
        this.turnLabel.setFont(new Font("Arial", Font.BOLD, FONT_SIZE_TURN));
        this.frame.add(turnLabel, BorderLayout.NORTH);

        this.boardView = new BoardView(nameP1, nameP2, lightgreen);
        this.combPanel1 = boardView.getCombPanel1();
        this.combPanel2 = boardView.getCombPanel2();
        this.frame.add(boardView, BorderLayout.CENTER);

        this.discardPanel = new JPanel();
        final JScrollPane discardScroll = new JScrollPane(discardPanel);
        discardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        discardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        discardScroll.setPreferredSize(new Dimension(DISCARD_WIDTH, DISCARD_HEIGHT));
        discardScroll.setBorder(null);

        this.initDist = new InitialDistributionView(discardPanel, selectionManager);

        this.deckView = new DeckView();
        this.deckPanel = new JPanel(new BorderLayout());
        this.deckPanel.setBackground(lightgreen);

        this.playerArea = new PlayerAreaView(discardScroll, deckView, deckPanel, lightgreen);
        frame.add(playerArea, BorderLayout.SOUTH);

        this.takeDiscardBtn = new JButton("TAKE DISCARD");
        this.putComboBtn = new JButton("PUT COMBINATION");
        final DiscardViewImpl dvImpl = new DiscardViewImpl(discardPanel, new JPanel());
        this.discardView = dvImpl;
        final JButton discardBtn = (JButton) dvImpl.getActionPanel().getComponent(0);
        discardBtn.setText("DISCARD");

        this.sideControlPanel = new ControlPanelView(takeDiscardBtn, putComboBtn, discardBtn, lightgreen);
        this.frame.add(sideControlPanel, BorderLayout.EAST);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.frame.setVisible(true);
    }

    @Override
    public void refreshTurnLabel(final boolean isP1) {
        this.turnLabel.setText("Turn: " + (isP1 ? this.nameP1 : this.nameP2));
    }

    @Override
    public void markPotTaken(final boolean isP1) {
        final JPanel targetPanel = isP1 ? this.combPanel1 : this.combPanel2;
        final String targetName = isP1 ? this.nameP1 : this.nameP2;
        ((TitledBorder) targetPanel.getBorder()).setTitle(targetName + " [POT TAKEN]");
        frame.repaint();
    }

    @Override
    public void setAttachButtonFactory(final AttachButtonFactory factory) {
        this.attachButtonFactory = factory;
    }

    @Override
    public void addCombinationToPlayerPanel(final List<Card> cards, final boolean isP1) {
        final JPanel panel = isP1 ? this.combPanel1 : this.combPanel2;
        final JComponent btn = this.attachButtonFactory.create(cards, isP1);
        btn.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(RIGID_AREA_WIDTH, 0)));
        this.frame.revalidate();
        this.frame.repaint();
    }

    @Override
    public void switchHand(final boolean isP1) {
        this.deckPanel.removeAll();
        this.deckPanel.add(new JLabel("Shift turn in progress...", SwingConstants.CENTER));
        this.deckPanel.revalidate();
        this.deckPanel.repaint();

        final String activeName = isP1 ? this.nameP1 : this.nameP2;
        final String idleName = isP1 ? this.nameP2 : this.nameP1;

        JOptionPane.showMessageDialog(
            this.frame,
            idleName + ", turn ended.\n\nHand the turn over to " + activeName + ".\n"
                + activeName + ", Press OK when you are ready to see your cards.",
            "Turn Privacy",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void startNewRound() {
        this.combPanel1.removeAll();
        this.combPanel2.removeAll();
        this.discardPanel.removeAll();
        ((TitledBorder) this.combPanel1.getBorder()).setTitle(this.nameP1);
        ((TitledBorder) this.combPanel2.getBorder()).setTitle(this.nameP2);
        this.frame.revalidate();
        this.frame.repaint();
    }

    @Override
    public void showScoreModal(final String title, final String message) {
        JOptionPane.showMessageDialog(this.frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void repaintTable() {
        this.discardPanel.revalidate();
        this.discardPanel.repaint();
        final Window w = SwingUtilities.getWindowAncestor(this.discardPanel);
         if (w != null) {
            w.revalidate();
            w.repaint();
        }
    }

    @Override
    public void showPotMessage(final String playerName, final boolean isDiscard) {
        final String msg = isDiscard
            ? playerName + " You took the pot! You'll see the new cards next turn."
            : playerName + " You took the pot on fly! Keep playing.";
        JOptionPane.showMessageDialog(this.frame, msg, "Pot", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showSelectionError(final String message) {
        JOptionPane.showMessageDialog(this.frame, message, "Selection", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void updateDiscardPile(final List<Card> cards) {
        this.discardView.updateDiscardPile(cards);
    }

    @Override
    public void setSelectionCardManager(final SelectionCardManager manager) {
    }

    @Override
    public HandView getHandViewForCurrentPlayer(final boolean isPlayer1) {
        return isPlayer1 ? this.getPlayer1HandView() : this.getPlayer2HandView();
    }

    @Override
    public HandView getPlayer1HandView() {
        return this.initDist.getPlayer1HandView();
    }

    @Override
    public HandView getPlayer2HandView() {
        return this.initDist.getPlayer2HandView();
    }

    @Override
    public void refreshHandPanel(final boolean isPlayer1, final List<Card> hand) {
        this.deckPanel.removeAll();
        this.deckPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, BORDER_THICKNESS),
            "Hand", TITLE_JUSTIFICATION, TITLE_POSITION,
            new Font(FONT_NAME, Font.BOLD, FONT_SIZE_HAND), Color.BLACK));

        final HandView hv = this.getHandViewForCurrentPlayer(isPlayer1);
        hv.refreshHand(hand);

        final JScrollPane handScroll = new JScrollPane((JComponent) hv);
        handScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        handScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        handScroll.setBorder(null);
        handScroll.setOpaque(false);
        handScroll.getViewport().setOpaque(false);
        this.deckPanel.add(handScroll, BorderLayout.CENTER);

        this.deckPanel.revalidate();
        this.deckPanel.repaint();
    }

    @Override
    public DiscardView getDiscardView() {
        return this.discardView;
    }

    @Override
    public JPanel getDiscardPanel() {
        return this.discardPanel;
    }

    @Override
    public JFrame getFrame() {
        return this.frame;
    }

    @Override
    public JButton getPutComboBtn() {
        return this.putComboBtn;
    }

    @Override
    public JButton getTakeDiscardBtn() {
        return this.takeDiscardBtn;
    }

    @Override
    public DeckView getDeckView() {
        return this.deckView;
    }

    /**
     * Provides access to the InitialDistributionView.
     *
     * @return the initial distribution view
     */
    public InitialDistributionView getInitDist() {
        return this.initDist;
    }
}

package it.unibo.burraco.view.table;

import it.unibo.burraco.model.GameModel;
import it.unibo.burraco.model.cards.Card;
import it.unibo.burraco.model.move.Move;
import it.unibo.burraco.model.move.MoveResult;
import it.unibo.burraco.model.player.Player;
import it.unibo.burraco.view.SelectionCardManager;
import it.unibo.burraco.view.components.attach.AttachButtonFactory;
import it.unibo.burraco.view.scenes.InitialDistributionView;
import it.unibo.burraco.view.table.deck.DeckView;
import it.unibo.burraco.view.table.discard.DiscardView;
import it.unibo.burraco.view.table.discard.DiscardViewImpl;
import it.unibo.burraco.view.table.hand.HandView;

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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class TableViewImpl implements TableView {

    private static final int FRAME_MIN_W    = 900;
    private static final int FRAME_MIN_H    = 650;
    private static final int DISCARD_W      = 400;
    private static final int DISCARD_H      = 110;
    private static final int RIGID_AREA_W   = 5;
    private static final int    FONT_TURN   = 25;
    private static final int    FONT_HAND   = 18;
    private static final String FONT_NAME   = "Arial";
    private static final Color LIGHT_GREEN = new Color(180, 220, 180);
    private static final int TITLE_JUST = 0;
    private static final int TITLE_POS  = 0;
    private static final int BORDER_PX  = 1;
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
    private final AttachButtonFactory attachButtonFactory;

    private boolean firstWakeUp      = true;
    private boolean currentIsPlayer1 = true;
    private CompletableFuture<Move> pendingFuture;


    /**
     * Builds the full game table and makes it visible.
     *
     * @param n1 display name for Player 1 (fallback: "Player 1")
     * @param n2 display name for Player 2 (fallback: "Player 2")
     */
    public TableViewImpl(final String n1, final String n2) {
        this.nameP1 = resolved(n1, "Player 1");
        this.nameP2 = resolved(n2, "Player 2");

        final SelectionCardManager sel = new SelectionCardManager();

        this.frame = new JFrame("Burraco - OOP Project");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());
        this.frame.getContentPane().setBackground(LIGHT_GREEN);

        this.turnLabel = buildTurnLabel();
        this.frame.add(this.turnLabel, BorderLayout.NORTH);

        final BoardView board = new BoardView(nameP1, nameP2, LIGHT_GREEN);
        this.combPanel1 = board.getCombPanel1();
        this.combPanel2 = board.getCombPanel2();
        this.frame.add(board, BorderLayout.CENTER);

        this.discardPanel = new JPanel();
        this.deckView     = new DeckView();
        this.deckPanel    = new JPanel(new BorderLayout());
        this.deckPanel.setBackground(LIGHT_GREEN);
        this.initDist = new InitialDistributionView(discardPanel, sel);
        this.frame.add(
                new PlayerAreaView(buildDiscardScroll(), deckView, deckPanel, LIGHT_GREEN),
                BorderLayout.SOUTH);

        this.takeDiscardBtn = new JButton("TAKE DISCARD");
        this.putComboBtn    = new JButton("PUT COMBINATION");
        final DiscardViewImpl dvImpl = new DiscardViewImpl(discardPanel, new JPanel());
        this.discardView = dvImpl;
        final JButton discardBtn = (JButton) dvImpl.getActionPanel().getComponent(0);
        discardBtn.setText("DISCARD");
        this.frame.add(
                new ControlPanelView(takeDiscardBtn, putComboBtn, discardBtn, LIGHT_GREEN),
                BorderLayout.EAST);

        // ── Frame finalisation ─────────────────────────────────────────────
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setMinimumSize(new Dimension(FRAME_MIN_W, FRAME_MIN_H));
        this.frame.setVisible(true);

        this.attachButtonFactory = new AttachButtonFactory(this, this.frame);
        wireButtons();
    }

    @Override
    public void wakeUp(final Player player, final boolean isPlayer1) {
        this.currentIsPlayer1 = isPlayer1;
        getHandViewForCurrentPlayer(isPlayer1).clearSelection();

        this.deckPanel.removeAll();
        this.deckPanel.revalidate();
        this.deckPanel.repaint();

        if (firstWakeUp) {
            firstWakeUp = false;
            JOptionPane.showMessageDialog(frame,
                    (isPlayer1 ? nameP1 : nameP2) + ", it's your turn!\nPress OK when ready.",
                    "Game start", JOptionPane.INFORMATION_MESSAGE);
        } else {
            switchHand(isPlayer1);
        }

        refreshTurnLabel(isPlayer1);
        refreshHandPanel(isPlayer1, player.getHand());
    }

    @Override
    public void setMoveFuture(final CompletableFuture<Move> future) {
        this.pendingFuture = future;
    }

    @Override
    public CompletableFuture<Move> getPendingFuture() {
        return this.pendingFuture;
    }

    @Override
    public void refresh(final GameModel model) {
        final Player current = model.getCurrentPlayer();
        final boolean isP1   = model.isPlayer1(current);
        this.currentIsPlayer1 = isP1;
        redrawAllCombinations(model);
        getHandViewForCurrentPlayer(isP1).clearSelection();
        refreshHandPanel(isP1, current.getHand());
        discardView.updateDiscardPile(model.getDiscardPile().getCards());
    }

    @Override
    public void showMoveError(final MoveResult error) {
        final String msg = switch (error.getStatus()) {
            case ALREADY_DRAWN       -> "You already drew this turn.";
            case NOT_DRAWN           -> "You must draw before playing.";
            case NO_CARDS_SELECTED   -> "Select at least one card.";
            case INVALID_COMBINATION -> "Invalid combination.";
            case WOULD_GET_STUCK     -> "This move would leave you stuck.";
            case WRONG_PLAYER        -> "It's not your turn.";
            default                  -> "Invalid move: " + error.getStatus();
        };
        JOptionPane.showMessageDialog(this.frame, msg,
                "Invalid move", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void showWinner(final Player winner) {
        JOptionPane.showMessageDialog(this.frame,
                "Game over!\nWinner: " + winner.getName(),
                "End of game", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void refreshTurnLabel(final boolean isP1) {
        this.turnLabel.setText("Turn: " + (isP1 ? nameP1 : nameP2));
    }

    @Override
    public void switchHand(final boolean isP1) {
        this.deckPanel.removeAll();
        this.deckPanel.add(new JLabel("Switching turn…", SwingConstants.CENTER));
        this.deckPanel.revalidate();
        this.deckPanel.repaint();

        JOptionPane.showMessageDialog(this.frame,
                (isP1 ? nameP2 : nameP1) + ", turn ended.\n\nPass to "
                        + (isP1 ? nameP1 : nameP2) + ".\nPress OK when ready.",
                "Turn privacy", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void startNewRound() {
        this.combPanel1.removeAll();
        this.combPanel2.removeAll();
        this.discardPanel.removeAll();
        ((TitledBorder) combPanel1.getBorder()).setTitle(nameP1);
        ((TitledBorder) combPanel2.getBorder()).setTitle(nameP2);
        this.frame.revalidate();
        this.frame.repaint();
    }

    @Override
    public void repaintTable() {
        this.discardPanel.revalidate();
        this.discardPanel.repaint();
        final Window w = SwingUtilities.getWindowAncestor(discardPanel);
        if (w != null) {
            w.revalidate();
            w.repaint();
        }
    }

    @Override
    public void refreshHandPanel(final boolean isPlayer1, final List<Card> hand) {
        this.deckPanel.removeAll();
        this.deckPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, BORDER_PX),
                "Hand", TITLE_JUST, TITLE_POS,
                new Font(FONT_NAME, Font.BOLD, FONT_HAND), Color.BLACK));

        final HandView hv = getHandViewForCurrentPlayer(isPlayer1);
        hv.refreshHand(new ArrayList<>(hand));

        final JScrollPane scroll = new JScrollPane((JComponent) hv);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        this.deckPanel.add(scroll, BorderLayout.CENTER);

        this.deckPanel.revalidate();
        this.deckPanel.repaint();
    }

    @Override
    public void addCombinationToPlayerPanel(final List<Card> cards, final boolean isP1) {
        addComboToPanel(new ArrayList<>(cards), isP1);
        this.frame.revalidate();
        this.frame.repaint();
    }

    @Override
    public void updateDiscardPile(final List<Card> cards) {
        this.discardPanel.removeAll();
        this.discardView.updateDiscardPile(new ArrayList<>(cards));
        this.discardPanel.revalidate();
        this.discardPanel.repaint();
    }

    @Override
    public void markPotTaken(final boolean isP1) {
        final JPanel target = isP1 ? combPanel1 : combPanel2;
        final String name   = isP1 ? nameP1 : nameP2;
        ((TitledBorder) target.getBorder()).setTitle(name + " [POT TAKEN]");
        this.frame.repaint();
    }

    @Override
    public HandView getHandViewForCurrentPlayer(final boolean isPlayer1) {
        return isPlayer1 ? getPlayer1HandView() : getPlayer2HandView();
    }

    @Override
    public HandView getPlayer1HandView() {
        return this.initDist.getPlayer1HandView();
    }

    @Override
    public HandView getPlayer2HandView() {
        return this.initDist.getPlayer2HandView();
    }

    @Override public JFrame getFrame() { 
        return this.frame; 
    }

    @Override public JButton getPutComboBtn() { 
        return this.putComboBtn; 
    }

    @Override public JButton     getTakeDiscardBtn() { 
        return this.takeDiscardBtn; 
    }

    @Override public DeckView getDeckView() { 
        return this.deckView; 
    }

    @Override public DiscardView getDiscardView() { 
        return this.discardView; 
    }

    @Override public JPanel getDiscardPanel() { 
        return this.discardPanel; 
    }

    @Override public boolean isCurrentPlayer1() { 
        return this.currentIsPlayer1; 
    }

    /** Required by {@code GameWiring} to pass the distribution view to wiring. */
    public InitialDistributionView getInitDist() { return this.initDist; }

    /** Resolves a nullable/blank player name to a safe fallback. */
    private static String resolved(final String name, final String fallback) {
        return (name == null || name.isBlank()) ? fallback : name;
    }

    private JLabel buildTurnLabel() {
        final JLabel lbl = new JLabel("Turn: " + this.nameP1);
        lbl.setFont(new Font(FONT_NAME, Font.BOLD, FONT_TURN));
        return lbl;
    }

    private JScrollPane buildDiscardScroll() {
        final JScrollPane s = new JScrollPane(discardPanel);
        s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        s.setPreferredSize(new Dimension(DISCARD_W, DISCARD_H));
        s.setBorder(null);
        return s;
    }

    /** Wires every button to complete the pending future with the right Move. */
    private void wireButtons() {
        this.deckView.getDeckButton().addActionListener(e ->
                complete(new Move(Move.Type.DRAW_DECK, Collections.emptyList())));

        this.takeDiscardBtn.addActionListener(e ->
                complete(new Move(Move.Type.DRAW_DISCARD, Collections.emptyList())));

        this.putComboBtn.addActionListener(e -> {
            final List<Card> sel = new ArrayList<>(
                    getHandViewForCurrentPlayer(currentIsPlayer1).getSelectedCards());
            complete(new Move(Move.Type.PUT_COMBINATION, sel));
        });

        this.discardView.setDiscardListener(e -> {
            final List<Card> sel = new ArrayList<>(
                    getHandViewForCurrentPlayer(currentIsPlayer1).getSelectedCards());
            complete(new Move(Move.Type.DISCARD, sel));
        });
    }

    /** Thread-safe helper: completes the pending future if still open. */
    private void complete(final Move move) {
        if (this.pendingFuture != null && !this.pendingFuture.isDone()) {
            this.pendingFuture.complete(move);
        }
    }

    private void redrawAllCombinations(final GameModel model) {
        this.combPanel1.removeAll();
        this.combPanel2.removeAll();

        for (final List<Card> combo : model.getPlayer1().getCombinations()) {
            addComboToPanel(new ArrayList<>(combo), true);
        }
        for (final List<Card> combo : model.getPlayer2().getCombinations()) {
            addComboToPanel(new ArrayList<>(combo), false);
        }

        this.frame.revalidate();
        this.frame.repaint();
    }

    private void addComboToPanel(final List<Card> cards, final boolean isP1) {
        final JPanel panel = isP1 ? combPanel1 : combPanel2;
        final JComponent btn = attachButtonFactory.create(cards, isP1);
        btn.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(RIGID_AREA_W, 0)));
    }

    /**
     * Displays a modal dialog with a score summary.
     * Called only from {@code ScoreController} when no dedicated ScoreView is available.
     *
     * @param title   dialog title
     * @param message dialog body
     */
    public void showScoreModal(final String title, final String message) {
        JOptionPane.showMessageDialog(this.frame, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}

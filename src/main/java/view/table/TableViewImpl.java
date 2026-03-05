package view.table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import core.discardcard.DiscardManagerImpl;
import core.distributioncard.DistributionManagerImpl;
import model.player.PlayerImpl;
import view.bottom.DiscardController;
import view.discard.DiscardViewImpl;
import view.distribution.InitialDistributionView;
import view.hand.handImpl;
import model.card.Card;
import model.deck.DeckImpl;
import model.discard.DiscardPileImpl;
import model.deck.DeckImpl;
import model.player.Player;
import view.bottom.DeckView;
import view.bottom.DeckController;

import java.util.List;

public class TableViewImpl implements TableView {

    private final DeckImpl commonDeck; 
    private final core.drawcard.DrawManager drawManager = new core.drawcard.DrawManager();
    private final JFrame frame;
    private final JLabel turnLabel;

    private final JPanel combPanel1;
    private final JPanel combPanel2;
    private final JPanel discardPanel;
    private final JPanel deckPanel;

    private final Font baseTitleFont = new Font("Arial", Font.BOLD, 23);

    private final InitialDistributionView initDist;
    private final PlayerImpl player1;
    private final PlayerImpl player2;

    private boolean turnoPlayer1 = true; // indica chi è il giocatore attivo

    public TableViewImpl() {
        frame = new JFrame("Burraco - OOOP Project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        this.commonDeck = new DeckImpl();

        DeckView deckView = new DeckView();
        new DeckController(deckView, drawManager, this);

        // ==== Turno ====
        turnLabel = new JLabel("Turn: Player 1");
        turnLabel.setFont(baseTitleFont);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        frame.add(turnLabel, BorderLayout.NORTH);

        // ==== Combinazioni centro ====
        JPanel combinationPanel = new JPanel(new GridLayout(1, 2, 20, 10));

        combPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        combPanel1.setBorder(BorderFactory.createTitledBorder("Player 1"));
        combinationPanel.add(new JScrollPane(combPanel1));

        combPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        combPanel2.setBorder(BorderFactory.createTitledBorder("Player 2"));
        combinationPanel.add(new JScrollPane(combPanel2));

        frame.add(combinationPanel, BorderLayout.CENTER);

        // ==== Scarti sopra le mani ====
        discardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        discardPanel.setBorder(BorderFactory.createTitledBorder("Discard Pile"));
        discardPanel.setBackground(new Color(250, 250, 240));

        // ==== Deck / mano in basso ====
        deckPanel = new JPanel(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder("Hand"));

        JPanel centralBottomPanel = new JPanel(new BorderLayout());
        centralBottomPanel.add(discardPanel, BorderLayout.CENTER); // Gli scarti al centro
        centralBottomPanel.add(deckView, BorderLayout.WEST);       // Il mazzo subito a sinistra degli scarti

        // Pannello finale del basso (Mazzo+Scarti sopra, Mano sotto)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(centralBottomPanel, BorderLayout.NORTH); 
        bottomPanel.add(deckPanel, BorderLayout.CENTER);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ==== Inizializzazione giocatori e distribuzione carte ====
        player1 = new PlayerImpl();
        player2 = new PlayerImpl();
        DistributionManagerImpl distManager = new DistributionManagerImpl();
        initDist = new InitialDistributionView(discardPanel);

        // Distribuisci carte e aggiorna GUI
        initDist.distribute(player1, player2, distManager);

        // Mostra inizialmente la mano del giocatore 1
        refreshHandPanel();

        // ==== Pannello bottoni a destra ====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(180, 400));

        DiscardViewImpl discardView = new DiscardViewImpl(discardPanel, new JPanel());
        JButton discardBtn = (JButton) discardView.getActionPanel().getComponent(0);

        JButton drawDiscardBtn = new JButton("Take discard");
        JButton putComboBtn = new JButton("Put combination");

        for (JButton b : new JButton[]{drawDiscardBtn, putComboBtn, discardBtn}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(160, 40));
            rightPanel.add(b);
            rightPanel.add(Box.createVerticalStrut(10));
        }

        frame.add(rightPanel, BorderLayout.EAST);

        DiscardController discardController = new DiscardController(
        turnoPlayer1 ? player1 : player2,       // giocatore attivo
        turnoPlayer1 ? initDist.getPlayer1HandView() : initDist.getPlayer2HandView(),
        new DiscardManagerImpl(new DiscardPileImpl()), // discard manager con la discard pile
        discardView
        );

        // ==== Resize responsive ====
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyResponsiveFonts();
            }
        });

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.pack();
        frame.setVisible(true);
        applyResponsiveFonts();
    }

    // ================= Funzioni per le mani =================

    /**
     * Aggiorna il pannello deckPanel per mostrare solo la mano del giocatore attivo
     */
    public void refreshHandPanel() {
        deckPanel.removeAll();
        handImpl handToShow = turnoPlayer1 ? initDist.getPlayer1HandView() : initDist.getPlayer2HandView();
        deckPanel.add(handToShow, BorderLayout.CENTER);
        deckPanel.revalidate();
        deckPanel.repaint();
    }

    /**
     * Cambia il turno del giocatore e aggiorna la mano visibile
     */
    public void switchTurn() {
        turnoPlayer1 = !turnoPlayer1;
        refreshTurnLabel(turnoPlayer1);
        refreshHandPanel();
    }

    // ================= Turno =================
    public void refreshTurnLabel(boolean turnoPlayer1) {
        turnLabel.setText("Turn: " + (turnoPlayer1 ? "Player 1" : "Player 2"));
        frame.revalidate();
        frame.repaint();
    }

    // ================= Messaggi =================
    public void showPotFly() {
        JOptionPane.showMessageDialog(frame, "You close your hand on 'fly', you can continue to play in this same turn!");
    }

    public void showPotnextTurn() {
        JOptionPane.showMessageDialog(frame, "You can take your pot! You can play it in the NEXT turn!");
    }

    public void showNotValideClosure() {
        JOptionPane.showMessageDialog(frame, "You can't discard your last card without even done a Burraco!");
    }

    public void showWinExit(boolean player1Won) {
        JOptionPane.showMessageDialog(frame, "You can exit now! the winner is... " + (player1Won ? "Player 1" : "Player 2"));
        frame.dispose();
        System.exit(0);
    }

    // ================= Fonts responsive =================
    private void applyResponsiveFonts() {
        int w = Math.max(frame.getWidth(), 1);
        double factor = clamp(w / 1280.0, 0.85, 1.4);

        turnLabel.setFont(scaleFont(baseTitleFont, factor));

        setTitledBorderFont(combPanel1, scaleFont(baseTitleFont, factor * 0.95));
        setTitledBorderFont(combPanel2, scaleFont(baseTitleFont, factor * 0.95));
        setTitledBorderFont(discardPanel, scaleFont(baseTitleFont, factor * 0.9));
        setTitledBorderFont(deckPanel, scaleFont(baseTitleFont, factor * 0.9));

        frame.revalidate();
        frame.repaint();
    }

    private void setTitledBorderFont(final JComponent comp, final Font font) {
        if (comp.getBorder() instanceof javax.swing.border.TitledBorder tb) {
            tb.setTitleFont(font);
            comp.repaint();
        }
    }

    private Font scaleFont(final Font base, double factor) {
        int newSize = (int) Math.round(base.getSize2D() * factor);
        newSize = Math.max(10, Math.min(newSize, 28));
        return base.deriveFont((float) newSize);
    }

    private double clamp(double b, double min, double max) {
        return Math.max(min, Math.min(max, b));
    }


    public void addCombinationToPlayerPanel(List<Card> cards, boolean player1Turn){
    JPanel targetPanel = player1Turn ? combPanel1 : combPanel2;

    JPanel newComboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    for(Card c : cards){
        JButton cardBtn = new JButton(c.toString());
        newComboPanel.add(cardBtn);
    }

    targetPanel.add(newComboPanel);
    targetPanel.revalidate();
    targetPanel.repaint();
    }

    public void refreshHand(PlayerImpl player) {
    deckPanel.removeAll();

    handImpl handView;
    if (player == player1) {
        handView = initDist.getPlayer1HandView();
    } else {
        handView = initDist.getPlayer2HandView();
    }

    deckPanel.add(handView, BorderLayout.CENTER);
    deckPanel.revalidate();
    deckPanel.repaint();
}
public Player getCurrentPlayer() {
        return turnoPlayer1 ? player1 : player2;
    }

    public DeckImpl getCommonDeck() {
        return this.commonDeck;
    }
}
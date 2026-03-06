package view.table;

import core.discardcard.DiscardManagerImpl;
import core.distributioncard.DistributionManagerImpl;
import core.drawcard.DrawManager;
import core.selectioncard.SelectionCardManager;
import model.card.Card;
import model.deck.DeckImpl;
import model.player.*;
import view.botton.*;
import view.discard.DiscardViewImpl;
import view.distribution.InitialDistributionView;
import view.hand.handImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class TableViewImpl implements TableView {

    private final DeckImpl commonDeck;
    private final DrawManager drawManager;
    private final JFrame frame;
    private final JLabel turnLabel;
    private final DeckView deckView;
    private final JPanel combPanel1;
    private final JPanel combPanel2;
    private final JPanel discardPanel;
    private final JPanel deckPanel;
    private final Font baseTitleFont = new Font("Arial", Font.BOLD, 23);
    private final InitialDistributionView initDist;
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final SelectionCardManager selectionManager;
    private DiscardViewImpl discardView;
    private boolean turnoPlayer1 = true;
    private final model.discard.DiscardPile discardPileModel;

    private JButton takeDiscardBtn;
    private DeckController deckController;
    private DiscardController discardController;
    private final JButton putComboBtn;

    public TableViewImpl(PlayerImpl player1,PlayerImpl player2,DeckImpl commonDeck, SelectionCardManager selectionManager, DrawManager drawManager, DistributionManagerImpl distManager) {

        this.player1 = player1;
        this.player2 = player2;
        this.commonDeck = commonDeck;
        this.selectionManager = selectionManager;
        this.drawManager = drawManager;

        frame = new JFrame("Burraco - OOOP Project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ==== Turn ====
        turnLabel = new JLabel("Turn: Player 1");
        turnLabel.setFont(baseTitleFont);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        frame.add(turnLabel, BorderLayout.NORTH);

        // ==== Central Combinations ====
        JPanel combinationPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        combPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        combPanel1.setBorder(BorderFactory.createTitledBorder("Player 1"));
        combinationPanel.add(new JScrollPane(combPanel1));

        combPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        combPanel2.setBorder(BorderFactory.createTitledBorder("Player 2"));
        combinationPanel.add(new JScrollPane(combPanel2));

        frame.add(combinationPanel, BorderLayout.CENTER);

        // ==== Discards and Decks ====
        discardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        discardPanel.setBorder(BorderFactory.createTitledBorder("Discard Pile"));
        discardPanel.setBackground(new Color(250, 250, 240));

        deckView = new DeckView();

        JPanel centralBottomPanel = new JPanel(new BorderLayout());
        centralBottomPanel.add(discardPanel, BorderLayout.CENTER);
        centralBottomPanel.add(deckView, BorderLayout.WEST);

        // ==== Deck / Hand in the south====
        deckPanel = new JPanel(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder("Hand"));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(centralBottomPanel, BorderLayout.NORTH);
        bottomPanel.add(deckPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ==== Distribuzione iniziale ====
        this.discardPileModel = new model.discard.DiscardPileImpl();
        this.discardView = new DiscardViewImpl(discardPanel, new JPanel());
        
        initDist = new InitialDistributionView(discardPanel, selectionManager);
        initDist.distribute(player1, player2, distManager,commonDeck, discardView, discardPileModel);

        // ==== Pannello bottoni a destra ====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(180, 400));

        JButton discardBtn = (JButton) discardView.getActionPanel().getComponent(0);
        this.takeDiscardBtn = new JButton("Take discard");
        this.putComboBtn = new JButton("Put combination");

        for (JButton b : new JButton[]{takeDiscardBtn, putComboBtn, discardBtn}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(160, 40));
            rightPanel.add(b);
            rightPanel.add(Box.createVerticalStrut(10));
        }
        frame.add(rightPanel, BorderLayout.EAST);

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
    public void wireControllers(final model.turn.TurnManager turnManager, final core.discardcard.DiscardManagerImpl discardManager){
        this.deckController=new DeckController(deckView,drawManager,this,turnManager);
        new TakeDiscardController(takeDiscardBtn, drawManager, this, turnManager, discardPileModel, discardView);
        PutCombinationController putController=new PutCombinationController(this,turnManager,selectionManager);
        putComboBtn.addActionListener(e -> putController.handlePutCombination());
        this.discardController=new DiscardController(this,turnManager,discardManager,discardView, discardPileModel, drawManager);
    }


    public void refreshHandPanel(Player player) {
        deckPanel.removeAll();

        handImpl handView=getHandViewForPlayer(player);
        handView.refreshHand(player.getHand());

        deckPanel.add(handView,BorderLayout.CENTER);
        deckPanel.revalidate();
        deckPanel.repaint();
    }
 
    public handImpl getHandViewForPlayer(Player player){
        return (player==player1) ? initDist.getPlayer1HandView() : initDist.getPlayer2HandView();
    }

    public boolean isPlayer1(Player player){
        return player==player1;
    }

    public Player getCurrentPlayer(){
        return turnoPlayer1 ? player1 : player2;
    }

    

    private Color getCardColor(String cardString){
        if(cardString.contains("♥") || cardString.contains("♦")) {
                return Color.RED;
            }
            return Color.BLACK;
    }


    public void switchHand(boolean isPlayer1Turn){
        refreshHandPanel(isPlayer1Turn ? player1 : player2);
    }


    // ================= Turno =================
    public void refreshTurnLabel(boolean turnoPlayer1) {
        turnLabel.setText("Turn: " + (turnoPlayer1 ? "Player 1" : "Player 2"));
        frame.revalidate();
        frame.repaint();
    }

    // ================= Messaggi =================

    public DeckImpl getCommonDeck(){
        return this.commonDeck;
    }
    
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
    double factor = clamp(w / 1280.0, 0.7, 1.2); 

    turnLabel.setFont(scaleFont(baseTitleFont, factor));

    // Ridimensiona i titoli dei pannelli (Player 1, Player 2, Discard, Hand)
    Font titleFont = scaleFont(baseTitleFont, factor * 0.6);
    setTitledBorderFont(combPanel1, titleFont);
    setTitledBorderFont(combPanel2, titleFont);
    setTitledBorderFont(discardPanel, titleFont);
    setTitledBorderFont(deckPanel, titleFont);

    // --- CALCOLO DIMENSIONE UNICA CARTE (PROPORZIONALE) ---
    int cardWidth = Math.max(45, (int)(w / 25)); 
    int cardHeight = (int)(cardWidth * 1.4);
    Dimension cardSize = new Dimension(cardWidth, cardHeight);
    
    // 1. RIDIMENSIONA GLI SCARTI (JLabel)
    for (Component comp : discardPanel.getComponents()) {
        if (comp instanceof JComponent jc) {
            jc.setPreferredSize(cardSize);
            jc.setMinimumSize(cardSize);
            jc.setMaximumSize(cardSize);
            jc.setFont(new Font("Arial", Font.BOLD, (int)(14 * factor)));
        }
    }

    // 2. RIDIMENSIONA IL MAZZO (Il bottone "DECK")
    // Grazie al metodo getDeckButton() che abbiamo aggiunto in DeckView
    JButton deckBtn = deckView.getDeckButton();
    if (deckBtn != null) {
        deckBtn.setPreferredSize(cardSize);
        deckBtn.setMinimumSize(cardSize);
        deckBtn.setMaximumSize(cardSize);
        // Scaliamo il font "DECK"
        deckBtn.setFont(new Font("Arial", Font.ITALIC, (int)(12 * factor)));
    }

    /* 3. RIDIMENSIONA LE CARTE IN MANO
    // Prendiamo la mano del giocatore corrente
    view.hand.handImpl currentHand = turnoPlayer1 ? initDist.getPlayer1HandView() : initDist.getPlayer2HandView();
    if (currentHand != null) {
        for (Component comp : currentHand.getComponents()) {
            if (comp instanceof JButton btn) {
                btn.setPreferredSize(cardSize);
                btn.setFont(new Font("Arial", Font.BOLD, (int)(14 * factor)));
            }
        }
    }*/

    for(Player p : new Player[]{player1,player2}){
        handImpl hv= getHandViewForPlayer(p);
        if(hv!=null){
            for(Component comp : hv.getComponents()){
                if(comp instanceof JButton btn){
                    btn.setPreferredSize(cardSize);
                    btn.setFont(new Font("Arial", Font.BOLD,(int)(14*factor)));
                }
            }
        }
    }

    // Refresh grafico totale
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

    JPanel newComboPanel = new JPanel();
    newComboPanel.setLayout(new BoxLayout(newComboPanel, BoxLayout.Y_AXIS));

    List<Card> sortedCards = new ArrayList<>(cards);
    sortedCards.sort((c1, c2) -> Integer.compare(c2.getNumericalValue(), c1.getNumericalValue()));

    for(Card c : sortedCards){
        JButton cardBtn = new JButton(c.toString());
        cardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newComboPanel.add(cardBtn);
        newComboPanel.add(Box.createVerticalStrut(5));
    }

    targetPanel.add(newComboPanel);
    targetPanel.revalidate();
    targetPanel.repaint();
    }
}
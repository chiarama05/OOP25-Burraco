package view.table;

import core.discardcard.DiscardManagerImpl;
import core.distributioncard.DistributionManagerImpl;
import core.drawcard.DrawManager;
import core.selectioncard.SelectionCardManager;
import model.card.Card;
import model.deck.DeckImpl;
import model.player.*;
import view.button.*;
import view.discard.DiscardViewImpl;
import view.distribution.InitialDistributionView;
import view.hand.handImpl;
import view.score.ScoreView;
import view.score.ScoreViewImpl;
import view.sound.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final String nameP1;
    private final String nameP2;
    private final SoundController audioController = new SoundControllerImpl();
    private final int winLimit;

    private JButton takeDiscardBtn;
    private DeckController deckController;
    private DiscardController discardController;
    private final JButton putComboBtn;

    public TableViewImpl(PlayerImpl player1, PlayerImpl player2, DeckImpl commonDeck, 
                         SelectionCardManager selectionManager, DrawManager drawManager, 
                         DistributionManagerImpl distManager, String n1, String n2, int winLimit) {

        this.player1 = player1;
        this.player2 = player2;
        this.commonDeck = commonDeck;
        this.selectionManager = selectionManager;
        this.drawManager = drawManager;
        this.winLimit = winLimit;

        this.nameP1 = (n1 == null || n1.isEmpty()) ? "Player 1" : n1;
        this.nameP2 = (n2 == null || n2.isEmpty()) ? "Player 2" : n2;

        frame = new JFrame("Burraco - OOP Project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        this.turnLabel = new JLabel("Turn: " + nameP1); 
        this.turnLabel.setFont(baseTitleFont);
        frame.add(turnLabel, BorderLayout.NORTH);
        
        turnLabel.setFont(baseTitleFont);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
  

        
        JPanel combinationPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        combPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        combPanel1.setBorder(BorderFactory.createTitledBorder(nameP1));
        combinationPanel.add(new JScrollPane(combPanel1));

        combPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        combPanel2.setBorder(BorderFactory.createTitledBorder(nameP2));
        combinationPanel.add(new JScrollPane(combPanel2));

    

        frame.add(combinationPanel, BorderLayout.CENTER);

        
        
        discardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        discardPanel.setBorder(BorderFactory.createTitledBorder("Discard Pile"));
        discardPanel.setBackground(new Color(250, 250, 240));

        deckView = new DeckView();

        JPanel centralBottomPanel = new JPanel(new BorderLayout());
        centralBottomPanel.add(discardPanel, BorderLayout.CENTER);
        centralBottomPanel.add(deckView, BorderLayout.WEST);

        
        
        deckPanel = new JPanel(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder("Hand"));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(centralBottomPanel, BorderLayout.NORTH);
        bottomPanel.add(deckPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        
        this.discardPileModel = new model.discard.DiscardPileImpl();
        this.discardView = new DiscardViewImpl(discardPanel, new JPanel());
        
        initDist = new InitialDistributionView(discardPanel, selectionManager);
        initDist.distribute(player1, player2, distManager,commonDeck, discardView, discardPileModel);


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


    //da mettere nel controller 
    public void wireControllers(final model.turn.TurnManager turnManager, final core.discardcard.DiscardManagerImpl discardManager){
        this.deckController=new DeckController(deckView,drawManager,this,turnManager);
        new TakeDiscardController(takeDiscardBtn, drawManager, this, turnManager, discardPileModel, discardView);
        PutCombinationController putController=new PutCombinationController(this,turnManager,selectionManager, this.drawManager);
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


    public void switchHand(boolean isPlayer1Turn){
    this.turnoPlayer1 = isPlayer1Turn;
    String activeName = isPlayer1Turn ? nameP1 : nameP2;
    String idleName = isPlayer1Turn ? nameP2 : nameP1;

    deckPanel.removeAll();
    //deckPanel.add(new JLabel("Waiting for " + activeName + "...", SwingConstants.CENTER));
    deckPanel.revalidate();
    deckPanel.repaint();

    JOptionPane.showMessageDialog(frame, 
        idleName + ", please leave the seat.\n" + 
        activeName + ", press OK when you are ready to see your cards.", 
        "Change Player", 
        JOptionPane.WARNING_MESSAGE);

    refreshHandPanel(getCurrentPlayer());
    }


    public void refreshTurnLabel(boolean turnoPlayer1) {
        turnLabel.setText("Turn: " + (turnoPlayer1 ? nameP1 : nameP2));
        frame.revalidate();
        frame.repaint();
    }


    public DeckImpl getCommonDeck(){
        return this.commonDeck;
    }
    
    /**
    * Aggiorna il titolo del pannello del giocatore per indicare che ha preso il pozzetto.
    */
    public void markPotTaken(boolean isPlayer1) {
       JPanel targetPanel = isPlayer1 ? combPanel1 : combPanel2;
       String baseName = isPlayer1 ? nameP1 : nameP2;
    
       if (targetPanel.getBorder() instanceof javax.swing.border.TitledBorder tb) {
           tb.setTitle(baseName + " - Pot Taken");
           targetPanel.repaint();
        }
    }

    
    public void resetPlayerTitles() {
      if (combPanel1.getBorder() instanceof javax.swing.border.TitledBorder tb1) {
          tb1.setTitle(nameP1);
        }
      if (combPanel2.getBorder() instanceof javax.swing.border.TitledBorder tb2) {
          tb2.setTitle(nameP2);
        }
        combPanel1.repaint();
        combPanel2.repaint();
    }


    public void showPotFly() {
        JOptionPane.showMessageDialog(frame, "You close your hand on 'fly', you can continue to play in this same turn!");
    }

    public void showPotnextTurn() {
        JOptionPane.showMessageDialog(frame, "You have taken your pot! You can play it in the NEXT turn!");
    }

    public void showNotValideClosure() {
        JOptionPane.showMessageDialog(frame, "You can't discard your last card without even done a Burraco!");
    }

    public SoundController getSoundController() {
        return this.audioController;
    }

    public void startNewRound() {
    // 1. Resetta i dati logici dei giocatori (mani e combinazioni)
    // Assicurati che PlayerImpl abbia il metodo resetForNewRound() visto prima
    player1.resetForNewRound();
    player2.resetForNewRound();

    // 2. Resetta il mazzo e la pila degli scarti
    commonDeck.reset(); 
    discardPileModel.getCards().clear();

    // 3. Pulisci la GUI
    combPanel1.removeAll();
    combPanel2.removeAll();
    discardPanel.removeAll();
    
    // Ripristina i titoli (togliendo "Pot Taken")
    resetPlayerTitles();

    // 4. Ridistribuisci le carte (chiama il manager della distribuzione)
    // Passa i riferimenti corretti che già usi nel costruttore
    initDist.distribute(player1, player2, new DistributionManagerImpl(), commonDeck, discardView, discardPileModel);

    // 5. Reset del turno
    drawManager.resetTurn();
    this.turnoPlayer1 = true; // Ripartiamo dal giocatore 1
    refreshTurnLabel(true);
    refreshHandPanel(player1);

    // 6. Rinfresca tutto il frame
    frame.revalidate();
    frame.repaint();

    JOptionPane.showMessageDialog(frame, "Nuovo Round iniziato!");
}
    public void showWinExit(boolean player1Won) {
        //SUONO VITTORIA
        this.getSoundController().playVictorySound();

        ScoreView scoreScreen = new ScoreViewImpl(player1, player2, nameP1, nameP2, this.winLimit, this);
        scoreScreen.display();
    }


    private void applyResponsiveFonts() {
    int w = Math.max(frame.getWidth(), 1);
    double factor = clamp(w / 1280.0, 0.7, 1.2); 

    turnLabel.setFont(scaleFont(baseTitleFont, factor));

    Font titleFont = scaleFont(baseTitleFont, factor * 0.6);
    setTitledBorderFont(combPanel1, titleFont);
    setTitledBorderFont(combPanel2, titleFont);
    setTitledBorderFont(discardPanel, titleFont);
    setTitledBorderFont(deckPanel, titleFont);

    
    Dimension fixedCardSize = new Dimension(60, 85); 
    
    for (Component comp : discardPanel.getComponents()) {
        if (comp instanceof JComponent jc) {
            jc.setPreferredSize(fixedCardSize);
            jc.setMinimumSize(fixedCardSize);
            jc.setMaximumSize(fixedCardSize);
            jc.setFont(new Font("Monospaced", Font.BOLD, 19));
        }
    }

    JButton deckBtn = deckView.getDeckButton();
    if (deckBtn != null) {
        deckBtn.setPreferredSize(fixedCardSize);
        deckBtn.setMinimumSize(fixedCardSize);
        deckBtn.setMaximumSize(fixedCardSize);
        deckBtn.setFont(new Font("Arial", Font.ITALIC, (int)(12 * factor)));
    }

    for(Player p : new Player[]{player1, player2}){
        handImpl hv = getHandViewForPlayer(p);
        if(hv != null){
            for(Component comp : hv.getComponents()){
                if(comp instanceof JButton btn){
                    btn.setPreferredSize(fixedCardSize);
                    btn.setFont(new Font("Monospaced", Font.BOLD, 19));
                }
            }
        }
    }

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


    public void addCombinationToPlayerPanel(List<Card> cards, boolean player1Turn) {
    JPanel targetPanel = player1Turn ? combPanel1 : combPanel2;

    List<Card> orderedCards = new ArrayList<>(cards);

    // Controlla se c'è un jolly
    int jokerIndex = -1;
    for (int i = 0; i < orderedCards.size(); i++) {
        if (orderedCards.get(i).getValue().equals("Jolly")) {
            jokerIndex = i;
            break;
        }
    }

    if (jokerIndex != -1) {
        boolean jokerAtExtremes =
                jokerIndex == 0 || jokerIndex == orderedCards.size() - 1;

        if (jokerAtExtremes) {
            Card joker = orderedCards.remove(jokerIndex);
            orderedCards.add(joker); // lo spostiamo in fondo
        }
    }

    AttachedButton comboBtn = new AttachedButton(orderedCards, this, player1Turn);

    targetPanel.add(comboBtn);
    targetPanel.revalidate();
    targetPanel.repaint();
    }

    public SelectionCardManager getSelectionManager() {
    return selectionManager;
    }

    public DrawManager getDrawManager() {
    return this.drawManager;
}
}
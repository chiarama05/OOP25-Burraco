package view.table;


import core.discardcard.DiscardManagerImpl;
import core.distributioncard.DistributionManagerImpl;
import core.drawcard.DrawManager;
import core.selectioncard.SelectionCardManager;
import model.card.Card;
import model.deck.DeckImpl;
import model.discard.DiscardPileImpl;
import model.player.*;
import view.button.*;
import view.discard.DiscardViewImpl;
import view.distribution.InitialDistributionView;
import view.hand.handImpl;
import view.score.ScoreView;
import view.score.ScoreViewImpl;
import view.sound.*;
import view.Utils; 

import javax.swing.*;

import controller.RoundController;
import controller.RoundControllerImpl;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class TableViewImpl implements TableView {

    //RIMUOVERE I RIFERIMENTI AI MODEL
    private final DeckImpl commonDeck;
    private final DrawManager drawManager;
    private final DeckView deckView;
    private final InitialDistributionView initDist;
    private final PlayerImpl player1;
    private final PlayerImpl player2;
    private final SelectionCardManager selectionManager;
    private DiscardViewImpl discardView;
    private final model.discard.DiscardPile discardPileModel;
    private final int winLimit;
    private final core.resetround.ResetManager resetManager = new core.resetround.ResetManagerImpl();
    private DeckController deckController;
    private DiscardController discardController;
    private boolean turnoPlayer1 = true; 


    //TENERE I JFRAME E JPANEL
    private final JFrame frame;
    private final JLabel turnLabel;
    private final JPanel combPanel1;
    private final JPanel combPanel2;
    private final JPanel discardPanel;
    private final JPanel deckPanel;
    private final Font baseTitleFont = new Font("Arial", Font.BOLD, 23);
    private final String nameP1;
    private final String nameP2;
    private final SoundController audioController = new SoundControllerImpl(); //va fuori in cartella soundManager
    private JButton takeDiscardBtn;
    private final JButton putComboBtn;


    public TableViewImpl(PlayerImpl player1, PlayerImpl player2, DrawManager drawManager, String n1, String n2, int winLimit) {

        this.player1 = player1;
        this.player2 = player2;
        this.commonDeck = new DeckImpl();
        this.selectionManager = new SelectionCardManager();
        this.drawManager = drawManager;
        this.winLimit = winLimit;
        DistributionManagerImpl distManager = new DistributionManagerImpl();

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
        combPanel1.setBackground(new Color(0, 102, 51));
        combPanel1.setBorder(BorderFactory.createTitledBorder(nameP1));
        combinationPanel.add(new JScrollPane(combPanel1));

        combPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        combPanel2.setBackground(new Color(0, 102, 51));
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








    //nel controller
    public void wireControllers(final model.turn.TurnManager turnManager){

        DiscardManagerImpl discardManager = new DiscardManagerImpl(new DiscardPileImpl());
        this.deckController=new DeckController(deckView,drawManager,this,turnManager);
        new TakeDiscardController(takeDiscardBtn, drawManager, this, turnManager, discardPileModel, discardView);
        
        PutCombinationController putController=new PutCombinationController(this,turnManager,selectionManager, this.drawManager);
        putComboBtn.addActionListener(e -> putController.handlePutCombination());
        this.discardController=new DiscardController(this,turnManager,discardManager,discardView, discardPileModel, drawManager);
    }







    //TENERE
    public void refreshHandPanel(Player player) {
        deckPanel.removeAll();

        handImpl handView=getHandViewForPlayer(player);
        handView.refreshHand(player.getHand());

        deckPanel.add(handView,BorderLayout.CENTER);
        deckPanel.revalidate();
        deckPanel.repaint();
        frame.repaint();
    }





    //VA NEL GAMECONTROLLER
    public handImpl getHandViewForPlayer(Player player){
        return (player==player1) ? initDist.getPlayer1HandView() : initDist.getPlayer2HandView();
    }






    //VA NEL TURNCONTROLLER
    public boolean isPlayer1(Player player){
        return player==player1;
    }





     //VA NEL TURNCONTROLLER
    public Player getCurrentPlayer(){
        return turnoPlayer1 ? player1 : player2;
    }

   




     //VA NEL TURNCONTROLLER
    public void switchHand(boolean isPlayer1Turn){
    this.turnoPlayer1 = isPlayer1Turn;
    String activeName = isPlayer1Turn ? nameP1 : nameP2;
    String idleName = isPlayer1Turn ? nameP2 : nameP1;

    deckPanel.removeAll();
    deckPanel.add(new JLabel("Waiting for " + activeName + "...", SwingConstants.CENTER));
    deckPanel.revalidate();
    deckPanel.repaint();

    JOptionPane.showMessageDialog(frame, 
        idleName + ", please leave the seat.\n" + 
        activeName + ", press OK when you are ready to see your cards.", 
        "Change Player", 
        JOptionPane.WARNING_MESSAGE);

    refreshHandPanel(getCurrentPlayer());
    }







    // TENERE
    public void refreshTurnLabel(boolean turnoPlayer1) {
        turnLabel.setText("Turn: " + (turnoPlayer1 ? nameP1 : nameP2));
        frame.revalidate();
        frame.repaint();
    }






     //VA NEL GAMECONTROLLER
    public DeckImpl getCommonDeck(){
        return this.commonDeck;
    }
    
   




    //TENERE
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








    //TENERE
    public void showPotFly() {
        JOptionPane.showMessageDialog(frame, "You close your hand on 'fly', you can continue to play in this same turn!");
    }

    public void showPotnextTurn() {
        JOptionPane.showMessageDialog(frame, "You have taken your pot! You can play it in the NEXT turn!");
    }

    public void showNotValideClosure() {
        JOptionPane.showMessageDialog(frame, "You can't discard your last card without even done a Burraco!");
    }




    // RIMUOVERE (VA ALTROVE)
    public SoundController getSoundController() {
        return this.audioController;
    }




    // TENERE
    public void startNewRound() {

        combPanel1.removeAll();
        combPanel2.removeAll();
        discardPanel.removeAll();
        resetPlayerTitles();

        frame.revalidate();
        frame.repaint();

        JOptionPane.showMessageDialog(frame, "New Round started!");
    }






    //VA NEL ROUNDCONTROLLER
    public void handleNewRoundRequest() {
    RoundController rc = new RoundControllerImpl(this, resetManager, player1, player2, commonDeck, discardPileModel);
    rc.processNewRound();
    }






     //VA NEL ROUNDCONTROLLER
    public void showWinExit(boolean player1Won) {
        int totalS1 = ((PlayerImpl)player1).getMatchTotalScore();
        int totalS2 = ((PlayerImpl)player2).getMatchTotalScore();

        this.getSoundController().playRoundEndSound();

        ScoreView scoreScreen = new ScoreViewImpl(player1, player2, nameP1, nameP2, this.winLimit, this);
        scoreScreen.display();
    }






    //TENERE
    private void applyResponsiveFonts() {
    int w = Math.max(frame.getWidth(), 1);
    double factor = Utils.clamp(w / 1280.0, 0.7, 1.2); 

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






// TENERE
private void setTitledBorderFont(final JComponent comp, final Font font) {
        if (comp.getBorder() instanceof javax.swing.border.TitledBorder tb) {
            tb.setTitleFont(font);
            comp.repaint();
        }
    }





// TENERE
private Font scaleFont(final Font base, double factor) {
        int newSize = (int) Math.round(base.getSize2D() * factor);
        newSize = Math.max(10, Math.min(newSize, 28));
        return base.deriveFont((float) newSize);
    }


    


// TENERE
public void addCombinationToPlayerPanel(List<Card> cards, boolean player1Turn) {
    JPanel targetPanel = player1Turn ? combPanel1 : combPanel2;
    List<Card> preparedCards = core.combination.CombinationManager.prepareForDisplay(cards);
    AttachedButton comboBtn = new AttachedButton(preparedCards, this, player1Turn);
    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setOpaque(false); 
    wrapper.add(comboBtn, BorderLayout.NORTH);

    targetPanel.add(wrapper);
    targetPanel.revalidate();
    targetPanel.repaint();
    }






    // RIMUOVERE 
    public SelectionCardManager getSelectionManager() {
    return this.selectionManager;
    }


    // RIMOSSO VA NEL CORE
    public DrawManager getDrawManager() {
    return this.drawManager;
}




@Override
public JPanel getDiscardPanel() {
    return this.discardPanel;
}

@Override
public view.discard.DiscardViewImpl getDiscardView() {
    return this.discardView;
}

public InitialDistributionView getInitDist() {
    return this.initDist;
}
}
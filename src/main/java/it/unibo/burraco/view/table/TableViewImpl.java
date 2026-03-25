package it.unibo.burraco.view.table;

import it.unibo.burraco.controller.closure.ClosureManager;
import it.unibo.burraco.controller.combination.PutCombinationController;
import it.unibo.burraco.controller.discardcard.DiscardController;
import it.unibo.burraco.controller.discardcard.DiscardManagerImpl;
import it.unibo.burraco.controller.discardcard.TakeDiscardController;
import it.unibo.burraco.controller.drawcard.DrawManager;
import it.unibo.burraco.controller.game.GameController;
import it.unibo.burraco.controller.pot.PotManager;
import it.unibo.burraco.controller.score.ScoreController;
import it.unibo.burraco.controller.selectioncard.SelectionCardManager;
import it.unibo.burraco.controller.sound.SoundController;
import it.unibo.burraco.controller.turn.TurnController;
import it.unibo.burraco.model.card.Card;
import it.unibo.burraco.model.player.*;
import it.unibo.burraco.view.attach.AttachButton;
import it.unibo.burraco.view.combination.PutCombinationButton;
import it.unibo.burraco.view.deck.DeckButton;
import it.unibo.burraco.view.deck.DeckView;
import it.unibo.burraco.view.discard.DiscardButton;
import it.unibo.burraco.view.discard.DiscardViewImpl;
import it.unibo.burraco.view.discard.TakeDiscardButton;
import it.unibo.burraco.view.discard.TakeDiscardView;
import it.unibo.burraco.view.distribution.InitialDistributionView;
import it.unibo.burraco.view.hand.HandViewImpl;
import it.unibo.burraco.view.notification.GameNotifier;
import it.unibo.burraco.view.notification.GameNotifierImpl;
import it.unibo.burraco.view.pot.PotView;
import it.unibo.burraco.view.selection.SelectionView;
import it.unibo.burraco.model.score.ScoreImpl;
import it.unibo.burraco.model.score.Score;



import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TableViewImpl implements TableView, PotView, SelectionView, TakeDiscardView {
    private final JFrame frame;
    private final JLabel turnLabel;
    private final JPanel combPanel1, combPanel2, discardPanel, deckPanel;
    private final PlayerImpl player1, player2;
    private final String nameP1, nameP2;
    private final InitialDistributionView initDist;
    private final DiscardViewImpl discardView;
    private DrawManager drawManager;
    private it.unibo.burraco.controller.game.GameController gameController;
    private final DeckView deckView; 
    private final JButton takeDiscardBtn; 
    private final JButton putComboBtn; 
    private final ControlPanelView sideControlPanel; 
    private final BoardView boardView;
    private final PlayerAreaView playerArea;
    private int targetScore;
    private ClosureManager closureManager;
    private PotManager potManager;
    private final SoundController soundController;
    private ScoreController scoreController;

    public TableViewImpl(PlayerImpl p1, PlayerImpl p2, String n1, String n2, SoundController sc) {
        this.player1 = p1; 
        this.player2 = p2;

       this.soundController = sc;
       
    this.nameP1 = (n1 == null || n1.isEmpty()) ? "Player 1" : n1;
    this.nameP2 = (n2 == null || n2.isEmpty()) ? "Player 2" : n2;

    // --- CONFIGURAZIONE BASE ---
    frame = new JFrame("Burraco - OOP Project");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    Color lightgreen = new Color(180, 220, 180); 
    frame.getContentPane().setBackground(lightgreen);

    // 1. NORD
    this.turnLabel = new JLabel("Turn: " + nameP1); 
    this.turnLabel.setFont(new Font("Arial", Font.BOLD, 25));
    frame.add(turnLabel, BorderLayout.NORTH);

    // 2. CENTRO
    this.boardView = new BoardView(nameP1, nameP2, lightgreen);
    this.combPanel1 = boardView.getCombPanel1();
    this.combPanel2 = boardView.getCombPanel2();
    frame.add(boardView, BorderLayout.CENTER);

    // 3. SUD
    this.discardPanel = new JPanel();

    JScrollPane discardScroll = new JScrollPane(discardPanel);
    discardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    discardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    discardScroll.setPreferredSize(new Dimension(400, 110)); 
    discardScroll.setBorder(null);

    this.deckView = new DeckView();
    this.deckPanel = new JPanel(new BorderLayout());
    this.deckPanel.setBackground(lightgreen);
    
    this.playerArea = new PlayerAreaView(discardScroll, deckView, deckPanel, lightgreen);
    frame.add(playerArea, BorderLayout.SOUTH);

    // 4. EST: Bottoni
    this.takeDiscardBtn = new JButton("TAKE DISCARD");
    this.putComboBtn = new JButton("PUT COMBINATION");
    this.discardView = new DiscardViewImpl(discardPanel, new JPanel());
    JButton discardBtn = (JButton) discardView.getActionPanel().getComponent(0);
    discardBtn.setText("DISCARD");

    this.sideControlPanel = new ControlPanelView(takeDiscardBtn, putComboBtn, discardBtn, lightgreen);
    frame.add(sideControlPanel, BorderLayout.EAST);


    this.initDist = new InitialDistributionView(discardPanel, new SelectionCardManager());
    
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setVisible(true);
    }

   @Override
    public void wireControllers(it.unibo.burraco.model.turn.Turn turnModel) {
    GameNotifier notifier = new GameNotifierImpl(frame);

    this.gameController = new GameController(player1, player2, turnModel, this.soundController);
    this.drawManager = gameController.getDrawManager();

    TurnController turnCtrl = new TurnController(turnModel, drawManager);
    this.potManager = new PotManager(turnModel, this);

    Score score = new ScoreImpl();
    this.scoreController = new ScoreController(
        score,
        player1,
        player2,
        nameP1,
        nameP2,
        this,
        this.gameController,
        this.soundController,
        this.targetScore
    );

    this.closureManager = new ClosureManager(turnModel, notifier, this.targetScore, this.scoreController);

    DiscardController discardCoreLogic = new DiscardController(
        new DiscardManagerImpl(gameController.getDiscardPile()),
        turnCtrl,
        this.potManager,
        this.closureManager,
        drawManager,
        turnModel);

    turnCtrl.setOnTurnChangedListener(() -> {
        refreshTurnLabel(turnModel.isPlayer1Turn());
        switchHand(turnModel.isPlayer1Turn());
    });

    new DiscardButton(this, discardView, notifier, discardCoreLogic);

    PutCombinationController putCombinationCtrl = new PutCombinationController(
        gameController,
        drawManager,
        this.potManager,
        this.closureManager,
        turnModel);

    PutCombinationButton putCombinationLogic = new PutCombinationButton(
        this,
        gameController,
        putCombinationCtrl);

    putComboBtn.addActionListener(e -> putCombinationLogic.handlePutCombination());

    new DeckButton(deckView, drawManager, this, gameController);

    TakeDiscardController takeDiscardCtrl = new TakeDiscardController(
        drawManager,
        turnModel,
        gameController.getDiscardPile());

    new TakeDiscardButton(
    takeDiscardBtn,
    takeDiscardCtrl,
    this,          
    turnModel,
    gameController.getDiscardPile()
    );
    }



    @Override 
    public void refreshHandPanel(Player p) {
        deckPanel.removeAll();

        deckPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 1), "Hand", 0, 0, new Font("Arial", Font.BOLD, 18), Color.BLACK));

        HandViewImpl hv = getHandViewForPlayer(p);
        hv.refreshHand(p.getHand());

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

    @Override 
    public void refreshTurnLabel(boolean isP1) { 
        turnLabel.setText("Turn: " + (isP1 ? nameP1 : nameP2)); 
    }
    
    @Override 
    public void markPotTaken(boolean isP1) {
        ((javax.swing.border.TitledBorder)(isP1 ? combPanel1 : combPanel2).getBorder()).setTitle((isP1 ? nameP1 : nameP2) + " [POT TAKEN]");
        frame.repaint();
    }

    @Override 
    public void addCombinationToPlayerPanel(List<Card> cards, boolean isP1) {
        (isP1 ? combPanel1 : combPanel2).add(new AttachButton(cards, this, gameController, isP1, this.closureManager,this.potManager));
        frame.revalidate();
        frame.repaint();
    }

    @Override public void switchHand(boolean isP1) { 
        deckPanel.removeAll();
        deckPanel.add(new JLabel("Shift turn in progress...", SwingConstants.CENTER));
        deckPanel.revalidate();
        deckPanel.repaint();

        String activeName = isP1 ? nameP1 : nameP2;
        String idleName = isP1 ? nameP2 : nameP1;

        JOptionPane.showMessageDialog(frame, 
        idleName + ", turn ended.\n\n" +
        "Hand the turn over to " + activeName + ".\n" +
        activeName + ", Press OK when you are ready to see your cards.", 
        "Turn Privacy", 
        JOptionPane.INFORMATION_MESSAGE);

   
        Player current = isP1 ? player1 : player2;
        refreshHandPanel(current);
    }


    @Override public HandViewImpl getHandViewForPlayer(Player p) { 
        return (p == player1) ? initDist.getPlayer1HandView() : initDist.getPlayer2HandView(); 
    }

    @Override public DiscardViewImpl getDiscardView() { 
        return discardView; 
    }

    @Override public JPanel getDiscardPanel() { 
        return discardPanel; 
    }

    @Override public void startNewRound() { 
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
        JOptionPane.showMessageDialog(
            frame,         
            message, 
            title, 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void repaintTable() {
        discardPanel.revalidate();
        discardPanel.repaint();
        Window w = SwingUtilities.getWindowAncestor(discardPanel);
        if (w != null) {
            w.revalidate();
            w.repaint();
        }
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
    public HandViewImpl getCurrentHandView() {
        Player current = gameController.getCurrentPlayer();
        return getHandViewForPlayer(current);
    }

    public DrawManager getDrawManager() {
        return this.drawManager;
    }

    public InitialDistributionView getInitDist() {
        return this.initDist;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public void setTargetScore(int score) {
        this.targetScore = score;
    }
}
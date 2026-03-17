package view.table;

import core.drawcard.DrawManager;
import core.selectioncard.SelectionCardManager;
import model.card.Card;
import model.player.*;
import view.button.*;
import view.discard.DiscardViewImpl;
import view.distribution.InitialDistributionView;
import view.hand.handImpl;
import view.notification.GameNotifier;
import view.notification.GameNotifierImpl;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TableViewImpl implements TableView {
    private final JFrame frame;
    private final JLabel turnLabel;
    private final JPanel combPanel1, combPanel2, discardPanel, deckPanel;
    private final PlayerImpl player1, player2;
    private final String nameP1, nameP2;
    private final InitialDistributionView initDist;
    private final DiscardViewImpl discardView;
    private DrawManager drawManager;
    private view.controller.GameController gameController;
    private final DeckView deckView; 
    private final JButton takeDiscardBtn; 
    private final JButton putComboBtn; 
    private final JPanel rightPanel;
    private int targetScore;

    public TableViewImpl(PlayerImpl p1, PlayerImpl p2, String n1, String n2) {
        this.player1 = p1; 
    this.player2 = p2;
    this.nameP1 = (n1 == null || n1.isEmpty()) ? "Player 1" : n1;
    this.nameP2 = (n2 == null || n2.isEmpty()) ? "Player 2" : n2;

    // --- CONFIGURAZIONE BASE ---
    frame = new JFrame("Burraco - OOP Project");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());


    Font font = new Font("Arial", Font.BOLD, 20);
    Font fontTurn = new Font("Arial", Font.BOLD, 25);
    Color lightgreen = new Color(180, 220, 180); 

    frame.getContentPane().setBackground(lightgreen);

    // --- NORD: Turno ---
    this.turnLabel = new JLabel("Turn: " + nameP1); 
    this.turnLabel.setFont(fontTurn);
    this.turnLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    frame.add(turnLabel, BorderLayout.NORTH);

    // --- CENTRO: Pannelli Combinazioni ---
    JPanel combinationPanel = new JPanel(new GridLayout(1, 2, 20, 10));
    combinationPanel.setBackground(lightgreen); 
    
    combPanel1 = createSection(nameP1);
    combPanel2 = createSection(nameP2);

    
    JScrollPane scroll1 = new JScrollPane(combPanel1);
    JScrollPane scroll2 = new JScrollPane(combPanel2);
    
    for (JScrollPane s : new JScrollPane[]{scroll1, scroll2}) {
        s.setBorder(BorderFactory.createEmptyBorder()); 
        s.getViewport().setBackground(lightgreen); 
        s.setBackground(lightgreen);              
    }

    combinationPanel.add(scroll1);
    combinationPanel.add(scroll2);
    frame.add(combinationPanel, BorderLayout.CENTER);

    this.discardPanel = new JPanel();

    // --- SUD: Mazzo, Scarti e Mano ---
    this.deckView = new DeckView();
    this.deckView.getDeckButton().setPreferredSize(new Dimension(70, 100));
    this.deckView.setBackground(lightgreen);


    JPanel centralBottomPanel = new JPanel(new BorderLayout());
    centralBottomPanel.setBackground(lightgreen);
    centralBottomPanel.add(discardPanel, BorderLayout.CENTER);
    centralBottomPanel.add(deckView, BorderLayout.WEST);

    deckPanel = new JPanel(new BorderLayout());
    deckPanel.setBackground(lightgreen);
    deckPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.WHITE, 1), "Hand", 0, 0, font, Color.BLACK));

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBackground(lightgreen);
    bottomPanel.add(centralBottomPanel, BorderLayout.NORTH);
    bottomPanel.add(deckPanel, BorderLayout.CENTER);
    
    frame.add(bottomPanel, BorderLayout.SOUTH);

    // --- EST: Barra Laterale (Bottoni) ---
    rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setPreferredSize(new Dimension(180, 400));
    rightPanel.setBackground(lightgreen); 
    
    this.takeDiscardBtn = new JButton("TAKE DISCARD");
    this.putComboBtn = new JButton("PUT COMBINATION");

    this.discardView = new DiscardViewImpl(discardPanel, new JPanel());


    JButton discardBtn = (JButton) discardView.getActionPanel().getComponent(0);
    discardBtn.setText("DISCARD");

    this.initDist = new InitialDistributionView(discardPanel, new SelectionCardManager());
    
    Color pinkUp = new Color(255, 245, 250); 
    Color pinkDown = new Color(255, 220, 235);
    for (JButton b : new JButton[]{takeDiscardBtn, putComboBtn, discardBtn}) {
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setMaximumSize(new Dimension(170, 45));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createLineBorder(new Color(230, 200, 215), 1));
        b.setContentAreaFilled(false);

        final boolean[] isHovered = {false};
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Se il mouse è sopra, invertiamo o scuriamo i colori
            Color c1 = isHovered[0] ? pinkDown : pinkUp; 
            Color c2 = isHovered[0] ? new Color(255, 180, 200) : pinkDown;
            
            GradientPaint gp = new GradientPaint(0, 0, c1, 0, c.getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, c.getWidth(), c.getHeight()); 
            g2.dispose();
            super.paint(g, c);
            }
        });
        b.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
            isHovered[0] = true;
            b.repaint();
        }
        public void mouseExited(java.awt.event.MouseEvent e) {
            isHovered[0] = false;
            b.repaint();
        }
        });

        rightPanel.add(b);
        rightPanel.add(Box.createVerticalStrut(10));
    }
    frame.add(rightPanel, BorderLayout.EAST);

    // AVVIO
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setMinimumSize(new Dimension(900, 600));
    frame.setVisible(true);
    }

    private JPanel createSection(String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(new Color(0, 102, 51));
        p.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.WHITE), 
        title, 0, 0, 
        new Font("Arial", Font.BOLD, 20), Color.WHITE));
        return p;
    }

    @Override
    public void wireControllers(model.turn.Turn turnModel) {
        GameNotifier notifier = new GameNotifierImpl(frame);
        
        this.gameController = new view.controller.GameController(player1, player2, turnModel);
        this.drawManager = gameController.getDrawManager(); 
    

    core.turn.TurnController turnCtrl = new core.turn.TurnController(turnModel, this, drawManager);
    core.pot.PotManager potCtrl = new core.pot.PotManager(turnModel, this);
    core.closure.ClosureManager closureCtrl = new core.closure.ClosureManager(turnModel, this, notifier, this.targetScore);

    new view.button.DeckController(deckView, drawManager, this, gameController);

    new view.button.TakeDiscardController(
        takeDiscardBtn, 
        drawManager, 
        this, 
        turnModel, 
        gameController.getDiscardPile(), 
        discardView
    );

    PutCombinationController putComboCtrl = new PutCombinationController(
        this, 
        gameController, 
        drawManager,
        potCtrl
    );
    putComboBtn.addActionListener(e -> putComboCtrl.handlePutCombination());
    
    
    new DiscardController(
        this, turnModel, 
        new core.discardcard.DiscardManagerImpl(gameController.getDiscardPile()), 
        discardView, 
        gameController.getDiscardPile(), 
        drawManager, 
        turnCtrl, potCtrl, closureCtrl, notifier
    );
    }

    @Override 
    public void refreshHandPanel(Player p) {
        deckPanel.removeAll();
        handImpl hv = getHandViewForPlayer(p);
        hv.refreshHand(p.getHand());
        deckPanel.add(hv);
        deckPanel.revalidate(); deckPanel.repaint();
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
        (isP1 ? combPanel1 : combPanel2).add(new AttachedButton(cards, this, gameController, isP1));
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


    @Override public handImpl getHandViewForPlayer(Player p) { 
        return (p == player1) ? initDist.getPlayer1HandView() : initDist.getPlayer2HandView(); 
    }

    @Override public DiscardViewImpl getDiscardView() { 
        return discardView; 
    }

    @Override public JPanel getDiscardPanel() { 
        return discardPanel; 
    }

    @Override public void startNewRound() { 
        combPanel1.removeAll(); combPanel2.removeAll(); discardPanel.removeAll(); frame.repaint(); 
    }

    public DrawManager getDrawManager() {
        return this.drawManager;
    }

    public InitialDistributionView getInitDist() {
        return this.initDist;
    }

    public view.controller.GameController getGameController() {
        return this.gameController;
    }

    public void setTargetScore(int score) {
        this.targetScore = score;
    }
}
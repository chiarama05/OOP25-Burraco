package view.table;

import javax.swing.*;

import core.distributioncard.DistributionManagerImpl;
import model.player.Player;
import model.player.PlayerImpl;
import view.discard.DiscardViewImpl;
import view.distribution.InitialDistributionView;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TableViewImpl implements TableView{
    
    private final JFrame frame;
    private final JLabel turnLabel;

    private final JPanel combPanel1;//
    private final JPanel combPanel2;//
    private final JPanel discardPanel;//
    private final JPanel deckPanel;//

    private final Font baseTitleFont= new Font("Arial", Font.BOLD, 32);
    private final Font baseLabelFont = new Font("Arial", Font.BOLD, 22);

    private final InitialDistributionView initDist;
    private final PlayerImpl player1;
    private final PlayerImpl player2;

    public TableViewImpl(){
        frame= new JFrame ("Burraco - OOOP Project");
        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        frame.setLayout(new BorderLayout());

        //turno
        turnLabel = new JLabel("Turn: Player 1");
        turnLabel.setFont(baseTitleFont);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        frame.add(turnLabel,BorderLayout.NORTH);

        //combinazioni
        JPanel combinationPanel = new JPanel(new GridLayout(1,2,20,10));

        combPanel1=new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        combPanel1.setBorder(BorderFactory.createTitledBorder("Player 1"));

        JScrollPane scroll1=new JScrollPane(combPanel1);
        scroll1.setBorder(BorderFactory.createEmptyBorder());
        combinationPanel.add(scroll1);

        combPanel2=new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        combPanel2.setBorder(BorderFactory.createTitledBorder("Player 2"));
        JScrollPane scroll2 = new JScrollPane(combPanel2);
        scroll2.setBorder(BorderFactory.createEmptyBorder());
        combinationPanel.add(scroll2);

        frame.add(combinationPanel, BorderLayout.CENTER);

        //scarti
        discardPanel=new JPanel (new FlowLayout(FlowLayout.LEFT,5,5));
        discardPanel.setBorder(BorderFactory.createTitledBorder("Discard Pile"));
        discardPanel.setBackground(new Color (250,250,240));


        //mano
        deckPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        deckPanel.setBorder(BorderFactory.createTitledBorder("Deck"));


        //contenitore inferiore
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(discardPanel,BorderLayout.NORTH);
        bottomPanel.add(deckPanel,BorderLayout.CENTER);
        frame.add(bottomPanel,BorderLayout.SOUTH);

        // --- DISTRIBUZIONE INIZIALE DELLE CARTE ---

        player1 = new PlayerImpl();
        player2 = new PlayerImpl();
        DistributionManagerImpl distManager = new DistributionManagerImpl();
        initDist = new InitialDistributionView(discardPanel);

        JPanel handsPanel = initDist.getHandsPanel();
        frame.add(handsPanel, BorderLayout.WEST);

        initDist.distribute(player1, player2, distManager);

        // Create the right-side panel for all action buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); // Vertical stacking of buttons
        rightPanel.setPreferredSize(new Dimension(180, 400)); // Fixed width for the sidebar


        // Initialize the DiscardView and retrieve its discard button
        DiscardViewImpl discardView = new DiscardViewImpl(discardPanel, new JPanel());
        JButton discardBtn = (JButton) discardView.getActionPanel().getComponent(0); // Access the discard button

        // Create additional action buttons
        JButton drawDeckBtn = new JButton("Draw from deck");
        JButton drawDiscardBtn = new JButton("Take discard");
        JButton putComboBtn = new JButton("Put combionation");

        // Add all action buttons to the right panel
        // Buttons are centered and constrained to a uniform size
        // Vertical spacing is added between buttons
        for (JButton b : new JButton[]{drawDeckBtn, drawDiscardBtn, putComboBtn, discardBtn}) {
           b.setAlignmentX(Component.CENTER_ALIGNMENT);
           b.setMaximumSize(new Dimension(160, 40));
           rightPanel.add(b);
           rightPanel.add(Box.createVerticalStrut(10));
        } 

        // Add the completed right-side panel to the frame
        frame.add(rightPanel, BorderLayout.EAST);


        //resize-responsive
        frame.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e ){
                applyResponsiveFonts();
            }
        });

        //mostra ridimensionata
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(900,600));
        frame.pack();
        frame.setVisible(true);
        applyResponsiveFonts(); 
    }


    //gestione giocatori
    public void refreshTurnLabel(boolean turnoGiocatore1){
        turnLabel.setText("Turn: "+(turnoGiocatore1 ? "Player 1" : "Player 2"));
        frame.revalidate();
        frame.repaint();
    }


    //gestione pozzetti
    public void showPotFly(){
        JOptionPane.showMessageDialog(frame,"You close your hand on 'fly', you can continue to play in this same turn!");
    }

    public void showPotnextTurn(){
        JOptionPane.showMessageDialog(frame, "You can take your pot! You can play it in the NEXT turn!");
    }

    public void showNotValideClosure(){
        JOptionPane.showMessageDialog(frame,"You can't discard your last card without even done a Burraco!");
    }

    public void showWinExit(boolean player1Won){
        JOptionPane.showMessageDialog(frame, "You can exit now! the winner is... " + (player1Won ? "Player 1" : "Player 2"));
        frame.dispose();
        System.exit(0);
    }

    private void applyResponsiveFonts(){
        int w = Math.max(frame.getWidth(),1);
        double factor = clamp(w / 1280.0,0.85,1.4);

        turnLabel.setFont(scaleFont(baseTitleFont,factor));

        setTitledBorderFont(combPanel1,scaleFont(baseTitleFont,factor*0.95));
        setTitledBorderFont(combPanel2,scaleFont(baseTitleFont,factor * 0.95));
        setTitledBorderFont(discardPanel,scaleFont(baseTitleFont,factor*0.9));
        setTitledBorderFont(deckPanel,scaleFont(baseTitleFont,factor*0.9));

        frame.revalidate();
        frame.repaint();
    }


    private void setTitledBorderFont(final JComponent comp, final Font font){
        if(comp.getBorder() instanceof javax.swing.border.TitledBorder tb){
            tb.setTitleFont(font);
            comp.repaint();
        }
    }

    private Font scaleFont(final Font base,double factor){ 
        int newSize=(int) Math.round(base.getSize2D() * factor);
        newSize = Math.max(10,Math.min(newSize,28));
        return base.deriveFont((float) newSize);
    }

    private double clamp (double b, double min, double max){
        return Math.max(min,Math.min(max,b)); 
    }

    public void initGame() {
    PlayerImpl player1 = new PlayerImpl();
    PlayerImpl player2 = new PlayerImpl();
    DistributionManagerImpl distManager = new DistributionManagerImpl();
    
    JPanel handsPanel = initDist.getHandsPanel();
    frame.add(handsPanel, BorderLayout.NORTH); // sopra combinazioni

    initDist.distribute(player1, player2, distManager);

    frame.revalidate();
    frame.repaint();
}
}
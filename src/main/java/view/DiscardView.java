package view;

import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import core.discardcard.DiscardManager;
import model.card.Card;
import player.Player;

/**
 * GUI component for handling only the discard pile in Burraco.
 * Handles selecting a card and discarding it.
 */
public class DiscardView {

    private JFrame frame;
    private JPanel discardPanel, handPanel;
    private JButton discardButton;
    private JLabel turnLabel;

    private DiscardManager discardManager;
    private Player currentPlayer;

    private Set<Card> selectedCards = new HashSet<>();

    public DiscardView(DiscardManager discardManager, Player currentPlayer) {
        this.discardManager = discardManager;
        this.currentPlayer = currentPlayer;

        setupGUI();
    }

    private void setupGUI(){
        frame = new JFrame("Discard Pile");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Turn label
        turnLabel = new JLabel("Select a card to discard");
        frame.add(turnLabel, BorderLayout.NORTH);

        // Panels
        handPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        handPanel.setBorder(BorderFactory.createTitledBorder("Mano"));
        frame.add(handPanel, BorderLayout.CENTER);

        discardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        discardPanel.setBorder(BorderFactory.createTitledBorder("Scarti"));
        frame.add(discardPanel, BorderLayout.SOUTH);

        // Scarta button
        discardButton = new JButton("Scarta");
        discardButton.addActionListener(e -> discardCard());
        frame.add(discardButton, BorderLayout.EAST);

        updateGUI();
        frame.setVisible(true);
    }
}

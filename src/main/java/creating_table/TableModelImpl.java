package creating_table;

import java.util.*;

import card.*;
import player.*;

public class TableModelImpl{

    private Player player1;
    private Player player2;
    private Deck deck;
    private List<Card> discardPile;

    private boolean player1Turn = true;
    private boolean haspicked = false;

    
    public TableModelImpl() {
        deck = new DeckImpl();
        discardPile = new ArrayList<>();
        player1 = new PlayerImpl();
        player2 = new PlayerImpl();
    }
}

   
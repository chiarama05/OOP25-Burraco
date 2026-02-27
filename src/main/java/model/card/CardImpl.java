package model.card;

// The CardImpl class is a concrete implementation of the Card interface.
public class CardImpl implements Card{
    private String seed;
    private String value;

     /**
     * Constructs a CardImpl with the specified seed and value.
     *
     * @param seed  the seed (suit) of the card
     * @param value the face value of the card
     */
    public CardImpl(String seed, String value){
        this.seed=seed;
        this.value=value;
    }

    @Override
    public String getSeed(){
        return this.seed;
    }

    @Override
    public String getValue(){
        return this.value;
    }

    /**
     * Returns the numerical value associated with the card value.
     * Face cards are mapped as follows:
     * A = 1, J = 11, Q = 12, K = 13.
     * "Jolly" is mapped to 0.
     * Returns -1 if the value is not recognized.
     *
     * @return the numerical value of the card
     */
    @Override
    public int getNumericalValue(){
        switch(value){
            case "A": return 1;
            case "2": return 2; //jolly
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "J": return 11;
            case "Q": return 12;
            case "K": return 13;
            case "Jolly": return 0; //pure jolly 
        }
        return -1;
    }


}

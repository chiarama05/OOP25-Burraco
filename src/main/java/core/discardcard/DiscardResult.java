package core.discardcard;

/**
 * Represents the result of a discard action.
 * This object allows the GUI to react without embedding
 * game logic inside the view layer.
 */
public class DiscardResult {

    private final boolean valid;
    private final boolean turnEnds;
    private final boolean gameWon;
    private final String message;
    
    /**
     * Creates a result of a discard operation.
     *
     * @param valid whether the discard was valid
     * @param turnEnds whether the turn should change
     * @param gameWon whether the discard caused a win
     * @param message message describing the outcome
     */
    public DiscardResult(boolean valid, boolean turnEnds, boolean gameWon, String message) {
        this.valid = valid;
        this.turnEnds = turnEnds;
        this.gameWon = gameWon;
        this.message = message;
    }
    public boolean isValid() { return valid; }
    public boolean isTurnEnds() { return turnEnds; }
    public boolean isGameWon() { return gameWon; }
    public String getMessage() { return message; }
}

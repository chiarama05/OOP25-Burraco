package core.turnvalidation;

public class TurnPlayOutcome {
    private final boolean allowed;
    private final boolean triggersPotFly;
    private final String message;

    private TurnPlayOutcome(boolean allowed, boolean triggersPotFly, String message) {
        this.allowed = allowed;
        this.triggersPotFly = triggersPotFly;
        this.message = message;
    }

    public static TurnPlayOutcome allow() {
        return new TurnPlayOutcome(true, false, null);
    }

    public static TurnPlayOutcome allowWithPotFly() {
        return new TurnPlayOutcome(true, true, null);
    }

    public static TurnPlayOutcome deny(String message) {
        return new TurnPlayOutcome(false, false, message);
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    public boolean triggersPotFly() {
        return this.triggersPotFly;
    }

    public String getMessage() {
        return this.message;
    }
}
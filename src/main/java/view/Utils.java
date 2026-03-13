package view;

public class Utils {
    
    private Utils() {}

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}

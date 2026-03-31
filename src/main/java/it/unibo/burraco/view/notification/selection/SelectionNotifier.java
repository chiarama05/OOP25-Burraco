package it.unibo.burraco.view.notification.selection;

/**
 * Interface responsible for notifying the user about errors related 
 * to card selection or combination validation before making a move.
 */
@FunctionalInterface
public interface SelectionNotifier {

    /**
     * Notifies the user of an error in the current selection.
     * 
     * @param errorCode a string representing the type of selection error
     */
    void notifySelectionError(String errorCode);

}

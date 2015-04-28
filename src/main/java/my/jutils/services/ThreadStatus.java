package my.jutils.services;

/**
 * List of status in creating new threads.
 *
 * @author Erieze and Einar Lagera
 */
public enum ThreadStatus {

    /**
     * Submitted thread initialization is still in progress.
     */
    Waiting,
    /**
     * Submitted thread initialization is executed.
     */
    Executed
}

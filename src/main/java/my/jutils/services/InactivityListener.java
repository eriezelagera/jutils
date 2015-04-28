package my.jutils.services;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.*;
import javax.swing.*;

/**
 * A class that monitors inactivity in an application.
 * <p>
 *
 * It does this by using a Swing Timer and by listening for specified AWT
 * events. When an event is received the Timer is restarted. If no event is
 * received during the specified time interval then the timer will fire and
 * invoke the specified Action.
 * <br /> <br />
 * When creating the listener the inactivity interval is specified in minutes.
 * However, once the listener has been created you can reset this value in
 * milliseconds if you need to.
 * <br /> <br />
 * Some common event masks have be defined with the class:
 * <blockquote>
 * KEY_EVENTS MOUSE_EVENTS - which includes mouse motion events USER_EVENTS -
 * includes KEY_EVENTS and MOUSE_EVENT (this is the default)
 * </blockquote>
 * <br />
 * The inactivity interval and event mask can be changed at any time, however,
 * they will not become effective until you stop and start the listener.
 * <br /> <br />
 * Before creating instance of this class, you should have an AbstractAction
 * first, which will be executed by this listener after the given time interval.
 * <br /> <br />
 * Source:
 * <i>http://tips4java.wordpress.com/2008/10/24/application-inactivity/</i>
 *
 * @author JavaTips
 */
public final class InactivityListener implements ActionListener, AWTEventListener {

    public final static long KEY_EVENTS = AWTEvent.KEY_EVENT_MASK;

    public final static long MOUSE_EVENTS = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;

    public final static long USER_EVENTS = KEY_EVENTS + MOUSE_EVENTS;

    private Window window;
    private Action action;
    private final AtomicInteger interval;
    private final AtomicLong eventMask;
    private final Timer timer = new Timer(0, this);

    /**
     * Specify the inactivity interval and listen for USER_EVENTS.
     * <p>
     * This will automatically execute the specified action after the given
     * interval.
     *
     * @param window Frame from which action will be executed
     * @param action Action to be executed after the given interval
     * @param seconds Time interval before executing the action
     */
    public InactivityListener(Window window, Action action, int seconds) {
        this(window, action, seconds, USER_EVENTS);
    }

    /**
     * Use a default inactivity interval of 1 minute and listen for USER_EVENTS.
     * <p>
     * This will automatically execute the specified action after the given
     * interval.
     *
     * @param window Frame from which action will be executed
     * @param action Action to be executed after the given interval
     */
    public InactivityListener(Window window, Action action) {
        this(window, action, 60);
    }

    /**
     * Specify the inactivity interval and listen for the specified Event.
     * <p>
     * This will automatically execute the specified action after the given
     * interval.
     *
     * @param window Frame from which action will be executed
     * @param action Action to be executed after the given interval
     * @param seconds Time interval before executing the action
     * @param eventMask Custom event listener
     */
    public InactivityListener(Window window, Action action, int seconds, long eventMask) {
        this.window = window;
        this.interval = new AtomicInteger();
        this.eventMask = new AtomicLong();
        setAction(action);
        setInterval(seconds);
        setEventMask(eventMask);
    }

    /**
     * The Action to be invoked after the specified inactivity period.
     *
     * @param action Action to be executed after the given interval
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * The interval before the Action is invoked specified in minutes.
     *
     * @param seconds Time interval before executing the action
     */
    public void setInterval(final int seconds) {
        setIntervalInMillis(seconds * 1000);
    }

    /**
     * The interval before the Action is invoked specified in milliseconds.
     *
     * @param interval Time interval in millis before executing the action
     */
    public void setIntervalInMillis(final int interval) {
        this.interval.set(interval);
        timer.setInitialDelay(interval);
    }

    /**
     * A mask specifying the events to be passed to the AWTEventListener
     *
     * @param eventMask
     */
    public void setEventMask(long eventMask) {
        this.eventMask.set(eventMask);
    }

    /**
     * Start listening for events.
     */
    public void start() {
        timer.setInitialDelay(interval.get());
        timer.setRepeats(false);
        timer.start();
        Toolkit.getDefaultToolkit().addAWTEventListener(this, eventMask.get());
    }

    /**
     * Stop listening for events.
     */
    public void stop() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final ActionEvent ae = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "");
        action.actionPerformed(ae);
    }

    @Override
    public void eventDispatched(AWTEvent e) {
        if (timer.isRunning()) {
            timer.restart();
        }
    }
}

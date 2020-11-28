package com.markussecundus.forms.events;


/**
 * Static class that specifies some concrete values of {@link EventListener} priorities,
 * that are used throughout the Forms library.
 *
 * @see EventDelegate
 * @see EventListener
 *
 * @author MarkusSecundus
 * * */
public class ListenerPriorities {

    /**
     * Listener, which checks whether a value meets some criteria and eventually aborts execution of a delegate right at its beginning.
     * */
    public static final int ARG_GUARD = 1000;

    /**
     * Listeners, that must run before the userspace ones.
     * */
    public static final int PRE_UTIL = 100;

    /**
     * Listeners managed by the user.
     * */
    public static final int USER = 0;

    /**
     * Listeners, that must run after the userspace ones.
     * */
    public static final int POST_UTIL = -100;

    /**
     * Listener for updating values bound to a property.
     * */
    public static final int BINDING_EXECUTOR = -1000;

    /**
     * Equivalent to <code>Integer.MIN_VALUE</code> :-D
     * */
    public static final int TAIL_RECURSION = Integer.MIN_VALUE;

}

package com.markussecundus.forms.events;


/**
 * Generic listener for an event.
 *
 * Takes one argument and returns,
 * whether there is any sense in continuing with actions that are next in order.
 *
 * @param <Args> Type to be passed to the listener as its argument
 *
 * @see EventDelegate
 * @see EventListenerX
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface EventListener<Args> {

    /**
     * Executes the listener.
     *
     * @return whether there is any sense in continuing with the actions this listener is part of
     * */
    public boolean exec(Args e);
}

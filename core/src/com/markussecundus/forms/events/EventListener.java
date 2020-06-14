package com.markussecundus.forms.events;


/**
 * Obecný Listener pro událost.
 *
 * Přebírá jeden argument a vrací,
 * zda má smysl pokračovat ve vykonávání náseldující činnosti.
 *
 *
 * @param <Args> Argument, který daný listener přebírá.
 *
 * @see EventDelegate
 * @see EventListenerX
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface EventListener<Args> {

    /**
     * Vykoná listener
     *
     * @return znamení zda má smysl pokračovat ve vykonávání náseldující činnosti.
     * */
    public boolean exec(Args e);
}

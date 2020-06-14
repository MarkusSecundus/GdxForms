package com.markussecundus.forms.events;


/**
 * Varianta {@link EventListener}u s implicitní návratovou hodnotou <code>true</code>.
 *
 * Slouží pro větší pohodlí uživatelů knihovny.
 *
 * @param <Args> Argument, který daný listener přebírá.
 *
 * @see EventListener
 * @see EventDelegate
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface EventListenerX<Args> extends EventListener<Args> {

    /**
     * Vykoná listener.
     * */
    public void x(Args e);

    /**
     * Vykoná <code>this.x(e)</code> a vrátí <code>true</code>.
     * */
    public default boolean exec(Args e){
        x(e);
        return true;
    }
}

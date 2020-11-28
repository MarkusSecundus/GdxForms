package com.markussecundus.forms.events;


/**
 * Variant of {@link EventListener} with implicit return value <code>true</code>.
 *
 * Serves for better convenience of the users of the library.
 *
 * @param <Args> Type to be passed to the listener as its argument
 *
 * @see EventListener
 * @see EventDelegate
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface EventListenerX<Args> extends EventListener<Args> {

    /**
     * Executes the listener.
     * */
    public void x(Args e);

    /**
     * Executes <code>this.x(e)</code> and returns <code>true</code>.
     *
     * @return <code>true</code>
     * */
    public default boolean exec(Args e){
        x(e);
        return true;
    }
}

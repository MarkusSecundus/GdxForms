package com.markussecundus.forms.wrappers.property;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;

/**
 * Základní rozhraní pro immutable Property, jejíž hodnota nemůže být změněna žádným způsobem a poskytuje tedy pouze getterové listenery.
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.GenericConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.LazyConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty
 *
 * @see ReadonlyProperty
 * @see Property
 * @see ReadonlyWrapper
 *
 * @author MarkusSecundus
 * */
public interface ConstProperty<T> extends ReadonlyWrapper<T> {

    /**
     * Delegát, jenž se provede při každém vyžádání vnitřní hodnoty voláním metody <code>get</code>.
     * */
    public ConstProperty<EventDelegate< GetterListenerArgs<T>>> getterListeners();

    /**
     * Pohodlnější zkratka pro <code>getterListeners().get()</code>.
     * */
    public default EventDelegate<GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }

    /**
     * Datová třída pro argumenty, které přebírá getterový listener.
     *
     * @see ConstProperty
     *
     * @author MarkusSecundus
     * */
    public static class GetterListenerArgs<T>{
        /**
         * {@link ConstProperty}, jejíž hodnota je čtena.
         * */
        public final ConstProperty<T> caller;

        /**
         * Wrapper, přes který lze aktuální hodnotu číst, aniž by byl volán getterový Delegát.
         * */
        public final ReadonlyWrapper<T> currentVal;

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public GetterListenerArgs(ConstProperty<T> caller, ReadonlyWrapper<T> currentVal){this.caller=caller;this.currentVal=currentVal;}
    }

}

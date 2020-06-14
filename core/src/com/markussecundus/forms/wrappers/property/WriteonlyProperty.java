package com.markussecundus.forms.wrappers.property;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;

/**
 * Základní rozhraní pro mutable Property, jejíž hodnota může být čtena kýmkoliv, ale měněna pouze přes specielní kontrakt.
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.AbstractWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.GenericWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.LazyWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.SimpleWriteonlyProperty
 *
 * @see WriteonlyWrapper
 * @see Property
 *
 * @author MarkusSecundus
 * */
public interface WriteonlyProperty<T> extends WriteonlyWrapper<T> {

    /**
     * Delegát, jenž se provede při každém vyžádání vnitřní hodnoty
     * */
    public ConstProperty<EventDelegate<GetterListenerArgs<T>>> getterListeners();
    /**
     * Delegát, jenž se provede při každém přepsání vnitřní hodnoty pomocí metody <code>set</code>.
     * */
    public ConstProperty<EventDelegate<SetterListenerArgs<T>>> setterListeners();


    /**
     * Pohodlnější zkratka pro <code>getterListeners().get()</code>.
     * */
    public default EventDelegate<GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }
    /**
     * Pohodlnější zkratka pro <code>setterListeners().get()</code>.
     * */
    public default EventDelegate<SetterListenerArgs<T>> getSetterListeners(){
        return setterListeners().get();
    }


    /**
     * Datová třída pro argumenty, které přebírá getterový listener.
     *
     * @see WriteonlyProperty
     *
     * @author MarkusSecundus
     * */
    public static class GetterListenerArgs<T>{
        /**
         * {@link WriteonlyProperty}, jejíž hodnota je čtena.
         * */
        public final WriteonlyProperty<T> caller;
        /**
         * {@link WriteonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a modifikovat ji bez aktivace listenerů
         * */
        public final WriteonlyWrapper<T> currentVal;

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public GetterListenerArgs(WriteonlyProperty<T> caller, WriteonlyWrapper<T> currentVal){this.caller=caller;this.currentVal=currentVal;}
    }

    /**
     * Datová třída pro argumenty, které přebírá setterový listener.
     *
     * @see WriteonlyProperty
     *
     * @author MarkusSecundus
     * */
    public static class SetterListenerArgs<T>{
        /**
         * {@link WriteonlyProperty}, jejíž hodnota je modifikována.
         * */
        public final WriteonlyProperty<T> caller;
        /**
         * {@link WriteonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a modifikovat ji bez aktivace listenerů
         * */
        public final WriteonlyWrapper<T> newVal;

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public SetterListenerArgs(WriteonlyProperty<T> caller, WriteonlyWrapper<T> newVal){this.caller=caller;this.newVal=newVal;}
    }


}

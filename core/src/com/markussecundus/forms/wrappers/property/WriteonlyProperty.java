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
     * Delegát, jenž se provede při každém vyžádání vnitřní hodnoty voláním metody <code>get</code>.
     *
     * @return Delegát, jenž se provede při každém vyžádání vnitřní hodnoty voláním metody <code>get</code>.
     * */
    public ConstProperty<? extends EventDelegate<? extends GetterListenerArgs<T>>> getterListeners();


    /**
     * Delegát, jenž se provede při každé změně vnitřní hodnoty.
     *
     * @return Delegát, jenž se provede při každé změně vnitřní hodnoty.
     * */
    public ConstProperty<? extends EventDelegate<? extends SetterListenerArgs<T>>> setterListeners();


    /**
     * Pohodlnější zkratka pro <code>getterListeners().get()</code>.
     *
     * @return Pohodlnější zkratka pro <code>getterListeners().get()</code>.
     * */
    public default EventDelegate<? extends GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }

    /**
     * Pohodlnější zkratka pro <code>setterListeners().get()</code>.
     *
     * @return Pohodlnější zkratka pro <code>setterListeners().get()</code>.
     * */
    public default EventDelegate<? extends SetterListenerArgs<T>> getSetterListeners(){
        return setterListeners().get();
    }


    /**
     * Datový interface pro argumenty přebírané getterovým listenerem.
     *
     * @see WriteonlyProperty
     *
     * @author MarkusSecundus
     * */
    public static interface GetterListenerArgs<T>{
        /**
         * {@link WriteonlyProperty}, jejíž hodnota je čtena.
         *
         * @return {@link WriteonlyProperty}, jejíž hodnota je čtena.
         * */
        public WriteonlyProperty<T> caller();
        /**
         * {@link WriteonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a modifikovat ji bez aktivace listenerů
         *
         * @return {@link WriteonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a modifikovat ji bez aktivace listenerů
         * */
        public WriteonlyWrapper<T> currentVal();

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public static<T> GetterListenerArgs<T> make(WriteonlyProperty<T> caller, WriteonlyWrapper<T> currentVal){
            return new GetterListenerArgs<T>() {
                public WriteonlyProperty<T> caller() { return caller; }
                public WriteonlyWrapper<T> currentVal() { return currentVal; }
            };
        }
    }

    /**
     * Datový interface pro argumenty přebírané setterovým listenerem.
     *
     * @see WriteonlyProperty
     *
     * @author MarkusSecundus
     * */
    public static interface SetterListenerArgs<T>{
        /**
         * {@link WriteonlyProperty}, jejíž hodnota je modifikována.
         *
         * @return {@link WriteonlyProperty}, jejíž hodnota je modifikována.
         * */
        public WriteonlyProperty<T> caller();
        /**
         * {@link WriteonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a modifikovat ji bez aktivace listenerů
         *
         * @return {@link WriteonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a modifikovat ji bez aktivace listenerů
         * */
        public WriteonlyWrapper<T> newVal();


        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public static<T> SetterListenerArgs<T> make(WriteonlyProperty<T> caller, WriteonlyWrapper<T> newVal){
            return new SetterListenerArgs<T>() {
                public WriteonlyProperty<T> caller() { return caller; }
                public WriteonlyWrapper<T> newVal() { return newVal; }
            };
        }
    }


}

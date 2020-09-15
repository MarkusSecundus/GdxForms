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
     *
     * @return Delegát, jenž se provede při každém vyžádání vnitřní hodnoty voláním metody <code>get</code>.
     * */
    public ConstProperty<? extends EventDelegate< ? extends GetterListenerArgs<T>>> getterListeners();

    /**
     * Pohodlnější zkratka pro <code>getterListeners().get()</code>.
     *
     * @return Pohodlnější zkratka pro <code>getterListeners().get()</code>.
     * */
    public default EventDelegate<? extends GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }

    /**
     * Datový interface pro argumenty, které přebírá getterový listener.
     *
     * @see ConstProperty
     *
     * @author MarkusSecundus
     * */
    public static interface GetterListenerArgs<T>{

        /**
         * {@link ConstProperty}, jejíž hodnota je čtena.
         *
         * @return {@link ConstProperty}, jejíž hodnota je čtena.
         * */
        public ConstProperty<T> caller();

        /**
         * Wrapper, přes který lze aktuální hodnotu číst, aniž by byl volán getterový Delegát.
         *
         * @return Wrapper, přes který lze aktuální hodnotu číst, aniž by byl volán getterový Delegát.
         * */
        public ReadonlyWrapper<T> currentVal();

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public static<T> GetterListenerArgs<T> make(ConstProperty<T> caller, ReadonlyWrapper<T> currentVal){
            return new GetterListenerArgs<T>() {
                public ConstProperty<T> caller() { return caller; }
                public ReadonlyWrapper<T> currentVal() { return currentVal; }
            };
        }
    }


}

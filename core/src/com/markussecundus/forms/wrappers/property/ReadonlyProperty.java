package com.markussecundus.forms.wrappers.property;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.Wrapper;

/**
 * Základní rozhraní pro mutable Property, jejíž hodnota může být čtena kýmkoliv, ale měněna pouze přes specielní kontrakt.
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.AbstractReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.LazyReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty
 *
 * @see ConstProperty
 * @see Property
 * @see ReadonlyWrapper
 *
 * @author MarkusSecundus
 * */
public interface ReadonlyProperty<T> extends ReadonlyWrapper<T> {

    /**
     * Delegát, jenž se provede při každém vyžádání vnitřní hodnoty voláním metody <code>get</code>.
     * */
    public ConstProperty<EventDelegate<GetterListenerArgs<T>>> getterListeners();
    /**
     * Delegát, jenž se provede při každé změně vnitřní hodnoty
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
     * Datová třída pro argumenty přebírané getterovým listenerem.
     *
     * @see ReadonlyProperty
     *
     * @author MarkusSecundus
     * */
    public static class GetterListenerArgs<T>{
        /**
         * {@link ReadonlyProperty}, jejíž hodnota je čtena.
         * */
        public final ReadonlyProperty<T> caller;
        /**
         * {@link ReadonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a číst ji bez aktivace listenerů
         * */
        public final ReadonlyWrapper<T> currentVal;

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public GetterListenerArgs(ReadonlyProperty<T> caller, ReadonlyWrapper<T> currentVal){this.caller=caller;this.currentVal=currentVal;}
    }

    /**
     * Datová třída pro argumenty přebírané setterovým listenerem.
     *
     * @see ReadonlyProperty
     *
     * @author MarkusSecundus
     * */
    public static class SetterListenerArgs<T>{
    //public:
        /**
         * {@link ReadonlyProperty}, jejíž hodnota je čtena.
         * */
        public final ReadonlyProperty<T> caller;

        /**
         * (pozn.: implementováno jako funkce, aby v budoucnu šlo jednodušeji implementovat poolování Args-objektů (Property a Wrapper jsou pro všechny Args dané Property zpravidla stejné, akorát stará hodnota se mění))
         *
         * @return Původní vnitřní hodnota, která byla nahrazena novou hodnotou.
         * */
        public T oldVal(){return oldVal;}
        /**
         * {@link ReadonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a číst ji bez aktivace listenerů
         * */
        public final ReadonlyWrapper<T> newVal;

        /**
         * Zkonstruuje instanci z daných argumentů
         * */
        public SetterListenerArgs(ReadonlyProperty<T> caller,T oldVal, ReadonlyWrapper<T> newVal){this.caller=caller;this.newVal=newVal;this.oldVal = oldVal;}

    //private:
        private final T oldVal;
    }

}

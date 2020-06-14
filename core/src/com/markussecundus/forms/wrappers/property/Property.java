package com.markussecundus.forms.wrappers.property;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.Wrapper;


/**
 * Základní rozhraní pro mutable Property, jejíž hodnota může být čtena i měněna kýmkoliv.
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.general.AbstractProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.GenericProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.LazyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.general.ArrayProperty
 *
 * @see ReadonlyProperty
 * @see WriteonlyProperty
 * @see ConstProperty
 * @see Wrapper
 *
 * @author MarkusSecundus
 * */
public interface Property<T> extends Wrapper<T> {

    /**
     * Delegát, jenž se provede při každém vyžádání vnitřní hodnoty voláním metody <code>get</code>.
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
     * Proběhne <code>setterListener</code> aniž by musela být modifikována vnitřní hodnota.
     * */
    public T pretendSet();


    /**
     * Datová třída pro argumenty, které přebírá getterový listener.
     *
     * @see Property
     *
     * @author MarkusSecundus
     * */
    public static class GetterListenerArgs<T>{
        /**
         * {@link Property}, jejíž hodnota je čtena.
         * */
        public final Property<T> caller;
        /**
         * {@link Wrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a měnit ji bez aktivace listenerů-
         * */
        public final Wrapper<T> currentVal;

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public GetterListenerArgs(Property<T> caller, Wrapper<T> currentVal){this.caller=caller;this.currentVal=currentVal;}
    }


    /**
     * Datová třída pro argumenty, které přebírá setterový listener.
     *
     * @see Property
     *
     * @author MarkusSecundus
     * */
    public static class SetterListenerArgs<T>{
        //public:
        /**
         * {@link Property}, jejíž hodnota je modifikována.
         * */
        public final Property<T> caller;

        /**
         * (pozn.: implementováno jako funkce, aby v budoucnu šlo jednodušeji implementovat poolování Args-objektů (Property a Wrapper jsou pro všechny Args dané Property zpravidla stejné, akorát stará hodnota se mění))
         *
         * @return Původní vnitřní hodnota, která byla nahrazena novou hodnotou.
         * */
        public T oldVal(){return oldVal;}

        /**
         * {@link Wrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a měnit ji bez aktivace listenerů
         * */
        public final Wrapper<T> newVal;


        /**
         * Zkonstruuje instanci z daných argumentů
         * */
        public SetterListenerArgs(Property<T> caller, T oldVal, Wrapper<T> newVal){this.caller=caller;this.newVal = newVal;this.oldVal=oldVal;}

        //private:
        private final T oldVal;
    }

    public static final EventListener<SetterListenerArgs<?>> SKIP_IF_VALUE_DIDNT_CHANGE = o-> !FormsUtil.equals(o.oldVal(), o.newVal.get());

}

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
public interface ReadonlyProperty<T> extends ReadonlyWrapper<T>, ConstProperty<T>{

    @Override
    public ConstProperty<? extends EventDelegate<? extends GetterListenerArgs<T>>> getterListeners();


    /**
     * Delegát, jenž se provede při každé změně vnitřní hodnoty.
     *
     * @return Delegát, jenž se provede při každé změně vnitřní hodnoty.
     * */
    public ConstProperty<? extends EventDelegate<? extends SetterListenerArgs<T>>> setterListeners();


    @Override
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
     * @see ReadonlyProperty
     *
     * @author MarkusSecundus
     * */
    public static interface GetterListenerArgs<T> extends ConstProperty.GetterListenerArgs<T>{
        @Override
        public ReadonlyProperty<T> caller();
        @Override
        public ReadonlyWrapper<T> currentVal();

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public static<T> GetterListenerArgs<T> make(ReadonlyProperty<T> caller, ReadonlyWrapper<T> currentVal){
            return new GetterListenerArgs<T>(){
                public ReadonlyProperty<T> caller(){return caller;}
                public ReadonlyWrapper<T> currentVal(){return currentVal;}
            };
        }
    }




    /**
     * Datový interface pro argumenty přebírané setterovým listenerem.
     *
     * @see ReadonlyProperty
     *
     * @author MarkusSecundus
     * */
    public static interface SetterListenerArgs<T>{
        /**
        * {@link ReadonlyProperty}, jejíž hodnota je čtena.
        *
         * @return {@link ReadonlyProperty}, jejíž hodnota je čtena.
        * */
        public ReadonlyProperty<T> caller();

        /**
         * Původní vnitřní hodnota, která byla nahrazena novou hodnotou.
         * <p></p>
         * (pozn.: implementováno jako funkce, aby v budoucnu šlo jednodušeji implementovat poolování Args-objektů (Property a Wrapper jsou pro všechny Args dané Property zpravidla stejné, akorát stará hodnota se mění))
         *
         * @return Původní vnitřní hodnota, která byla nahrazena novou hodnotou.
         * */
        public T oldVal();

        /**
         * {@link ReadonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a číst ji bez aktivace listenerů
         *
         * @return {@link ReadonlyWrapper} přes který lze přistupovat k aktuální vnitřní hodnotě a číst ji bez aktivace listenerů
         * */
        public ReadonlyWrapper<T> newVal();

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public static<T> SetterListenerArgs<T> make(ReadonlyProperty<T> caller,T oldVal, ReadonlyWrapper<T> newVal) {
            return new SetterListenerArgs<T>(){
                public ReadonlyProperty<T> caller(){return caller;}
                public T oldVal(){return oldVal;}
                public ReadonlyWrapper<T> newVal(){return newVal;}
            };

        }


    }

}

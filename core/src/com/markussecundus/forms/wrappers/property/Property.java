package com.markussecundus.forms.wrappers.property;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.function.Consumer;
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
public interface Property<T> extends Wrapper<T>, ReadonlyProperty<T>, WriteonlyProperty<T>, ConstProperty<T> {

    @Override
    public ConstProperty<? extends EventDelegate<? extends GetterListenerArgs<T>>> getterListeners();

    @Override
    public ConstProperty<? extends EventDelegate<? extends SetterListenerArgs<T>>> setterListeners();

    @Override
    public default EventDelegate<? extends GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }
    @Override
    public default EventDelegate<? extends SetterListenerArgs<T>> getSetterListeners(){
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
    public static interface GetterListenerArgs<T> extends ReadonlyProperty.GetterListenerArgs<T>, WriteonlyProperty.GetterListenerArgs<T>{
        @Override
        public Property<T> caller();
        @Override
        public Wrapper<T> currentVal();

        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public static<T> GetterListenerArgs<T> make(Property<T> caller, Wrapper<T> currentVal){
            return new GetterListenerArgs<T>() {
                public Property<T> caller() { return caller; }
                public Wrapper<T> currentVal() { return currentVal; }
            };
        }
    }


    /**
     * Datová třída pro argumenty, které přebírá setterový listener.
     *
     * @see Property
     *
     * @author MarkusSecundus
     * */
    public static interface SetterListenerArgs<T> extends ReadonlyProperty.SetterListenerArgs<T>, WriteonlyProperty.SetterListenerArgs<T>{
        @Override
        public Property<T> caller();
        @Override
        public T oldVal();
        @Override
        public Wrapper<T> newVal();


        /**
         * Zkonstruuje instanci z daných argumentů.
         * */
        public static<T> SetterListenerArgs<T> make(Property<T> caller, T oldVal, Wrapper<T> newVal){
            return new SetterListenerArgs<T>() {
                public Property<T> caller() { return caller; }
                public T oldVal() { return oldVal; }
                public Wrapper<T> newVal() { return newVal; }
            };
        }

    }


}

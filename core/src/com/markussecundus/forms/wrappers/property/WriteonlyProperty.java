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


    public ConstProperty<? extends EventDelegate<? extends GetterListenerArgs<T>>> getterListeners();
    public ConstProperty<? extends EventDelegate<? extends SetterListenerArgs<T>>> setterListeners();


    public default EventDelegate<? extends GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }
    public default EventDelegate<? extends SetterListenerArgs<T>> getSetterListeners(){
        return setterListeners().get();
    }


    public static interface GetterListenerArgs<T>{
        public WriteonlyProperty<T> caller();
        public WriteonlyWrapper<T> currentVal();

        public static<T> GetterListenerArgs<T> make(WriteonlyProperty<T> caller, WriteonlyWrapper<T> currentVal){
            return new GetterListenerArgs<T>() {
                public WriteonlyProperty<T> caller() { return caller; }
                public WriteonlyWrapper<T> currentVal() { return currentVal; }
            };
        }
    }

    public static interface SetterListenerArgs<T>{
        public WriteonlyProperty<T> caller();
        public WriteonlyWrapper<T> newVal();


        public static<T> SetterListenerArgs<T> make(WriteonlyProperty<T> caller, WriteonlyWrapper<T> newVal){
            return new SetterListenerArgs<T>() {
                public WriteonlyProperty<T> caller() { return caller; }
                public WriteonlyWrapper<T> newVal() { return newVal; }
            };
        }
    }


}

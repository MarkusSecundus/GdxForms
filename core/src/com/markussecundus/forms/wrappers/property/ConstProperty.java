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

    public ConstProperty<? extends EventDelegate< ? extends GetterListenerArgs<T>>> getterListeners();

    public default EventDelegate<? extends GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }

    public static interface GetterListenerArgs<T>{
        public ConstProperty<T> caller();
        public ReadonlyWrapper<T> currentVal();

        public static<T> GetterListenerArgs<T> make(ConstProperty<T> caller, ReadonlyWrapper<T> currentVal){
            return new GetterListenerArgs<T>() {
                public ConstProperty<T> caller() { return caller; }
                public ReadonlyWrapper<T> currentVal() { return currentVal; }
            };
        }
    }


}

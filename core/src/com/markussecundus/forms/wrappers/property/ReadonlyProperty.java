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


    public ConstProperty<? extends EventDelegate<? extends GetterListenerArgs<T>>> getterListeners();
    public ConstProperty<? extends EventDelegate<? extends SetterListenerArgs<T>>> setterListeners();



    public default EventDelegate<? extends GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }

    public default EventDelegate<? extends SetterListenerArgs<T>> getSetterListeners(){
        return setterListeners().get();
    }


    public static interface GetterListenerArgs<T> extends ConstProperty.GetterListenerArgs<T>{
        public ReadonlyProperty<T> caller();
        public ReadonlyWrapper<T> currentVal();

        public static<T> GetterListenerArgs<T> make(ReadonlyProperty<T> caller, ReadonlyWrapper<T> currentVal){
            return new GetterListenerArgs<T>(){
                public ReadonlyProperty<T> caller(){return caller;}
                public ReadonlyWrapper<T> currentVal(){return currentVal;}
            };
        }
    }




    public static interface SetterListenerArgs<T>{
        public ReadonlyProperty<T> caller();
        public T oldVal();
        public ReadonlyWrapper<T> newVal();

        public static<T> SetterListenerArgs<T> make(ReadonlyProperty<T> caller,T oldVal, ReadonlyWrapper<T> newVal) {
            return new SetterListenerArgs<T>(){
                public ReadonlyProperty<T> caller(){return caller;}
                public T oldVal(){return oldVal;}
                public ReadonlyWrapper<T> newVal(){return newVal;}
            };

        }


    }

}

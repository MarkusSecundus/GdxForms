package com.markussecundus.forms.wrappers.property;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.utils.FormsUtil;
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
public interface Property<T> extends Wrapper<T>, ReadonlyProperty<T>, WriteonlyProperty<T> {

    public ConstProperty<? extends EventDelegate<? extends GetterListenerArgs<T>>> getterListeners();

    public ConstProperty<? extends EventDelegate<? extends SetterListenerArgs<T>>> setterListeners();


    public default EventDelegate<? extends GetterListenerArgs<T>> getGetterListeners(){
        return getterListeners().get();
    }
    public default EventDelegate<? extends SetterListenerArgs<T>> getSetterListeners(){
        return setterListeners().get();
    }

    public T pretendSet();


    public static interface GetterListenerArgs<T> extends ReadonlyProperty.GetterListenerArgs<T>, WriteonlyProperty.GetterListenerArgs<T>{
        public Property<T> caller();
        public Wrapper<T> currentVal();

        public static<T> GetterListenerArgs<T> make(Property<T> caller, Wrapper<T> currentVal){
            return new GetterListenerArgs<T>() {
                public Property<T> caller() { return caller; }
                public Wrapper<T> currentVal() { return currentVal; }
            };
        }
    }


    public static interface SetterListenerArgs<T> extends ReadonlyProperty.SetterListenerArgs<T>, WriteonlyProperty.SetterListenerArgs<T>{
        public Property<T> caller();
        public T oldVal();

        public Wrapper<T> newVal();


        public static<T> SetterListenerArgs<T> make(Property<T> caller, T oldVal, Wrapper<T> newVal){
            return new SetterListenerArgs<T>() {
                public Property<T> caller() { return caller; }
                public T oldVal() { return oldVal; }
                public Wrapper<T> newVal() { return newVal; }
            };
        }

    }



    public static final EventListener<SetterListenerArgs<?>> SKIP_IF_VALUE_DIDNT_CHANGE = o-> !FormsUtil.equals(o.oldVal(), o.newVal().get());

}

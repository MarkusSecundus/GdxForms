package com.markussecundus.forms.wrappers.property.impl.general;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.Wrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty;

/**
 * Základní parciální implementace {@link Property}, ze které se již velmi jednoduše dají odvozovat
 * implementace konkrétní.
 *
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.general.GenericProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.LazyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty
 *
 * @author MarkusSecundus
 * */
public abstract class AbstractProperty<T> extends Wrapper.AbstractSimpleWrapper<T>  implements Property<T> {


//protected:

    /**
     * Skutečná vnitřní implementace procesu získání vnitřní hodnoty. Volána před tím, než se provede getter.
     * Neměla by mít postranní efekty.
     *
     * Nutno implementovat.
     * */
    protected abstract T obtain();

    /**
     * Skutečná vnitřní implementace procesu modifikace vnitřní hodnoty. Volána před tím, než se provede setter.
     * Neměla by mít postranní efekty.
     *
     * Nutno implementovat.
     * */
    protected abstract T change(T val);

    /**
     * Factory na wrappery, přes něž je možno uvnitř getteru číst a modifikovat vnitřní hodnotu bez spuštění listenerů.
     * */
    protected Wrapper<T> obtainListenerlessWrapper(){return new ListenerlessWrapper();}


//public:

    /**{@inheritDoc}*/
    @Override public T get() {
        T ret = obtain();
        if(getterListeners!=null)
            getterListeners.get().exec( GetterListenerArgs.make(this, obtainListenerlessWrapper()));
        return obtain();
    }

    /**{@inheritDoc}*/
    @Override public T set(T t) {
        T old = this.obtain();
        this.change(t);
        if(setterListeners!=null)
            setterListeners.get().exec( SetterListenerArgs.make(this, old, obtainListenerlessWrapper()));
        return obtain();
    }

    /**{@inheritDoc}*/
    @Override public T pretendSet() {
        T old = this.obtain();
        this.change(obtain());
        if(setterListeners!=null)
            setterListeners.get().exec( SetterListenerArgs.make(this, old, obtainListenerlessWrapper()));
        return obtain();
    }

    /**
     * {@inheritDoc}
     *
     * Listenery jsou generovány líně.
     * */
    @Override public ConstProperty<EventDelegate<SetterListenerArgs<T>>> setterListeners() {
        if(setterListeners==null)
            setterListeners=new SimpleConstProperty<>(EventDelegate.make());
        return setterListeners;
    }

    /**
     * {@inheritDoc}
     *
     * Listenery jsou generovány líně.
     * */
    @Override public ConstProperty<EventDelegate<GetterListenerArgs<T>>> getterListeners() {
        if(getterListeners==null)
            getterListeners=new SimpleConstProperty<>(EventDelegate.make());
        return getterListeners;
    }


//private:

    private AbstractConstProperty<EventDelegate<SetterListenerArgs<T>>> setterListeners = null;
    private AbstractConstProperty<EventDelegate<GetterListenerArgs<T>>> getterListeners = null;


    private class ListenerlessWrapper extends Wrapper.AbstractSimpleWrapper<T> {
        @Override public T get() {
            return obtain();
        }
        @Override public T set(T t) {
            return change(t);
        }
    }



}

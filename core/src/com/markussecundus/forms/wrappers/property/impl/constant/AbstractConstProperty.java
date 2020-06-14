package com.markussecundus.forms.wrappers.property.impl.constant;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;

import java.util.Objects;

/**
 * Základní parciální implementace {@link ConstProperty}, ze které se již velmi jednoduše dají odvozovat
 * implementace konkrétní.
 *
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.GenericConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.LazyConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty
 *
 * @author MarkusSecundus
 * */
public abstract class AbstractConstProperty<T> extends ReadonlyWrapper.AbstractSimpleWrapper<T> implements ConstProperty<T> {
//protected:

    /**
     * Skutečná vnitřní implementace procesu získání vnitřní hodnoty. Volána před tím, než se provede getter.
     * Neměla by mít postranní efekty.
     *
     * Nutno implementovat.
     * */
    protected abstract T obtain();

    /**
     * Factory na wrappery, přes něž je možno uvnitř getteru číst vnitřní hodnotu bez spuštění listenerů.
     * */
    protected ReadonlyWrapper<T> obtainListenerlessWrapper(){return new ListenerlessWrapper();}

//public:

    /**Provede getter a vrátí vnitřní hodnotu.*/
    @Override public T get() {
        T ret = obtain();
        if(getterListeners!=null)
            getterListeners.get().exec(new GetterListenerArgs<>(this, obtainListenerlessWrapper()));
        return obtain();
    }

    /**{@inheritDoc}*/
    @Override public ConstProperty<EventDelegate<GetterListenerArgs<T>>> getterListeners() {
        if(getterListeners==null)
            getterListeners = new SimpleConstProperty<>(EventDelegate.make());
        return getterListeners;
    }


//private:

    private AbstractConstProperty<EventDelegate< GetterListenerArgs<T>>> getterListeners = null;

    private class ListenerlessWrapper extends ReadonlyWrapper.AbstractSimpleWrapper<T>{
        @Override public T get() {
            return obtain();
        }
    }

}

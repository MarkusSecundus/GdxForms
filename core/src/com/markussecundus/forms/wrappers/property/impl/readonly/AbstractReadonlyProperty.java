package com.markussecundus.forms.wrappers.property.impl.readonly;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty;

/**
 * Základní parciální implementace {@link ReadonlyProperty}, ze které se již velmi jednoduše dají odvozovat
 * implementace konkrétní.
 *
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.LazyReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty
 *
 * @author MarkusSecundus
 * */
public abstract class AbstractReadonlyProperty<T> extends ReadonlyWrapper.AbstractSimpleWrapper<T> implements ReadonlyProperty<T> {
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
     * Factory na wrappery, přes něž je možno uvnitř getteru číst vnitřní hodnotu bez spuštění listenerů.
     * */
    protected ReadonlyWrapper<T> obtainListenerlessWrapper(){return new ListenerlessWrapper();}


    /**
     * Factory na proxy, která jediná smí modifikovat hodnotu obsaženou v Property.
     *
     * @return Proxy která modifikuje hodnotu v Property
     * */
    protected WriteonlyWrapper<T> makeSetter(){return new Setter();}


//public:

    /**{@inheritDoc}*/
    @Override public final T get() {
        T ret = obtain();
        if( getterListeners!=null)
            getterListeners.get().exec( new GetterListenerArgs<>(this, obtainListenerlessWrapper()));
        return obtain();
    }

    /**
     * Nastaví novou hodnotu a provede setter.
     *
     * Smí být volána pouze skrze proxy z <code>makeSetter</code>.
     * */
    private final T set(T t){
        T old = this.obtain();
        this.change(t);
        if(setterListeners!=null)
            setterListeners.get().exec( new SetterListenerArgs<>(this, old, obtainListenerlessWrapper()));
        return obtain();
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

    /**
     * {@inheritDoc}
     *
     * Listenery jsou generovány líně.
     * */
    @Override public final ConstProperty<EventDelegate<SetterListenerArgs<T>>> setterListeners() {
        if(setterListeners==null)
            setterListeners=new SimpleConstProperty<>(EventDelegate.make());
        return setterListeners;
    }

    //private:

    private AbstractConstProperty<EventDelegate<GetterListenerArgs<T>>> getterListeners = null;
    private AbstractConstProperty<EventDelegate<SetterListenerArgs<T>>> setterListeners = null;


    private class ListenerlessWrapper extends ReadonlyWrapper.AbstractSimpleWrapper<T>{
        @Override public T get() {
            return obtain();
        }
    }

    private class Setter implements WriteonlyWrapper<T>{
        @Override public T set(T t) {
            return AbstractReadonlyProperty.this.set(t);
        }
    }
}

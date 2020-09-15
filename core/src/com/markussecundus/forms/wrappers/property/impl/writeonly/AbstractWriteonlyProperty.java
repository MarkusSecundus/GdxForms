package com.markussecundus.forms.wrappers.property.impl.writeonly;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;
import com.markussecundus.forms.wrappers.property.WriteonlyProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty;

/**
 * Základní parciální implementace {@link WriteonlyProperty}, ze které se již velmi jednoduše dají odvozovat
 * implementace konkrétní.
 *
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.GenericWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.LazyWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.SimpleWriteonlyProperty
 *
 * @author MarkusSecundus
 * */
public abstract class AbstractWriteonlyProperty<T> implements WriteonlyProperty<T> {
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
     * Factory na wrappery, přes něž je možno uvnitř getteru modifikovat vnitřní hodnotu bez spuštění listenerů.
     * */
    protected WriteonlyWrapper<T> obtainListenerlessWrapper(){return new ListenerlessWrapper();}

    /**
     * Factory na proxy, která jediná smí číst hodnotu obsaženou v Property.
     *
     * @return Proxy která čte hodnotu v Property
     * */
    protected ReadonlyWrapper<T> makeGetter(){return new Getter();}

//public:

    /**
     * {@inheritDoc}
     *
     * @return hodnota která byla předána jako parametr funkce
     * */
    @Override public T set(T t) {
        change(t);
        if(setterListeners!=null)
            setterListeners.get().exec( SetterListenerArgs.make(this, obtainListenerlessWrapper()));
        return t;
    }

    /**
     * Nastaví novou hodnotu a provede setter.
     *
     * Smí být volána pouze skrze proxy z <code>makeGetter</code>.
     * */
    private T get(){
        if(getterListeners!=null)
            getterListeners.get().exec( GetterListenerArgs.make(this, obtainListenerlessWrapper()));
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
    @Override
    public ConstProperty<EventDelegate<GetterListenerArgs<T>>> getterListeners() {
        if(getterListeners==null)
            getterListeners=new SimpleConstProperty<>(EventDelegate.make());
        return getterListeners;
    }


//private:

    private AbstractConstProperty<EventDelegate<SetterListenerArgs<T>>> setterListeners = null;
    private AbstractConstProperty<EventDelegate<GetterListenerArgs<T>>> getterListeners = null;


    private class ListenerlessWrapper implements WriteonlyWrapper<T>{
        @Override public T set(T t) {
            return change(t);
        }
    }

    private class Getter extends ReadonlyWrapper.AbstractSimpleWrapper<T>{
        @Override public T get() {
            return AbstractWriteonlyProperty.this.get();
        }
    }

}

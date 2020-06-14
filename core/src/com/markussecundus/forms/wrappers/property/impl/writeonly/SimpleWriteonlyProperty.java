package com.markussecundus.forms.wrappers.property.impl.writeonly;

import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty;

/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.WriteonlyProperty}, která má v sobě přímo obsaženu svou hodnotu.
 *
 *
 * @param <T> typ na který Property ukazuje

 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.AbstractWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.GenericWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.LazyWriteonlyProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty
 *
 * @author MarkusSecundus
 * */
public class SimpleWriteonlyProperty<T> extends AbstractWriteonlyProperty<T> {
//public:
    /**
     * Factory, která vytvoří a vrátí novou instanci líné {@link SimpleWriteonlyProperty}
     * rovnou spolu s její getterovou proxy.
     *
     * @param <T> typ na který Property ukazuje
     *
     * @param val iniciální hodnota
     *
     * @return dvojice nově vytvořené Property a jejího getteru
     * */
    public static<T> Pair<SimpleWriteonlyProperty<T>, ReadonlyWrapper<T>> make(T val){
        SimpleWriteonlyProperty<T> ret = new SimpleWriteonlyProperty<>(val);
        return Pair.make(ret, ret.makeGetter());
    }

//protected:

    /**{@inheritDoc}*/
    @Override protected T change(T t) {
        return this.t = t;
    }

    /**{@inheritDoc}*/
    @Override protected T obtain() {
        return t;
    }

//private:
    private SimpleWriteonlyProperty(T t){ this.t = t;}

    private T t;

}

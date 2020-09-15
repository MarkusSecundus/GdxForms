package com.markussecundus.forms.wrappers.property.impl.readonly;

import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;

/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.ReadonlyProperty}, která má v sobě přímo obsaženu svou hodnotu.
 *
 *
 * @param <T> typ na který Property ukazuje

 * @see com.markussecundus.forms.wrappers.property.impl.readonly.AbstractReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.LazyReadonlyProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.SimpleWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty
 *
 * @author MarkusSecundus
 * */
public class SimpleReadonlyProperty<T> extends AbstractReadonlyProperty<T> {
//public:
    /**
     * Factory, která vytvoří a vrátí novou instanci {@link SimpleReadonlyProperty}
     * rovnou spolu s její setterovou proxy.
     *
     * @param <T> typ na který Property ukazuje
     *
     * @param val iniciální hodnota
     *
     * @return dvojice nově vytvořené Property a jejího setteru
     * */
    public static<T>Pair<SimpleReadonlyProperty<T>, WriteonlyWrapper<T>> make(T val){
        SimpleReadonlyProperty<T> ret = new SimpleReadonlyProperty<>(val);
        return Pair.make(ret, ret.makeSetter());
    }

//protected:

    @Override
    protected T obtain() {
        return val;
    }

    @Override
    protected T change(T val) { return this.val=val; }

//private:

    private SimpleReadonlyProperty(T val){ this.val = val;}

    private T val;

}

package com.markussecundus.forms.wrappers.property.impl.general;



/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.Property}, která má v sobě přímo obsaženu svou hodnotu.
 *
 *
 * @param <T> typ na který Property ukazuje

 * @see com.markussecundus.forms.wrappers.property.impl.general.AbstractProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.GenericProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.LazyProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.SimpleWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty
 *
 * @author MarkusSecundus
 * */
public class SimpleProperty<T> extends AbstractProperty<T> {
//public:

    /**
     * Inicializuje Property danou hodnotou.
     *
     * @param val iniciální hodnota pro Property
     * */
    public SimpleProperty(T val){ this.t = val;}


//protected:

    /**{@inheritDoc}*/
    @Override protected T obtain() {
        return t;
    }

    /**{@inheritDoc}*/
    @Override protected T change(T t) {
        return this.t = t;
    }

//private:

    private T t;

}

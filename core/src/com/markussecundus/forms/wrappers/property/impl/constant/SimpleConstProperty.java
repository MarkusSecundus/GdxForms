package com.markussecundus.forms.wrappers.property.impl.constant;


import com.markussecundus.forms.wrappers.property.ConstProperty;

/**
 * Implementace {@link ConstProperty},  která má v sobě přímo obsaženu svou hodnotu.
 *
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty
 * @see GenericConstProperty
 * @see LazyConstProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.SimpleWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty
 *
 * @author MarkusSecundus
 * */
public class SimpleConstProperty<T> extends AbstractConstProperty<T> {
//public:

    /**
     * Inicializuje Property danou hodnotou.
     *
     * @param val iniciální hodnota pro Property
     * */
    public SimpleConstProperty(T val){ this.val = val;}

//protected:

    /**{@inheritDoc}*/
    @Override protected T obtain() {
        return val;
    }

//private:

    private final T val;

}

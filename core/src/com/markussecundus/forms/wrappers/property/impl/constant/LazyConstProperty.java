package com.markussecundus.forms.wrappers.property.impl.constant;

import com.markussecundus.forms.utils.function.Supplier;
import com.markussecundus.forms.wrappers.property.ConstProperty;


/**
 * Implementace {@link ConstProperty}, která svou hodnotu generuje líně při jejím prvním vyžádání.
 *
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty
 * @see GenericConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.LazyWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.LazyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.LazyReadonlyProperty
 *
 * @author MarkusSecundus
 * */
public class LazyConstProperty<T> extends AbstractConstProperty<T> {
//public:

    /**
     * Vytvoří línou {@link ConstProperty} s danou generující funkcí.
     *
     * @param generator vygeneruje hodnotu pro odkazovaný objekt až bude potřeba
     * */
    public LazyConstProperty(Supplier<T> generator){ this.sup = generator;}

//protected:

    /**
     * Při 1. zavolání vygeneruje líně hodnotu.
     *
     * {@inheritDoc}
     * */
    @Override protected T obtain() {
        if(sup!=null) {
            val = sup.get();
            sup = null;
        }
        return val;
    }

//private:

    private T val;
    private Supplier<T> sup;

}

package com.markussecundus.forms.wrappers.property.impl.general;


import com.markussecundus.forms.utils.function.Supplier;
import com.markussecundus.forms.wrappers.property.ConstProperty;


/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.Property}, která svou hodnotu generuje líně při jejím prvním vyžádání.
 *
 *
 * @param <T> typ na který Property ukazuje

 * @see com.markussecundus.forms.wrappers.property.impl.general.AbstractProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.GenericProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.LazyWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.LazyConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.LazyReadonlyProperty
 *
 * @author MarkusSecundus
 * */
public class LazyProperty<T> extends AbstractProperty<T> {
//public:

    /**
     * Vytvoří línou {@link ConstProperty} s danou generující funkcí.
     *
     * @param generator vygeneruje hodnotu pro odkazovaný objekt až bude potřeba
     * */
    public LazyProperty(Supplier<T> generator){ this.sup = generator;}


//protected:

    /**
     * Při 1. zavolání vygeneruje líně hodnotu.
     *
     * {@inheritDoc}
     * */
    @Override protected T obtain() {
        if(sup!=null)
            consumeSupply();
        return t;
    }

    /**
     * {@inheritDoc}
     * */
    @Override protected T change(T t) {
        if(sup!=null)
            consumeSupply();
        return this.t = t;
    }

//private:

    private void consumeSupply(){
        this.t = sup.get();
        sup = null;
    }

    private T t;
    private Supplier<T> sup;
}

package com.markussecundus.forms.wrappers.property.impl.writeonly;

import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.utils.function.Supplier;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty;


/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.WriteonlyProperty}, která svou hodnotu generuje líně při jejím prvním vyžádání.
 *
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.GenericProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.LazyWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.LazyConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.LazyReadonlyProperty
 *
 * @author MarkusSecundus
 * */
public class LazyWriteonlyProperty<T> extends AbstractWriteonlyProperty<T> {
//public:
    /**
     * Factory, která vytvoří a vrátí novou instanci líné {@link GenericReadonlyProperty}
     * rovnou spolu s její getterovou proxy.
     *
     * @param <T> typ na který Property ukazuje
     *
     * @param generator vygeneruje hodnotu pro odkazovaný objekt až bude potřeba
     *
     * @return dvojice nově vytvořené Property a jejího getteru
     * */
    public static<T> Pair<LazyWriteonlyProperty<T>, ReadonlyWrapper<T>> make(Supplier<T> generator){
        LazyWriteonlyProperty<T> ret = new LazyWriteonlyProperty<>(generator);
        return Pair.make(ret, ret.makeGetter());
    }

//protected:

    /**
     * Při 1. zavolání vygeneruje líně hodnotu.
     *
     * {@inheritDoc}
     * */
    @Override protected T change(T t) {
        if(sup!=null)
            consumeSupply();
        return this.t = t;
    }

    /**
     * {@inheritDoc}
     * */
    @Override protected T obtain() {
        if(sup!=null)
            consumeSupply();
        return t;
    }

//private:
    private LazyWriteonlyProperty(Supplier<T> sup){ this.sup = sup;}

    private void consumeSupply(){
        t = sup.get();
        sup = null;
    }

    private Supplier<T> sup;
    private T t;

}

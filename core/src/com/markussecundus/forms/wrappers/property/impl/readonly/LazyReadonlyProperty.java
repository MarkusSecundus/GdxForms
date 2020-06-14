package com.markussecundus.forms.wrappers.property.impl.readonly;

import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.utils.function.Supplier;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;


/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.ReadonlyProperty}, která svou hodnotu generuje líně při jejím prvním vyžádání.
 *
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.AbstractReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.LazyReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.LazyWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.LazyConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.LazyProperty
 *
 * @author MarkusSecundus
 * */
public class LazyReadonlyProperty<T> extends AbstractReadonlyProperty<T> {
//public:

    /**
     * Factory, která vytvoří a vrátí novou instanci líné {@link GenericReadonlyProperty}
     * rovnou spolu s její setterovou proxy.
     *
     * @param <T> typ na který Property ukazuje
     *
     * @param generator vygeneruje hodnotu pro odkazovaný objekt až bude potřeba
     *
     * @return dvojice nově vytvořené Property a jejího setteru
     * */
    public static<T>Pair<LazyReadonlyProperty<T>, WriteonlyWrapper<T>> make(Supplier<T> generator){
        LazyReadonlyProperty<T> ret = new LazyReadonlyProperty<>(generator);
        return Pair.make(ret, ret.makeSetter());
    }

//protected:

    /**
     * Při 1. zavolání vygeneruje líně hodnotu.
     *
     * {@inheritDoc}
     * */
    @Override protected T obtain() {
        if(sup!=null)
            consumeSupply();
        return val;
    }

    /**
     * {@inheritDoc}
     * */
    @Override protected T change(T val) {
        if(sup!=null)
            consumeSupply();
        return this.val=val;
    }

//private:

    private LazyReadonlyProperty(Supplier<T> sup){ this.sup = sup;}

    private void consumeSupply(){
        val = sup.get();
        sup = null;
    }

    private Supplier<T> sup;
    private T val;

}

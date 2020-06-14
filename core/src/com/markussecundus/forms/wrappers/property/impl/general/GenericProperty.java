package com.markussecundus.forms.wrappers.property.impl.general;

import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.Wrapper;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;

/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.Property}, která neobsahuje přímo hodnotu, ale
 * funkce, skrze které hodnotu získává a modifikuje
 *
 * Ideálně by mělo jít o čisté funkce bez postranních efektů. Obě funkce by měly přistupovat
 * ke stejnému objektu a onen objekt, ke kterému přistupují, by neměl být přístupný nikomu jinému žádným jiným způsobem.
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.LazyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.GenericConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.GenericWriteonlyProperty
 *
 * @author MarkusSecundus
 * */
public class GenericProperty<T> extends AbstractProperty<T>{
//public:

    /**
     * Je poskytnut jeden objekt, který umí jak číst, tak zapisovat.
     *
     * @param get_and_setTer proxy, skrze kterou bude Property provádět čtení a zápis
     * */
    public GenericProperty(Wrapper<T> get_and_setTer){this(get_and_setTer, get_and_setTer);}

    /**
     * Inicializujeme příslušnými funkcemi pro zápis a pro čtení.
     *
     * @param getter proxy, skrze kterou bude Property provádět čtení
     * @param setter proxy, skrze kterou bude Property provádět zápis
     * */
    public GenericProperty(ReadonlyWrapper<T> getter, WriteonlyWrapper<T> setter){
        this.reader = getter;
        this.writer = setter;
    }

//protected:

    /**
     * {@inheritDoc}
     *
     * Odkazuje na funkci getteru.
     * */
    @Override protected T obtain() {
        return reader.get();
    }

    /**
     * {@inheritDoc}
     *
     * Odkazuje na funkci setteru.
     * */
    @Override protected T change(T t) {
        return writer.set(t);
    }


    /**
     * {@inheritDoc}
     *
     * Pokusí se použít přímo set-getterový objekt, přes který k hodnotám sama přistupuje.
     * */
    @Override protected Wrapper<T> obtainListenerlessWrapper() {
        if (reader instanceof Wrapper)
            return (Wrapper<T>) reader;     //aby objekt byl instanceof ReadonlyWrapper<A> && instanceof Wrapper<B>, A!=B, to Java dovolí jenom kdyby A byl potomek B, a pokud někdo vážně použije tak hnusný obrat, tak si jenom zaslouží, aby se mu na tomhle místě program rozbil.
        if(writer instanceof Wrapper)
            return (Wrapper<T>) writer;     //zde obdobně jako výše
        return super.obtainListenerlessWrapper();
    }

//private:
    private final ReadonlyWrapper<T> reader;
    private final WriteonlyWrapper<T> writer;

}

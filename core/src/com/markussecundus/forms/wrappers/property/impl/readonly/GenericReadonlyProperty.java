package com.markussecundus.forms.wrappers.property.impl.readonly;

import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.Wrapper;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;

/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.ReadonlyProperty}, která neobsahuje přímo hodnotu, ale
 * funkce, skrze které hodnotu získává a modifikuje
 *
 * Ideálně by mělo jít o čisté funkce bez postranních efektů. Obě funkce by měly přistupovat
 * ke stejnému objektu a onen objekt, ke kterému přistupují, by neměl být přístupný nikomu jinému žádným jiným způsobem.
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.AbstractReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.LazyReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.SimpleReadonlyProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.GenericConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.GenericProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.GenericWriteonlyProperty
 *
 * @author MarkusSecundus
 * */
public class GenericReadonlyProperty<T> extends AbstractReadonlyProperty<T> {
//public:

    /**
     * Factory, která vytvoří a vrátí novou instanci {@link GenericReadonlyProperty}
     * rovnou spolu s její setterovou proxy.
     *
     * @param <T> typ na který Property ukazuje
     *
     * @param getter proxy, skrze kterou bude Property provádět čtení
     * @param setter proxy, skrze kterou bude Property provádět zápis
     *
     * @return dvojice nově vytvořené Property a jejího setteru
     * */
    public static<T>Pair<GenericReadonlyProperty<T>, WriteonlyWrapper<T>> make(ReadonlyWrapper<T> getter, WriteonlyWrapper<T> setter){
        GenericReadonlyProperty<T> ret = new GenericReadonlyProperty<>(getter, setter);
        return Pair.make(ret, ret.makeSetter());
    }

    /**
     * Factory, která vytvoří a vrátí novou instanci {@link GenericReadonlyProperty}
     * rovnou spolu s její setterovou proxy.
     *
     * @param <T> typ na který Property ukazuje
     *
     * @param get_and_setTer proxy, skrze kterou bude Property provádět čtení a zápis
     *
     * @return dvojice nově vytvořené Property a jejího setteru
     * */
    public static<T> Pair<GenericReadonlyProperty<T>, WriteonlyWrapper<T>> make(Wrapper<T> get_and_setTer){
        return make(get_and_setTer, get_and_setTer);
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
    @Override protected T change(T val) { return writer.set(val); }

//private:
    private GenericReadonlyProperty(Wrapper<T> wrap){this(wrap,wrap);}
    private GenericReadonlyProperty(ReadonlyWrapper<T> reader, WriteonlyWrapper<T> writer){
        this.reader = reader;
        this.writer = writer;
    }

    private final ReadonlyWrapper<T> reader;
    private final WriteonlyWrapper<T> writer;
}

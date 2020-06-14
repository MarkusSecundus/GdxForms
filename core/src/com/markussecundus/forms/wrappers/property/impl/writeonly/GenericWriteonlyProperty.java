package com.markussecundus.forms.wrappers.property.impl.writeonly;

import com.markussecundus.forms.utils.Pair;
import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.Wrapper;
import com.markussecundus.forms.wrappers.WriteonlyWrapper;
import com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty;

/**
 * Implementace {@link com.markussecundus.forms.wrappers.property.WriteonlyProperty}, která neobsahuje přímo hodnotu, ale
 * funkce, skrze které hodnotu získává a modifikuje
 *
 * Ideálně by mělo jít o čisté funkce bez postranních efektů. Obě funkce by měly přistupovat
 * ke stejnému objektu a onen objekt, ke kterému přistupují, by neměl být přístupný nikomu jinému žádným jiným způsobem.
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.AbstractWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.LazyWriteonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.SimpleWriteonlyProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.GenericConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.GenericProperty
 * @see GenericReadonlyProperty
 *
 * @author MarkusSecundus
 * */
public class GenericWriteonlyProperty<T> extends AbstractWriteonlyProperty<T> {
//public:
    /**
     * Factory, která vytvoří a vrátí novou instanci {@link GenericReadonlyProperty}
     * rovnou spolu s její getterovou proxy.
     *
     * @param <T> typ na který Property ukazuje
     *
     * @param getter proxy, skrze kterou bude Property provádět čtení
     * @param setter proxy, skrze kterou bude Property provádět zápis
     *
     * @return dvojice nově vytvořené Property a jejího getteru
     * */
    public static<T> Pair<GenericWriteonlyProperty<T>, ReadonlyWrapper<T>> make(ReadonlyWrapper<T> getter, WriteonlyWrapper<T> setter){
        GenericWriteonlyProperty<T> ret = new GenericWriteonlyProperty<>(getter, setter);
        return Pair.make(ret, ret.makeGetter());
    }
    /**
     * Factory, která vytvoří a vrátí novou instanci {@link GenericReadonlyProperty}
     * rovnou spolu s její getterovou proxy.
     *
     * @param <T> typ na který Property ukazuje
     *
     * @param get_and_setTer proxy, skrze kterou bude Property provádět čtení a zápis
     *
     * @return dvojice nově vytvořené Property a jejího getteru
     * */
    public static<T> Pair<GenericWriteonlyProperty<T>, ReadonlyWrapper<T>> make(Wrapper<T> get_and_setTer){
        return make(get_and_setTer, get_and_setTer);
    }

//protected:

    /**
     * {@inheritDoc}
     *
     * Odkazuje na funkci getteru.
     * */
    @Override protected T change(T t) {
        return writer.set(t);
    }

    /**
     * {@inheritDoc}
     *
     * Odkazuje na funkci setteru.
     * */
    @Override protected T obtain() {
        return reader.get();
    }

//private:
    private GenericWriteonlyProperty(Wrapper<T> wrap){this(wrap,wrap);}
    private GenericWriteonlyProperty(ReadonlyWrapper<T> reader, WriteonlyWrapper<T> writer){
        this.reader = reader;
        this.writer = writer;
    }

    private final ReadonlyWrapper<T> reader;
    private final WriteonlyWrapper<T> writer;

}

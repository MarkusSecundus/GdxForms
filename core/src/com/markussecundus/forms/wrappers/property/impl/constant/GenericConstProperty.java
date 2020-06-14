package com.markussecundus.forms.wrappers.property.impl.constant;

import com.markussecundus.forms.wrappers.ReadonlyWrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;

/**
 * Implementace {@link ConstProperty}, která neobsahuje přímo hodnotu, ale
 * funkci, skrze kterou hodnotu získává.
 *
 * Ideálně by mělo jít o čistou funkci bez postranních efektů objekt skrze
 * ni získávaný by neměl být přístupný žádným jiným způsobem.
 *
 * @param <T> typ na který Property ukazuje
 *
 * @see com.markussecundus.forms.wrappers.property.impl.constant.AbstractConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.LazyConstProperty
 * @see com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty
 *
 * @see com.markussecundus.forms.wrappers.property.impl.readonly.GenericReadonlyProperty
 * @see com.markussecundus.forms.wrappers.property.impl.general.GenericProperty
 * @see com.markussecundus.forms.wrappers.property.impl.writeonly.GenericWriteonlyProperty
 *
 * @author MarkusSecundus
 * */
public class GenericConstProperty<T> extends AbstractConstProperty<T> {
//public:

    /**
     * Inicializuje novou instanci daným getterem
     *
     * @param getter proxy, skrze kterou bude Property provádět čtení
     * */
    public GenericConstProperty(ReadonlyWrapper<T> getter){ this.wrap = getter;}

//protected:

    /**
     * {@inheritDoc}
     *
     * Odkazuje na funkci getteru.
     * */
    @Override protected T obtain() {
        return wrap.get();
    }

//private:

    private ReadonlyWrapper<T> wrap;

}

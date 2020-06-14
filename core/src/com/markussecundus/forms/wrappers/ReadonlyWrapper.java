package com.markussecundus.forms.wrappers;

import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.impl.constant.GenericConstProperty;

/**
 * Základní rozhraní pro Wrapperový typ, který umí číst hodnotu, na kterou odkazuje.
 *
 * @param <T> typ, na který wrapper ukazuje
 *
 * @see WriteonlyWrapper
 * @see Wrapper
 * @see com.markussecundus.forms.wrappers.property.ReadonlyProperty
 * @see ConstProperty
 *
 * @author MarkusSecundus
 * */
public interface ReadonlyWrapper<T> {

    /**
     * @return hodnota na kterou wrapper ukazuje.
     * */
    public T get();

    /**
     * @param prototype wrapper poskytující funkcionalitu pro získání hodnoty
     *
     * @return instance {@link ReadonlyWrapper} v kanonické implementaci, která má korektně
     *      definované funkce <code>equals</code> a <code>hashCode</code>
     * */
    public static<T> ReadonlyWrapper<T> make(ReadonlyWrapper<T> prototype){
        return new AbstractSimpleWrapper<T>() {
            public T get() { return prototype.get(); }
        };
    }

    /**
     * Kanonický formát pro výstup funkce <code>ReadonlyWrapper.toString</code>
     * */
    static String TO_STRING(Object o){
        return "{" + o + "}";
    }

    /**
     * Kanonická parciální implementace {@link ReadonlyWrapper}u,
     * která poskytuje funkce <code>equals</code>, <code>hashCode</code>
     * a <code>toString</code> odkazující na stejné funkce vnitřního objektu.
     *
     * @param <T> typ, na který wrapper ukazuje
     *
     * @author MarkusSecundus
     * */
    static abstract class AbstractSimpleWrapper<T> implements ReadonlyWrapper<T>{
        /**
         * Přesměrovává na funkci vnitřního objektu.
         * {@inheritDoc}
         * */
        @Override public int hashCode() {
            T o = get();
            return o==null?0:o.hashCode();
        }
        /**
         * Přesměrovává na funkci vnitřního objektu.
         * {@inheritDoc}
         * */
        @Override public boolean equals(Object obj) {
            return FormsUtil.equals(obj, get());
        }

        /**
         * {@inheritDoc}
         * @return řetězcová reprezentace vnitřního objektu upravená podle formátu daného v <code>ReadonlyWrapper.TO_STRING</code>
         * */
        @Override public String toString() {
            return TO_STRING(get());
        }
    }

}

package com.markussecundus.forms.wrappers;

/**
 * Základní rozhraní pro Wrapperový typ, který umí změnit hodnotu, na kterou odkazuje.
 *
 * @param <T> typ, na který wrapper ukazuje
 *
 * @see ReadonlyWrapper
 * @see Wrapper
 * @see com.markussecundus.forms.wrappers.property.WriteonlyProperty
 *
 * @author MarkusSecundus
 * */
public interface WriteonlyWrapper<T> {

    /**
     * Změní hodnotu, na kterou wrapper odkazuje
     *
     * @return právě přiřazená hodnota (pro účely řetězení operací)
     * */
    public T set(T t);
}

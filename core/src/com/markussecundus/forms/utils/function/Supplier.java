package com.markussecundus.forms.utils.function;

/**
 * Náhražka za standardní {@link java.util.function.Supplier}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.Supplier
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface Supplier<T> {

    /**
     * @see java.util.function.Supplier
     * */
    T get();
}

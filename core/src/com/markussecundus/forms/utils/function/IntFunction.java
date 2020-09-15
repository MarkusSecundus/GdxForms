package com.markussecundus.forms.utils.function;

/**
 * Náhražka za standardní {@link java.util.function.IntFunction}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.IntFunction
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface IntFunction<R> {

    /**
     * @see java.util.function.IntFunction
     * */
    R apply(int var1);
}

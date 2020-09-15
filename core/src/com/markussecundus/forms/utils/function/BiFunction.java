package com.markussecundus.forms.utils.function;

import java.util.Objects;



/**
 * Náhražka za standardní {@link java.util.function.BiFunction}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.BiFunction
 *
 * @see Function
 * @see TriFunction
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface BiFunction<T,U,R> {

    /**
     * @see java.util.function.BiFunction
     * */
    public R apply(T var1, U var2);

    /**
     * @see java.util.function.BiFunction
     * */
    default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> var1) {
        Objects.requireNonNull(var1);
        return (var2, var3) -> var1.apply(this.apply(var2, var3));
    }
}

package com.markussecundus.forms.utils.function;

import java.util.Objects;

/**
 * Náhražka za standardní {@link java.util.function.Function}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.Function
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface Function<T, R> {
    /**
     * @see java.util.function.Function
     * */
    R apply(T var1);

    /**
     * @see java.util.function.Function
     * */
    default <V> Function<V, R> compose(Function<? super V, ? extends T> var1) {
        Objects.requireNonNull(var1);
        return (var2) -> this.apply(var1.apply(var2));
    }

    /**
     * @see java.util.function.Function
     * */
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> var1) {
        Objects.requireNonNull(var1);
        return (var2) -> var1.apply(this.apply(var2));
    }

    /**
     * @see java.util.function.Function
     * */
    static <T> Function<T, T> identity() {
        return x -> x;
    }
}

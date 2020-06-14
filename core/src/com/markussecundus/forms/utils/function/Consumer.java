package com.markussecundus.forms.utils.function;

import java.util.Objects;

/**
 * Náhražka za standardní {@link java.util.function.Consumer}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.Consumer
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface Consumer<T> {

    /**
     * @see java.util.function.Consumer
     * */
    void accept(T var1);

    /**
     * @see java.util.function.Consumer
     * */
    default Consumer<T> andThen(Consumer<? super T> var1) {
        Objects.requireNonNull(var1);
        return (var2) -> {
            this.accept(var2);
            var1.accept(var2);
        };
    }
}

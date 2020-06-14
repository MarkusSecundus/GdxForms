package com.markussecundus.forms.utils.function;

import java.util.Objects;


/**
 * Náhražka za standardní {@link java.util.function.BiConsumer}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.BiConsumer
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface BiConsumer<T,U> {

    /**
     * @see java.util.function.BiConsumer
     * */
    void accept(T var1, U var2);

    /**
     * @see java.util.function.BiConsumer
     * */
    default BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> var1) {
        Objects.requireNonNull(var1);
        return (var2, var3) -> {
            this.accept(var2, var3);
            var1.accept(var2, var3);
        };
    }
}

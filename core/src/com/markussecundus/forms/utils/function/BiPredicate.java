package com.markussecundus.forms.utils.function;

import java.util.Objects;

/**
 * Náhražka za standardní {@link java.util.function.BiPredicate}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.BiPredicate
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface BiPredicate<T,U> {

    /**
     * @see java.util.function.BiPredicate
     * */
    boolean test(T var1, U var2);

    /**
     * @see java.util.function.BiPredicate
     * */
    default BiPredicate<T, U> and(BiPredicate<? super T, ? super U> var1) {
        Objects.requireNonNull(var1);
        return (var2, var3) -> this.test(var2, var3) && var1.test(var2, var3);
    }

    /**
     * @see java.util.function.BiPredicate
     * */
    default BiPredicate<T, U> negate() {
        return (var1, var2) -> !this.test(var1, var2);
    }

    /**
     * @see java.util.function.BiPredicate
     * */
    default BiPredicate<T, U> or(BiPredicate<? super T, ? super U> var1) {
        Objects.requireNonNull(var1);
        return (var2, var3) -> this.test(var2, var3) || var1.test(var2, var3);
    }
}

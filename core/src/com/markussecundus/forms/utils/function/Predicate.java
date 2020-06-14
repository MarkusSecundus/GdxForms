package com.markussecundus.forms.utils.function;

import java.util.Objects;

/**
 * Náhražka za standardní {@link java.util.function.Predicate}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.Predicate
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface Predicate<T> {

    /**
     * @see java.util.function.Predicate
     * */
    boolean test(T var1);

    /**
     * @see java.util.function.Predicate
     * */
    default Predicate<T> and(Predicate<? super T> var1) {
        Objects.requireNonNull(var1);
        return (var2) -> this.test(var2) && var1.test(var2);
    }

    /**
     * @see java.util.function.Predicate
     * */
    default Predicate<T> negate() {
        return (var1) -> !this.test(var1);
    }

    /**
     * @see java.util.function.Predicate
     * */
    default Predicate<T> or(Predicate<? super T> var1) {
        Objects.requireNonNull(var1);
        return (var2) -> this.test(var2) || var1.test(var2);
    }

    /**
     * @see java.util.function.Predicate
     * */
    static <T> Predicate<T> isEqual(Object var0) {
        return null == var0 ? (o->o==null) : var0::equals;
    }
}

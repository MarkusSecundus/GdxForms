package com.markussecundus.forms.utils.function;


/**
 * Obdoba {@link Predicate} přeijímající 3 argumenty.
 *
 * @see Predicate
 * @see BiPredicate
 *
 * @author MarkusSecundus
 *
 * */
public interface TriPredicate<T,U,V> {
    public boolean test(T t, U u, V v);
}

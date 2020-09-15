package com.markussecundus.forms.utils.function;


/**
 * Obdoba {@link Function} přeijímající 3 argumenty.
 *
 * @see Function
 * @see BiFunction
 *
 * @author MarkusSecundus
 *
 * */
@FunctionalInterface
public interface TriFunction<T,U,V,R> {

    public R apply(T t, U u, V v);
}

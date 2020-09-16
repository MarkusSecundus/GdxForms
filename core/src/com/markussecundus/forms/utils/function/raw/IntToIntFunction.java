package com.markussecundus.forms.utils.function.raw;

import com.markussecundus.forms.utils.function.Function;

/**
 * Rozhraní pro funkci, jež přebírá i vrací celočíselnou hodnotu.
 *
 * @see Function
 * @see IntFunction
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface IntToIntFunction{
    public int apply(int i);
}

package com.markussecundus.forms.utils.function.raw;

import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.utils.function.IntFunction;

/**
 * Rozhraní pro funkci, jež přebírá celočíselnou hodnotu a vrací long.
 *
 * @see Function
 * @see IntFunction
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface IntToLongFunction {
    public long apply(int i);
}

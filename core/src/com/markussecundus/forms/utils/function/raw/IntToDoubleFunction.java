package com.markussecundus.forms.utils.function.raw;

import com.markussecundus.forms.utils.function.Function;

/**
 * Rozhraní pro funkci, jež přebírá celočíselnou hodnotu a vrací float.
 *
 * @see Function
 * @see IntFunction
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface IntToDoubleFunction {
    public double apply(int i);
}

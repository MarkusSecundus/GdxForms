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
public interface IntToFloatFunction {
    public float apply(int i);
}

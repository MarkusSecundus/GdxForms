package com.markussecundus.forms.utils.function.raw;

import com.markussecundus.forms.utils.function.Function;

/**
 * Náhražka za standardní {@link java.util.function.IntFunction}, který není dostupný na starších verzích Androidího API.
 *
 * @see java.util.function.IntFunction
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface IntFunction<R> extends Function<Integer, R>{

    public default R apply(Integer var1){return apply_raw_arg(var1);}

    /**
     * @see java.util.function.IntFunction
     * */
    R apply_raw_arg(int var1);






    /**
     * Rozhraní pro funkci, jež přebírá celočíselnou hodnotu a vrací znak.
     *
     * @see Function
     * @see IntFunction
     *
     * @author MarkusSecundus
     * */
    @FunctionalInterface
    public interface ToChar extends IntFunction<Character>{
        public default Character apply_raw_arg(int var1){return apply_raw(var1);}
        public char apply_raw(int i);
    }


    /**
     * Rozhraní pro funkci, jež přebírá celočíselnou hodnotu a vrací float.
     *
     * @see Function
     * @see IntFunction
     *
     * @author MarkusSecundus
     * */
    @FunctionalInterface
    public interface ToDouble extends IntFunction<Double> {
        public default Double apply_raw_arg(int var1){return apply_raw(var1);}
        public double apply_raw(int i);
    }

    /**
     * Rozhraní pro funkci, jež přebírá celočíselnou hodnotu a vrací float.
     *
     * @see Function
     * @see IntFunction
     *
     * @author MarkusSecundus
     * */
    @FunctionalInterface
    public interface ToFloat extends IntFunction<Float> {
        public default Float apply_raw_arg(int var1){return apply_raw(var1);}
        public float apply_raw(int i);
    }

    /**
     * Rozhraní pro funkci, jež přebírá i vrací celočíselnou hodnotu.
     *
     * @see Function
     * @see IntFunction
     *
     * @author MarkusSecundus
     * */
    @FunctionalInterface
    public interface ToInt extends IntFunction<Integer>{
        public default Integer apply_raw_arg(int var1){return apply_raw(var1);}
        public int apply_raw(int i);
    }

    /**
     * Rozhraní pro funkci, jež přebírá celočíselnou hodnotu a vrací long.
     *
     * @see Function
     * @see IntFunction
     *
     * @author MarkusSecundus
     * */
    @FunctionalInterface
    public interface ToLong  extends IntFunction<Long>{
        public default Long apply_raw_arg(int var1){return apply_raw(var1);}
        public long apply_raw(int i);
    }

}

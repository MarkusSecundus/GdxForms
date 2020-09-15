package com.markussecundus.forms.utils.function;



/**
 * Obdoba {@link java.util.Comparator} umožnující porovnávat dva objekty navzájem různých typů.
 *
 * @see java.util.Comparator
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface BiComparator<A, B> {

    /**
     * Vzájemně porovná oba objekty - obdobně jako metoda v knihovním {@link java.util.Comparator}
     *
     * @return <code>-1</code> pokud <code>a < b</code>,  <code>1</code> pokud <code> a > b</code>,
     *      <code>0</code> pokud a je rovno b
     * * */
    public int compareTo(A a, B b);

    /**
     * @return defaultní komparátor pro porovnatelné typy
     * */
    public static<A  extends Comparable<B>, B> BiComparator<A,B> make(){
        return (BiComparator<A, B>) Util.DEF_COMPARATOR;
    }

    static class Util{
        private static final BiComparator<Comparable<Object>, Object> DEF_COMPARATOR = Comparable::compareTo;
    }
}

package com.markussecundus.forms.utils.function;

import java.util.Comparator;

@FunctionalInterface
public interface BiComparator<A, B> {
    public int compareTo(A a, B b);


    public static<A  extends Comparable<B>, B> BiComparator<A,B> make(){
        return Comparable::compareTo;
    }
}

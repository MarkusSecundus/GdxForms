package com.markussecundus.forms.utils;


import com.markussecundus.forms.wrappers.ReadonlyWrapper;

import java.util.Objects;

/**
 * Uspořádaná dvojice hodnot.
 *
 * Šikovné v situacích, kde je potřeba vrátit z funkce dvě různé věci najednou.
 * (viz např. {@link com.markussecundus.forms.wrappers.property.ReadonlyProperty},
 *  {@link com.markussecundus.forms.wrappers.property.WriteonlyProperty},...)
 *
 * @author MarkusSecundus
 * */
public interface Pair<A, B> {

    /**
     * První hodnota páru.
     * */
    public A first();

    /**
     * Druhá hodnota páru.
     * */
    public B second();


    /**
     * Vytvoří pár daných dvou hodnot v kanonické implementaci.
     *
     * @param a první hodnota
     * @param b druhá hodnota
     * @param <A> datový typ první hodnoty
     * @param <B> datový typ druhé hodnoty
     * */
    public static<A,B> Pair<A,B> make(A a, B b){
        return new Abstract<A,B>(){
            public A first() { return a; }
            public B second() { return b; }
        };
    }


    public static<A,B> Pair<A,B> makeKeyValuePair(A a, B b){
        return new Abstract.AbstractFirstWeighted<A,B>(){
            public A first() { return a; }
            public B second() { return b; }
        };
    }
    public static<A,B> Pair<A,B> makeValueKeyPair(A a, B b){
        return new Abstract.AbstractSecondWeighted<A,B>(){
            public A first() { return a; }
            public B second() { return b; }
        };
    }



    /**
     * Vytvoří {@link Pair}, který odkazuje na hodnoty nacházející se jinde.
     *
     * @param a getter pro první hodnotu
     * @param b getter pro druhou hodnotu
     * @param <A> datový typ první hodnoty
     * @param <B> datový typ druhé hodnoty
     * */
    public static<A,B> Pair<A,B> proxy(ReadonlyWrapper<A> a, ReadonlyWrapper<B> b){
        return new Abstract<A, B>() {
            public A first() { return a.get(); }
            public B second() { return b.get(); }
        };
    }




    public static abstract class Abstract<A,B> implements Pair<A,B>{
        @Override
        public int hashCode() {
            return FormsUtil.hashCode(first(), second());
        }
        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Pair<?,?>))
                return false;
            Pair<?,?> p = ((Pair<?,?>)o);
            return FormsUtil.equals(first(), p.first()) && FormsUtil.equals(second(), p.second());
        }
        @Override
        public String toString() {
            return String.format("{%s, %s}", first(), second());
        }



        private static abstract class AbstractWeighted<A,B> extends Abstract<A,B>{
            protected abstract Object importantPart();

            public int hashCode() { return FormsUtil.hashCode(importantPart()); }
            public boolean equals(Object o) { return o.equals(importantPart()); }
        }


        public static abstract class AbstractFirstWeighted<A,B> extends Abstract.AbstractWeighted<A,B>{
            protected Object importantPart() { return first(); }
        }
        public static abstract class AbstractSecondWeighted<A,B> extends Abstract.AbstractWeighted<A,B>{
            protected Object importantPart() { return second(); }
        }
    }




    /**
     * Lehkotonážní implementace {@link Pair}, která vždy všude vrací <code>null</code>.
     *
     * Šikovné jako specielní značková hodnota na místech, kde se očekává, že bude předána
     * instance {@link Pair} a kde není vhodné předávat <code>null</code>.
     *
     * @see Pair
     *
     * @author MarkusSecundus
     * */
    public static<A,B>  Pair<A,B> dummy(){
        return new Abstract<A, B>() {
            public A first() { return null; }
            public B second() { return null; }
        };
    }
}

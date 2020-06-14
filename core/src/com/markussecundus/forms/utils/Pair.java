package com.markussecundus.forms.utils;


import com.markussecundus.forms.wrappers.ReadonlyWrapper;

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
    public static<A,B> Pair<A,B> make(A a, B b){return new Impl<>(a,b);}


    /**
     * Vytvoří {@link Pair}, který odkazuje na hodnoty nacházející se jinde.
     *
     * @param a getter pro první hodnotu
     * @param b getter pro druhou hodnotu
     * @param <A> datový typ první hodnoty
     * @param <B> datový typ druhé hodnoty
     * */
    public static<A,B> Pair<A,B> proxy(ReadonlyWrapper<A> a, ReadonlyWrapper<B> b){
        return new Pair<A, B>() {
            public A first() { return a.get(); }
            public B second() { return b.get(); }
        };
    }


    /**
     * Kanonická implementace {@link Pair}.
     *
     * {@inheritDoc}
     *
     * @param <A> datový typ první hodnoty
     * @param <B> datový typ druhé hodnoty
     *
     * @see Pair
     *
     * @author MarkusSecundus
     * */
    public static class Impl<A,B> implements Pair<A,B>{
        public final A first;
        public final B second;
        public Impl(A first, B second){this.first=first;this.second = second;}

        public A first(){return first;}
        public B second(){return second;}
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
    public static class Dummy<A,B> implements Pair<A,B>{
        public A first() { return null; }
        public B second() { return null; }
    }
}

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
    public static<A,B> Pair<A,B> make(A a, B b){
        return new Abstract<A,B>(){
            public A first() { return a; }
            public B second() { return b; }
        };
    }


    /**
     * Vytvoří pár daných dvou hodnot, kde pouze 1. prvek
     * hraje roli pro metody <code>equlas</code> a <code>hashCode</code>.
     *
     * @param keyElem první hodnota - klíč
     * @param secondElem druhá hodnota
     * @param <K> datový typ první hodnoty
     * @param <T> datový typ druhé hodnoty
     * */
    public static<K, T> Pair<K, T> makeKeyValuePair(K keyElem, T secondElem){
        return new Abstract.AbstractFirstWeighted<K, T>(){
            public K first() { return keyElem; }
            public T second() { return secondElem; }
        };
    }

    /**
     * Vytvoří pár daných dvou hodnot, kde pouze 2. prvek
     * hraje roli pro metody <code>equlas</code> a <code>hashCode</code>.
     *
     * @param firstElem první hodnota
     * @param keyElem druhá hodnota - klíč
     * @param <T> datový typ první hodnoty
     * @param <K> datový typ druhé hodnoty
     * */
    public static<T, K> Pair<T, K> makeValueKeyPair(T firstElem, K keyElem){
        return new Abstract.AbstractSecondWeighted<T, K>(){
            public T first() { return firstElem; }
            public K second() { return keyElem; }
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


    /**
     * Základní abstraktní implementace poskytující kanonickou implementaci metod
     * <code>hashCode</code>, <code>equals</code> a <code>toString</code>.
     * */
    public static abstract class Abstract<A,B> implements Pair<A,B>{

        /**
         * Vrátí hodnotu závisející na hodnotách prvků páru.
         *
         * {@inheritDoc}
         * */
        @Override
        public int hashCode() {
            return FormsUtil.hashCode(first(), second());
        }


        /**
         * Závisí na shodě jednotlivých prvků páru.
         *
         * {@inheritDoc}
         * */
        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Pair<?,?>))
                return false;
            Pair<?,?> p = ((Pair<?,?>)o);
            return FormsUtil.equals(first(), p.first()) && FormsUtil.equals(second(), p.second());
        }

        @Override
        public String toString() {
            return String.format("{'%s', '%s'}", first(), second());
        }



        private static abstract class AbstractWeighted<A,B> extends Abstract<A,B>{
            protected abstract Object importantPart();

            public int hashCode() { return FormsUtil.hashCode(importantPart()); }
            public boolean equals(Object o) { return o.equals(importantPart()); }
        }

        /**
         * Varianta {@link Pair.Abstract}, kde se výsledek metod <code>hashCode</code>
         * a <code>equals</code> odvíjí čistě od hodnoty prvního prvku páru.
         *
         * @see Pair.Abstract
         *
         * @author MarkusSecundus
         * */
        public static abstract class AbstractFirstWeighted<A,B> extends Abstract.AbstractWeighted<A,B>{
            /**
             * Vrátí první prvek páru.
             *
             * @return <code>this.first()</code>
             * */
            protected Object importantPart() { return first(); }
        }
        /**
         * Varianta {@link Pair.Abstract}, kde se výsledek metod <code>hashCode</code>
         * a <code>equals</code> odvíjí čistě od hodnoty druhého prvku páru.
         *
         * @see Pair.Abstract
         *
         * @author MarkusSecundus
         * */
        public static abstract class AbstractSecondWeighted<A,B> extends Abstract.AbstractWeighted<A,B>{
            /**
             * Vrátí druhý prvek páru.
             *
             * @return <code>this.second()</code>
             * */
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
     * */
    public static<A,B>  Pair<A,B> dummy(){
        return new Abstract<A, B>() {
            public A first() { return null; }
            public B second() { return null; }
        };
    }
}

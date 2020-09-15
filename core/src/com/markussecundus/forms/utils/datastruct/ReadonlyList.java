package com.markussecundus.forms.utils.datastruct;

import com.markussecundus.forms.utils.FormsUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;


/**
 * Rozhraní pro libovolnou posloupnost prvků, jež má stanovenou délku
 * a umožnuje náhodný read-only přístup k prvkům.
 *
 * @param <T> typ prvků přitomných v listu
 *
 * @author MarkusSecundus
 * */
public interface ReadonlyList<T> extends Serializable, Iterable<T> {

    /**
     * @param n index, na kterém se nalézá tázaný prvek
     *
     * @throws IndexOutOfBoundsException pokud je <code>n < 0 || n >= size()</code>
     *
     * @return prvek na n-té pozici v listu
     * */
    public T getNth(int n);

    /**
     * @return počet prvků v listu
     * */
    public int size();

    @Override
    public default Iterator<T> iterator(){
        return new Iterator<T>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i<size();
            }

            @Override
            public T next() {
                return getNth(i++);
            }
        };
    }

    /**
     * @param cls třída, podle které má být objekt pole vytvořen
     *
     * @return pole obsahující prvky daného {@link ReadonlyList}.
     * */
    public default<U> U[] toArray(Class<U> cls){
        U[] ret = (U[])Array.newInstance(cls, size());

        for(int t=size()-1;t>=0;--t)
            ret[t] = (U)getNth(t);

        return ret;
    }

    /**
     * @param item hledaný prvek
     *
     * @return 1. index, na kterém se nalézá daný prvek (resp. prvek x, pro který <code>Objects.equals(item, x)</code>)
     * či <code>-1</code>, pokud takový prvek neexistuje.
     * */
    public default int indexOf(T item){
        for(int t=0;t<size();++t) {
            if (FormsUtil.equals(item, getNth(t)))
                return t;
        }
        return -1;
    }

    /**
     * @param item prvek, jenž má v listu být obsažen.
     * @param count počet, kolikrát má item v listu být přítomen
     * @param cls třída typu prvků listu
     *
     * @param <T> typ prvků přitomných ve vytvářeném listu
     *
     * Vrací {@link ReadonlyList} obsahující <code>count</code>-krát za sebou prvek <code>item</code>.
     * */
    public static<T> ReadonlyList<T> ofItem(T item, int count, Class<T> cls){
        T[] array = (T[])Array.newInstance(cls, count);
        Arrays.fill(array, item);
        return make(array);
    }
    /**
     * @param item prvek, jenž má v listu být obsažen.
     * @param count počet, kolikrát má item v listu být přítomen
     *
     * Vrací {@link ReadonlyList.Char} obsahující <code>count</code>-krát za sebou prvek <code>item</code>.
     * */
    public static Char ofItem(char item, int count){
        char[] array = new char[count];
        Arrays.fill(array, item);
        return Char.make(array);
    }
    /**
     * @param item prvek, jenž má v listu být obsažen.
     * @param count počet, kolikrát má item v listu být přítomen
     *
     * Vrací {@link ReadonlyList.Int} obsahující <code>count</code>-krát za sebou prvek <code>item</code>.
     * */
    public static Int ofItem(int item, int count){
        int[] array = new int[count];
        Arrays.fill(array, item);
        return Int.make(array);
    }
    /**
     * @param item prvek, jenž má v listu být obsažen.
     * @param count počet, kolikrát má item v listu být přítomen
     *
     * Vrací {@link ReadonlyList.Long} obsahující <code>count</code>-krát za sebou prvek <code>item</code>.
     * */
    public static Long ofItem(long item, int count){
        long[] array = new long[count];
        Arrays.fill(array, item);
        return Long.make(array);
    }
    /**
     * @param item prvek, jenž má v listu být obsažen.
     * @param count počet, kolikrát má item v listu být přítomen
     *
     * Vrací {@link ReadonlyList.Float} obsahující <code>count</code>-krát za sebou prvek <code>item</code>.
     * */
    public static Float ofItem(float item, int count){
        float[] array = new float[count];
        Arrays.fill(array, item);
        return Float.make(array);
    }
    /**
     * @param item prvek, jenž má v listu být obsažen.
     * @param count počet, kolikrát má item v listu být přítomen
     *
     * Vrací {@link ReadonlyList.Double} obsahující <code>count</code>-krát za sebou prvek <code>item</code>.
     * */
    public static Double ofItem(double item, int count){
        double[] array = new double[count];
        Arrays.fill(array, item);
        return Double.make(array);
    }



    /**
     * @param col {@link List}, na který má {@link ReadonlyList} odkazovat
     *
     * @param <T> typ prvků přitomných ve vytvářeném listu
     *
     * @return {@link ReadonlyList} nad daným obecným {@link List}em.
     * */
    public static<T> ReadonlyList<T> make(List<T> col){
        return new ReadonlyList<T>() {
            public T getNth(int n) {
                return col.get(n);
            }
            public int size() {
                return col.size();
            }

            public int indexOf(T item){return col.indexOf(item);}
            public Iterator<T> iterator() { return col.iterator(); }
            public void forEach(Consumer<? super T> consumer) { col.forEach(consumer); }
            public Spliterator<T> spliterator() { return col.spliterator(); }

            public <U> U[] toArray(Class<U> cls){return col.toArray((U[])Array.newInstance(cls, size()));}

            public int hashCode() { return col.hashCode(); }
            public boolean equals(Object o) { return FormsUtil.equals(o, col); }
        };
    }
    /**
     * @param arr pole, na které má {@link ReadonlyList} odkazovat
     *
     * @param <T> typ prvků přitomných ve vytvářeném listu
     *
     * @return {@link ReadonlyList} nad daným polem.
     * */
    @SuppressWarnings("unchecked")
    public static<T> ReadonlyList<T> make(T... arr){
        return new ReadonlyList<T>() {
            public T getNth(int n) { return arr[n]; }
            public int size() { return arr.length; }

            public int hashCode() { return Arrays.hashCode(arr); }
            public boolean equals(Object o) { return FormsUtil.equals(o, arr); }
        };
    }

    /**
     * Specializace {@link ReadonlyList} pro surový typ <code>char</code>.
     *
     * @see ReadonlyList
     *
     * @author MarkusSecundus
     * */
    public interface Char extends ReadonlyList<Character>{

        /**
         * @param n index, na kterém se nalézá tázaný prvek
         *
         * @throws IndexOutOfBoundsException pokud je <code>n < 0 || n >= size()</code>
         *
         * @return prvek na n-té pozici v listu
         * */
        public char getNth_raw(int n);

        @Override
        public default Character getNth(int n){
            return getNth_raw(n);
        }

        /**
         * @return pole obsahující prvky daného {@link ReadonlyList.Char}.
         * */
        public default char[] toArray(){
            return FormsUtil.fillArray(new char[size()], this::getNth_raw);
        }

        /**
         * @param arr pole, na které má {@link ReadonlyList.Char} odkazovat
         *
         * @return {@link ReadonlyList} nad daným polem.
         * */
        public static Char make(char... arr){
            return new Char() {
                public char getNth_raw(int n) { return arr[n]; }
                public int size() { return arr.length; }

                public int hashCode() { return Arrays.hashCode(arr); }
                public boolean equals(Object o) { return FormsUtil.equals(o, arr); }
            };
        }
    }

    /**
     * Specializace {@link ReadonlyList} pro surový typ <code>int</code>.
     *
     * @see ReadonlyList
     *
     * @author MarkusSecundus
     * */
    public interface Int extends ReadonlyList<Integer> {
        /**
         * @param n index, na kterém se nalézá tázaný prvek
         *
         * @throws IndexOutOfBoundsException pokud je <code>n < 0 || n >= size()</code>
         *
         * @return prvek na n-té pozici v listu
         * */
        public int getNth_raw(int n);

        @Override
        public default Integer getNth(int n){
            return getNth_raw(n);
        }

        /**
         * @return pole obsahující prvky daného {@link ReadonlyList.Int}.
         * */
        public default int[] toArray(){
            return FormsUtil.fillArray(new int[size()], this::getNth_raw);
        }

        /**
         * @param arr pole, na které má {@link ReadonlyList.Int} odkazovat
         *
         * @return {@link ReadonlyList} nad daným polem.
         * */
        public static Int make(int... arr){
            return new Int() {
                public int getNth_raw(int n) { return arr[n]; }
                public int size() { return arr.length; }

                public int hashCode() { return Arrays.hashCode(arr); }
                public boolean equals(Object o) { return FormsUtil.equals(o, arr); }
            };
        }
    }

    /**
     * Specializace {@link ReadonlyList} pro surový typ <code>long</code>.
     *
     * @see ReadonlyList
     *
     * @author MarkusSecundus
     * */
    public interface Long extends ReadonlyList<java.lang.Long> {
        /**
         * @param n index, na kterém se nalézá tázaný prvek
         *
         * @throws IndexOutOfBoundsException pokud je <code>n < 0 || n >= size()</code>
         *
         * @return prvek na n-té pozici v listu
         * */
        public long getNth_raw(int n);

        @Override
        public default java.lang.Long getNth(int n){
            return getNth_raw(n);
        }

        /**
         * @return pole obsahující prvky daného {@link ReadonlyList.Long}.
         * */
        public default long[] toArray(){
            return FormsUtil.fillArray(new long[size()], this::getNth_raw);
        }

        /**
         * @param arr pole, na které má {@link ReadonlyList.Long} odkazovat
         *
         * @return {@link ReadonlyList} nad daným polem.
         * */
        public static Long make(long... arr){
            return new Long() {
                public long getNth_raw(int n) { return arr[n]; }
                public int size() { return arr.length; }

                public int hashCode() { return Arrays.hashCode(arr); }
                public boolean equals(Object o) { return FormsUtil.equals(o, arr); }
            };
        }
    }



    /**
     * Specializace {@link ReadonlyList} pro surový typ <code>float</code>.
     *
     * @see ReadonlyList
     *
     * @author MarkusSecundus
     * */
    public interface Float extends ReadonlyList<java.lang.Float> {
        /**
         * @param n index, na kterém se nalézá tázaný prvek
         *
         * @throws IndexOutOfBoundsException pokud je <code>n < 0 || n >= size()</code>
         *
         * @return prvek na n-té pozici v listu
         * */
        public float getNth_raw(int n);

        @Override
        public default java.lang.Float getNth(int n){
            return getNth_raw(n);
        }

        /**
         * @return pole obsahující prvky daného {@link ReadonlyList.Float}.
         * */
        public default float[] toArray(){
            return FormsUtil.fillArray(new float[size()], this::getNth_raw);
        }

        /**
         * @param arr pole, na které má {@link ReadonlyList.Float} odkazovat
         *
         * @return {@link ReadonlyList} nad daným polem.
         * */
        public static Float make(float... arr){
            return new Float() {
                public float getNth_raw(int n) { return arr[n]; }
                public int size() { return arr.length; }

                public int hashCode() { return Arrays.hashCode(arr); }
                public boolean equals(Object o) { return FormsUtil.equals(o, arr); }
            };
        }
    }

    /**
     * Specializace {@link ReadonlyList} pro surový typ <code>double</code>.
     *
     * @see ReadonlyList
     *
     * @author MarkusSecundus
     * */
    public interface Double extends ReadonlyList<java.lang.Double> {
        /**
         * @param n index, na kterém se nalézá tázaný prvek
         *
         * @throws IndexOutOfBoundsException pokud je <code>n < 0 || n >= size()</code>
         *
         * @return prvek na n-té pozici v listu
         * */
        public double getNth_raw(int n);

        @Override
        public default java.lang.Double getNth(int n){
            return getNth_raw(n);
        }

        /**
         * @return pole obsahující prvky daného {@link ReadonlyList.Double}.
         * */
        public default double[] toArray(){
            return FormsUtil.fillArray(new double[size()], this::getNth_raw);
        }

        /**
         * @param arr pole, na které má {@link ReadonlyList.Double} odkazovat
         *
         * @return {@link ReadonlyList} nad daným polem.
         * */
        public static Double make(double... arr){
            return new Double() {
                public double getNth_raw(int n) { return arr[n]; }
                public int size() { return arr.length; }

                public int hashCode() { return Arrays.hashCode(arr); }
                public boolean equals(Object o) { return FormsUtil.equals(o, arr); }
            };
        }

    }
}

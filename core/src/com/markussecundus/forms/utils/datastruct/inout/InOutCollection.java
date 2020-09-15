package com.markussecundus.forms.utils.datastruct.inout;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;



/**
 * Rozhraní pro kolekci objektů, kde lze pouze přidávat na `konec` a odebírat ze `začátku`.
 * <p>
 * Abstrakce sdružující všemožné zásobníky, fronty, prioritní fronty apod. .
 *
 * @author MarkusSecundus
 * */
public interface InOutCollection<T> {

    /**
     * Odebere a navrátí prvek, jež je právě na řadě.
     *
     * @return právě odebraný prvek, příp. <code>null</code>, pokud je kolekce prázdná.
     * */
    public T removeNext();

    /**
     * @return zda je kolekce prázdná
     * */
    public boolean isEmpty();

    /**
     * Přidá do kolekce daný prvek.
     *
     * @param item prvek, jenž má být přidán
     * */
    public void add(T item);

    /**
     * Odebere z kolekce všechny prvky - zařídí, aby byla prázdná.
     * */
    public default void clear(){
        while (!isEmpty())removeNext();
    }


    /**
     * Jednoduchá implementace zásobníku jakožto {@link InOutCollection}
     * nad {@link java.util.List}em.
     * */
    public static class Stack<T> implements InOutCollection<T>{
        /**
         * Postaví zásobník nad daným {@link java.util.List}em.
         * */
        public Stack(List<T> base){
            this.base = base;
        }

        /**
         * Postaví zásobník nad novým {@link java.util.ArrayList}em.
         * */
        public Stack(){ this(new ArrayList<>()); }

        /**
         * {@link List} na který tento zásobník odkazuje.
         * */
        public final List<T> base;

        @Override
        public T removeNext() {
            return base.remove(base.size()-1);
        }

        @Override
        public boolean isEmpty() {
            return base.isEmpty();
        }

        @Override
        public void add(T item) {
            base.add(item);
        }

        @Override
        public void clear() {
            base.clear();
        }
    }


    /**
     * Jednoduchá implementace fronty jakožto {@link InOutCollection}
     * nad {@link java.util.Queue}.
     * */
    public static class Queue<T> implements InOutCollection<T>{
        /**
         * Postaví frontu nad danou {@link java.util.Queue}.
         * */
        public Queue(java.util.Queue<T> base){
            this.base = base;
        }
        /**
         * Postaví frontu nad novou {@link java.util.ArrayDeque}.
         * */
        public Queue(){this(new ArrayDeque<>());}

        /**
         * {@link Queue} na kterou tento zásobník odkazuje.
         * */
        public final java.util.Queue<T> base;

        @Override
        public T removeNext() {
            return base.poll();
        }

        @Override
        public boolean isEmpty() {
            return base.isEmpty();
        }

        @Override
        public void add(T item) {
            base.add(item);
        }

        @Override
        public void clear() {
            base.clear();
        }
    }
}

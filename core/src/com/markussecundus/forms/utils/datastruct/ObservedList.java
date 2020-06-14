package com.markussecundus.forms.utils.datastruct;

import java.util.*;


/**
 * Wrapper nad {@link List}, která sleduje modifikace jeho prvků a poskytuje listenery těchto událostíss.
 *
 * @param <T> typ objektů ve sledovaném {@link List}u
 *
 * @author MarkusSecundus
 * */
public abstract class ObservedList<T> implements List<T> {
//public:
    /**
     * Vytvoří wrapper nad danou bází.
     *
     * @param baseList báze, na kterou bude wrapper ukazovat
     * */
    public ObservedList(List<T> baseList){this.base = baseList;}

    /**
     * Volána kdykoliv je objekt přidán do {@link List}u.
     *
     * Volána poté, co je objekt přidán.
     *
     * @param t nově přidaný objekt
     * @param index index na který byl nový objekt přidán nebo <code>-1</code>, pokud je index neznámý
     * */
    protected abstract void onAdded(T t, int index);

    /**
     * Volána kdykoliv je vyžádáno odstranění nějakého objektu.
     *
     * Volána před pokusem o odstranění.
     *
     * @param t objekt vyžádaný k odstranění
     * */
    protected abstract void onDelete(Object t);

    /**
     * Volána kdykoliv je vyžádána změna hodnoty na určité pozici za jinou.
     *
     * @param index pozice na které jsou hodnoty měněny nebo <code>-1</code>, pokud je neznámá
     * @param oldElem původní hodnota na dané pozici
     * @param newElem nová hodnota na dané pozici
     * */
    protected abstract void onSet(T oldElem, T newElem, int index);

    /**
     * Volána před každým voláním metody <code>clear()</code>.
     * Defaultně pro všechny prvky v tomto listu zavolá <code>onDelete</code>.
     * */
    protected void onClear(){
        for(Object v: base)
            onDelete(v);
    }

    /**
     * Volána při každém pokusu o odstranění množiny prvků.
     * Defaultně pro všechny prvky v odebírané {@link Collection} zavolá <code>onDelete</code>.
     *
     * @param col kolekce odstranovaných prvků
     * */
    protected void onRemoveAll(Collection<?> col){
        for(Object v:col)
            onDelete(v);
    }


    /**
     * Varianta s prázdnými listener-metodami pro větší pohodlí když není potřeba
     * přepisovat všechny.
     *
     * @param <T> typ objektů ve sledovaném {@link List}u
     *
     * @author MarkusSecundus
     * */
    public static class Blank<T> extends ObservedList<T>{
        public Blank(List<T> baseList){super(baseList);}
        /**{@inheritDoc}*/@Override protected void onAdded(T t, int index) { }
        /**{@inheritDoc}*/@Override protected void onDelete(Object t) { }
        /**{@inheritDoc}*/@Override protected void onSet(T oldElem, T newElem, int index) { }
    }


    /**{@inheritDoc}*/@Override public int size() {
        return base.size();
    }

    /**{@inheritDoc}*/@Override public boolean isEmpty() {
        return base.isEmpty();
    }

    /**{@inheritDoc}*/@Override public boolean contains(Object o) {
        return base.contains(o);
    }

    /**{@inheritDoc}*/@Override public Iterator<T> iterator() {
        return base.iterator();
    }

    /**{@inheritDoc}*/@Override public Object[] toArray() {
        return base.toArray();
    }

    /**{@inheritDoc}*/@Override public <T1> T1[] toArray(T1[] a) {
        return base.toArray(a);
    }



    /**{@inheritDoc}*/@Override public boolean add(T t) {
        boolean ret = base.add(t);
        onAdded(t, base.size()-1);
        return ret;
    }

    /**{@inheritDoc}*/@Override public boolean remove(Object o) { //TODO: udělat aby to fungovalo správně!
        boolean ret = base.remove(o);
        if(ret) onDelete(o);
        return ret;
    }

    /**{@inheritDoc}*/@Override public boolean containsAll(Collection<?> c) {
        return base.containsAll(c);
    }

    /**{@inheritDoc}*/@Override public boolean addAll(Collection<? extends T> c) {
        int i = base.size();
        boolean ret = base.addAll(c);
        for(T v:c)
            onAdded(v, i++);
        return ret;
    }

    /**{@inheritDoc}*/@Override public boolean addAll(int index, Collection<? extends T> c) {
        boolean ret = base.addAll(index, c);
        for(T v:c)
            onAdded(v, index++);
        return ret;
    }

    /**{@inheritDoc}*/@Override public boolean removeAll(Collection<?> c) {
        onRemoveAll(c);
        return base.removeAll(c);
    }

    /**{@inheritDoc}*/@Override public boolean retainAll(Collection<?> col) {
        Collection<?> map = new HashSet<>(col);

        int probableApproximateSize = base.size()-col.size();

        Collection<T> pomList = new HashSet<>(probableApproximateSize);
        for(T v:base)
            if(!map.contains(v))
                pomList.add(v);

        return removeAll(pomList);
    }

    /**{@inheritDoc}*/@Override public void clear() {
        onClear();
        base.clear();
    }

    /**{@inheritDoc}*/@Override public T get(int index) {
        return base.get(index);
    }

    /**{@inheritDoc}*/@Override public T set(int index, T element) {
        T ret = base.set(index, element);
        onSet(ret, element, index);
        return ret;
    }

    /**{@inheritDoc}*/@Override public void add(int index, T element) {
        base.add(index,element);
        onAdded(element, index);
    }

    /**{@inheritDoc}*/@Override public T remove(int index) {
        T ret = base.remove(index);
        onDelete(ret);
        return ret;
    }

    /**{@inheritDoc}*/@Override public int indexOf(Object o) {
        return base.indexOf(o);
    }

    /**{@inheritDoc}*/@Override public int lastIndexOf(Object o) {
        return base.lastIndexOf(o);
    }

    /**{@inheritDoc}*/@Override public ListIterator<T> listIterator() {
        return this.listIterator(0);
    }

    /**{@inheritDoc}*/@Override public ListIterator<T> listIterator(int index) {
        return new ListIterator<T>() {
            //public:

            /**{@inheritDoc}*/@Override public boolean hasNext() {
                return it.hasNext();
            }

            /**{@inheritDoc}*/@Override public T next() {
                ++currentIndex;
                return lastObserved = it.next();
            }

            /**{@inheritDoc}*/@Override public boolean hasPrevious() {
                return it.hasPrevious();
            }

            /**{@inheritDoc}*/@Override public T previous() {
                --currentIndex;
                return lastObserved = it.previous();
            }

            /**{@inheritDoc}*/@Override public int nextIndex() {
                lastObserved = base.get(currentIndex = it.nextIndex());
                return currentIndex;
            }

            /**{@inheritDoc}*/@Override public int previousIndex() {
                lastObserved = base.get(currentIndex = it.previousIndex());
                return currentIndex;
            }

            /**{@inheritDoc}*/@Override public void remove() {    //TODO: nejsem si jistý, jestli tímhle nerozbiju svoje počítání indexů
                it.remove();
                onDelete(lastObserved);
            }

            /**{@inheritDoc}*/@Override public void set(T t) {
                it.set(t);
                onSet(lastObserved, t,-1);
            }

            /**{@inheritDoc}*/@Override public void add(T t) {    //TODO: nejsem si jistý, jestli tímhle nerozbiju svoje počítání indexů
                it.add(t);
                onAdded(t, currentIndex);
            }

            //private:

            private ListIterator<T> it = base.listIterator(index);
            private T lastObserved;
            private int currentIndex = index;
        };
    }

    /**{@inheritDoc}*/
    @Override public List<T> subList(int fromIndex, int toIndex) { //TODO: zamyslet se jestli to takhle vážně má být
        return new ObservedList<T>(base.subList(fromIndex, toIndex)){
            //public:
            /**{@inheritDoc}*/@Override public void onAdded(T t, int index) {
                ObservedList.this.onAdded(t, realIndex(index));
            }

            /**{@inheritDoc}*/@Override public void onDelete(Object t) {
                ObservedList.this.onDelete(t);
            }

            /**{@inheritDoc}*/@Override public void onSet(T oldElem, T newElem, int index) {
                ObservedList.this.onSet(oldElem, newElem, realIndex(index));
            }

            //private:
            private int realIndex(int i){return i + fromIndex;}
        };
    }

    /**{@inheritDoc}*/@Override public int hashCode() {
        return base.hashCode();
    }

    /**{@inheritDoc}*/@Override public boolean equals(Object obj) {
        return base.equals(obj);
    }

    /**{@inheritDoc}*/@Override public String toString() {
        return base.toString();
    }

    //private:
    public final List<T> base;
}

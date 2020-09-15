package com.markussecundus.forms.utils.datastruct;

import com.markussecundus.forms.utils.function.IntFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * {@link List}ová obdova {@link DefaultDict}.
 *
 * Wrapper nad libovolným {@link List}. Navíc má určen generátor výchozích hodnot.
 * Je-li tázána hodnota pro neobsazený index, pomocí této funkce je pro
 * všechny indexy až do tázaného hodnota vygenerována.
 *
 * @param <T> typ pro hodnoty uložené v {@link List}u
 *
 * @see DefaultDictByIdentity
 *
 * @author MarkusSecundus
 * */
public class DefaultList<T> implements List<T> {

    /**
     * Vytvoří instanci wrapperu nad daným objektem {@link List} a používající daný generátor
     * výchozích hodnot.
     *
     * @param base instance {@link List}, nad kterou bude tento objekt fungovat jako wrapper
     * @param supplier generátor výchozích hodnot; jako parametr přebírá index, pro který je hodnota generována
     * */
    public DefaultList(List<T> base, IntFunction<T> supplier){
        this.base = base;
        this.supplier = supplier;
    }
    /**
     * Vytvoří instanci wrapperu nad novou, prázdnou instancí {@link List} a používající daný generátor
     * výchozích hodnot.
     *
     * @param supplier generátor výchozích hodnot; jako parametr přebírá index, pro který je hodnota generována
     * */
    public DefaultList(IntFunction<T> supplier){
        this(DEF_LIST_INSTANCE_FACTORY(), supplier);
    }


    /**
     * @return nová instance {@link List}, nad kterou je defaultně {@link DefaultList} postaven,
     * není-li udáno jinak
     * */
    public static<T> List<T> DEF_LIST_INSTANCE_FACTORY(){return new ArrayList<>();}

    /**
     * Báze, na kterou wrapper odkazuje.
     * */
    public final List<T> base;

    /**
     * Generátor výchozích hodnot.
     * */
    private final IntFunction<T> supplier;



    @Override
    public T get(int i) {
        addElemsUpTo(i);
        return base.get(i);
    }

    @Override
    public T set(int i, T t) {
        addElemsUpTo(i);
        return base.get(i);
    }

    @Override
    public void add(int i, T t) {
        addElemsUpTo(i-1);
        base.add(i, t);
    }



    private void addElemsUpTo(int requiredIndex){
        for(int t = size(); t<= requiredIndex; ++t)
            base.add(supplier.apply(t));
    }







    @Override
    public int size() {
        return base.size();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return base.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return base.iterator();
    }

    @Override
    public Object[] toArray() {
        return base.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return base.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        return base.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return base.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return base.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return base.addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        return base.addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return base.retainAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return base.retainAll(collection);
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public T remove(int i) {
        return base.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return base.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return base.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return base.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return base.listIterator(i);
    }

    @Override
    public List<T> subList(int i, int i1) {
        return new DefaultList<>(base.subList(i, i1), supplier);
    }

}

package com.markussecundus.forms.utils.datastruct.redirected;

import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.function.Function;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;


/**
 * Báze pro všechny wrappery nad kolekčními typy, jež přesměrovávají na kolekce objektů jiného datového typu.
 * <p>
 * Poskytuje základní funkcionalitu pro přesměrování volání na {@link java.util.Collection}.
 *
 * @param <From> typ, jímž je parametrizována bazická kolekce.
 * @param <To>> typ, na nějž je kolekce přesměrována
 * @param <Base> typ bazické kolekce, k specifikaci v jednotlivých konkrétních podtřídách
 *
 * @see RedirectedCollection
 * @see RedirectedSet
 *
 * @author MarkusSecundus
 * */
public class RedirectedCollectionGeneric<From, To, Base extends Collection<From>> implements Collection<To> {


    /**
     * Vytvoří přesměrovávanou kolekci nad danou bází a s danými převodními funkcemi.
     *
     * @param base Vnitřní implementace, k jejímuž přesměrování dojde.
     * @param convertor Funkce provádějící konverzi objektů bazického datového typu na objekty typu cílového.
     * @param backwardsConvertor Inverzní funkce k <code>convertor</code>u.
     * */
    public RedirectedCollectionGeneric(Base base, Function<From, To> convertor, Function<To, From> backwardsConvertor){
        this.base = base;
        this.convertor = convertor;
        this.backwardsConvertor = backwardsConvertor;
    }



    /**
     * Vnitřní implementace, k jejímuž přesměrování dochází.
     * */
    public final Base base;

    /**
     * Funkce provádějící konverzi objektů bazického datového typu na objekty typu cílového.
     * */
    public final Function<From, To> convertor;

    /**
     * Inverzní funkce k <code>convertor</code>u.
     * */
    public final Function<To, From> backwardsConvertor;



    protected final To conv(From f){return convertor.apply(f);}
    protected final To convObj(Object o){
        try{
            return conv((From) o);
        }catch (Exception e){
            return (To) o;
        }
    }

    protected final From convBack(To t){return backwardsConvertor.apply(t);}
    protected final From convBackObj(Object o){
        try{
            return convBack((To) o);
        }catch(Exception e){
            return (From) o;
        }
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
        return base.contains(convObj(o));
    }

    @Override
    public Iterator<To> iterator() {
        return new Iterator<To>() {
            Iterator<From> it = base.iterator();

            public boolean hasNext() { return it.hasNext(); }
            public To next() { return conv(it.next()); }
        };
    }

    @Override
    public Object[] toArray() {
        return FormsUtil.transformInPlace(base.toArray(), this::convObj);
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return  FormsUtil.transform(base.toArray(), (T[])Array.newInstance(ts.getClass().getComponentType(), size()), o->(T)convObj(o));
    }

    @Override
    public boolean add(To to) {
        return base.add(convBack(to));
    }

    @Override
    public boolean remove(Object o) {
        return base.remove(convBackObj(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for(Object o: collection) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends To> collection) {
        boolean ret = false;

        for(To t:collection)
            ret |= add(t);

        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return base.retainAll(new RedirectedCollection(collection, this::convBackObj, this::convObj));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean ret = false;

        for(Object o: collection)
            ret |= remove(o);

        return ret;
    }

    @Override
    public void clear() {
        base.clear();
    }
}

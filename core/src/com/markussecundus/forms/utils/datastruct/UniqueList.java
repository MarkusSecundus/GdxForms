package com.markussecundus.forms.utils.datastruct;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Wrapper nad {@link List}em, která zaručuje neduplicitnost a rychlé testování prvků na jejich přítomnost.
 *
 * Provádí to tak, že si vedle bázového {@link List}u vede ještě instanci {@link Set}u,
 * kam si prvky taktéž poznamenává.
 *
 * @param <T> typ prvků, které jsou v listu uloženy
 *
 * @author MarkusSecundus
 * */
public class UniqueList<T> extends ObservedList<T> {

    /**
     * Pomocná kolekce, kde jsou poznamenávány všechny prvky.
     * */
    protected final Set<T> baseSet;


    /**
     * Vytvoří novou instanci ukazující na daný {@link List} s daným pomocným {@link Set}em.
     *
     * @param baseList list, na který tato wrapper ukazuje
     * @param baseSet pomocná kolekce, do které budou poznamenávány všechny prvky
     *                pokud se v ní již nalézají nějaké prvky, budou odstraněny
     * */
    public UniqueList(List<T> baseList, Set<T> baseSet){
        super(baseList);
        this.baseSet = baseSet;
        baseSet.clear();
        int i = 0;
        for(T t:baseList)
            onAdded(t, i++);
    }


    /**
     * Zaručí, že přidávaný prvek v listu není přítomný
     *
     * @throws ItemAlreadyPresentException
     * */
    @Override protected final void onAdded(T t, int index) {
        if(baseSet.contains(t))
            throw new ItemAlreadyPresentException(t, index);
        baseSet.add(t);
    }

    @Override protected final void onDelete(Object t) {
        baseSet.remove(t);
    }

    @Override protected final void onSet(T oldElem, T newElem, int index) {
        onDelete(oldElem);
        onAdded(newElem, index);
    }

    /**
     * Výjimka, vyhozená v případě, že se v listu již nalézá prvek ekvivalentní s právě přidávaným.
     *
     * @author MarkusSecundus
     * */
    public static class ItemAlreadyPresentException extends RuntimeException{

        /**
         * Přidávaný objekt.
         * */
        public final Object item;

        /**
         * Pozice, na kterou byl objekt právě přidáván.
         * */
        public final int index;

        public ItemAlreadyPresentException(Object item, int index){this.item = item; this.index = index;}
    }



    @Override protected void onClear() {
        baseSet.clear();
    }

    /**
     * Probíhá rychlým způsobe přes pomocnou instanci {@link Set}.
     *
     * {@inheritDoc}
     * */
    @Override public boolean contains(Object o) {
        return baseSet.contains(o);
    }

    /**
     * Probíhá rychlým způsobe přes pomocnou instanci {@link Set}.
     *
     * {@inheritDoc}
     * */
    @Override public boolean containsAll(Collection<?> c) {
        return baseSet.containsAll(c);
    }
}

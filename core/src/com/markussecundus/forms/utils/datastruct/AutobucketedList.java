package com.markussecundus.forms.utils.datastruct;

import java.util.Comparator;
import java.util.List;


/**
 * Rozhraní pro seřazený list prvků, jenž je podélně rozdělen na přihrádky.
 *
 * @param <T> typ prvků v listu
 * @param <K> typ, jímž jsou značeny jednotlivé přihrádky
 *
 *
 * @see IAutobucketedList
 *
 * @author MarkusSecundus
 * */
public interface AutobucketedList<T, K>{

    /**
     * Vrátí celý vnitřně popřihrádkovaný list, ve variantě pouze pro čtení.
     * */
    public ReadonlyList<T> getBase();

    /**
     * @return podlist náležicí dané přihrádce (podlist nulové délky, pokud takový dosud neexistoval)
     * */
    public List<T> getBucket(K bucket);

    /**
     * Odebere prvek na i-té pozici v celém listu, bez ohledu na to, v jaké se nalézá přihrádce.
     *
     * @param i index (brán globálně vůči celému listu, indexováno od 0), na němž se nachází prvek k odebrání
     *
     * @throws IndexOutOfBoundsException pokud je i záporné, nebo větší než délka listu
     * */
    public T remove(int i);

    /**
     * Najde daný prvek kdekoliv v celém listu (prvek, pro nějž <code>Objects.equals(o, ?)</code>) (v libovolné přihrádce) a odebere jeho první výskyt.
     *
     * @param o prvek k odebrání
     * */
    public boolean remove(Object o);

    /**
     * Odstraní všechny prvky ze všech přihrádek.
     * */
    public void clear();

    /**
     * @param comp řadič, podle kterého jsou řazeny jednotlivé přihrádky
     *             v závislosti na jejich klíčích
     *
     * @return instance kanonické implementace {@link AutobucketedList}
     * používající daný komparátor pro řazení přihrádek.
     * */
    public static<T,K> AutobucketedList<T,K> make(Comparator<K> comp){
        return new IAutobucketedList<>(comp);
    }
    /**
     * @return instance kanonické implementace {@link AutobucketedList}
     * používající standardní komparátor pro řazení přihrádek.
     * */
    public static<T, K extends Comparable<K>> AutobucketedList<T,K> make(){
        return make(Comparable::compareTo);
    }
}

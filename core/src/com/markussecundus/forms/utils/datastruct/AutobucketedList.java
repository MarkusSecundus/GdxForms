package com.markussecundus.forms.utils.datastruct;

import java.util.Comparator;
import java.util.List;

public interface AutobucketedList<T, K>{

    public List<? extends T> getBase();

    public List<T> getBucket(K bucket);


    public T remove(int t);


    public static<T,K> AutobucketedList<T,K> make(Comparator<K> comp){
        return new IAutobucketedList<>(comp);
    }

    public static<T, K extends Comparable<K>> AutobucketedList<T,K> make(){
        return make(Comparable::compareTo);
    }
}

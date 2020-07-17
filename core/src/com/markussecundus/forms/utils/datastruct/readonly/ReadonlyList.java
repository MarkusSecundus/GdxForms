package com.markussecundus.forms.utils.datastruct.readonly;

import java.util.List;

public interface ReadonlyList<T> {
    public T getNth(int pos);

    public int size();



    @SuppressWarnings("unchecked")
    public static<T> ReadonlyList<T> make(T... arr){
        return new ReadonlyList<T>() {
            @Override
            public T getNth(int pos) {
                return arr[pos];
            }

            @Override
            public int size() {
                return arr.length;
            }
        };
    }

    public static<T> ReadonlyList<T> make(List<T> col){
        return new ReadonlyList<T>() {
            @Override
            public T getNth(int pos) {
                return col.get(pos);
            }

            @Override
            public int size() {
                return col.size();
            }
        };
    }
}

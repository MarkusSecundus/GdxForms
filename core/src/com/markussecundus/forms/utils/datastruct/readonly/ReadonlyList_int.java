package com.markussecundus.forms.utils.datastruct.readonly;

public interface ReadonlyList_int {
    public int getNth(int pos);

    public int size();


    public static ReadonlyList_int make(int... arr){
        return new ReadonlyList_int() {
            @Override
            public int getNth(int pos) {
                return arr[pos];
            }

            @Override
            public int size() {
                return arr.length;
            }
        };
    }
}

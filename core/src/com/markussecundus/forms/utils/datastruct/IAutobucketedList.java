package com.markussecundus.forms.utils.datastruct;

import com.badlogic.gdx.Gdx;
import com.markussecundus.forms.utils.FormsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IAutobucketedList<T,K> implements AutobucketedList<T,K> {

    public IAutobucketedList(List<T> base, Comparator<K> ordering){
        if(!base.isEmpty())
            throw new IllegalArgumentException("Base list must be empty!");
        this.base = base;
        this.orderer = ordering;
    }

    public IAutobucketedList(Comparator<K> ordering){
        this(DEF_LIST_GENERATOR(), ordering);
    }



    @Override
    public List<? extends T> getBase() {
        return base;
    }

    @Override
    public T remove(int t) {

        int stara_pozice_v_listu = 0;
        for(int i = 0;i < bucketKeys.size();++i){
            int pozice_v_listu = bucketKeys.get(i).index;

            if(pozice_v_listu> t)
                return getBucket(i).remove( t - stara_pozice_v_listu);

            stara_pozice_v_listu = pozice_v_listu;
        }
        return null;
    }

    @Override
    public List<T> getBucket(K bucket) {
;
        int i = FormsUtil.binarySearchNearest(bucketKeys, bucket, (a,b)-> orderer.compare(a,b.key));

        if(i<0){
            bucketKeys.add(0, new KeyIndexPair<>(bucket, 0));
            i = 0;
        }else if(i>=bucketKeys.size()){
            i = bucketKeys.size();
            bucketKeys.add(new KeyIndexPair<>(bucket, base.size()));
        }else{
            int cmp = orderer.compare(bucketKeys.get(i).key, bucket);
            switch (cmp){
                case -1:
                    bucketKeys.add(i, new KeyIndexPair<>(bucket, i>0?bucketKeys.get(i-1).index:0));
                    break;
                case 1:
                    bucketKeys.add(i + 1, new KeyIndexPair<>(bucket, bucketKeys.get(i).index));
                    ++i;
                    break;
            }
        }

        return getBucket(i);
    }

    protected List<T> getBucket(int bucketIndex){

        final int beg = bucketIndex>0? bucketKeys.get(bucketIndex-1).index : 0;
        final int endd = bucketKeys.get(bucketIndex).index;
        try {
            return new ObservedList.Blank<T>(base.subList(beg, endd)) {
                @Override
                protected void onAdded(T t, int index) {
                    modifyIndices(1);
                }

                @Override
                protected void onDelete(Object t) {
                    modifyIndices(-1);
                }

                private void modifyIndices(int sum) {
                    for (int i = bucketIndex; i < bucketKeys.size(); ++i)
                        bucketKeys.get(i).index += sum;
                }
            };
        }catch (Exception e){
            Gdx.app.log("rr", String.format("%d - %d", beg, endd));
            throw e;
        }
    }


    protected static<T> List<T> DEF_LIST_GENERATOR(){return new ArrayList<>();}


    private final List<T> base;

    private final Comparator<K> orderer;

    private final List<KeyIndexPair<K>> bucketKeys = new ArrayList<>();

    private final static class KeyIndexPair<K>{

        public KeyIndexPair(K key, int index) {
            this.key = key;
            this.index = index;
        }

        public final K key;
        public int index;
    }
}

package com.markussecundus.forms.utils.datastruct;

import com.badlogic.gdx.Gdx;
import com.markussecundus.forms.utils.FormsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

public class IAutobucketedList<T,K> implements AutobucketedList<T,K> {


    private final List<T> base;

    private final Comparator<K> orderer;

    private final List<BucketInfo<K>> bucketList = new ArrayList<>();




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


    protected int findBucketForIndex(int t){
        for(int i = 0; i < bucketList.size(); ++i)
            if(bucketList.get(i).endIndex > t)
                return i;

        return -1;
    }


    @Override
    public T remove(int t) {

        int kyblik = findBucketForIndex(t);
        if(kyblik < 0)
            return null;

        shiftIndices(kyblik, -1);

        return base.remove(t);
    }

    @Override
    public boolean remove(Object t){
        int i = base.indexOf(t);
        if(i>=0){
            remove(i);
            return true;
        }
        return false;
    }


    @Override
    public void clear(){
        base.clear();
        bucketList.clear();
    }

    @Override
    public List<T> getBucket(K bucket) {
;
        int i = FormsUtil.binarySearchNearest(bucketList, bucket, (a, b)-> orderer.compare(a,b.bucketKey));

        if(i<0){
            bucketList.add(0, new BucketInfo<>(bucket, 0));
            i = 0;
        }else if(i>= bucketList.size()){
            i = bucketList.size();
            bucketList.add(new BucketInfo<>(bucket, base.size()));
        }else{
            int cmp = orderer.compare(bucketList.get(i).bucketKey, bucket);
            switch (cmp){
                case -1:
                    bucketList.add(i, new BucketInfo<>(bucket, i>0? bucketList.get(i-1).endIndex :0));
                    break;
                case 1:
                    bucketList.add(i + 1, new BucketInfo<>(bucket, bucketList.get(i).endIndex));
                    ++i;
                    break;
            }
        }

        return getBucket(i);
    }

    protected List<T> getBucket(int bucketIndex){

        final int beg = bucketIndex>0? bucketList.get(bucketIndex-1).endIndex : 0;
        final int endd = bucketList.get(bucketIndex).endIndex;
        try {
            return new ObservedList.Blank<T>(base.subList(beg, endd)) {
                @Override
                protected void onAdded(T t, int index) {
                    shiftIndices(bucketIndex,1);
                }

                @Override
                protected void onDelete(Object t) {
                    shiftIndices(bucketIndex, -1);
                }

                @Override
                protected void onClear() {
                    shiftIndices(bucketIndex, - base.size());
                }
            };
        }catch (Exception e){
            Gdx.app.log("rr", String.format("%d - %d", beg, endd));
            throw e;
        }
    }

    protected void shiftIndices(int beginBucketIndex, int shiftAmmount){
        for (int i = beginBucketIndex; i < bucketList.size(); ++i)
            bucketList.get(i).endIndex += shiftAmmount;
    }



    private final static class BucketInfo<K>{

        public BucketInfo(K bucketKey, int index) {
            this.bucketKey = bucketKey;
            this.endIndex = index;
        }

        public final K bucketKey;
        public int endIndex;
    }





    protected static<T> List<T> DEF_LIST_GENERATOR(){return new ArrayList<>();}
}

package com.markussecundus.forms.utils.datastruct;

import com.badlogic.gdx.Gdx;
import com.markussecundus.forms.utils.FormsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Kanonická implementace {@link AutobucketedList}.
 *
 * @see AutobucketedList
 *
 * @author MarkusSecundus
 * */
public class IAutobucketedList<T,K> implements AutobucketedList<T,K> {


    private final List<T> base;

    private final ReadonlyList<T> base_readonly;

    private final Comparator<K> orderer;

    private final List<BucketInfo<K>> bucketList = new ArrayList<>();



    /**
     * Vytvoří přihrádkovaný list nad danou instancí {@link List}u a s daným použitým řazením.
     *
     * @param base Bazický list - musí být prázdný
     * @param ordering řazení použité pro řazení přihrádek v závislosti na jejich klíči
     *
     * @throws IllegalArgumentException pokud dodávaná báze není prázdná
     * */
    public IAutobucketedList(List<T> base, Comparator<K> ordering){
        if(!base.isEmpty())
            throw new IllegalArgumentException("Base list must be empty!");
        this.base = base;
        this.base_readonly = ReadonlyList.make(base);
        this.orderer = ordering;
    }

    /**
     * Vytvoří přihrádkovaný list nad novou instancí {@link List}u a s daným použitým řazením.
     *
     * @param ordering řazení použité pro řazení přihrádek v závislosti na jejich klíči
     * */
    public IAutobucketedList(Comparator<K> ordering){
        this(DEF_LIST__FACTORY(), ordering);
    }



    @Override
    public ReadonlyList<T> getBase() {
        return base_readonly;
    }


    private int findBucketForIndex(int t){
        for(int i = 0; i < bucketList.size(); ++i)
            if(bucketList.get(i).endIndex > t)
                return i;

        return -1;
    }


    @Override
    public T remove(int i) {

        int kyblik = findBucketForIndex(i);
        if(kyblik < 0)
            return null;

        shiftIndices(kyblik, -1);

        return base.remove(i);
    }

    @Override
    public boolean remove(Object o){
        int i = base.indexOf(o);
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

    private List<T> getBucket(int bucketIndex){

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

    private void shiftIndices(int beginBucketIndex, int shiftAmmount){
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




    /**
     * @return nová instance {@link List}, nad kterou je defaultně {@link IAutobucketedList} postaven,
     * není-li udáno jinak
     * */
    public static<T> List<T> DEF_LIST__FACTORY(){return new ArrayList<>();}
}

package com.markussecundus.forms.utils;

import com.markussecundus.forms.utils.vector.VectDecomposer;

import java.util.Arrays;


/**
 * Statická třída s rozličnými pomocnými funkcemi a konstantami,
 * které byly někde v programu potřeba a nenašly si lepší místo jinde.
 *
 *
 *
 * @author MarkusSecundus
 * */
public class FormsUtil {

    /**
     * @return větší z obou vzájemně porovnatelných čísel
     * */
    public static<T extends Comparable<T>> T max(T a, T b){
        return a.compareTo(b)>=0?a:b;
    }
    /**
     * @return menší z obou vzájemně porovnatelných čísel
     * */
    public static<T extends Comparable<T>> T min(T a, T b){
        return a.compareTo(b)<=0?a:b;
    }


    /**
     * @return větší z posloupnosti vzájemně porovnatelných čísel
     * */
    @SafeVarargs
    public static<T extends Comparable<T>> T max(T... args){
        if(args.length<=0) return null;
        T ret = args[0];
        for(T t :args) {
            if (ret.compareTo(t) < 0) ret = t;
        }
        return ret;
    }

    /**
     * @return menší z posloupnosti vzájemně porovnatelných čísel
     * */
    @SafeVarargs
    public static<T extends Comparable<T>> T min(T... args){
        if(args.length<=0) return null;
        T ret = args[0];
        for(T t :args) {
            if (ret.compareTo(t) > 0) ret = t;
        }
        return ret;
    }

    /**
     * @return zda je prostřední hodnota v intervalu daném hodnotami krajními
     * */
    public static<T> boolean isClosedRange(Comparable<T> min, T middle, Comparable<T> max){
        return min.compareTo(middle)<=0 && max.compareTo(middle)>=0;
    }

    /**
     * @return prostřední hodnota po jejím natěsnání do intervalu daného hodnotami krajními
     * */
    public static float intoBounds(float min, float val, float max){
        return Math.min(Math.max(min, val),max);
    }

    /**
     * Naplní dané pole danou hodnotou.
     *
     * @param arr pole k naplnění
     * @param val hodnota kterou má být naplněno
     *
     *
     * @return vstupní pole - pro účely řetězení příkazů
     * */
    public static<T> T[] fillArray(T[] arr, T val){
        Arrays.fill(arr, val);
        return arr;
    }
    /**
     * Naplní dané pole danou hodnotou.
     *
     * @param arr pole k naplnění
     * @param val hodnota kterou má být naplněno
     *
     *
     * @return vstupní pole - pro účely řetězení příkazů
     * */
    public static char[] fillArray(char[] arr, char val){
        Arrays.fill(arr, val);
        return arr;
    }
    /**
     * Naplní dané pole danou hodnotou.
     *
     * @param arr pole k naplnění
     * @param val hodnota kterou má být naplněno
     *
     *
     * @return vstupní pole - pro účely řetězení příkazů
     * */
    public static int[] fillArray(int[] arr, int val){
        Arrays.fill(arr, val);
        return arr;
    }
    /**
     * Naplní dané pole danou hodnotou.
     *
     * @param arr pole k naplnění
     * @param val hodnota kterou má být naplněno
     *
     *
     * @return vstupní pole - pro účely řetězení příkazů
     * */
    public static float[] fillArray(float[] arr, float val){
        Arrays.fill(arr, val);
        return arr;
    }
    /**
     * Naplní dané pole danou hodnotou.
     *
     * @param arr pole k naplnění
     * @param val hodnota kterou má být naplněno
     *
     *
     * @return vstupní pole - pro účely řetězení příkazů
     * */
    public static double[] fillArray(double[] arr, double val){
        Arrays.fill(arr, val);
        return arr;
    }


    /**
     * Pomocný wrapper, jehož metoda <code>equals</code> bere v úvahu pouze rovnost
     * referencí vnitřního objektu.
     *
     * Šikovná věc, když je potřeba např. v hešmapě ukládat data svázaná s
     * konkrétní jedinečnou instanci klíče.
     *
     * @author MarkusSecundus
     * */
    public static final class WrapperForReferenceComparison<T>{

        /**
         * Vnitřní objekt.
         * Mutable pro účely jednoduchého poolování.
         * */
        public T item;

        /**
         * Inicializuje wrapper danou hodnotou.
         * */
        public WrapperForReferenceComparison(T item){this.item = item;}

        /**
         * Přesměrovává na vnitřní objekt
         * */
        public int hashCode() { return item==null?0:item.hashCode(); }
        public boolean equals(Object o) { return o==item || (o instanceof WrapperForReferenceComparison<?> && ((WrapperForReferenceComparison<?>) o).item == item); }
        public String toString() { return ""+item; }

        /**
         * Nastaví novou hodnotu vnitřního objektu.
         *
         * @return <code>this</code> pro účely řetězení příkazů
         * */
        public WrapperForReferenceComparison<T> with(T newItem){
            this.item = newItem;
            return this;
        }

        /**
         * @return nová instance se stejnou vnitřní hodnotou
         * */
        public WrapperForReferenceComparison<T> cpy(){
            return new WrapperForReferenceComparison<>(item);
        }
    }


    /**
     * Náhražka za <code>Objects.equals</code>, jež z nějakého důvodu není dostupné
     * na starších Androidích API.
     *
     * @return přesně to, co by ve stejné situaci vrátilo <code>Objects.equals</code>
     * */
    public static boolean equals(Object a, Object b){
        return a==b || (a!=null && a.equals(b));
    }

    /**
     * Generická defaultní chybová zpráva pro výjimku <code>VectDecomposer.InconsistentNumberOfDimensionsException</code>.
     * */
    public static final String NUM_DIMENSIONS_ERROR_MESSAGE = "";

    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testované pole nemá odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, T[] b, String error_message){
        if(dims!=b.length)
            throw new VectDecomposer.InconsistentNumberOfDimensionsException(error_message + String.format("%d vs %d", dims, b.length));
    }
    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testované pole nemá odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, T[] b){checkNumDimensions(dims,b, NUM_DIMENSIONS_ERROR_MESSAGE);}
    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testovaná pole nemají všechna odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, T[] b, T[] c, String error_message){
        if(dims!=b.length || dims!=c.length)
            throw new VectDecomposer.InconsistentNumberOfDimensionsException(error_message + String.format("%d vs %d, %d", dims, b.length, c.length));
    }

    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testovaná pole nemají všechna odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, T[] b, T[] c){checkNumDimensions(dims,b,c,NUM_DIMENSIONS_ERROR_MESSAGE);}

    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testovaná pole nemají všechna odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, T[] b, T[] c, T[] d, String error_message){
        if(dims!=b.length || dims!=c.length || dims!=d.length)
            throw new VectDecomposer.InconsistentNumberOfDimensionsException(error_message + String.format("%d vs %d, %d, %d", dims, b.length, c.length, d.length));
    }

    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testovaná pole nemají všechna odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, T[] b, T[] c, T[] d){checkNumDimensions(dims,b,c,d,NUM_DIMENSIONS_ERROR_MESSAGE);}

    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testované pole nemá odpovídající vyžadovanou dimenzi
     * */
    public static<T> void checkSufficientNumDimensions(int needed_dim, T[] b, String message){
        if(needed_dim >=b.length)
            throw new VectDecomposer.InconsistentNumberOfDimensionsException(message + String.format("%d >= %d", needed_dim, b.length));
    }
    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testované pole nemá odpovídající vyžadovanou dimenzi
     * */
    public static<T> void checkSufficientNumDimensions(int needed_dim, T[] b){checkSufficientNumDimensions(needed_dim, b, NUM_DIMENSIONS_ERROR_MESSAGE);}
}

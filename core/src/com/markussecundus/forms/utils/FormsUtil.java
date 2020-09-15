package com.markussecundus.forms.utils;

import com.markussecundus.forms.utils.datastruct.DefaultDict;
import com.markussecundus.forms.utils.datastruct.DefaultDictByIdentity;
import com.markussecundus.forms.utils.function.BiComparator;
import com.markussecundus.forms.utils.function.BiFunction;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.utils.function.raw.IntToCharFunction;
import com.markussecundus.forms.utils.function.raw.IntToDoubleFunction;
import com.markussecundus.forms.utils.function.raw.IntToFloatFunction;
import com.markussecundus.forms.utils.function.raw.IntToIntFunction;
import com.markussecundus.forms.utils.function.Predicate;
import com.markussecundus.forms.utils.function.raw.IntToLongFunction;
import com.markussecundus.forms.utils.vector.VectDecomposer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;


/**
 *
 * Statická třída s rozličnými pomocnými funkcemi a konstantami,
 * které byly někde v programu potřeba a nenašly si lepší místo jinde.
 *
 *
 *
 * @author MarkusSecundus
 * */
public class FormsUtil {

    /**
     * Vrátí větší z obou vzájemně porovnatelných čísel.
     *
     * @return větší z obou vzájemně porovnatelných čísel
     * */
    public static<T extends Comparable<T>> T max(T a, T b){
        return a.compareTo(b)>=0?a:b;
    }
    /**
     * Vrátí menší z obou vzájemně porovnatelných čísel.
     *
     * @return menší z obou vzájemně porovnatelných čísel
     * */
    public static<T extends Comparable<T>> T min(T a, T b){
        return a.compareTo(b)<=0?a:b;
    }


    /**
     * Vrátí větší z posloupnosti vzájemně porovnatelných čísel.
     *
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
     * Vrátí menší z posloupnosti vzájemně porovnatelných čísel.
     *
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
     * Rozpozná, zda je prostřední hodnota v intervalu daném hodnotami krajními.
     *
     * @return zda je prostřední hodnota v intervalu daném hodnotami krajními
     * */
    public static<T> boolean isClosedRange(Comparable<T> min, T middle, Comparable<T> max){
        return min.compareTo(middle)<=0 && max.compareTo(middle)>=0;
    }

    /**
     * Rozpozná prostřední hodnota po jejím natěsnání do intervalu daného hodnotami krajními.
     *
     * @return prostřední hodnota po jejím natěsnání do intervalu daného hodnotami krajními
     * */
    public static float intoBounds(float min, float val, float max){
        return Math.min(Math.max(min, val),max);
    }


    /**
     * Vytvoří jednorozměrné pole prvků daného typu, dané délky.
     *
     * @param <T> datový typ prvků pole
     * @param type třída datového typu prvků pole
     * @param len požadovaná délka nového pole
     *
     * @throws NegativeArraySizeException pokud je požadovaná délka pole záporná
     *
     * @return jednorozměrné pole prvků daného typu, dané délky
     * */
    @SuppressWarnings("unchecked")
    public static<T> T[] makeArray(Class<T> type, int len){
        return (T[]) Array.newInstance(type, len);
    }

    /**
     * Upraví na místě hodnoty v předaném poli pomocí transformační funkce, přihlížejíce k hodnotám
     * druhého předaného pole.<p>(Transformační funkce přebere pro daný index vždy hodnotu na daném indexu
     * v transformovaném a v kontextovém poli, vrací novou hodnotu pro daný index transformovaného pole.)
     *
     * @param <T> datový typ transformovaného pole
     * @param <U> datový typ pole s kontextovými hodnotami
     *
     * @param ret pole k transformaci
     * @param secondary pole, jehož hodnoty poslouží jako kontext ke transformaci
     * @param fnc transformační funkce
     *
     * @return pole přebrané jako 1. argument - pro účely řetězení
     * */
    public static<T, U> T[] transformInPlaceWith(T[] ret, U[] secondary, BiFunction<T,U,T> fnc){
        for(int t = Math.min(ret.length, secondary.length)-1;t>=0;--t)
            ret[t] = fnc.apply(ret[t], secondary[t]);
        return ret;
    }

    /**
     * Do výsledného pole zkopíruje hodnoty zdrojového pole, transformované specifikovanou funkcí.
     *
     * @param <T> datový typ zdrojového pole
     * @param <U> datový typ výsledného pole
     *
     * @param from pole, jehož prvky budou transformovány do druhého pole
     * @param ret pole, do něhož budou transformované prvky ukládány
     * @param fnc transformační funkce
     *
     * @return pole přebrané jako 2. argument - pro účely řetězení
     * */
    public static<T, U> U[] transform(T[] from, U[] ret, Function<T,U> fnc){
        for(int t=Math.min(from.length, ret.length)-1;t>=0;--t)
            ret[t] = fnc.apply(from[t]);
        return ret;
    }

    /**
     * Upraví na místě hodnoty v předaném poli pomocí transformační funkce.
     *
     * @param <T> datový typ transformovaného pole
     *
     * @param ret pole k transformaci
     * @param fnc transformační funkce
     *
     * @return vstupní pole - pro účely řetězení
     * */
    public static<T> T[] transformInPlace(T[] ret, Function<T,T> fnc){
        return transform(ret, ret, fnc);
    }

    /**
     * Sbalí zleva hodnoty daného pole do jedné výsledné hodnoty, po vzoru Haskellího <code>foldl</code>.
     *
     * @param <T> datový typ zdrojového pole
     * @param <U> datový typ výsledné hodnoty
     *
     * @param arr zdrojové pole, jehož prvky budou agregovány
     * @param beginVal počáteční hodnota baleného prvku
     * @param fnc agregační funkce
     *
     * @return agregovaná hodnota
     * */
    public static<T,U> U foldLeft(T[] arr, U beginVal, BiFunction<T, U, U> fnc){
        for(T t: arr)
            beginVal = fnc.apply(t, beginVal);
        return beginVal;
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
     * Naplní dané pole <code>char</code>ů hodnotami dodanými zdrojovou funkcí.
     *
     * @param arr pole k naplnění
     * @param supplier přebírá index v poli a vrací pro něj novou hodnotu
     *
     * @return vstupní pole - pro účely řetězení
     * */
    public static char[] fillArray(char[] arr, IntToCharFunction supplier){
        for(int t=arr.length;--t>=0;)
            arr[t] = supplier.apply(t);
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
     * Naplní dané pole <code>int</code>ů hodnotami dodanými zdrojovou funkcí.
     *
     * @param arr pole k naplnění
     * @param supplier přebírá index v poli a vrací pro něj novou hodnotu
     *
     * @return vstupní pole - pro účely řetězení
     * */
    public static int[] fillArray(int[] arr, IntToIntFunction supplier){
        for(int t=arr.length;--t>=0;)
            arr[t] = supplier.apply(t);
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
    public static long[] fillArray(long[] arr, long val){
        Arrays.fill(arr, val);
        return arr;
    }
    /**
     * Naplní dané pole <code>long</code>ů hodnotami dodanými zdrojovou funkcí.
     *
     * @param arr pole k naplnění
     * @param supplier přebírá index v poli a vrací pro něj novou hodnotu
     *
     * @return vstupní pole - pro účely řetězení
     * */
    public static long[] fillArray(long[] arr, IntToLongFunction supplier){
        for(int t=arr.length;--t>=0;)
            arr[t] = supplier.apply(t);
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
     * Naplní dané pole <code>float</code>ů hodnotami dodanými zdrojovou funkcí.
     *
     * @param arr pole k naplnění
     * @param supplier přebírá index v poli a vrací pro něj novou hodnotu
     *
     * @return vstupní pole - pro účely řetězení
     * */
    public static float[] fillArray(float[] arr, IntToFloatFunction supplier){
        for(int t = arr.length ; --t>=0 ;)
            arr[t] = supplier.apply(t);
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
     * Naplní dané pole <code>double</code>ů hodnotami dodanými zdrojovou funkcí.
     *
     * @param arr pole k naplnění
     * @param supplier přebírá index v poli a vrací pro něj novou hodnotu
     *
     * @return vstupní pole - pro účely řetězení
     * */
    public static double[] fillArray(double[] arr, IntToDoubleFunction supplier){
        for(int t = arr.length ; --t>=0 ;)
            arr[t] = supplier.apply(t);
        return arr;
    }


    /**
     * V seřazeném listu, jehož prvky jsou porovnatelné s hledaným objektem, najde objekt, jenž je z hlediska
     * použitého řazení hledanému objektu nejblíže.
     *
     *
     * @param <Elems> datový typ prvků v prohledávaném listu
     * @param <Sought> typ hledaného objektu
     *
     * @param sortedList seřazený list, mezi jehož prvky je hledáno
     * @param soughtElem hledaný objekt
     * @param comparator použité řazení
     *
     *
     * @return index odpovídající hodnotě nejbližší hodnotě hledané (přímo hledané hodnotě, pokud je přítomna),
     * popř. -1 pro prázdný list
     * */
    public static<Sought, Elems> int binarySearchNearest(List<Elems> sortedList, Sought soughtElem, BiComparator<Sought, Elems> comparator){
        if(sortedList == null || sortedList.isEmpty())
            return -1;

        int begin = 0, end = sortedList.size();

        while(begin< end){
            int mid = (begin + end) / 2;
            int res = comparator.compareTo(soughtElem, sortedList.get(mid));
            switch (res){
                case  0: return mid;
                case -1:
                    begin = mid + 1;
                    break;
                case  1:
                    end = mid;
                    break;
            }
        }
        return begin;
    }

    /**
     * V seřazeném listu, jehož prvky jsou porovnatelné s hledaným objektem, najde objekt, jenž je z hlediska
     * použitého řazení hledanému objektu nejblíže.
     *
     *
     * @param <Elems> datový typ prvků v prohledávaném listu
     * @param <Sought> typ hledaného objektu
     *
     * @param sortedList seřazený list, mezi jehož prvky je hledáno
     * @param soughtElem hledaný objekt
     *
     *
     * @return index odpovídající hodnotě nejbližší hodnotě hledané (přímo hledané hodnotě, pokud je přítomna),
     * popř. -1 pro prázdný list
     * */
    public static<Elems, Sought extends Comparable<Elems>> int binarySearchNearest(List<Elems> sortedList, Sought soughtElem){
        return binarySearchNearest(sortedList, soughtElem, Comparable::compareTo);
    }



    /**
     * @return vstupní funkce obalená do dekorátoru provádějícího automatické kešování funkčních hodnot.
     * */
    public static<T,R> Function<T,R> autocache(Function<T,R> fnc){
        final DefaultDict<T, R> cache = new DefaultDict<>(fnc);
        return cache::get;
    }

    /**
     * @return vstupní funkce obalená do dekorátoru provádějícího automatické kešování funkčních hodnot.
     * */
    public static <T,R> Function<T,R> autocacheById(Function<T,R> fnc){
        final DefaultDictByIdentity<T,R> cache = new DefaultDictByIdentity<>(fnc);
        return cache::get;
    }


    /**
     * Jednoduché celočíselné thread-safe počítadlo.
     *
     * @author MarkusSecundus
     * */
    public static final class Counter{
        private volatile int counter = 0;


        /**
         * Přičte k hodnotě počítadla jedničku.
         *
         * @return nová hodnota počítadla po inkrementaci
         * */
        public synchronized int inc(){return ++counter;}

        /**
         * Odečte od hodnoty počítadla jedničku.
         *
         * @return nová hodnota počítadla po dekrementaci
         * */
        public synchronized int dec(){return --counter;}

        /**
         * Přičte k hodnotě počítadla specifikovanou hodnotu.
         *
         * @param toAdd hodnota, jenž bude přičtena k aktuální hodnotě počítadla
         *
         * @return nová hodnota počítadla
         * */
        public synchronized int add(int toAdd){return counter += toAdd;}

        /**
         * Vrátí aktuální hodnotu počítadla.
         *
         * @return aktuální hodnota počítadla
         * */
        public synchronized int get(){return counter;}

        /**
         * Vytvoří novou instanci {@link Counter} nastavenou na 0.
         *
         * @return nová instance {@link Counter} nastavená na 0.
         * */
        public static Counter make(){return new Counter();}
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
        public int hashCode() { return System.identityHashCode(item); }
        public boolean equals(Object o) { return o==item || (o instanceof WrapperForReferenceComparison<?> && ((WrapperForReferenceComparison<?>) o).equals(item)); }

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
     * Vytvoří objekt jenž používá specifikovaný predikát jako svou metodu <code>equals</code>
     * a specifikované číslo jako svůj heškód.
     * <p>
     * Užitečné např. pro pohodlné a stručné hledání výskytu prvku s určitou vlastností
     * v kolekci, je-li k dispozici pouze funkce <code>indexOf(Object)</code> apod. .
     *
     * @param <T> datový typ prvků k testování
     *
     * @param findingCondition predikát, jenž bude použit metodou <code>equals</code> výsledného objektu
     * @param hash hodnota, kterou bude vracet metoda <code>hashCode</code> výsledného objektu
     *
     * @return objekt jenž používá specifikovaný predikát jako svou metodu <code>equals</code>
     *    a specifikované číslo jako svůj heškód
     * */
    public static<T> Object finder(Predicate<T> findingCondition, int hash){
        return new Object(){
            public boolean equals(Object o) { return findingCondition.test((T)o); }
            public int hashCode(){ return hash; }
        };
    }

    /**
     * Vytvoří objekt jenž používá specifikovaný predikát jako svou metodu <code>equals</code>
     * a jehož heškód je vždy 0.
     * <p>
     * Užitečné např. pro pohodlné a stručné hledání výskytu prvku s určitou vlastností
     * v kolekci, je-li k dispozici pouze funkce <code>indexOf(Object)</code> apod. .
     *
     * @param <T> datový typ prvků k testování
     *
     * @param findingCondition predikát, jenž bude použit metodou <code>equals</code> výsledného objektu
     *
     * @return objekt jenž používá specifikovaný predikát jako svou metodu <code>equals</code>
     * */
    public static<T> Object finder(Predicate<T> findingCondition){
        return finder(findingCondition, 0);
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
     * Náhražka za <code>Objects.hashCode</code>, jež z nějakého důvodu není dostupné
     * na starších Androidích API.
     *
     * @return přesně to, co by ve stejné situaci vrátilo <code>Objects.hashCode</code>
     * */
    public static int hashCode(Object o){
        return o == null ? 0 : o.hashCode();
    }

    /**
     * @return nějaký jakžtakž rozumný heškód pro uspořádanou dvojici hodnot, rozlišující dvojice lišící se pořadím
     * */
    public static int hashCode(Object a, Object b){
        int ha = hashCode(a);
        return (1+ha)*hashCode(b) + ha;
    }


    private static final Function<?,?> _IDENTITY = o->o;

    /**
     * @param <T> typ, jejž musí splnovat argument funkce
     * @param <R> typ návratové hodnoty funkce, může být libovolný nadtyp T
     *
     * @return funkce identity (vrací hodnotu, kterou dostala jako argument, nezměněnou)
     * */
    public static<T extends R, R> Function<T,R> identity(){
        return (Function)_IDENTITY;
    }


    /**
     * Generická defaultní chybová zpráva pro výjimku <code>VectDecomposer.InconsistentNumberOfDimensionsException</code>.
     * */
    public static final String NUM_DIMENSIONS_ERROR_MESSAGE = "";

    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testované pole nemá odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, int b, String error_message){
        if(dims!=b)
            throw new VectDecomposer.InconsistentNumberOfDimensionsException(error_message + String.format("%d vs %d", dims, b));
    }
    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testované pole nemá odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, int b){checkNumDimensions(dims,b, NUM_DIMENSIONS_ERROR_MESSAGE);}
    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testovaná pole nemají všechna odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, int b, int c, String error_message){
        if(dims!= b || dims!=c)
            throw new VectDecomposer.InconsistentNumberOfDimensionsException(error_message + String.format("%d vs %d, %d", dims, b, c));
    }

    /**
     * @throws VectDecomposer.InconsistentNumberOfDimensionsException pokud testovaná pole nemají všechna odpovídající počet dimenzí
     * */
    public static<T> void checkNumDimensions(int dims, int b, int c){checkNumDimensions(dims, b,c,NUM_DIMENSIONS_ERROR_MESSAGE);}

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

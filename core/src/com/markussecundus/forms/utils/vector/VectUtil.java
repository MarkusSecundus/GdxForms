package com.markussecundus.forms.utils.vector;

import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.function.BiFunction;
import com.markussecundus.forms.utils.function.Function;


/**
 * Rozhraní pro pomocné třídy, skrze které ostatní komponenty této knihovny mohou
 * manipulovat s vektorovými a jejich příslušnými skalárními typy.
 *
 * Zvoleno jako robustnější, ačkoliv na použití méně pohodlná alternativa k rozhraní,
 * které by musely splnovat vektorové typy samy o sobě, jelikož takové rozhraní by
 * samozřejmě nebyly schopny formálně implementovat nativní typy již existující v prostředích,
 * kde tato knihovna může potenciálně být uplatněna.
 *
 * Na mnoha místech v knihovně se předpokládá, že pro vektorový typ platí, že všechny operace zde přítomné,
 *  uplatní-li se na dekomponovaný vektor po složkách a následně se složí dohromady,
 *  přinesou stejný výsledek, jako kdyby byly uplatněny na vektor přímo.
 *  -tedy např. že takováta implementace funkce <code>add</code> je korektní:
 *  <pre><code>
 *   public default Vect add(Vect a, Vect b){
 *       Scalar[] as = decompose(a), bs = decompose(b);
 *       for(int t = 0 ; t < as.length ; ++t)
 *          as[t] = addScalar(as[t], bs[t]);
 *       return compose(as);
 *    }
 *  </code></pre>
 *
 * @see Vect2f
 * @see Vect2i
 * @see com.markussecundus.forms.utils.vector.VectDecomposer
 *
 * @see com.markussecundus.forms.elements.Drawable
 * @see com.markussecundus.forms.gfx.GraphicalPrimitive
 *
 * @author MarkusSecundus
 * */
public interface VectUtil<Vect , Scalar extends Comparable<Scalar>> extends VectDecomposer<Vect, Scalar> {

    /**
     * @return součet obou vektorů
     * */
    public Vect add(Vect a, Vect b);
    /**
     * @return rozdíl obou vektorů
     * */
    public Vect sub(Vect a, Vect b);

    /**
     * @return násobek daného vektoru se skalárem
     * */
    public Vect scl(Vect a, Scalar b);
    /**
     * @return podíl daného vektoru se skalárem
     * */
    public Vect div(Vect a, Scalar b);

    /**
     * @return násobek daného vektoru
     * */
    public Vect scl(Vect a, double b);

    /**
     * @return podíl daného vektoru
     * */
    public Vect div(Vect a, double b);

    /**
     * @return Vektor vynásobený po složkách s jednotlivými poměry
     * */
    public default Scalar[] sclComponents(Scalar[] a, Double[] b) throws InconsistentNumberOfDimensionsException{
        FormsUtil.checkSufficientNumDimensions(b.length-1, a);
        for(int t=0;t<b.length;++t)
            a[t] = sclScalar(a[t], b[t]);
        return a;
    }
    /**
     * @return Vektor vynásobený po složkách s jednotlivými poměry
     * */
    public default Scalar[] sclComponents(Scalar[] a, double[] b) throws InconsistentNumberOfDimensionsException{
        FormsUtil.checkSufficientNumDimensions(b.length-1, a);
        for(int t=0;t<b.length;++t)
            a[t] = sclScalar(a[t], b[t]);
        return a;
    }
    /**
     * @return Vektor vydělený po složkách jednotlivými poměry
     * */
    public default Scalar[] divComponents(Scalar[] a, Double[] b) throws InconsistentNumberOfDimensionsException{
        FormsUtil.checkSufficientNumDimensions(b.length-1, a);
        for(int t=0;t<b.length;++t)
            a[t] = divScalar(a[t], b[t]);
        return a;
    }
    /**
     * @return Vektor vydělený po složkách jednotlivými poměry
     * */
    public default Scalar[] divComponents(Scalar[] a, double[] b) throws InconsistentNumberOfDimensionsException{
        FormsUtil.checkSufficientNumDimensions(b.length-1, a);
        for(int t=0;t<b.length;++t)
            a[t] = divScalar(a[t], b[t]);
        return a;
    }
    /**
     * @return Vektor vynásobený po složkách s jednotlivými poměry
     * */
    public default Vect sclComponents(Vect a, Double[] b){return compose(sclComponents(decompose(a), b));}
    /**
     * @return Vektor vynásobený po složkách s jednotlivými poměry
     * */
    public default Vect sclComponents(Vect a, double[] b){return compose(sclComponents(decompose(a), b));}
    /**
     * @return Vektor vydělený po složkách jednotlivými poměry
     * */
    public default Vect divComponents(Vect a, Double[] b){return compose(divComponents(decompose(a), b));}
    /**
     * @return Vektor vydělený po složkách jednotlivými poměry
     * */
    public default Vect divComponents(Vect a, double[] b){return compose(divComponents(decompose(a), b));}

    /**
     * Může vrátit libovolnou hodnotu tak, aby vždy platilo:
     *  <code>
     *      len2(a).compareTo(len2(b))  ==  len(a).compareTo(len(b))
     *  </code>
     *  pro libovolné <code>Vect a,b</code>;
     *  Měla by být rychlejší než funkce <code>len</code>.
     * */
    public Scalar len2(Vect v);
    /**
     * @return skutečná délka vektoru v příslušném metrickém systému
     * */
    public Scalar len(Vect v);

    /**
     * Může vrátit libovolnou hodnotu tak, aby vždy platilo:
     *  <code>
     *      dst2(a,b).compareTo(dst2(c,d))  ==  dst(a,b).compareTo(dst(c,d))
     *  </code>
     *  pro libovolné <code>Vect a,b,c,d</code>;
     *  Měla by být rychlejší než funkce <code>dst</code>.
     * */
    default Scalar dst2(Vect a, Vect b){
        return len2(sub(b,a));
    }

    /**
     * @return skutečná vzdálenost vektorů v příslušném metrickém systému
     * */
    public Scalar dst(Vect a, Vect b);

    /**
     * Vrátí vektor ekvivalentní se vstupním, takový, že voláním libovolné další zde přítomné funkce
     * na návratovou hodnotu, nezměníme stav argumentu, s kterým byla funkce volána.
     *
     * @return přímo vstupní argument u immutable typu, kopii u mutable typu
     * */
    public Vect cpy(Vect a);

    /**
     * @return součet obou skalárů
     * */
    public default Scalar addScalar(Scalar a, Scalar b){return subScalar(a, negScalar(cpyScalar(b)) );} //a - (-b) == a + b
    /**
     * @return rozdíl obou skalárů
     * */
    public Scalar subScalar(Scalar a, Scalar b);

    /**
     * @return součin obou skalárů
     * */
    public Scalar sclScalar(Scalar a, Scalar b);

    /**
     * @return podíl obou skalárů
     * */
    public Scalar divScalar(Scalar a, Scalar b);

    /**
     * @return daný násobek skaláru
     * */
    public Scalar sclScalar(Scalar a, double scl);

    /**
     * @return daný převrácený násobek skaláru
     * */
    public default Scalar divScalar(Scalar a, double scl){return sclScalar(a, 1f/scl);}


    /**
     * @return absolutní hodnotu skaláru
     * */
    public default Scalar absScalar(Scalar a){
        return (a.compareTo(ZERO_SCALAR())<0) ? negScalar(a) : a;
    }

    /**
     * @return opačnou hodnotu ke skaláru
     * */
    public default Scalar negScalar(Scalar a){
        return subScalar(ZERO_SCALAR(), a);
    }

    /**
     * @return průměr z dvojice skalárů, může se v něm objevit zaokrouhlovací chyba
     * */
    public default Scalar avgScalar(Scalar a, Scalar b){
        return sclScalar(addScalar(a,b), 0.5f);
    }

    /**
     * @return zda mají dva skaláry stejnou hodnotu (může být méně striktní, než <code>Objects.equals</code>)
     * */
    public default boolean eqScalar(Scalar a, Scalar b){return a.equals(b);}

    /**
     * Vrátí skalár ekvivalentní se vstupním, takový, že voláním libovolné další zde přítomné funkce
     * na návratovou hodnotu, nezměníme stav argumentu, s kterým byla funkce volána.
     *
     * @return přímo vstupní argument u immutable typu, kopii u mutable typu
     * */
    public Scalar cpyScalar(Scalar s);


    /**
     * @return nejmenší ze složek daného vektoru
     * */
    public default Scalar minScalar(Vect v){ return FormsUtil.min(decompose(v)); }

    /**
     * @return @return největší ze složek daného vektoru
     * */
    public default Scalar maxScalar(Vect v){return FormsUtil.max(decompose(v));}


    /**
     * @return Počátek vektorovým typem používaného souřadného systému.
     * */
    public Vect ZERO();

    /**
     * @return Počátek skalárním typem používaného souřadného systému.
     * */
    public Scalar ZERO_SCALAR();
    /**
     * @return Nejvyšší hodnota, jaké může vektorový typ nabýt ve všech složkách
     * */
    public Vect MAX_VAL();
    /**
     * @return Nejvyšší hodnota, jaké může nabýt skalární typ
     * */
    public Scalar MAX_VAL_SCALAR();

    /**
     * @return hodnota vektoru <code>ZERO()</code> dekomponovaná do pole po jednotlivých dimenzích
     * */
    public default Scalar[] ZERO_DECOMPOSED(){return decompose(ZERO());}
    /**
     * @return hodnota vektoru <code>MAX_VAL()</code> dekomponovaná do pole po jednotlivých dimenzích
     * */
    public default Scalar[] MAX_VAL_DECOMPOSED(){return decompose(MAX_VAL());}

    /**
     * {@inheritDoc}
     * */
    @Override
    public default int DIMENSION_COUNT(){return ZERO_DECOMPOSED().length;}

    /**
     * @return třídao dpovídající používanému skalárnímu typu
     * */
    public Class<Scalar> getScalarClass();
    /**
     * @return třídao dpovídající používanému vektorovému typu
     * */
    public Class<Vect>  getVectClass();


    /**
     * Testuje, zda je počet prvků v poli shodný s <code>DIMENSION_COUT()</code>. Pokud ne, vyhodí {@link com.markussecundus.forms.utils.vector.VectDecomposer.InconsistentNumberOfDimensionsException}
     *
     * @throws com.markussecundus.forms.utils.vector.VectDecomposer.InconsistentNumberOfDimensionsException pokud počet prvků pole neodpovídá požadovanému počtu dimenzí
     * */
    public default void checkNumDimensions(Scalar[] s)throws InconsistentNumberOfDimensionsException
    { FormsUtil.checkNumDimensions(DIMENSION_COUNT(), s); }

    /**
     * Testuje, zda je počet prvků v polích shodný s <code>DIMENSION_COUT()</code>. Pokud ne, vyhodí {@link com.markussecundus.forms.utils.vector.VectDecomposer.InconsistentNumberOfDimensionsException}
     *
     * @throws com.markussecundus.forms.utils.vector.VectDecomposer.InconsistentNumberOfDimensionsException pokud počet prvků alespon 1 pole neodpovídá požadovanému počtu dimenzí
     * */
    public default void checkNumDimensions(Scalar[] a, Scalar[] b)throws InconsistentNumberOfDimensionsException
    { FormsUtil.checkNumDimensions(DIMENSION_COUNT(), a,b); }

    /**
     * Testuje, zda je počet prvků v polích shodný s <code>DIMENSION_COUT()</code>. Pokud ne, vyhodí {@link com.markussecundus.forms.utils.vector.VectDecomposer.InconsistentNumberOfDimensionsException}
     *
     * @throws com.markussecundus.forms.utils.vector.VectDecomposer.InconsistentNumberOfDimensionsException pokud počet prvků alespon 1 pole neodpovídá požadovanému počtu dimenzí
     * */
    public default void checkNumDimensions(Scalar[] a, Scalar[] b, Scalar[] c)throws InconsistentNumberOfDimensionsException
    { FormsUtil.checkNumDimensions(DIMENSION_COUNT(), a,b,c); }



    /**
     * Statická třída, ve které budou shromažďovány základní parciální implementace pro nejběžnější typy.
     *
     * @author MarkusSecundus
     * */
    public static final class BasicImplementations{
        private BasicImplementations(){}



        private static final class Helper {
            private static <Vect, Scalar extends Comparable<Scalar>> Vect transformWith(VectUtil<Vect, Scalar> self, Vect ret, Vect secondary, BiFunction<Scalar, Scalar, Scalar> fnc) {
                return self.compose(FormsUtil.transformInPlaceWith(self.decompose(ret), self.decompose(secondary), fnc));
            }

            private static <Vect, Scalar extends Comparable<Scalar>> Vect transform(VectUtil<Vect, Scalar> self, Vect ret, Function<Scalar, Scalar> fnc) {
                return self.compose(FormsUtil.transformInPlace(self.decompose(ret), fnc));
            }

            private static <Vect, Scalar extends Comparable<Scalar>> Vect filledWith(VectUtil<Vect, Scalar> self, Scalar val) {
                return self.compose(FormsUtil.fillArray(FormsUtil.makeArray(self.getScalarClass(), self.DIMENSION_COUNT()), val));
            }
        }

        public static interface VectorUtil_ByComponents<Vect, Scalar extends Comparable<Scalar>> extends VectUtil<Vect, Scalar>{

            @Override default Vect sub(Vect a, Vect b){ return Helper.transformWith(this, a, b, this::subScalar); }

            @Override default Vect add(Vect a, Vect b){ return Helper.transformWith(this, a,b, this::addScalar); }

            @Override default Vect scl(Vect a, Scalar b){ return Helper.transform(this, a, x->sclScalar(x,b)); }
            @Override default Vect scl(Vect a, double b){ return Helper.transform(this, a, x->sclScalar(x,b)); }

            @Override default Vect div(Vect a, Scalar b){ return Helper.transform(this, a, x->divScalar(x,b)); }
            @Override default Vect div(Vect a, double b){ return Helper.transform(this, a, x->divScalar(x,b)); }


            @Override default Vect ZERO(){ return Helper.filledWith(this, ZERO_SCALAR()); }

            @Override default Vect MAX_VAL(){ return Helper.filledWith(this, MAX_VAL_SCALAR()); }

            @Override default Scalar len2(Vect v){
                return FormsUtil.foldLeft(decompose(v), ZERO_SCALAR(), (a,x)->addScalar(x, sclScalar(a,a)) );
            }

        }



        /**
         * Parciální implementace sloužící jako základ pro {@link VectUtil}
         * libovolného typu používající {@link Float} jako Skalární typ.
         *
         * @param <Vect> Vektorový typ, který je třeba doimplementovat
         *
         * @see Vect2f
         *
         * @author MarkusSecundus
         * */
        public static interface VectorUtil_FloatAsScalar<Vect> extends VectUtil<Vect, Float> {

            @Override public default Float subScalar(Float a, Float b) { return a-b; }

            @Override public default Float addScalar(Float a, Float b) { return a+b; }

            @Override public default Float sclScalar(Float a, Float b) { return a*b; }
            @Override public default Float sclScalar(Float a, double scl) { return (float)(a*scl); }

            @Override public default Float divScalar(Float a, Float b) { return a/b; }
            @Override public default Float divScalar(Float a, double scl) { return (float)(a/scl); }


            @Override public default Float cpyScalar(Float s) { return s; }


            @Override public default Float ZERO_SCALAR() { return 0f; }

            @Override public default Float MAX_VAL_SCALAR() { return Float.POSITIVE_INFINITY; }



            @Override public default Float absScalar(Float a) { return Math.abs(a); }

            @Override public default Float negScalar(Float a) { return -a; }

            @Override public default Float avgScalar(Float a, Float b) { return (a+b)*0.5f; }

            @Override public default boolean eqScalar(Float a, Float b) { return Float.floatToIntBits(a)==Float.floatToIntBits(b); }


            @Override public default Class<Float> getScalarClass() { return Float.class; }
        }

        /**
         * Parciální implementace sloužící jako základ pro {@link VectUtil}
         * libovolného typu používající {@link Double} jako Skalární typ.
         *
         * @param <Vect> Vektorový typ, který je třeba doimplementovat
         *
         * @author MarkusSecundus
         * */
        public static interface VectorUtil_DoubleAsScalar<Vect> extends VectUtil<Vect, Double> {

            @Override public default Double subScalar(Double a, Double b) { return a-b; }

            @Override public default Double addScalar(Double a, Double b) { return a+b; }

            @Override public default Double sclScalar(Double a, Double b) { return a*b; }
            @Override public default Double sclScalar(Double a, double scl) { return (a*scl); }

            @Override public default Double divScalar(Double a, Double b) { return a/b; }
            @Override public default Double divScalar(Double a, double scl) { return (a/scl); }


            @Override public default Double cpyScalar(Double s) { return s; }


            @Override public default Double ZERO_SCALAR() { return 0d; }

            @Override public default Double MAX_VAL_SCALAR() { return Double.POSITIVE_INFINITY; }




            @Override public default Double absScalar(Double a) { return Math.abs(a); }

            @Override public default Double negScalar(Double a) { return -a; }

            @Override public default Double avgScalar(Double a, Double b) { return (a+b)*0.5f; }

            @Override public default boolean eqScalar(Double a, Double b) { return Double.doubleToLongBits(a)==Double.doubleToLongBits(b); }


            @Override public default Class<Double> getScalarClass() { return Double.class; }
        }

        /**
         * Parciální implementace sloužící jako základ pro {@link VectUtil}
         * libovolného typu používající {@link Integer} jako Skalární typ.
         *
         * @param <Vect> Vektorový typ, který je třeba doimplementovat
         *
         * @see Vect2i
         *
         * @author MarkusSecundus
         * */
        public static interface VectorUtil_IntegerAsScalar<Vect> extends VectUtil<Vect, Integer> {

            @Override public default Integer subScalar(Integer a, Integer b) { return a-b; }

            @Override public default Integer addScalar(Integer a, Integer b) { return a+b; }

            @Override public default Integer sclScalar(Integer a, Integer b) { return a*b; }
            @Override public default Integer sclScalar(Integer a, double scl) { return (int)(a*scl); }

            @Override public default Integer divScalar(Integer a, Integer b) { return a/b; }
            @Override public default Integer divScalar(Integer a, double scl) { return (int)(a/scl); }


            @Override public default Integer cpyScalar(Integer s) { return s; }


            @Override public default Integer ZERO_SCALAR() { return 0; }

            @Override public default Integer MAX_VAL_SCALAR() { return Integer.MAX_VALUE; }



            @Override public default Integer absScalar(Integer a) { return Math.abs(a); }

            @Override public default Integer negScalar(Integer a) { return -a; }

            @Override public default Integer avgScalar(Integer a, Integer b) { return (a+b)/2; }

            @Override public default boolean eqScalar(Integer a, Integer b) { return FormsUtil.equals(a,b); }


            @Override public default Class<Integer> getScalarClass() { return Integer.class; }
        }

        /**
         * Parciální implementace sloužící jako základ pro {@link VectUtil}
         * libovolného typu používající {@link Long} jako Skalární typ.
         *
         * @param <Vect> Vektorový typ, který je třeba doimplementovat
         *
         * @author MarkusSecundus
         * */
        public static interface VectorUtil_LongAsScalar<Vect> extends VectUtil<Vect, Long> {

            @Override public default Long subScalar(Long a, Long b) { return a-b; }

            @Override public default Long addScalar(Long a, Long b) { return a+b; }

            @Override public default Long sclScalar(Long a, Long b) { return a*b; }
            @Override public default Long sclScalar(Long a, double scl) { return (long)(a*scl); }

            @Override public default Long divScalar(Long a, Long b) { return a/b; }
            @Override public default Long divScalar(Long a, double scl) { return (long)(a/scl); }

            @Override public default Long cpyScalar(Long s) { return s; }


            @Override public default Long ZERO_SCALAR() { return 0L; }

            @Override public default Long MAX_VAL_SCALAR() { return Long.MAX_VALUE; }


            @Override public default Long absScalar(Long a) { return Math.abs(a); }

            @Override public default Long negScalar(Long a) { return -a; }

            @Override public default Long avgScalar(Long a, Long b) { return (a+b)/2; }

            @Override public default boolean eqScalar(Long a, Long b) { return FormsUtil.equals(a,b); }


            @Override public default Class<Long> getScalarClass() { return Long.class; }
        }
    }
}

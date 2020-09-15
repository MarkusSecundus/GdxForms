package com.markussecundus.forms.utils.vector;


/**
 * Pomocná utilita, která umí rozložit daný vektorový typ na pole
 * skalárů odpovídajících jednotlivým dimensím, příp. ho naopak složit.
 *
 *
 * @see VectUtil
 * @see VectComparator
 * @see Vect2f
 * @see Vect2i
 * @see Vect2d
 *
 * @author MarkusSecundus
 * */
public interface VectDecomposer<Vect, Scalar> {

    /**
     * Rozloží vektor na pole skalárů odpovídajících jeho jednotlivým dimenzím.
     *
     * @param p vektor k rozložení
     * @return jednotlivé složky vektoru
     * */
    public Scalar[] decompose(Vect p);

    /**
     * Poskládá příslušný vektor z jednotlivých složek.
     *
     * @param s jednotlivé složky vektoru
     * @return poskládaný vektor
     *
     * @throws InconsistentNumberOfDimensionsException pokud vstupní hodnota nemá požadovaný počet dimenzí
     * */
    public Vect compose(Scalar... s) throws InconsistentNumberOfDimensionsException;

    /**
     * Udává konstantní počet dimenzí, kterého by mělo být vždy,
     * za všech okolností při dekompozici přesně dosaženo.
     * */
    public int DIMENSION_COUNT();

    /**
     * Vyextrahuje z daného vektoru skalár odpovídající n-té dimenzi.
     *
     * Poskytuje základní, velmi neefektivní implementaci, kterou doporučuji pokud možno v potomcích přepsat.
     *
     * @param n dimenze, jejíž skalár má být získán
     * @param v vektor, jehož n-tý rozměr chceme vědět
     *
     * @return skalár odpovídající n-té dimenzi daného vektoru
     *
     * @throws InconsistentNumberOfDimensionsException pokud vstupní hodnota nemá požadovaný počet dimenzí
     * */
    public default Scalar getNth(Vect v, int n)throws InconsistentNumberOfDimensionsException{
        if(n<0 || n>= DIMENSION_COUNT())
            throw new InconsistentNumberOfDimensionsException();
        return decompose(v)[n];
    }

    /**
     * Vrátí kopii vstupního vektoru s hodnotou <code>s</code> na n-té dimenzi místo hodnoty původní.
     *
     * Poskytuje základní, velmi neefektivní implementaci, kterou doporučuji pokud možno v potomcích přepsat.
     *
     * @param n dimenze, jejíž skalár má být změněn
     * @param p vektor, jehož n-tý rozměr chceme změnit
     * @param s nová hodnota pro n-tou dimenzi vektoru
     *
     * @return nový vektor s pozměněnou n-tou dimenzí
     *
     * @throws InconsistentNumberOfDimensionsException pokud vstupní hodnota nemá požadovaný počet dimenzí
     * */
    public default Vect withNth(Vect p, int n, Scalar s) throws InconsistentNumberOfDimensionsException{
        if(n<0 || n>= DIMENSION_COUNT())
            throw new InconsistentNumberOfDimensionsException();
        Scalar[] v = decompose(p);
        v[n] = s;
        return compose(v);
    }

    /**
     * Vyhozena pokud nebyl dodržen požadovaný počet dimenzí pro vektor.
     *
     * @see RuntimeException
     * @see IllegalArgumentException
     *
     * @author MarkusSecundus
     * */
    public static class InconsistentNumberOfDimensionsException extends IllegalArgumentException{
        public InconsistentNumberOfDimensionsException(){super();}
        public InconsistentNumberOfDimensionsException(String message){super(message);}
        public InconsistentNumberOfDimensionsException(String message, Throwable cause){super(message, cause);}
        public InconsistentNumberOfDimensionsException(Throwable cause){super( cause);}
    }
}

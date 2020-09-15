package com.markussecundus.forms.utils.vector;

/**
 * Pomocná utilita pro porovnávání vektorů po složkách.
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
public interface VectComparator<Vect> {

    /**
     * Vrací <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     * a jsou ve všech složkách shodné.
     *
     * @return <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     * a jsou ve všech složkách shodné
     * */
    public default boolean eq(Vect a, Vect b){
        return a!=null && a.equals(b);
    }

    /**
     * Vrací <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     *  a první vektor je ve všech složkách menší než vektor druhý.
     *  <p>
     *  Např.<p>
     *      <code> a = (0,0)^T, b = (1,1)^T --> true</code> <p>
     *      <code> a = (1,0)^T, b = (1,1)^T --> false</code> <p>
     *
     * @return <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     *  a první vektor je ve všech složkách menší než vektor druhý.
     * */
    public boolean lt(Vect a, Vect b);

    /**
     * Vrací <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     *  a první vektor je ve všech složkách menší nebo roven složkám vektoru druhého.
     *  <p>
     *  Např.<p>
     *      <code> a = (0,0)^T, b = (1,1)^T --> true</code> <p>
     *      <code> a = (1,0)^T, b = (1,1)^T --> true</code> <p>
     *      <code> a = (1,1)^T, b = (0,4)^T --> false</code> <p>
     *
     * @return <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     *  a první vektor je ve všech složkách menší nebo roven složkám vektoru druhého.
     * */
    public default boolean le(Vect a, Vect b){
        return lt(a,b) || eq(a,b);
    }


    /**
     * Vrací <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     *  a první vektor je ve všech složkách vetší než vektor druhý.
     *  <p>
     *  Např.<p>
     *      <code> a = (1,1)^T, b = (0,0)^T --> true</code> <p>
     *      <code> a = (1,1)^T, b = (1,0)^T --> false</code> <p>
     *
     * @return <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     *  a první vektor je ve všech složkách vetší než vektor druhý.
     * */
    public default boolean gt(Vect a, Vect b){
        return lt(b,a);
    }

    /**
     * Vrací <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     *  a první vektor je ve všech složkách vetší nebo roven složkám vektoru druhého.
     *  <p>
     *  Např.<p>
     *      <code> a = (1,1)^T, b = (0,0)^T --> true</code> <p>
     *      <code> a = (1,1)^T, b = (1,0)^T --> false</code> <p>
     *      <code> a = (0,4)^T, b = (1,1)^T --> false</code> <p>
     *
     * @return <code>true</code>, pokud oba vektory nabývají platných hodnot (např. nejsou <code>null</code>, <code>NaN</code> apod)
     *  a první vektor je ve všech složkách vetší nebo roven složkám vektoru druhého.
     * */
    public default boolean ge(Vect a, Vect b){
        return le(b,a);
    }
}

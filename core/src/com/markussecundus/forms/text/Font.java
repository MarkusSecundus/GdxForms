package com.markussecundus.forms.text;

import com.markussecundus.forms.utils.datastruct.ReadonlyList;
import com.markussecundus.forms.utils.vector.VectUtil;



/**
 * Rozhraní pro immutable textový font.
 *
 * @see com.markussecundus.forms.elements.impl.BasicLabel
 *
 * @author MarkusSecundus
 * */
public interface Font<Rend, Vect, Scalar extends Comparable<Scalar>> {

    /**
     * @return rozměry, jichž by nabyl daný text, byl-li by vykreslen v tomto fontu.
     * */
    public Vect computeSize(CharSequence text);

    /**
     * @return nová instance stejného fontu, s novým škálováním.
     * */
    public Font<Rend, Vect, Scalar> withScale(ReadonlyList.Double scale);

    /**
     * @return nová instance stejného fontu, s novým škálováním. (varianta pohodlnější na zápis)
     * */
    public default Font<Rend, Vect, Scalar> withScale(double... scale){return withScale(ReadonlyList.Double.make(scale));}

    /**
     * @return škálování použité touto instancí fontu.
     * */
    public ReadonlyList.Double getScale();

    /**
     * Vykreslí danou posloupnost textu na danou pozici na obrazovce.
     *
     * @param renderer objekt, jež provede vykreslení na obrazovku
     * @param pos pozice, na kterou má text být vykreslen (dle konvence levý dolní roh oblasti s textem)
     * @param text text k vykreslení
     *
     * */
    public void draw(Rend renderer, Vect pos, CharSequence text);

    /**
     * Vrací pomocnou utilitu pro manipulaci s používaným vektorovým typem.
     *
     * @return Pomocnoá utilita pro manipulaci s používaným vektorovým typem.
     *  */
    public VectUtil<Vect, Scalar> getVectUtil();
}

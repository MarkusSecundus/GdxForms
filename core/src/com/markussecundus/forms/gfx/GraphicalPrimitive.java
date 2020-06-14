package com.markussecundus.forms.gfx;

import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;


/**
 * Základní rozhraní pro všechny útvary, které lze vykreslit na obrazovku
 * z nichž bude poskládána grafická stránka uživatelského rozhraní.
 *
 * Implementace závisí na konkrétní platformě, pro kterou
 * bude tato knihovna uplatněna.
 *
 * @param <Rend> Typ objektu, skrze který bude prováděno vykreslení na obrazovku či kamkoliv jinam
 * @param <Pos> Vektorový typ, v jehož souřadnicích grafika funguje
 * @param <Scalar> Skalární typ, z něhož sestávají jednotlivé složky vektoru <code>Pos</code>
 *
 * @see VectUtil
 * @see BinaryGraphicalComposite
 * @see com.markussecundus.forms.elements.Drawable
 *
 * @author MarkusSecundus
 * */
public interface GraphicalPrimitive<Rend, Pos, Scalar extends Comparable<Scalar>> {

    /**
     * Vykreslí objekt na obrazovku.
     *
     * @param pos pozice na obrazovce, na které má objekt být vykreslen,
     *            dle konvence udává levý dolní roh obdélníku opsaného
     *            tomuto objektu (příp. zobecnění této definice pro n-rozměrný prostor)
     * @param renderer objekt, který provede samotné vykreslení na obrazovku
     *
     * @return zda vykreslení proběhlo v pořádku
     * */
    public boolean draw(Rend renderer, Pos pos);

    /**
     * Rozměry, kterých objekt nabývá.
     *
     * @return Rozměry, kterých objekt nabývá.
     * */
    public Property<Pos> dimensions();

    /**
     * Pohodlnější zkratka pro <code>dimensions().get()</code>.
     *
     * @return Pohodlnější zkratka pro <code>dimensions().get()</code>.
     * */
    public default Pos getDimensions(){
        return dimensions().get();
    }

    /**
     * Pohodlnější zkratka pro <code>dimensions().set(newPos)</code>.
     *
     * @param newPos nová hodnota pro rozměry objektu.
     *
     * @return Pohodlnější zkratka pro <code>dimensions().set(newPos)</code>.
     * */
    public default Pos setDimensions(Pos newPos){
        return dimensions().set(newPos);
    }

    /**
     * Vrací pomocnou utilitu pro manipulaci s používaným vektorovým typem.
     *
     * @return Pomocnoá utilita pro manipulaci s používaným vektorovým typem.
     *  */
    public VectUtil<Pos, Scalar> getVectUtil();
}

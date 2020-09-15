package com.markussecundus.formsgdx;

import com.badlogic.gdx.graphics.Color;
import com.markussecundus.forms.utils.vector.Vect2f;

/**
 * Datová třída k pohodlnější inicializaci složitějších grafických útvarů.
 *
 * @see com.markussecundus.formsgdx.graphics.RoundedRectangle
 * @see com.markussecundus.formsgdx.graphics.BasicRectangle
 *
 * @author MarkusSecnudus
 * */
public class Style {
    public Style(){}

    /**
     * Vnitřní barva pro dvoubarevné útvary.
     * */
    public Color innerColor;


    /**
     * Vnější barva pro dvoubarevné útvary.
     * */
    public Color outerColor;

    /**
     * Speciální barva, značící, že je útvar v přechodném stavu.
     * */
    public Color transitionColor;

    /**
     * Kulatost pro zaoblené útvary.
     * */
    public float edgeRoundness;

    /**
     * Poměr tloušťky okrajů pro relativně obrubové útvary.
     * */
    public float borderRatio;

    /**
     * Rozměry okrajů pro absolutně obrubové útvary.
     * */
    public Vect2f borderThickness;



}

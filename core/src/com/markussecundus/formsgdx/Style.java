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
    public Style(Color innerColor, Color outerColor, Color transitionColor, float edgeRoundness, float borderRatio, Vect2f borderThickness) {
        this.innerColor = innerColor;
        this.outerColor = outerColor;
        this.transitionColor = transitionColor;
        this.edgeRoundness = edgeRoundness;
        this.borderRatio = borderRatio;
        this.borderThickness = borderThickness;
    }

    /**
     * Vnitřní barva pro dvoubarevné útvary.
     * */
    public final Color innerColor;


    /**
     * Vnější barva pro dvoubarevné útvary.
     * */
    public final Color outerColor;

    /**
     * Speciální barva, značící, že je útvar v přechodném stavu.
     * */
    public final Color transitionColor;

    /**
     * Kulatost pro zaoblené útvary.
     * */
    public final float edgeRoundness;

    /**
     * Poměr tloušťky okrajů pro relativně obrubové útvary.
     * */
    public final float borderRatio;

    /**
     * Rozměry okrajů pro absolutně obrubové útvary.
     * */
    public final Vect2f borderThickness;



}

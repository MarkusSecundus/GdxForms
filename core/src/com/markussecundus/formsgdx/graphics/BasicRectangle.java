package com.markussecundus.formsgdx.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.markussecundus.forms.gfx.BinaryGraphicalComposite;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;
import com.markussecundus.formsgdx.Style;

/**
 * Jednobarevný obdélník vykreslitelný v enginu LibGDX.
 *
 * @see GraphicalPrimitive
 * @see ShapeRenderer
 * @see com.markussecundus.formsgdx.rendering.BasicRenderer
 * @see RoundedRectangle
 * @see TextureDrawable
 * @see Vect2f
 * @see Color
 *
 * @author MarkusSecundus
 * */
public class BasicRectangle implements GraphicalPrimitive<ShapeRenderer, Vect2f, Float> {
//public:

    /**
     * Inicializuje obdélník danými hodnotami.
     * */
    public BasicRectangle(Vect2f dims,  Color color){
        this.dimensions = new SimpleProperty<>(dims);
        this.color = new SimpleProperty<>(color);
    }

    /**
     * Vykreslí obdélník na dané souřadnice obrazovky.
     *
     * {@inheritDoc}
     * */
    @Override public boolean draw(ShapeRenderer renderer, Vect2f pos) {
        Vect2f dims = getDimensions();
        Color col = getColor();

        renderer.setColor(col);
        renderer.rect(pos.x, pos.y, dims.x, dims.y);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return rozměry obdélníku
     * */
    @Override public Property<Vect2f> dimensions() { return dimensions; }

    /**
     * @return Barva v které bude obdélník vykreslen na obrazovku.
     * */
    public Property<Color> color(){return color;}

    /**
     * @return Pohodnlnější zkrataka za <code>color().get()</code>
     * */
    public Color getColor(){return color().get();}

    /**
     * @return Pohodnlnější zkrataka za <code>color().set(newColor)</code>
     * */
    public Color setColor(Color newColor){return color().set(newColor);}


    /**{@inheritDoc}*/
    @Override public VectUtil<Vect2f, Float> getVectUtil() {return Vect2f.getUtility();}

//private:
    private final Property<Vect2f> dimensions;
    private final Property<Color> color;



//public:

    /**
     * Kompozitum složené ze dvou obdélníků tak, že jeden tvoří vnitřní grafickou část a druhý
     * je vykreslen pod ním a tvoří obrubu.
     *
     * Tloušťka obruby je konstantní.
     *
     * @author MarkusSecundus
     * */
    public static class SObrubou extends BinaryGraphicalComposite.Obruba<ShapeRenderer, BasicRectangle>{

        /**
         * Inicializuje instanci danými hodnotami.
         *
         * @param dims iniciální velikost útvaru
         * @param borderSize tloušťky obruby v jednotlivých dimenzích
         * @param innerCol barva vnitřní části útvaru
         * @param outerCol barva obruby
         * */
        public SObrubou(Vect2f dims, Vect2f borderSize, Color innerCol, Color outerCol){
            super(borderSize, new BasicRectangle(dims, outerCol), new BasicRectangle(dims, innerCol));
        }


        /**
         * Inicializuje instanci hodnotami daného stylu.
         *
         * @param dims iniciální velikost útvaru
         * @param style datová třída obsahující ostatní incializační hodnoty
         * */
        public SObrubou(Vect2f dims, Style style){this(dims, style.borderThickness, style.innerColor, style.outerColor);}
    }



    /**
     * Kompozitum složené ze dvou obdélníků tak, že jeden tvoří vnitřní grafickou část a druhý
     * je vykreslen pod ním a tvoří obrubu.
     *
     * Tloušťka obruby je relativní vůči velikosti celého útvaru.
     *
     * @author MarkusSecundus
     * */
    public static class SRelativniObrubou extends BinaryGraphicalComposite.RelativniObruba<ShapeRenderer, BasicRectangle> {
        /**
         * Inicializuje instanci danými hodnotami.
         *
         * @param dims iniciální velikost útvaru
         * @param borderRatio poměr tloušťky obruby vůči celkové velikosti útvaru (počítá se vůči menšímu z obou jeho rozměrů)
         * @param innerCol barva vnitřní části útvaru
         * @param outerCol barva obruby
         * */
        public SRelativniObrubou(Vect2f dims, float borderRatio, Color innerCol, Color outerCol){
            super(borderRatio, new BasicRectangle(dims, outerCol), new BasicRectangle(dims, innerCol));
        }

        /**
         * Inicializuje instanci hodnotami daného stylu.
         *
         * @param dims iniciální velikost útvaru
         * @param style datová třída obsahující ostatní incializační hodnoty
         * */
        public SRelativniObrubou(Vect2f dims, Style style){this(dims, style.borderRatio, style.innerColor, style.outerColor);}
    }
}

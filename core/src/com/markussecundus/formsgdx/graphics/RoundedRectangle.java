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
 * Jednobarevný obdélník se zaoblenými hranami vykreslitelný v enginu LibGDX.
 *
 * @see GraphicalPrimitive
 * @see ShapeRenderer
 * @see com.markussecundus.formsgdx.rendering.BasicRenderer
 * @see BasicRectangle
 * @see TextureDrawable
 * @see Vect2f
 * @see Color
 *
 * @author MarkusSecundus
 * */
public class RoundedRectangle implements GraphicalPrimitive<ShapeRenderer, Vect2f, Float> {
//public:
    /**
     * Inicializuje obdélník danými hodnotami.
     * */
    public RoundedRectangle(Vect2f dims, float roundness, Color color){
        this.dimensions = new SimpleProperty<>(dims);
        this.roundness = new SimpleProperty<>(roundness);
        this.color = new SimpleProperty<>(color);
    }


    /**
     * Vykreslí obdélník na dané souřadnice obrazovky.
     *
     * {@inheritDoc}
     * */
    @Override public boolean draw(ShapeRenderer renderer, Vect2f pos) {
        float roundness = getRoundness();
        Vect2f dim = getDimensions();
        Color col = getColor();

        renderer.setColor(col);


        float r = Math.min(dim.x, dim.y)*0.5f*roundness;

        renderer.rect(pos.x + r, pos.y, dim.x -r -r, dim.y);
        renderer.rect(pos.x, pos.y + r, dim.x, dim.y-r-r);

        renderer.circle(pos.x+r, pos.y+r, r);
        renderer.circle(pos.x+dim.x - r, pos.y+r, r);
        renderer.circle(pos.x+r, pos.y+dim.y-r, r);
        renderer.circle(pos.x+dim.x - r, pos.y+dim.y-r, r);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return rozměry obdélníku
     * */
    @Override public Property<Vect2f> dimensions() { return dimensions; }

    /**
     * @return zaoblenost hran - poměr poloměrů rohových zaoblovcích kružnic vůči minimu z rozměrů obdélníku
     * */
    public Property<Float> roundness(){return roundness;}

    /**
     * @return Barva v které bude obdélník vykreslen na obrazovku.
     * */
    public Property<Color> color(){return color;}

    /**
     * @return Pohodnlnější zkrataka za <code>roundness().get()</code>
     * */
    public Float getRoundness(){return roundness().get();}
    /**
     * @return Pohodnlnější zkrataka za <code>color().get()</code>
     * */
    public Color getColor(){return color().get();}

    /**
     * @return Pohodnlnější zkrataka za <code>roundness().set(newRoundness)</code>
     * */
    public Float setRoundness(float newRoundness){return roundness().set(newRoundness);}
    /**
     * @return Pohodnlnější zkrataka za <code>color().set(newColor)</code>
     * */
    public Color setColor(Color newColor){return color().set(newColor);}

    @Override public VectUtil<Vect2f, Float> getVectUtil() {return Vect2f.getUtility();}



//private:
    private final Property<Vect2f> dimensions;
    private final Property<Float> roundness;
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
    public static class SObrubou extends BinaryGraphicalComposite.Obruba<ShapeRenderer, RoundedRectangle> {
        /**
         * Inicializuje instanci danými hodnotami.
         *
         * @param dims iniciální velikost útvaru
         * @param roundness iniciální zaoblenost pro obě složky útvaru
         * @param borderSize tloušťky obruby v jednotlivých dimenzích
         * @param innerCol barva vnitřní části útvaru
         * @param outerCol barva obruby
         * */
        public SObrubou(Vect2f dims, float roundness, Vect2f borderSize, Color innerCol, Color outerCol){
            super(borderSize, new RoundedRectangle(dims, roundness, outerCol), new RoundedRectangle(dims, roundness, innerCol));
            obj1.roundness().getSetterListeners()._getPostUtilListeners().add(e->{
                obj2.setRoundness(e.newVal.get());
                return true;
            });
        }

        /**
         * Inicializuje instanci hodnotami daného stylu.
         *
         * @param dims iniciální velikost útvaru
         * @param style datová třída obsahující ostatní incializační hodnoty
         * */
        public SObrubou(Vect2f dims, Style style){this(dims, style.edgeRoundness, style.borderThickness, style.innerColor, style.outerColor);}

        /**
         * Zaoblenost obou komponent
         * @return zaoblenost obou komponent
         * */
        public Property<Float> roundness(){return obj1.roundness();}
    }


    /**
     * Kompozitum složené ze dvou obdélníků tak, že jeden tvoří vnitřní grafickou část a druhý
     * je vykreslen pod ním a tvoří obrubu.
     *
     * Tloušťka obruby je relativní vůči velikosti celého útvaru.
     *
     * @author MarkusSecundus
     * */
    public static class SRelativniObrubou extends BinaryGraphicalComposite.RelativniObruba<ShapeRenderer, RoundedRectangle> {
        /**
         * Inicializuje instanci danými hodnotami.
         *
         * @param dims iniciální velikost útvaru
         * @param roundness iniciální zaoblenost pro obě složky útvaru
         * @param borderRatio poměr tloušťky obruby vůči celkové velikosti útvaru (počítá se vůči menšímu z obou jeho rozměrů)
         * @param innerCol barva vnitřní části útvaru
         * @param outerCol barva obruby
         * */
        public SRelativniObrubou(Vect2f dims, float roundness, float borderRatio, Color innerCol, Color outerCol){
            super(borderRatio, new RoundedRectangle(dims, roundness, outerCol), new RoundedRectangle(dims, roundness, innerCol));
        }


        /**
         * Inicializuje instanci hodnotami daného stylu.
         *
         * @param dims iniciální velikost útvaru
         * @param style datová třída obsahující ostatní incializační hodnoty
         * */
        public SRelativniObrubou(Vect2f dims, Style style){this(dims, style.edgeRoundness, style.borderRatio, style.innerColor, style.outerColor);}
    }
}

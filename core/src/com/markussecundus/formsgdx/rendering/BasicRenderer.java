package com.markussecundus.formsgdx.rendering;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;


/**
 * Základní renderovací utilita pro implementaci formulářů nad LibGDX.
 *
 * Zastřešuje spojenou funkcionalitu {@link Batch} a {@link ShapeRenderer} a režii potřebnou při jejich střídavém používání.
 *
 * @see com.markussecundus.forms.elements.Drawable
 * @see GraphicalPrimitive
 *
 * @see Batch
 * @see SpriteBatch
 * @see ShapeRenderer
 *
 * @see BasicShapeRenderer
 * @see BatchRenderer
 *
 * @author MarkusSecundus
 * */
public class BasicRenderer implements BatchRenderer, BasicShapeRenderer {
    private final Batch ba;
    private final ShapeRenderer sh;

    private boolean baActive=false, shActive=false;

    /**
     * Inicializuje renderer novými instancemi {@link SpriteBatch} a {@link ShapeRenderer}.
     * */
    public BasicRenderer(){this(new SpriteBatch(), new ShapeRenderer());}

    /**
     * Inicializuje renderer danými instancemi {@link SpriteBatch} a {@link ShapeRenderer}.
     * */
    public BasicRenderer(Batch batch, ShapeRenderer shape){
        this.ba = batch;
        this.sh = shape;
    }

    private void endBatch(){
        if(baActive){
            ba.end();
            baActive=false;
        }
    }
    private void endShape(){
        if(shActive){
            sh.end();
            shActive=false;
        }
    }

    /**{@inheritDoc}*/
    public Batch getBatch(){
        if(!baActive) {
            endShape();
            ba.begin();
            baActive = true;
        }
        return ba;
    }
    /**{@inheritDoc}*/
    public ShapeRenderer getShape(){
        if(!shActive) {
            endBatch();
            sh.begin(usedShapeType.get());
            shActive = true;
        }
        return sh;
    }

    /**
     * Typ způsobu vykreslování, s kterým je startována používaná instance {@link ShapeRenderer}.
     * */
    public Property<ShapeRenderer.ShapeType> usedShapeType = new SimpleProperty<>(ShapeRenderer.ShapeType.Filled);

    /**
     * Zastaví renderovací procesy a flushne na obrazovku.
     * */
    public void end(){
        endBatch();
        endShape();
    }

    /**{@inheritDoc}*/
    @Override public void dispose(){
        end();
        ba.dispose();
        sh.dispose();
    }

    /**{@inheritDoc}*/
    @Override public void flush(){
        if(baActive)ba.flush();
        if(shActive)sh.flush();
    }

    /**
     * @return Grafické primitivum ze vstupu obalené ve wrapperu přijímajícím {@link BasicRenderer} jako renderovací utilitu.
     * */
    public static<Vect, Scalar extends Comparable<Scalar>> GraphicalPrimitive<BasicRenderer, Vect, Scalar> conv(GraphicalPrimitive<ShapeRenderer, Vect, Scalar> base){
        return new ShapeToBasicDrw<>(base);
    }

    /**
     * Wrapper typ pro konverze {@link GraphicalPrimitive} renderujících se přímo přes {@link ShapeRenderer}
     * na Primitiva renderovaná přes {@link BasicRenderer}.
     *
     * @see GraphicalPrimitive
     * @see BasicRenderer
     * @see ShapeRenderer
     *
     * @author MarkusSecundus
     * */
    public static class ShapeToBasicDrw<Vect, Scalar extends Comparable<Scalar>, Type extends GraphicalPrimitive<ShapeRenderer, Vect, Scalar>> implements GraphicalPrimitive<BasicRenderer, Vect, Scalar>{
        /**
         * Primitivum renderující se přes {@link ShapeRenderer}.
         * */
        public final Type base;

        public ShapeToBasicDrw(Type base){this.base = base;}

        /**
         * Získá z renderovací instance instanci {@link ShapeRenderer} a vykreslí s ní vnitřní útvar.
         * {@inheritDoc}*/
        @Override
        public boolean draw(BasicRenderer renderer, Vect pos) {
            return base.draw(renderer.getShape(), pos);
        }

        /**
         * Přesměrováno na Property vnitřního útvaru.
         *
         * {@inheritDoc}
         * */
        @Override
        public Property<Vect> dimensions() {
            return base.dimensions();
        }

        /**
         * Přesměrováno na vnitřní útvar.
         *
         * {@inheritDoc}
         * */
        @Override
        public VectUtil<Vect, Scalar> getVectUtil() {
            return base.getVectUtil();
        }
    }
}

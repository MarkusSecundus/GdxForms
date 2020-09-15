package com.markussecundus.formsgdx.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.markussecundus.forms.text.Font;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.datastruct.DefaultDictByIdentity;
import com.markussecundus.forms.utils.datastruct.ReadonlyList;
import com.markussecundus.forms.utils.vector.Vect2d;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.formsgdx.GdxFormsUtils;

import java.util.Arrays;


/**
 * Wrapper nad LibGDXím {@link BitmapFont}em, splnující rozhraní {@link Font}
 * a zaručující vykreslování na obrazovku s pevně daným škálováním a barvou.
 *
 * @param <Rend> datový typ použitý fontem k vykreslení textu na obrazovku
 * @param <Vect> fontem použitý vektorový typ; se skalárními složkami typu {@link Float}
 *
 *
 * @see Font
 * @see com.markussecundus.forms.elements.impl.BasicLabel
 *
 * @author MarkusSecundus
 * */
public abstract class FormsBitmapFont<Rend extends Batch, Vect> implements Font<Rend, Vect, Float>, Disposable {

    /**
     * Sestrojí novou instanci nad dodanými hodnotami.
     * */
    public FormsBitmapFont(BitmapFont base, Color textColor, float scaleX, float scaleY){
        this.textColor =textColor;
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        this._base = base;
        referenceCounterForBases.get(base).inc();
    }

    /**
     * Surová vnitřní implementace {@link BitmapFont}u, na kterou tento wrapper odkazuje.
     * */
    protected final BitmapFont _base;

    /**
     * Zaručí, že vnitřní implementace {@link BitmapFont}u má nastavené
     * správné škálování a barvu, a vrátí odkaz na ni.
     *
     * @return vnitřní implementace {@link BitmapFont}u, na kterou tento wrapper odkazuje.
     * */
    protected BitmapFont base(){
        _base.setColor(textColor);
        _base.getData().setScale(scaleX, scaleY);
        return _base;
    }

    /**
     * Barva písma.
     * */
    public final Color textColor;

    /**
     * Škálování fontu v x-ové souřadnici.
     * */
    public final float scaleX;
    /**
     * Škálování fontu v y-ové souřadnici.
     * */
    public final float scaleY;


    /**
     * Vytáhne z daného vektoru složku odpovídající x-ové souřadnici.
     * <p></p>
     * K přepsání v podtřídách specializovaných na konkrétní typ vektoru, pro lepší výkon.
     *
     * @return složka daného vektoru odpovídající x-ové souřadnici
     * */
    protected float extractX(Vect v){return getVectUtil().getNth(v, 0);}

    /**
     * Vytáhne z daného vektoru složku odpovídající y-ové souřadnici.
     * <p></p>
     * K přepsání v podtřídách specializovaných na konkrétní typ vektoru, pro lepší výkon.
     *
     * @return složka daného vektoru odpovídající y-ové souřadnici
     * */
    protected float extractY(Vect v){return getVectUtil().getNth(v, 1);}



    private final GlyphLayout glyphLayout = new GlyphLayout();
    private void setTextToGlyph(CharSequence text){
        glyphLayout.setText(base(), text);
    }


    @Override
    public Vect computeSize(CharSequence text) {
        setTextToGlyph(text);
        return getVectUtil().compose(glyphLayout.width, glyphLayout.height);
    }


    @Override
    public Font<Rend, Vect, Float> withScale(ReadonlyList.Double scale) {
        return with(textColor, (float)scale.getNth_raw(0), (float)scale.getNth_raw(1));
    }

    @Override
    public ReadonlyList.Double getScale() {
        return Vect2d.make(scaleX, scaleY);
    }

    /**
     * Vrátí kopii tohoto fontu s barvou písma změněnou na specifikovanou hodnotu.
     *
     * @param newColor barva písma, kterou má disponovat nový font
     *
     * @return Kopie tohoto fontu s barvou písma změněnou na specifikovanou hodnotu.
     * */
    public Font<Rend, Vect, Float> withColor(Color newColor){
        return with(newColor, scaleX, scaleY);
    }

    /**
     * Vrátí kopii tohoto fontu s barvou písma a škálováním změněnými na specifikovanou hodnotu.
     *
     * @param newColor barva písma, kterou má disponovat nový font
     * @param scaleX škálování, kterým má disponovat nový font
     * @param scaleY škálování, kterým má disponovat nový font
     *
     * @return Kopie tohoto fontu s barvou písma změněnou na specifikovanou hodnotu.
     * */
    public abstract Font<Rend, Vect, Float> with(Color newColor, float scaleX, float scaleY);


    @Override
    public void draw(Rend renderer, Vect pos, CharSequence text) {
        setTextToGlyph(text);
        base().draw(renderer, text, extractX(pos), extractY(pos) + glyphLayout.height);
    }




    @Override
    public void dispose() {
        if(referenceCounterForBases.get(_base).dec() <=0) {
            referenceCounterForBases.remove(_base);
            _base.dispose();
        }
    }

    private final DefaultDictByIdentity<BitmapFont, FormsUtil.Counter> referenceCounterForBases = new DefaultDictByIdentity<>(font-> FormsUtil.Counter.make());


    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{_base, textColor, scaleX, scaleY});
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof FormsBitmapFont<?,?>))
            return false;
        FormsBitmapFont<?,?> f = ((FormsBitmapFont<?,?>)o);
        return FormsUtil.equals(f.getVectUtil(), getVectUtil())
                && f.scaleX == scaleX && f.scaleY == scaleY
                && f._base == _base
                && FormsUtil.equals(f.textColor, textColor);
    }

    /**
     * Barva sloužící jako výchozí hodnota, pokud žádná není specifikována.
     * */
    public static final Color DEFAULT_COLOR = Color.BLACK;
    /**
     * Škálování použité jako výchozí hodnota, pokud žádné není specifikováno.
     * */
    public static final float DEFAULT_SCALE = 1f;

    /**
     * Vytvoří novou instanci, parametrizovanou vektorovým typem {@link Vect2f}.
     *
     * @param <Rend> datový typ použitý fontem k vykreslení textu na obrazovku
     *
     * @param font instance {@link BitmapFont}u, nad kterou má být {@link FormsBitmapFont} postaven
     * @param col barva, které má font nabývat
     * @param scaleX škálování, kterého má font nabývat v x-ové souřadnici
     * @param scaleY škálování, kterého má font nabývat v y-ové souřadnici
     *
     * @return nová instance {@link FormsBitmapFont} nesoucí specifikovaná hodnoty.
     * */
    public static<Rend extends Batch> FormsBitmapFont<Rend, Vect2f> make_Vect2f(BitmapFont font, Color col, float scaleX, float scaleY){
        return new WithVect2f<>(font, col, scaleX, scaleY);
    }
    /**
     * Vytvoří novou instanci, parametrizovanou vektorovým typem {@link Vect2f}.
     *
     * @param <Rend> datový typ použitý fontem k vykreslení textu na obrazovku
     *
     * @param font instance {@link BitmapFont}u, nad kterou má být {@link FormsBitmapFont} postaven
     * @param col barva, které má font nabývat
     * @param scaleXY škálování, kterého má font nabývat v x-ové i y-ové souřadnici
     *
     * @return nová instance {@link FormsBitmapFont} nesoucí specifikovaná hodnoty.
     * */
    public static<Rend extends Batch> FormsBitmapFont<Rend, Vect2f> make_Vect2f(BitmapFont font, Color col, float scaleXY){
        return make_Vect2f(font, col, scaleXY, scaleXY);
    }

    /**
     * Vytvoří novou instanci, parametrizovanou vektorovým typem {@link Vect2f}.
     *
     * @param <Rend> datový typ použitý fontem k vykreslení textu na obrazovku
     *
     * @param font instance {@link BitmapFont}u, nad kterou má být {@link FormsBitmapFont} postaven
     *
     * @return nová instance {@link FormsBitmapFont} nesoucí specifikovaná hodnoty.
     * */
    public static<Rend extends Batch> FormsBitmapFont<Rend, Vect2f> make_Vect2f(BitmapFont font){
        return make_Vect2f(font, DEFAULT_COLOR, DEFAULT_SCALE);
    }



    /**
     * Vytvoří novou instanci, parametrizovanou vektorovým typem {@link Vector2}.
     *
     * @param <Rend> datový typ použitý fontem k vykreslení textu na obrazovku
     *
     * @param font instance {@link BitmapFont}u, nad kterou má být {@link FormsBitmapFont} postaven
     * @param col barva, které má font nabývat
     * @param scaleX škálování, kterého má font nabývat v x-ové souřadnici
     * @param scaleY škálování, kterého má font nabývat v y-ové souřadnici
     *
     * @return nová instance {@link FormsBitmapFont} nesoucí specifikovaná hodnoty.
     * */
    public static<Rend extends Batch> FormsBitmapFont<Rend, Vector2> make_Vector2(BitmapFont font, Color col, float scaleX, float scaleY){
        return new WithVector2<>(font, col, scaleX, scaleY);
    }
    /**
     * Vytvoří novou instanci, parametrizovanou vektorovým typem {@link Vector2}.
     *
     * @param <Rend> datový typ použitý fontem k vykreslení textu na obrazovku
     *
     * @param font instance {@link BitmapFont}u, nad kterou má být {@link FormsBitmapFont} postaven
     * @param col barva, které má font nabývat
     * @param scaleXY škálování, kterého má font nabývat v x-ové i y-ové souřadnici
     *
     * @return nová instance {@link FormsBitmapFont} nesoucí specifikovaná hodnoty.
     * */
    public static<Rend extends Batch> FormsBitmapFont<Rend, Vector2> make_Vector2(BitmapFont font, Color col, float scaleXY){
        return make_Vector2(font, col, scaleXY, scaleXY);
    }
    /**
     * Vytvoří novou instanci, parametrizovanou vektorovým typem {@link Vector2}.
     *
     * @param <Rend> datový typ použitý fontem k vykreslení textu na obrazovku
     *
     * @param font instance {@link BitmapFont}u, nad kterou má být {@link FormsBitmapFont} postaven
     *
     * @return nová instance {@link FormsBitmapFont} nesoucí specifikovaná hodnoty.
     * */
    public static<Rend extends Batch> FormsBitmapFont<Rend, Vector2> make_Vector2(BitmapFont font){
        return make_Vector2(font, DEFAULT_COLOR, DEFAULT_SCALE);
    }




    private static class WithVect2f<Rend extends Batch> extends FormsBitmapFont<Rend, Vect2f> {
        public WithVect2f(BitmapFont base, Color textColor, float scaleX, float scaleY) {
            super(base, textColor, scaleX, scaleY);
        }

        @Override
        public WithVect2f<Rend> with(Color newColor, float scaleX, float scaleY) {
            return new WithVect2f<>(_base, newColor, scaleX, scaleY);
        }

        @Override
        public VectUtil<Vect2f, Float> getVectUtil() {
            return Vect2f.getUtility();
        }

        @Override protected final float extractX(Vect2f v) { return v.x; }
        @Override protected final float extractY(Vect2f v) { return v.y;}
    }


    private static class WithVector2<Rend extends Batch> extends FormsBitmapFont<Rend, Vector2> {
        public WithVector2(BitmapFont base, Color textColor, float scaleX, float scaleY) {
            super(base, textColor, scaleX, scaleY);
        }

        @Override
        public WithVector2<Rend> with(Color newColor, float scaleX, float scaleY) {
            return new WithVector2<>(_base, newColor, scaleX, scaleY);
        }

        @Override
        public VectUtil<Vector2, Float> getVectUtil() {
            return GdxFormsUtils.Vector2Util.INSTANCE;
        }

        @Override protected final float extractX(Vector2 v) { return v.x; }
        @Override protected final float extractY(Vector2 v) { return v.y; }
    }
}

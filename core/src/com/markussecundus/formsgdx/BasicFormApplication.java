package com.markussecundus.formsgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.markussecundus.forms.elements.Drawable;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.formsgdx.rendering.BasicRenderer;


/**
 * Základní třída koncipovaná jako báze vhodná pro libovolnou LibGDXí aplikaci
 * založenou na Formulářové Knihovně.
 *
 * @see com.badlogic.gdx.ApplicationAdapter
 * @see Drawable
 * @see BasicRenderer
 * @see Vect2f
 * @see Color
 *
 * @author MarkusSecundus
 * */
public abstract class BasicFormApplication extends ApplicationAdapter {
    private Drawable<BasicRenderer, Vect2f> Form;
    public BasicRenderer Renderer;

    public Color BackgroundColor;
    public final Vect2f Origin = Vect2f.ZERO;

    /**
     * Metoda která vytvoří formulář a vrátí jeho kořenový Prvek.
     *
     * @return kořenový prvek formuláře
     * */
    public abstract Drawable<BasicRenderer, Vect2f> createForm();

    /**{@inheritDoc}*/
    @Override
    public final void create() {
        super.create();
        BackgroundColor = Color.BLACK;
        Renderer = new BasicRenderer();
        Form = createForm();
    }


    /**
     * Vrátí číslo aktuálního snímku.
     * @see Drawable
     * */
    public final int getFrameNum() {
        return frameNum;
    }

    private int frameNum = -1;

    /**{@inheritDoc}*/
    @Override
    public final void render() {
        Form.update(Gdx.graphics.getDeltaTime(), ++frameNum);

        Gdx.gl.glClearColor(BackgroundColor.r, BackgroundColor.g, BackgroundColor.b, BackgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Form.draw(Renderer, Origin);

        Renderer.end();
    }

    /**{@inheritDoc}*/
    @Override
    public void dispose() {
        Renderer.dispose();
        Form.dispose();
    }

}

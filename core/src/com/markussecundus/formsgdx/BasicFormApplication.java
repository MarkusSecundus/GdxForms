package com.markussecundus.formsgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.formsgdx.rendering.BasicRenderer;


/**
 * Základní třída koncipovaná jako báze vhodná pro libovolnou LibGDXí aplikaci
 * založenou na Formulářové Knihovně.
 *
 * @see com.badlogic.gdx.ApplicationAdapter
 * @see DrawableElem
 * @see BasicRenderer
 * @see Vect2f
 * @see Color
 *
 * @author MarkusSecundus
 * */
public abstract class BasicFormApplication extends ApplicationAdapter {
    private DrawableElem<BasicRenderer, Vect2f> Form;

    /**
     * Objekt sloužící k vykreslení kořenového formuláře na obrazovku.
     * */
    public BasicRenderer Renderer;

    /**
     * Barva pozadí.
     * */
    public Color BackgroundColor;

    /**
     * Pozice, na kterou bude vykreslován kořenový formulář.
     * */
    public final Vect2f Origin = Vect2f.ZERO;

    /**
     * Metoda která vytvoří formulář a vrátí jeho kořenový Prvek.
     *
     * @return kořenový prvek formuláře
     * */
    public abstract DrawableElem<BasicRenderer, Vect2f> createForm();


    @Override
    public final void create() {
        super.create();
        BackgroundColor = Color.BLACK;
        Renderer = new BasicRenderer();
        Form = createForm();
    }


    /**
     * Vrátí číslo aktuálního snímku.
     * @see DrawableElem
     * */
    public final int getFrameNum() {
        return frameNum;
    }

    private int frameNum = -1;


    @Override
    public final void render() {
        Form.update(Gdx.graphics.getDeltaTime(), ++frameNum);

        Gdx.gl.glClearColor(BackgroundColor.r, BackgroundColor.g, BackgroundColor.b, BackgroundColor.a);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        Form.draw(Renderer, Origin);

        Renderer.end();
    }


    @Override
    public void dispose() {
        Renderer.dispose();
        Form.dispose();
    }

}

package com.markussecundus.formsgdx.input.args;

import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.InputManager;

/**
 * Základní datová třída sdružující argumenty společné pro všechny typy událostí zpracování vstupu.
 *
 * @see OnInputEventArgs
 *
 * @see InputConsumer
 * @see InputManager
 *
 * @see OnKeyClickedArgs
 * @see OnKeyTypedArgs
 *
 * @see OnMouseMovedArgs
 * @see OnTouchDraggedArgs
 * @see OnTouchArgs
 *
 * @see OnScrolledArgs
 *
 * @author MarkusSecundus
 * */
public class OnInputEventArgs {
    /**
     * Instance {@link InputManager}, v níž započala daná událost.
     * */
    public final InputManager manager;

    /**
     * Instance {@link InputConsumer}, v jejímž listeneru je tato instance argumentů používána.
     * */
    public final InputConsumer actor;

    /**
     * Inicializuje instanci danými hodnotami.
     * */
    public OnInputEventArgs(InputManager manager, InputConsumer actor){
        this.manager = manager;
        this.actor = actor;
    }
}

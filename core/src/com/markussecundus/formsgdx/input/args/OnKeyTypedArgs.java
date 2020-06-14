package com.markussecundus.formsgdx.input.args;

import com.badlogic.gdx.InputProcessor;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.InputManager;

/**
 * Datová třída reprezentující argument pro funkci <code>onKeyTyped</code> rozhraní {@link InputConsumer}.
 *
 * @see OnKeyClickedArgs
 *
 * @see OnInputEventArgs
 *
 * @see InputConsumer
 * @see InputManager
 *
 * @see OnMouseMovedArgs
 * @see OnTouchDraggedArgs
 * @see OnTouchArgs
 *
 * @see OnScrolledArgs
 *
 * @author MarkusSecundus
 * */
public class OnKeyTypedArgs extends OnInputEventArgs {
    /**
     * Stejný význam jako hodnota <code>character</code> předávaná
     * metodě <code>onKeyTyped</code> na LibGDXím rozhraní {@link InputProcessor}.
     * */
    public final char character;

    /**
     * Inicializuje instanci danými hodnotami.
     * */
    protected OnKeyTypedArgs(InputManager manager, InputConsumer actor, char character){
        super(manager, actor);
        this.character = character;
    }


    /**
     * @return Instance datové třídy sestávající z daných hodnot.
     * */
    public static OnKeyTypedArgs make(InputManager manager, InputConsumer actor, char character){
        return new OnKeyTypedArgs(manager, actor, character);
    }

    /**
     * @return Instance datové třídy sestávající z nových hodnot, zachovávajíce původní hodnoty tam, kde nové nejsou poskytnuty.
     * */
    public OnKeyTypedArgs with(InputConsumer actor){ return actor==this.actor?this:make(manager, actor, character);}
}

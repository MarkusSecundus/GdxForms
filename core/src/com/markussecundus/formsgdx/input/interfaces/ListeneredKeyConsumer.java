package com.markussecundus.formsgdx.input.interfaces;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.formsgdx.input.mixins.IListeneredKeyConsumer;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.args.OnKeyClickedArgs;
import com.markussecundus.formsgdx.input.args.OnKeyTypedArgs;

/**
 * Rozhraní pro {@link InputConsumer}, jenž má pro zpracovávání klávesnicových událostí dedikované {@link EventDelegate}s.
 *
 * Pro kanonickou implementaci viz {@link IListeneredKeyConsumer}.
 *
 * @see IListeneredKeyConsumer
 *
 * @see ListeneredScrollConsumer
 * @see ListeneredTouchConsumer
 *
 * @see ListeneredUniversalConsumer
 *
 * @author MarkusSecundus
 * */
public interface ListeneredKeyConsumer extends InputConsumer{


    /**
     * @return Delegát zpracovávající volání <code>keyDown</code>.
     * */
    ConstProperty<EventDelegate<OnKeyClickedArgs>> onKeyDownListener();
    /**
     * @return Delegát zpracovávající volání <code>keyUp</code>.
     * */
    ConstProperty<EventDelegate<OnKeyClickedArgs>> onKeyUpListener();
    /**
     * @return Delegát zpracovávající volání <code>keyTyped</code>.
     * */
    ConstProperty<EventDelegate<OnKeyTypedArgs>> onKeyTypedListener();


    /**
     * Vykoná <code>onKeyDownListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean keyDown(OnKeyClickedArgs e){
        return getOnKeyDownListener().exec(e.with(this));
    }

    /**
     * Vykoná <code>onKeyUpListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean keyUp(OnKeyClickedArgs e){
        return getOnKeyUpListener().exec(e.with(this));
    }

    /**
     * Vykoná <code>onKeyTypedListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean keyTyped(OnKeyTypedArgs e){
        return getOnKeyTypedListener().exec(e.with(this));
    }


    /**
     * Pohodlnější zkratka pro <code>onKeyDownListener().get()</code>
     * */
    default EventDelegate<OnKeyClickedArgs> getOnKeyDownListener(){ return onKeyDownListener().get(); }
    /**
     * Pohodlnější zkratka pro <code>onKeyUpListener().get()</code>
     * */
    default EventDelegate<OnKeyClickedArgs> getOnKeyUpListener(){ return onKeyUpListener().get(); }
    /**
     * Pohodlnější zkratka pro <code>onKeyTypedListener().get()</code>
     * */
    default EventDelegate<OnKeyTypedArgs> getOnKeyTypedListener(){ return onKeyTypedListener().get(); }


}

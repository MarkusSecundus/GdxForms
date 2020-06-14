package com.markussecundus.formsgdx.input.interfaces;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.formsgdx.input.InputConsumer;
import com.markussecundus.formsgdx.input.args.OnMouseMovedArgs;
import com.markussecundus.formsgdx.input.args.OnTouchArgs;
import com.markussecundus.formsgdx.input.args.OnTouchDraggedArgs;

/**
 * Rozhraní pro {@link InputConsumer}, jenž má pro zpracovávání událostí vstupu z myši / dotykové obrazovky dedikované {@link EventDelegate}s.
 *
 * Pro kanonickou implementaci viz {@link com.markussecundus.formsgdx.input.mixins.IListeneredTouchConsumer}.
 *
 * @see com.markussecundus.formsgdx.input.mixins.IListeneredTouchConsumer
 *
 * @see ListeneredKeyConsumer
 * @see ListeneredScrollConsumer
 *
 * @see ListeneredUniversalConsumer
 *
 * @author MarkusSecundus
 * */
public interface ListeneredTouchConsumer extends InputConsumer {

    /**
     * @return Delegát zpracovávající volání <code>touchDown</code>.
     * */
    ConstProperty<EventDelegate<OnTouchArgs>> onTouchDownListener();
    /**
     * @return Delegát zpracovávající volání <code>touchUp</code>.
     * */
    ConstProperty<EventDelegate<OnTouchArgs>> onTouchUpListener();
    /**
     * @return Delegát zpracovávající volání <code>touchDragged</code>.
     * */
    ConstProperty<EventDelegate<OnTouchDraggedArgs>> onTouchDraggedListener();
    /**
     * @return Delegát zpracovávající volání <code>mouseMoved</code>.
     * */
    ConstProperty<EventDelegate<OnMouseMovedArgs>> onMouseMovedListener();

    /**
     * @return Delegát zpracovávající volání <code>clicked</code>.
     * */
    ConstProperty<EventDelegate<OnTouchArgs>> onClickedListener();
    /**
     * @return Delegát zpracovávající volání <code>unclicked</code>.
     * */
    ConstProperty<EventDelegate<OnTouchArgs>> onUnclickedListener();




    /**
     * Vykoná <code>onTouchDownListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean touchDown(OnTouchArgs e ){
        return getOnTouchDownListener().exec(e.with(this));
    }

    /**
     * Vykoná <code>onTouchUpListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean touchUp(OnTouchArgs e){
        return getOnTouchUpListener().exec(e.with(this));
    }

    /**
     * Vykoná <code>onTouchDraggedListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean touchDragged(OnTouchDraggedArgs e){
        return getOnTouchDraggedListener().exec(e.with(this));
    }

    /**
     * Vykoná <code>onMouseMovedListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean mouseMoved(OnMouseMovedArgs e){
        return getOnMouseMovedListener().exec(e.with(this));
    }

    /**
     * Vykoná <code>onClickedListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean clicked(OnTouchArgs e){
        return getOnClickedListener().exec(e.with(this));
    }

    /**
     * Vykoná <code>onUnclickedListener</code> s předaným argumentem.
     *
     * {@inheritDoc}
     * */
    @Override default boolean unclicked(OnTouchArgs e){
        return getOnUnclickedListener().exec(e.with(this));
    }


    /**
     * Pohodlnější zkratka pro <code>onTouchDownListener().get()</code>
     * */
    default EventDelegate<OnTouchArgs> getOnTouchDownListener(){ return onTouchDownListener().get(); }
    /**
     * Pohodlnější zkratka pro <code>onTouchUpListener().get()</code>
     * */
    default EventDelegate<OnTouchArgs> getOnTouchUpListener(){ return onTouchUpListener().get(); }
    /**
     * Pohodlnější zkratka pro <code>onTouchDraggedListener().get()</code>
     * */
    default EventDelegate<OnTouchDraggedArgs> getOnTouchDraggedListener(){ return onTouchDraggedListener().get(); }
    /**
     * Pohodlnější zkratka pro <code>onMouseMovedListener().get()</code>
     * */
    default EventDelegate<OnMouseMovedArgs> getOnMouseMovedListener(){ return onMouseMovedListener().get(); }

    /**
     * Pohodlnější zkratka pro <code>onClickedListener().get()</code>
     * */
    default EventDelegate<OnTouchArgs> getOnClickedListener(){ return onClickedListener().get(); }
    /**
     * Pohodlnější zkratka pro <code>onUnclickedListener().get()</code>
     * */
    default EventDelegate<OnTouchArgs> getOnUnclickedListener(){ return onUnclickedListener().get(); }


}

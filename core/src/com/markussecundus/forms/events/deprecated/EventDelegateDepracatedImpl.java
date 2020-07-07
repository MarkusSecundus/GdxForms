package com.markussecundus.forms.events.deprecated;

import com.markussecundus.forms.events.EventDelegate;
import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.events.ListenerPriorities;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.AbstractProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**
 * Původní kanonická implementace {@link EventDelegate}.
 * <p>
 *     Schopen pojmout pouze Listenery s prioritami
 *     <code>EventDelegate.USER</code>, <code>EventDelegate.PRE_UTIL</code>
 *     a <code>EventDelegate.POST_UTIL</code>
 * </p>
 *
 * @see com.markussecundus.forms.events.EventDelegate
 * @see com.markussecundus.forms.events.IEventDelegate
* */
@Deprecated
public class EventDelegateDepracatedImpl<Args> implements EventDelegate<Args> {
//public:


    @Override public boolean exec(Args e) {
        if((util_listeners ==null || iterateList(e, _getUtilListeners()))
                && iterateList(e, getListeners())
                && (post_util_listeners == null || iterateList(e, _getPostUtilListeners())))
            return getReturnValuePolicy().convRetVal(true);
        else
            return getReturnValuePolicy().convRetVal(false);
    }




    @Override public List<EventListener<? super Args>> getListeners(Integer priority){
        switch (priority){
            case ListenerPriorities.Raw.PRE_UTIL:
                if(util_listeners ==null)
                    util_listeners = MAKE_UTIL_LISTENER_LIST();
                return util_listeners;

            case ListenerPriorities.Raw.USER:
                return listeners;

            case ListenerPriorities.Raw.POST_UTIL:
                if(post_util_listeners==null)
                    post_util_listeners = MAKE_POST_UTIL_LISTENER_LIST();
                return post_util_listeners;

            default:
                return null;
        }
    }



    @Override
    public boolean removeListener(EventListener<?> list) {
        return _getUtilListeners().remove(list) || getListeners().remove(list) || _getPostUtilListeners().remove(list);
    }

    @Override
    public void clear() {
        if(util_listeners != null)
            _getUtilListeners().clear();

        getListeners().clear();

        if(post_util_listeners!=null)
            _getPostUtilListeners().clear();
    }



    /**
     *
     * {@inheritDoc}
     * <p></p>
     * {@link com.markussecundus.forms.events.EventDelegate.ReturnValuePolicy} je generována líně.
     * */
    @Override public Property<ReturnValuePolicy> returnValuePolicy(){
        if(returnValuePolicyProperty == null)
            returnValuePolicyProperty = new ReturnValuePolicyPropertyType();
        return returnValuePolicyProperty;
    }


    @Override public ReturnValuePolicy getReturnValuePolicy() {
        return returnValuePolicyProperty==null ? returnValuePolicy : EventDelegate.super.getReturnValuePolicy();
    }

    @Override public ReturnValuePolicy setReturnValuePolicy(ReturnValuePolicy pol) {
        return returnValuePolicyProperty==null? returnValuePolicy = pol : EventDelegate.super.setReturnValuePolicy(pol);
    }









//protected:
    /**
     *
     * Vrací novou instanci prázného {@link List}u, který bude sloužit k přechovávání Listenerů
     * pro <code>getListeners</code>.
     * <p></p>
     * Volá se jen jednou, v konstruktoru.
     * Tuto metodu přepište, chcete-li listenery přechovávat v jiné implementaci {@link List}u,
     * než je defaultní {@link ArrayList}.
     *
     * @return nová instance prázdného {@link List}u pro <code>getListeners</code>
     * */
    protected List<EventListener<? super Args>> MAKE_LISTENER_LIST(){ return new ArrayList<>();}

    /**
     *
     * Vrací novou instanci prázného {@link List}u, který bude sloužit k přechovávání Listenerů
     * pro <code>_getUtilListeners</code>.
     * <p></p>
     * Volá se jen jednou, v konstruktoru.
     * Tuto metodu přepište, chcete-li listenery přechovávat v jiné implementaci {@link List}u,
     * než je defaultní.
     * <p>
     * Defaultně odkazuje na <code>MAKE_LISTENER_LIST</code>
     *
     * @return nová instance prázdného {@link List}u pro <code>_getUtilListeners</code>
     * */
    protected List<EventListener<? super Args>> MAKE_UTIL_LISTENER_LIST(){ return MAKE_LISTENER_LIST();}

    /**
     * Vrací novou instanci prázného {@link List}u, který bude sloužit k přechovávání Listenerů
     * pro <code>_getPostUtilListeners</code>.
     * <p></p>
     * Volá se jen jednou, v konstruktoru.
     * Tuto metodu přepište, chcete-li listenery přechovávat v jiné implementaci {@link List}u,
     * než je defaultní.
     * <p>
     * Defaultně odkazuje na <code>MAKE_LISTENER_LIST</code>
     *
     * @return nová instance prázdného {@link List}u pro <code>_getPostUtilListeners</code>
     * */
    protected List<EventListener<? super Args>> MAKE_POST_UTIL_LISTENER_LIST(){ return MAKE_UTIL_LISTENER_LIST();}

//private:

    private final List<EventListener<? super Args>> listeners = MAKE_LISTENER_LIST();
    private List<EventListener<? super Args>> util_listeners = null, post_util_listeners = null;

    private ReturnValuePolicy returnValuePolicy = DEFAULT_RET_VAL_POLICY;

    private Property<ReturnValuePolicy> returnValuePolicyProperty = null;

    private class ReturnValuePolicyPropertyType extends AbstractProperty<ReturnValuePolicy>{
        @Override protected ReturnValuePolicy obtain() {
            return returnValuePolicy;
        }
        @Override protected ReturnValuePolicy change(ReturnValuePolicy val) {
            return returnValuePolicy = (val==null ? DEFAULT_RET_VAL_POLICY : val);
        }
    }



    private boolean iterateList(Args e, List<EventListener<? super Args>> listeners){
        for(ListIterator<EventListener<? super Args>> it = listeners.listIterator();it.hasNext();){
            EventListener<? super Args> akt_listener = it.next();
            try{
                if(!akt_listener.exec(e))
                    return false;
            }catch (EventDelegate.ApplyOnParentDelegate apply_request){
                if(!apply_request.exec(this, akt_listener))
                    return false;
            }
        }
        return true;
    }
}

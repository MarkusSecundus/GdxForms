package com.markussecundus.forms.events;

import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.AbstractProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


/**
 * Kanonická implementace {@link EventDelegate}.
 *
 * 'I' jako 'Implementace'
 * (Ano, je to provokace vůči .NET. A stojím si za tím.)
* */
public class IEventDelegate<Args> implements EventDelegate<Args> {
//public:
    /**
     * {@inheritDoc}
     * */
    @Override public List<EventListener<? super Args>> getListeners(){return listeners;}


    /**
     * {@inheritDoc}
     * List je generován líně.
     * */
    @Override public List<EventListener<? super Args>> _getUtilListeners() {
        if(util_listeners ==null)
            util_listeners = MAKE_UTIL_LISTENER_LIST();
        return util_listeners;
    }

    /**
     * {@inheritDoc}
     * List je generován líně.
     * */
    @Override public List<EventListener<? super Args>> _getPostUtilListeners() {
        if(post_util_listeners==null)
            post_util_listeners = MAKE_POST_UTIL_LISTENER_LIST();
        return post_util_listeners;
    }


    /**
     * {@inheritDoc}
     * {@link com.markussecundus.forms.events.EventDelegate.ReturnValuePolicy} je generována líně.
     * */
    @Override public Property<ReturnValuePolicy> returnValuePolicy(){
        if(returnValuePolicyProperty == null)
            returnValuePolicyProperty = new ReturnValuePolicyPropertyType();
        return returnValuePolicyProperty;
    }

    /**
     * {@inheritDoc}
     * */
    @Override public ReturnValuePolicy getReturnValuePolicy() {
        return returnValuePolicyProperty==null ? returnValuePolicy : EventDelegate.super.getReturnValuePolicy();
    }

    /**
     * {@inheritDoc}
     * */
    @Override public ReturnValuePolicy setReturnValuePolicy(ReturnValuePolicy pol) {
        return returnValuePolicyProperty==null? returnValuePolicy = pol : EventDelegate.super.setReturnValuePolicy(pol);
    }

    /**
     * {@inheritDoc}
     * */
    @Override public boolean exec(Args e) {
        if((util_listeners ==null || iterateList(e, _getUtilListeners()))
           && iterateList(e, getListeners())
           && (post_util_listeners == null || iterateList(e, _getPostUtilListeners())))
                return getReturnValuePolicy().convRetVal(true);
        else
            return getReturnValuePolicy().convRetVal(false);
    }

    /**
     * Pomocná funkce pro iteraci přes listenery.
     * */
    private boolean iterateList(Args e, List<EventListener<? super Args>> listeners){
        for(ListIterator<EventListener<? super Args>> it = listeners.listIterator();it.hasNext();){
            try{
                if(!it.next().exec(e))
                    return false;
            }catch(EventDelegate.DeleteSelf delete_request){
                it.remove();
                if(!delete_request.retVal())
                    return false;
            }
        }
        return true;
    }



//protected:
    /**
     * Vrací novou instanci prázného {@link List}u, který bude sloužit k přechovávání Listenerů
     * pro <code>getListeners</code>.
     * Volá se jen jednou, v konstruktoru.
     * Tuto metodu přepište, chcete-li listenery přechovávat v jiné implementaci {@link List}u,
     * než je defaultní {@link ArrayList}.
     *
     * @return nová instance prázdného {@link List}u pro <code>getListeners</code>
     * */
    protected List<EventListener<? super Args>> MAKE_LISTENER_LIST(){ return new ArrayList<>();}

    /**
     * Vrací novou instanci prázného {@link List}u, který bude sloužit k přechovávání Listenerů
     * pro <code>_getUtilListeners</code>.
     * Volá se jen jednou, v konstruktoru.
     * Tuto metodu přepište, chcete-li listenery přechovávat v jiné implementaci {@link List}u,
     * než je defaultní.
     * Defaultně odkazuje na <code>MAKE_LISTENER_LIST</code>
     *
     * @return nová instance prázdného {@link List}u pro <code>_getUtilListeners</code>
     * */
    protected List<EventListener<? super Args>> MAKE_UTIL_LISTENER_LIST(){ return MAKE_LISTENER_LIST();}

    /**
     * Vrací novou instanci prázného {@link List}u, který bude sloužit k přechovávání Listenerů
     * pro <code>_getPostUtilListeners</code>.
     * Volá se jen jednou, v konstruktoru.
     * Tuto metodu přepište, chcete-li listenery přechovávat v jiné implementaci {@link List}u,
     * než je defaultní.
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
            return IEventDelegate.this.returnValuePolicy = (returnValuePolicy==null?DEFAULT_RET_VAL_POLICY:returnValuePolicy);
        }
    }
}

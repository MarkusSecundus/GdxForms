package com.markussecundus.forms.events;

import com.markussecundus.forms.utils.datastruct.AutobucketedList;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.AbstractProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * Nová kanonická implementace {@link EventDelegate}.
 * <p></p>
 * 'I' jako 'Implementace'
 * (Ano, je to provokace vůči .NET. A stojím si za tím.)
* */
public class NIEventDelegate<Args> implements EventDelegate<Args> {
//public:


    public List<EventListener<? super Args>> getListeners(int priority){
        return listeners.getBucket(priority);
    }

    /**
     *
     * {@inheritDoc}
     * */
    @Override public List<EventListener<? super Args>> getListeners(){return getListeners(PRIORITIES.USER);}


    /**
     *
     * {@inheritDoc}
     * <p></p>
     * List je generován líně.
     * */
    @Override public List<EventListener<? super Args>> _getUtilListeners() { return getListeners(PRIORITIES.PRE_UTIL); }

    /**
     *
     * {@inheritDoc}
     * <p></p>
     * List je generován líně.
     * */
    @Override public List<EventListener<? super Args>> _getPostUtilListeners() { return getListeners(PRIORITIES.POST_UTIL); }



    /**
     *
     * {@inheritDoc}
     * <p></p>
     * {@link ReturnValuePolicy} je generována líně.
     * */
    @Override public Property<ReturnValuePolicy> returnValuePolicy(){
        if(returnValuePolicyProperty == null)
            returnValuePolicyProperty = new ReturnValuePolicyPropertyType();
        return returnValuePolicyProperty;
    }

    /**
     *
     * {@inheritDoc}
     * */
    @Override public ReturnValuePolicy getReturnValuePolicy() {
        return returnValuePolicyProperty==null ? returnValuePolicy : EventDelegate.super.getReturnValuePolicy();
    }

    /**
     *
     * {@inheritDoc}
     * */
    @Override public ReturnValuePolicy setReturnValuePolicy(ReturnValuePolicy pol) {
        return returnValuePolicyProperty==null? returnValuePolicy = pol : EventDelegate.super.setReturnValuePolicy(pol);
    }

    /**
     *
     * {@inheritDoc}
     * */
    @Override public boolean exec(Args e) {
        return getReturnValuePolicy().convRetVal(iterateList(e));
    }

    /**
     *
     * Pomocná funkce pro iteraci přes listenery.
     * */
    private boolean iterateList(Args e){
        List<? extends EventListener<? super Args>> list = listeners.getBase();

        for(int t=0;t<list.size();){
            try{
                if(!list.get(t).exec(e))
                    return false;
                ++t;
            }catch (DeleteSelf delete_request){

                this.listeners.remove(t);
                if(!delete_request.retVal())
                    return false;
            }
        }

        return true;
    }


    public final static class PRIORITIES{
        public static final int USER = 0;
        public static final int PRE_UTIL = 100;
        public static final int POST_UTIL = -100;
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
    protected AutobucketedList<EventListener<? super Args>, Integer> MAKE_LISTENER_LIST(){ return AutobucketedList.make();}

//private:

    private final AutobucketedList<EventListener<? super Args>, Integer> listeners = MAKE_LISTENER_LIST();

    private ReturnValuePolicy returnValuePolicy = DEFAULT_RET_VAL_POLICY;

    private Property<ReturnValuePolicy> returnValuePolicyProperty = null;

    private class ReturnValuePolicyPropertyType extends AbstractProperty<ReturnValuePolicy>{
        @Override protected ReturnValuePolicy obtain() {
            return returnValuePolicy;
        }
        @Override protected ReturnValuePolicy change(ReturnValuePolicy val) {
            return NIEventDelegate.this.returnValuePolicy = (returnValuePolicy==null?DEFAULT_RET_VAL_POLICY:returnValuePolicy);
        }
    }

}

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
public class IEventDelegate<Args> implements EventDelegate<Args> {
//public:


    /**
     *
     * {@inheritDoc}
     * */
    @Override public boolean exec(Args e) {
        return getReturnValuePolicy().convRetVal(iterateList(e));
    }


    public List<EventListener<? super Args>> getListeners(Integer priority){
        return listeners.getBucket(priority);
    }




    @Override
    public boolean removeListener(EventListener<?> list) {
        return listeners.remove(list);
    }

    @Override
    public void clear() {
        listeners.clear();
    }




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
            return returnValuePolicy = (val==null ? DEFAULT_RET_VAL_POLICY : val);
        }
    }


    private boolean iterateList(Args e){
        List<? extends EventListener<? super Args>> list = listeners.getBase();

        while(true) {
            for (int t = 0; t < list.size() - 1; ) {
                EventListener<? super Args> aktualniListener = list.get(t);

                if(!invokeListener(e, aktualniListener))
                    return false;

                if (list.get(t) == aktualniListener)
                    ++t;
                else {
                    int i = list.indexOf(aktualniListener);
                    if (i >= 0)
                        t = i + 1;
                }
            }

            if(list.size()<=0)
                return true;

            EventListener<? super Args> posledniListener = list.get(list.size()-1);
            if(posledniListener != this)
                return invokeListener(e, posledniListener);
        }
    }

    private boolean invokeListener(Args e, EventListener<? super Args> list){
        try {
            return list.exec(e);
        } catch (EventDelegate.ApplyOnParentDelegate apply_request) {
            return apply_request.exec(this, list);
        }
    }

}

package com.markussecundus.forms.events;

import com.markussecundus.forms.utils.datastruct.AutobucketedList;
import com.markussecundus.forms.utils.datastruct.ReadonlyList;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.AbstractProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * Canonical implementation of {@link EventDelegate}.
 * <p></p>
 * 'I' as 'Implementation'
 * (Yes, it IS a provocation against .NET, and I am utterly and completely convinced it is well justified)
 * <p></p>
 * Provides tail recursion optimisation :-D.
 *
 * @see EventDelegate
 *
 * @author MarkusSecundus
* */
public class IEventDelegate<Args> implements EventDelegate<Args> {
//public:


    @Override public boolean exec(Args e) {
        return getReturnValuePolicy().convRetVal(iterateList(e));
    }

    @Override
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
     * {@link EventDelegate.ReturnValuePolicy} is generated lazily.
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
     * Returns a new instance of an empty {@link AutobucketedList}, which will serve as carrier for the listeners.
     * <p></p>
     * Gets called only once, in the constructor.
     * <p>
     * Override this method, if you want to use other implementation of {@link AutobucketedList}
     * than the default {@link com.markussecundus.forms.utils.datastruct.IAutobucketedList}
     *
     * @return new instance of an empty {@link AutobucketedList}
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
        ReadonlyList<EventListener<? super Args>> list = listeners.getBase();

        while(true) {
            for (int t = 0; t < list.size() - 1; ) {
                EventListener<? super Args> aktualniListener = list.getNth(t);

                if(!invokeListener(e, aktualniListener))
                    return false;

                if (list.getNth(t) == aktualniListener)
                    ++t;
                else {
                    int i = list.indexOf(aktualniListener);
                    if (i >= 0)
                        t = i + 1;
                }
            }

            if(list.size()<=0)
                return true;

            EventListener<? super Args> posledniListener = list.getNth(list.size()-1);
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

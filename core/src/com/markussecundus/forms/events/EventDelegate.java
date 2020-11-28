package com.markussecundus.forms.events;

import com.markussecundus.forms.utils.function.BiPredicate;
import com.markussecundus.forms.wrappers.property.Property;

import java.util.List;


/**
 * An {@link EventListener} that encapsulates a list of listeners.
 *
 * @see IEventDelegate
 * @see EventListener
 * @see EventListenerX
 *
 * @author MarkusSecundus
 * */
public interface EventDelegate<Args> extends EventListener<Args> {


    /**
     * Returns a new instance of the canonical implementation of this interface.
     *
     * @return New instance of cannonical implementation of {@link EventDelegate}
     * */
    public static <T> EventDelegate<T> make(){return new IEventDelegate<>();}



    /**
     * {@link List} containing all listeners with the desired priority.
     * Its contents can be added or modified in any possible way.
     * <p>
     * When the delegate gets executed, all the listeners present inside get executed
     * in order determined firstly by their priority - delegates with the same priority are ordered by their position in this list.
     * (the higher the priority and the lower the index, the earlier the listener gets executed)
     *
     * @return List of listeners with the desired priority
     * */
    public List<EventListener<? super Args>> getListeners(Integer priority);


    /**
     * {@link List} containing all userland listeners.
     * Its contents can be added or modified in any possible way.
     * <p>
     * When the delegate gets executed, all the listeners present inside get executed
     * in order determined firstly by their priority - delegates with the same priority are ordered by their position in this list.
     * (the higher the priority and the lower the index, the earlier the listener gets executed)
     * <p>
     * Equivalent to <code>getListeners(ListenerPriorities.USER)</code>
     *
     * @return List of userland listeners
     * */
    public default List<EventListener<? super Args>> getUserListeners(){ return getListeners(ListenerPriorities.USER); }

    /**
     * {@link List} containing all basic utility listeners that get executed before the userland ones.
     * Its contents can be added or modified in any possible way.
     * Serves rather as a convenience shortcut for use by the writer of the library and is not intended for use by an ordinary user, unless he knows what he is doing.
     * <p>
     * When the delegate gets executed, all the listeners present inside get executed
     * in order determined firstly by their priority - delegates with the same priority are ordered by their position in this list.
     * (the higher the priority and the lower the index, the earlier the listener gets executed)
     * <p>
     * Equivalent to <code>getListeners(ListenerPriorities.PRE_UTIL)</code>
     *
     * @return List of utility listeners
     * */
    public default List<EventListener<? super Args>> _getUtilListeners(){
        return getListeners(ListenerPriorities.PRE_UTIL);
    }


    /**
     * {@link List} containing all basic utility listeners that get executed after the userland ones.
     * Its contents can be added or modified in any possible way.
     * Serves rather as a convenience shortcut for use by the writer of the library and is not intended for use by an ordinary user, unless he knows what he is doing.
     * <p>
     * When the delegate gets executed, all the listeners present inside get executed
     * in order determined firstly by their priority - delegates with the same priority are ordered by their position in this list.
     * (the higher the priority and the lower the index, the earlier the listener gets executed)
     * <p>
     * Equivalent to <code>getListeners(ListenerPriorities.POST_UTIL)</code>
     *
     * @return List of utility listeners
     * */
    public default List<EventListener<? super Args>> _getPostUtilListeners(){
        return getListeners(ListenerPriorities.POST_UTIL);
    }

    /**
     * Removes the desired listener if it resides anywhere in the delegate, no matter what its priority is.
     *
     * @return <code>true</code> if the specified listener was removed succesfully
     * */
    public boolean removeListener(EventListener<?> list);

    /**
     * Clears the whole delegate - removes completely all of its listeners, including the utility ones,
     * which you probably don't want to remove.
     * <p>
     * For removal of only userland listeners, use <code>getUserListeners().clear()</code> instead.
     * */
    public void clear();



    /**
     * Reference to {@link ListenerPriorities} visible directly inside {@link EventDelegate} for more clarity.
     *
     * @see ListenerPriorities
     * */
    public final class Priorities extends ListenerPriorities{}



    /**
     * Determines what return value the delegate will have depending on the return value of its elements.
     *
     * @see ReturnValuePolicy
     *
     * @return Property determining what return value the delegate will have depending on the return value of its elements.
     * */
    public Property<ReturnValuePolicy> returnValuePolicy();

    /**
     * Convenience shortcut for <code>returnValuePolicy().get()</code>.
     *
     * @return Convenience shortcut for <code>returnValuePolicy().get()</code>.
     * */
    public default ReturnValuePolicy getReturnValuePolicy(){return returnValuePolicy().get();}

    /**
     * Convenience shortcut for <code>returnValuePolicy().set(pol)</code>.
     *
     * @param pol new value for the <code>returnValuePolicy</code> property
     *
     * @return Convenience shortcut for <code>returnValuePolicy().set(pol)</code>.
     * */
    public default ReturnValuePolicy setReturnValuePolicy(ReturnValuePolicy pol){ return returnValuePolicy().set(pol); }


    /**
     *
     * Determines what return value the delegate will have depending on the return value of its elements.
     * <p></p>
     * If the execution of the delegate gets aborted by one of its listeners (by it returning <code>false</code>),
     * <code>false</code> gets passed to the instance,
     * <code>true</code> in the opposite scenarion.
     *
     * @author MarkusSecundus
     * */
    @FunctionalInterface
    public static interface ReturnValuePolicy{
        boolean convRetVal(boolean retVal);

        /**
         * The delegate always returns <code>true</code>, no matter if it finished successfully or not.
         * */
        public static final ReturnValuePolicy ALWAYS_TRUE = new ReturnValuePolicy() {
            @Override
            public boolean convRetVal(boolean retVal) {
                return true;
            }

            @Override
            public String toString() {
                return "ALWAYS_TRUE";
            }
        };

        /**
         * The delegate always returns <code>false</code>, no matter if it finished successfully or not.
         * */
        public static final ReturnValuePolicy ALWAYS_FALSE = new ReturnValuePolicy() {
            @Override
            public boolean convRetVal(boolean retVal) {
                return false;
            }

            @Override
            public String toString() {
                return "ALWAYS_FALSE";
            }
        };

        /**
         * The delegate returns <code>true</code> if it finished succesfully, <code>false</code>
         * if it did not (i.e. if one of its elements had returned <code>false</code>).
         * */
        public static final ReturnValuePolicy USE_CHILD = new ReturnValuePolicy() {
            @Override
            public boolean convRetVal(boolean retVal) {
                return retVal;
            }

            @Override
            public String toString() {
                return "USE_CHILD";
            }
        };

        /**
         * The delegate returns <code>false</code> if it finished succesfully, <code>true</code>
         * if it did not (i.e. if one of its elements had returned <code>false</code>).
         * */
        public static final ReturnValuePolicy INVERSE_CHILD= new ReturnValuePolicy() {
            @Override
            public boolean convRetVal(boolean retVal) {
                return !retVal;
            }

            @Override
            public String toString() {
                return "INVERSE_CHILD";
            }
        };
    }


    /**
     * Default value of {@link ReturnValuePolicy} property in every implementation of {@link EventDelegate}
     * if it isn't stated otherwise.
     * */
    public static final ReturnValuePolicy DEFAULT_RET_VAL_POLICY = ReturnValuePolicy.USE_CHILD;


    /**
     * Executes all the listeners encapsulated by this delegate, one by one in order determined first
     * by their priority (higher is first) and then by their position in the <code>getListeners()</code> list (lower index is first).
     * <p></p>
     * If one of the listeners returns <code>false</code> the execution of the delegate gets aborted.
     * The return value is determined by the <code>returnValuePolicy</code> property { -see {@link ReturnValuePolicy}}.
     *
     * @param  e arguments to be passed to the listeners
     *
     * @return sign whether there is sense in continuing with subsequent operations; depends on the <code>returnValuePolicy</code> property
     * */
    @Override public boolean exec(Args e);

    /**
     * Adds the desired {@link EventListenerX} instance to the end of the list of userspace listeners.
     * <p></p>
     * Serves only for better convenience when writing lambdas
     * (so that the user doesn't have to explicitly return <code>true</code> everywhere).
     *
     * @param list listener to be added into this delegate
     * */
    public default void add(EventListenerX<? super Args> list){
        getListeners(Priorities.USER).add(list);
    }


    /**
     * Listener that is currently being executed is to be erased, after which
     * the execution of the rest of listeners should continue as if the erased listener returned <code>true</code>.
     *
     * @see ApplyOnParentDelegate
     * */
    public static final ApplyOnParentDelegate DELETE_SELF = ApplyOnParentDelegate.make((d,l)->{d.removeListener(l);return true;});


    /**
     * Listener that is currently being executed is to be erased, after which
     * the execution of the delegate should be suspended as if the erased listener returned <code>false</code>.
     *
     * @see ApplyOnParentDelegate
     * */
    public static final ApplyOnParentDelegate DELETE_SELF_AND_ABORT = ApplyOnParentDelegate.make((d,l)->{d.removeListener(l);return false;});



    /**
     * If it is thrown from a listener, the delegate catches it and performs its method <code>exec</code> on itself.
     * */
    public static abstract class ApplyOnParentDelegate extends RuntimeException{
        private ApplyOnParentDelegate(){}

        /**
         * The action the delegate should perform on itself.
         *
         * @param  parent delegate, that had called the listener, which has just thrown this instance of {@link ApplyOnParentDelegate}
         * @param currentlyExecuted the listener, that has just thrown this instance of {@link ApplyOnParentDelegate}
         *
         * @return value that should be considered as return value of the listener that has thrown this exception
         * (<code>false</code> if this delegate should be suspended).
         * */
        public abstract boolean exec(EventDelegate<?> parent, EventListener<?> currentlyExecuted);

        /**
         * Makes an instance of {@link ApplyOnParentDelegate} using provided function as its implementation.
         *
         * @return an instance of {@link ApplyOnParentDelegate} with provided function in place of its implementation.
         * */
        public static ApplyOnParentDelegate make(BiPredicate<EventDelegate<?>, EventListener<?>> action){
            return new ApplyOnParentDelegate() {
                public boolean exec(EventDelegate<?> parent, EventListener<?> currentlyExecuted) {
                    return action.test(parent, currentlyExecuted);
                }
            };
        }
        /**
         * Makes an instance of {@link ApplyOnParentDelegate} using provided function as its implementation.
         *
         * @return an instance of {@link ApplyOnParentDelegate} with provided function in place of its implementation.
         * */
        public static ApplyOnParentDelegate make(EventListener<EventDelegate<?>> action){
            return new ApplyOnParentDelegate() {
                public boolean exec(EventDelegate<?> parent, EventListener<?> currentlyExecuted) {
                    return action.exec(parent);
                }
            };
        }
    }
}

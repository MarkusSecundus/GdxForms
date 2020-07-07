package com.markussecundus.forms.events;

public class ListenerPriorities {
    public static final class Raw {
        public static final int USER = 0;
        public static final int PRE_UTIL = 100;
        public static final int POST_UTIL = -100;
        public static final int ARG_GUARD = 127;
        public static final int TAIL_RECURSION = Integer.MIN_VALUE;
    }


    public static final Integer USER = Raw.USER;
    public static final Integer PRE_UTIL = Raw.PRE_UTIL;
    public static final Integer POST_UTIL = Raw.POST_UTIL;
    public static final Integer ARG_GUARD = Raw.ARG_GUARD;
    public static final Integer TAIL_RECURSION = Raw.TAIL_RECURSION;


}

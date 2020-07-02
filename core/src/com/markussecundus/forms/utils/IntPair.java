package com.markussecundus.forms.utils;

public class IntPair {

    private static final long MAX_INT = (1L<<32) - 1;

    public static int getFirst(long pair){
        return (int)pair;
    }

    public static int getSecond(long pair){
        return (int)(pair>>>32);
    }

    public static long withFirst(long pair, int first){
        return make(first, getSecond(pair));
    }

    public static long withSecond(long pair, int second){
        return make(getFirst(pair), second);
    }

    public static long make(int first, int second){
        return (((long)second) << 32) | (first & MAX_INT);
    }
}

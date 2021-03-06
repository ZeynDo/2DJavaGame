package com.zeynep.game.handlers;

public class MyInput {
    public static boolean[] previousKeys;
    public static boolean[] keys;

    public static final int NUM_KEYS = 2;
    public static final int BUTTON1 = 0;
    public static final int BUTTON2 = 1;


    static {
        keys = new boolean[NUM_KEYS];
        previousKeys = new boolean[NUM_KEYS];
    }
    public static void update() {
        for (int i = 0; i < NUM_KEYS;i++) {
            previousKeys[i]=keys[i];
        }
    }
    public static void setKey (int i, boolean b ) {
        keys[i] = b;
    }
    public static boolean isDown(int i) {
        return keys[i];
    }
    public static boolean isPressed(int i) {
        return keys[i] && !previousKeys[i];
    }

}

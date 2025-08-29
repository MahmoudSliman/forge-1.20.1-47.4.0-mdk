package com.ghost.test.wave;

import java.util.Random;

/**
 * RNG باستخدام java.util.Random
 */
public class JavaRandomRng implements IRng {
    private final Random random;

    public JavaRandomRng(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}

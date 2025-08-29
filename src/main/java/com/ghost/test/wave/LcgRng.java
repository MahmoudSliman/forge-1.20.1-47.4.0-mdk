package com.ghost.test.wave;

/**
 * RNG باستخدام Linear Congruential Generator (LCG).
 */
public class LcgRng implements IRng {
    private long seed;

    public LcgRng(long seed) {
        this.seed = seed;
    }

    @Override
    public double nextDouble() {
        return (nextInt(Integer.MAX_VALUE) / (double) Integer.MAX_VALUE);
    }

    @Override
    public int nextInt(int bound) {
        seed = (1664525 * seed + 1013904223) & 0xFFFFFFFFL;
        return (int) (seed % bound);
    }
}

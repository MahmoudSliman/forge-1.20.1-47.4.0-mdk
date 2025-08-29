package com.ghost.test.wave;

/**
 * واجهة مولّد أرقام عشوائية (RNG).
 */
public interface IRng {
    double nextDouble();
    int nextInt(int bound);
}

package com.ghost.test.wave;

import java.util.HashMap;
import java.util.Map;

/**
 * الحالة الحالية للموجة.
 */
public class WaveState {
    public int currentWave = 0;
    public int aliveCount = 0;
    public int killCounter = 0;

    public Map<MobType, Integer> perTypeAlive = new HashMap<>();
}

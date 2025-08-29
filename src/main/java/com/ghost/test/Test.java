package com.ghost.test;

import com.ghost.test.wave.WaveManager;
import com.ghost.test.wave.JavaRandomRng;
import com.ghost.test.wave.IRng;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod(Test.MOD_ID)  // <-- استخدم الثابت هنا
public class Test {

    public static final String MOD_ID = "test"; // <-- عرّف MOD_ID هنا

    private static WaveManager waveManager;

    public Test() {
        // نبدأ RNG (ممكن نغيرها بعدين من Config)
        IRng rng = new JavaRandomRng(System.currentTimeMillis());
        waveManager = new WaveManager(rng);

        // نسمع للأحداث
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        WaveCommand.register(event.getDispatcher(), waveManager);
    }

    public static WaveManager getWaveManager() {
        return waveManager;
    }
}

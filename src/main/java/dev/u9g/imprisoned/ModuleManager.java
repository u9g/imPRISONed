package dev.u9g.imprisoned;

import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class ModuleManager {
    public List<Module> allModules;

    public ModuleManager(List<Module> allModules) {
        this.allModules = allModules;
        allModules.forEach(MinecraftForge.EVENT_BUS::register);
    }
}

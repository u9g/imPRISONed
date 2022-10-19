package dev.u9g.imprisoned;

import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModuleManager implements Iterable<Module> {
    private final List<Module> allModules;

    public ModuleManager(List<Module> allModules) {
        this.allModules = allModules;
        allModules.forEach(MinecraftForge.EVENT_BUS::register);
    }

    @NotNull
    @Override
    public Iterator<Module> iterator() {
        return allModules.iterator();
    }

    @Override
    public void forEach(Consumer<? super Module> action) {
        allModules.forEach(action);
    }
}

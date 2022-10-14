package dev.u9g.imprisoned.mixin.accessor;

import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatStyle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatComponentStyle.class)
public interface ChatComponentStyleAccessor {
    @Accessor("style")
    ChatStyle style();
}

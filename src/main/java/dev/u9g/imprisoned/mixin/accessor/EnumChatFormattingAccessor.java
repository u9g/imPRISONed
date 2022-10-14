package dev.u9g.imprisoned.mixin.accessor;

import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnumChatFormatting.class)
public interface EnumChatFormattingAccessor {
    @Accessor("formattingCode")
    char formattingCode();
}

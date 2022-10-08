package com.example.mixin.accessor;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GuiNewChat.class)
public interface GuiNewChatAccessor {
    @Accessor("chatLines")
    List<ChatLine> chatLines();
}

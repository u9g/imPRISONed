package com.example.mods.nbt_dumper;

import com.example.PrisonsModConfig;
import com.example.mods.block_nbt_manager.TileEntityNBTManager;
import jline.internal.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Iterator;
import java.util.Map;

public class NBTDumper {
    /**
     * <p>Converts an NBT tag into a pretty-printed string.</p>
     * <p>For constant definitions, see {@link Constants.NBT}</p>
     *
     * @param nbt the NBT tag to pretty print
     * @return pretty-printed string of the NBT data
     */
    public static String prettyPrintNBT(NBTBase nbt) {
        final String INDENT = "    ";

        int tagID = nbt.getId();
        StringBuilder stringBuilder = new StringBuilder();

        // Determine which type of tag it is.
        if (tagID == Constants.NBT.TAG_END) {
            stringBuilder.append('}');

        } else if (tagID == Constants.NBT.TAG_BYTE_ARRAY || tagID == Constants.NBT.TAG_INT_ARRAY) {
            stringBuilder.append('[');
            if (tagID == Constants.NBT.TAG_BYTE_ARRAY) {
                NBTTagByteArray nbtByteArray = (NBTTagByteArray) nbt;
                byte[] bytes = nbtByteArray.getByteArray();

                for (int i = 0; i < bytes.length; i++) {
                    stringBuilder.append(bytes[i]);

                    // Don't add a comma after the last element.
                    if (i < (bytes.length - 1)) {
                        stringBuilder.append(", ");
                    }
                }
            } else {
                NBTTagIntArray nbtIntArray = (NBTTagIntArray) nbt;
                int[] ints = nbtIntArray.getIntArray();

                for (int i = 0; i < ints.length; i++) {
                    stringBuilder.append(ints[i]);

                    // Don't add a comma after the last element.
                    if (i < (ints.length - 1)) {
                        stringBuilder.append(", ");
                    }
                }
            }
            stringBuilder.append(']');

        } else if (tagID == Constants.NBT.TAG_LIST) {
            NBTTagList nbtTagList = (NBTTagList) nbt;

            stringBuilder.append('[');
            for (int i = 0; i < nbtTagList.tagCount(); i++) {
                NBTBase currentListElement = nbtTagList.get(i);

                stringBuilder.append(prettyPrintNBT(currentListElement));

                // Don't add a comma after the last element.
                if (i < (nbtTagList.tagCount() - 1)) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append(']');

        } else if (tagID == Constants.NBT.TAG_COMPOUND) {
            NBTTagCompound nbtTagCompound = (NBTTagCompound) nbt;

            stringBuilder.append('{');
            if (!nbtTagCompound.hasNoTags()) {
                Iterator<String> iterator = nbtTagCompound.getKeySet().iterator();

                stringBuilder.append(System.lineSeparator());

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    NBTBase currentCompoundTagElement = nbtTagCompound.getTag(key);

                    stringBuilder.append(key).append(": ").append(
                            prettyPrintNBT(currentCompoundTagElement));

                    // Don't add a comma after the last element.
                    if (iterator.hasNext()) {
                        stringBuilder.append(",").append(System.lineSeparator());
                    }
                }

                // Indent all lines
                String indentedString = stringBuilder.toString().replaceAll(System.lineSeparator(), System.lineSeparator() + INDENT);
                stringBuilder = new StringBuilder(indentedString);
            }

            stringBuilder.append(System.lineSeparator()).append('}');
        }
        // This includes the tags: byte, short, int, long, float, double, and string
        else {
            stringBuilder.append(nbt);
        }

        return stringBuilder.toString();
    }

    private boolean on = false;

    @SubscribeEvent
    public void onTypeWhileNotInGui(TickEvent event) {
        if (!PrisonsModConfig.INSTANCE.debug.dumpNBT) return;

        if (!on && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_H)) {
            @Nullable GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen instanceof GuiContainer) {
                Slot slot = ((GuiContainer)screen).getSlotUnderMouse();
                if (slot.getHasStack() && slot.getStack().hasTagCompound()) {
                    writeToClipboard(prettyPrintNBT(slot.getStack().getTagCompound()));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Copied item's NBT to clipboard!"));
                }
            } else if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().currentScreen == null) {
                MovingObjectPosition m = Minecraft.getMinecraft().objectMouseOver;
                if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(m.getBlockPos());
                    NBTTagCompound compound;
                    if (PrisonsModConfig.INSTANCE.misc.newSigns && te instanceof TileEntitySign) {
                        compound = te.serializeNBT();
                    } else {
                        compound = TileEntityNBTManager.data.get(te);
                    }
                    writeToClipboard(prettyPrintNBT(compound));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Copied block's NBT to clipboard!"));
                }
            }
            on = true;
        } else if (on && !(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_H))) {
            on = false;
        }
    }

    public static void writeToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection output = new StringSelection(text);

        try {
            clipboard.setContents(output, output);
        } catch (IllegalStateException ignored) {}
    }
}

package com.example.mods.block_nbt_manager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.Map;
import java.util.WeakHashMap;

public class TileEntityNBTManager {
    public static Map<TileEntity, NBTTagCompound> data = new WeakHashMap<>();
}

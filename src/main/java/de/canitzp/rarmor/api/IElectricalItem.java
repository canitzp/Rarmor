package de.canitzp.rarmor.api;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author canitzp
 */
public interface IElectricalItem {

    String getComponentName();

    IElectricalComponent getComponent(int guiLeft, int guiTop, int mouseX, int mouseY);

    NBTTagCompound writeNBT(NBTTagCompound compound);

    NBTTagCompound readNBT(NBTTagCompound compound);

}

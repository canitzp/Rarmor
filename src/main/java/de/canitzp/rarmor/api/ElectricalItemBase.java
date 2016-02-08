package de.canitzp.rarmor.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author canitzp
 */
public class ElectricalItemBase extends Item implements IElectricalItem{

    private String componentName;

    public ElectricalItemBase(String modid, String name, String componentName, CreativeTabs tab){
        this.componentName = componentName;
        this.setUnlocalizedName(modid + "." + name);
        this.setRegistryName(modid + "." + name);
        this.setCreativeTab(tab);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public String getComponentName() {
        return this.componentName;
    }

    @Override
    public IElectricalComponent getComponent(int guiLeft, int guiTop, int mouseX, int mouseY) {
        return new ElectricalComponent(mouseX - guiLeft, mouseY - guiTop, 8, 5, 0, 0, this.getComponentName());
    }

    @Override
    public NBTTagCompound writeNBT(NBTTagCompound compound) {
        return compound;
    }

    @Override
    public NBTTagCompound readNBT(NBTTagCompound compound) {
        return compound;
    }
}

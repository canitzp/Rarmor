/*
 * This file 'ItemModuleModuleSplitter.java' is part of Rarmor by canitzp.
 * It isn't allowed to use more than 15% of the code
 * or redistribute the compiled jar file.
 * The source code can be found here: https://github.com/canitzp/Rarmor
 * © canitzp, 2016
 */

package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
public class ItemModuleModuleSplitter extends ItemModule implements IRarmorModule{

    public ItemModuleModuleSplitter(){
        super("moduleModuleSplitter");
    }

    @Override
    public String getUniqueName(){
        return "ModuleSplitter";
    }

    @Override
    public String getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return TextFormatting.GRAY + "The ModuleSplitter lets you activate three Modules " + TextFormatting.GRAY + "instead of one. All Modules are categorised in '" +
                TextFormatting.GREEN + "Active" + TextFormatting.GRAY + "' or '" + TextFormatting.RED + "Passive" + TextFormatting.GRAY + "'." +
                TextFormatting.GRAY + "The Active Modules have special abilities and they have to stay" + TextFormatting.GRAY + "in the top slot." +
                "The passive ones can be stored in all three slots." + TextFormatting.GRAY + "Two modules of the same type don't work.";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.NONE;
    }

    private void toggleSlots(boolean value){
        RarmorUtil.toggleSlotInGui(-16, 14, value);
        RarmorUtil.toggleSlotInGui(-16, 34, value);
        RarmorUtil.toggleSlotInGui(-16, 54, value);
    }

    @Override
    public void onContainerTick(Container container, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        if(player.worldObj.isRemote) toggleSlots(false);
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onContainerTick(container, player, armorChestplate, mod1, inventory);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onContainerTick(container, player, armorChestplate, mod2, inventory);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onContainerTick(container, player, armorChestplate, mod3, inventory);
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        InventoryBase base = RarmorUtil.readRarmor(player);
        ItemStack mod1 = base.getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onPickupFromSlot(world, player, armorChestplate, mod1, inventory);
        }
        ItemStack mod2 = base.getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onPickupFromSlot(world, player, armorChestplate, mod2, inventory);
        }
        ItemStack mod3 = base.getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onPickupFromSlot(world, player, armorChestplate, mod3, inventory);
        }
        RarmorUtil.dropSlot(mod1, player);
        RarmorUtil.dropSlot(mod2, player);
        RarmorUtil.dropSlot(mod3, player);
        //slot.inv.setInventorySlotContents(31, null);
        //slot.inv.setInventorySlotContents(32, null);
        //slot.inv.setInventorySlotContents(33, null);
        if(world.isRemote) this.toggleSlots(true);
        RarmorUtil.saveRarmor(player, base);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainer gui, ItemStack armor, ItemStack module, boolean settingActivated, boolean isColoringTab, float partialTicks, int mouseX, int mouseY){
        toggleSlots(false);
        minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
        gui.drawTexturedModalRect(gui.guiLeft - 22, gui.guiTop + 7, 0, 129, 25, 71);
        ItemStack mod1 = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).drawGuiContainerBackgroundLayer(minecraft, gui, armor, mod1, settingActivated, isColoringTab, partialTicks, mouseX, mouseY);
        }
    }

    @Override
    public void drawScreen(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, boolean isColoringTab, float partialTicks, int mouseX, int mouseY){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).drawScreen(minecraft, gui, armorChestplate, mod1, settingActivated, isColoringTab, partialTicks, mouseX, mouseY);
        }
    }

    @Override
    public void initGui(Minecraft minecraft, ItemStack armorChestplate, GuiContainer gui){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).initGui(minecraft, armorChestplate, gui);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).initGui(minecraft, armorChestplate, gui);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).initGui(minecraft, armorChestplate, gui);
        }
    }

    @Override
    public void onGuiOpenEvent(World worldObj, EntityPlayer player, ItemStack armorChestplate, ItemStack module, GuiContainer gui){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onGuiOpenEvent(worldObj, player, armorChestplate, mod1, gui);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onGuiOpenEvent(worldObj, player, armorChestplate, mod2, gui);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onGuiOpenEvent(worldObj, player, armorChestplate, mod3, gui);
        }
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onModuleTickInArmor(world, player, armorChestplate, mod1, inventory);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onModuleTickInArmor(world, player, armorChestplate, mod2, inventory);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onModuleTickInArmor(world, player, armorChestplate, mod3, inventory);
        }
    }

    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainer gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, boolean isColoringTab, int type, int mouseX, int mouseY){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onMouseClicked(minecraft, gui, armorChestplate, mod1, settingActivated, isColoringTab, type, mouseX, mouseY);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onMouseClicked(minecraft, gui, armorChestplate, mod2, settingActivated, isColoringTab, type, mouseX, mouseY);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onMouseClicked(minecraft, gui, armorChestplate, mod3, settingActivated, isColoringTab, type, mouseX, mouseY);
        }
    }

    @Override
    public void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onPlayerLoginEvent(world, player, armorChestplate, mod1);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onPlayerLoginEvent(world, player, armorChestplate, mod2);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onPlayerLoginEvent(world, player, armorChestplate, mod3);
        }
    }

    @Override
    public boolean onPlayerTakeDamage(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, DamageSource damageSource, float damage){
        boolean[] b = new boolean[3];
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            b[0] = ((IRarmorModule) mod1.getItem()).onPlayerTakeDamage(world, player, armorChestplate, module, damageSource, damage);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            b[1] = ((IRarmorModule) mod2.getItem()).onPlayerTakeDamage(world, player, armorChestplate, module, damageSource, damage);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            b[2] = ((IRarmorModule) mod3.getItem()).onPlayerTakeDamage(world, player, armorChestplate, module, damageSource, damage);
        }
        return b[0] || b[1] || b[2];
    }

    @Override
    public void renderWorldScreen(Minecraft minecraft, EntityPlayer player, ItemStack playersRarmorChestplate, ItemStack module, ScaledResolution resolution, FontRenderer fontRendererObj, RenderGameOverlayEvent.ElementType type, float partialTicks){
        ItemStack mod1 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).renderWorldScreen(minecraft, player, playersRarmorChestplate, mod1, resolution, fontRendererObj, type, partialTicks);
        }
        ItemStack mod2 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).renderWorldScreen(minecraft, player, playersRarmorChestplate, mod2, resolution, fontRendererObj, type, partialTicks);
        }
        ItemStack mod3 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).renderWorldScreen(minecraft, player, playersRarmorChestplate, mod3, resolution, fontRendererObj, type, partialTicks);
        }
    }

    @Override
    public void initModule(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).initModule(world, player, armorChestplate, mod1, inventory);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).initModule(world, player, armorChestplate, mod2, inventory);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).initModule(world, player, armorChestplate, mod3, inventory);
        }
    }

}

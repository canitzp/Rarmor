package de.canitzp.rarmor.items.rfarmor.modules;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.container.ContainerBase;
import de.canitzp.rarmor.api.gui.GuiCheckBox;
import de.canitzp.rarmor.api.gui.GuiContainerBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.SlotUpdate;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.items.rfarmor.ItemModule;
import de.canitzp.rarmor.items.rfarmor.ItemRFArmorBody;
import de.canitzp.rarmor.util.JavaUtil;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

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
    public List<String> getGuiHelp(){
        return JavaUtil.newList(TextFormatting.GRAY + "The ModuleSplitter lets you activate three Modules",
                TextFormatting.GRAY + "instead of one. All Modules are categorised in '" + TextFormatting.GREEN + "Active" + TextFormatting.GRAY + "' or '" + TextFormatting.RED + "Passive" + TextFormatting.GRAY + "'.",
                TextFormatting.GRAY + "The Active Modules have special abilities and they have to stay",
                TextFormatting.GRAY + "in the top slot. The passive ones can be stored in all three slots.",
                TextFormatting.GRAY + "Two modules of the same type don't work.",
                "",
                TextFormatting.DARK_RED + "The ModuleSplitter is WIP and you use it at your OWN risk!",
                TextFormatting.DARK_RED + "Some strange bugs may happen!");
    }

    @Override
    public List<String> getDescription(EntityPlayer player, ItemStack stack, boolean advancedTooltips){
        return JavaUtil.newList(TextFormatting.DARK_RED + "The ModuleSplitter is WIP and you use it at your OWN risk!",
                TextFormatting.DARK_RED + "Some strange bugs may happen!");
    }

    private void toggleSlots(boolean value){
        RarmorUtil.toggleSlotInGui(-16, 14, value);
        RarmorUtil.toggleSlotInGui(-16, 34, value);
        RarmorUtil.toggleSlotInGui(-16, 54, value);
    }

    @Override
    public void onContainerTick(ContainerBase container, EntityPlayer player, InventoryBase inventory, ItemStack armorChestplate, ItemStack module){
        if (player.worldObj.isRemote) toggleSlots(false);
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onContainerTick(container, player, inventory, armorChestplate, module);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onContainerTick(container, player, inventory, armorChestplate, module);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onContainerTick(container, player, inventory, armorChestplate, module);
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, SlotUpdate slot){
        InventoryBase base = RarmorUtil.readRarmor(player);
        ItemStack mod1 = base.getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onPickupFromSlot(world, player, armorChestplate, mod1, inventory, slot);
        }
        ItemStack mod2 = base.getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onPickupFromSlot(world, player, armorChestplate, mod2, inventory, slot);
        }
        ItemStack mod3 = base.getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onPickupFromSlot(world, player, armorChestplate, mod3, inventory, slot);
        }
        RarmorUtil.dropSlot(mod1, player);
        RarmorUtil.dropSlot(mod2, player);
        RarmorUtil.dropSlot(mod3, player);
        slot.inv.setInventorySlotContents(31, null);
        slot.inv.setInventorySlotContents(32, null);
        slot.inv.setInventorySlotContents(33, null);
        if (world.isRemote) this.toggleSlots(true);
        RarmorUtil.saveRarmor(player, base);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainerBase gui, ItemStack armor, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop){
        toggleSlots(false);
        minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
        gui.drawTexturedModalRect(gui.getGuiLeft() - 22, gui.getGuiTop() + 7, 0, 129, 25, 71);
        ItemStack mod1 = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).drawGuiContainerBackgroundLayer(minecraft, gui, armor, module, settingActivated, partialTicks, mouseX, mouseY, guiLeft, guiTop);
        }
    }

    @Override
    public void drawScreen(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).drawScreen(minecraft, gui, armorChestplate, module, settingActivated, partialTicks, mouseX, mouseY);
        }
    }

    @Override
    public void initGui(World world, EntityPlayer player, ItemStack armorChestplate, GuiContainerBase gui, List<GuiCheckBox> checkBoxes, ResourceLocation checkBoxResource){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).initGui(world, player, armorChestplate, gui, checkBoxes, checkBoxResource);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).initGui(world, player, armorChestplate, gui, checkBoxes, checkBoxResource);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).initGui(world, player, armorChestplate, gui, checkBoxes, checkBoxResource);
        }
    }

    @Override
    public void onGuiOpenEvent(World worldObj, EntityPlayer player, GuiScreen gui, ItemStack armorChestplate, ItemStack module){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onGuiOpenEvent(worldObj, player, gui, armorChestplate, module);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onGuiOpenEvent(worldObj, player, gui, armorChestplate, module);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onGuiOpenEvent(worldObj, player, gui, armorChestplate, module);
        }
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onModuleTickInArmor(world, player, armorChestplate, module, inventory);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onModuleTickInArmor(world, player, armorChestplate, module, inventory);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onModuleTickInArmor(world, player, armorChestplate, module, inventory);
        }
    }

    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onMouseClicked(minecraft, gui, armorChestplate, module, settingActivated, type, mouseX, mouseY, guiLeft, guiTop);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onMouseClicked(minecraft, gui, armorChestplate, module, settingActivated, type, mouseX, mouseY, guiLeft, guiTop);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onMouseClicked(minecraft, gui, armorChestplate, module, settingActivated, type, mouseX, mouseY, guiLeft, guiTop);
        }
    }

    @Override
    public void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onPlayerLoginEvent(world, player, armorChestplate, mod1);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onPlayerLoginEvent(world, player, armorChestplate, mod2);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onPlayerLoginEvent(world, player, armorChestplate, mod3);
        }
    }

    @Override
    public boolean onPlayerTakeDamage(World world, EntityPlayer player, ItemStack armorChestplate, DamageSource damageSource, float damage){
        boolean[] b = new boolean[3];
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            b[0] = ((IRarmorModule) mod1.getItem()).onPlayerTakeDamage(world, player, armorChestplate, damageSource, damage);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            b[1] = ((IRarmorModule) mod2.getItem()).onPlayerTakeDamage(world, player, armorChestplate, damageSource, damage);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            b[2] = ((IRarmorModule) mod3.getItem()).onPlayerTakeDamage(world, player, armorChestplate, damageSource, damage);
        }
        return b[0] || b[1] || b[2];
    }

    @Override
    public void renderWorldScreen(Minecraft minecraft, EntityPlayer player, ScaledResolution resolution, FontRenderer fontRendererObj, RenderGameOverlayEvent.ElementType type, ItemStack playersRarmorChestplate, ItemStack module, float partialTicks){
        ItemStack mod1 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).renderWorldScreen(minecraft, player, resolution, fontRendererObj, type, playersRarmorChestplate, mod1, partialTicks);
        }
        ItemStack mod2 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).renderWorldScreen(minecraft, player, resolution, fontRendererObj, type, playersRarmorChestplate, mod2, partialTicks);
        }
        ItemStack mod3 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).renderWorldScreen(minecraft, player, resolution, fontRendererObj, type, playersRarmorChestplate, mod3, partialTicks);
        }
    }

    @Override
    public void initModule(World world, EntityPlayer player, InventoryBase inventory, ItemStack armorChestplate, ItemStack module){
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if (mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).initModule(world, player, inventory, armorChestplate, module);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if (mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).initModule(world, player, inventory, armorChestplate, module);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if (mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).initModule(world, player, inventory, armorChestplate, module);
        }
    }

}

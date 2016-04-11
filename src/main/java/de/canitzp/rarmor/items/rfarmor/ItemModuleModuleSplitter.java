package de.canitzp.rarmor.items.rfarmor;

import de.canitzp.rarmor.RarmorUtil;
import de.canitzp.rarmor.api.InventoryBase;
import de.canitzp.rarmor.api.container.ContainerBase;
import de.canitzp.rarmor.api.gui.GuiCheckBox;
import de.canitzp.rarmor.api.gui.GuiContainerBase;
import de.canitzp.rarmor.api.modules.IRarmorModule;
import de.canitzp.rarmor.api.slots.IModuleSlot;
import de.canitzp.rarmor.inventory.gui.GuiRFArmor;
import de.canitzp.rarmor.inventory.slots.SlotModuleSplitter;
import de.canitzp.rarmor.util.NBTUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author canitzp
 */
public class ItemModuleModuleSplitter extends ItemModule implements IRarmorModule {

    private SlotModuleSplitter slot1, slot2, slot3;

    public ItemModuleModuleSplitter() {
        super("moduleModuleSplitter");
    }

    @Override
    public String getUniqueName() {
        return "ModuleSplitter";
    }

    @Override
    public void onContainerTick(ContainerBase container, EntityPlayer player, InventoryBase inventory, ItemStack armorChestplate, ItemStack module) {
        boolean isFirst = NBTUtil.getBoolean(module, "opened");
        System.out.println("" + isFirst);
        if(!isFirst){
            NBTUtil.setBoolean(module, "opened", true);
            RarmorUtil.reopenRarmorGui(player);
            NBTUtil.setBoolean(module, "opened", true);
        }
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onContainerTick(container, player, inventory, armorChestplate, module);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onContainerTick(container, player, inventory, armorChestplate, module);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onContainerTick(container, player, inventory, armorChestplate, module);
        }
    }

    @Override
    public void onPickupFromSlot(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory, Slot slot) {
        NBTUtil.setBoolean(module, "opened", false);
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onPickupFromSlot(world, player, armorChestplate, module, inventory, slot);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onPickupFromSlot(world, player, armorChestplate, module, inventory, slot);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onPickupFromSlot(world, player, armorChestplate, module, inventory, slot);
        }
        InventoryBase base = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount);
        RarmorUtil.dropSlot(base, mod1, player, armorChestplate);
        RarmorUtil.dropSlot(base, mod2, player, armorChestplate);
        RarmorUtil.dropSlot(base, mod3, player, armorChestplate);
        NBTUtil.saveItemStack(module, armorChestplate, "CurrentHoldItemStack");
        base.setInventorySlotContents(ItemRFArmorBody.MODULESLOT, null);
        NBTUtil.saveSlots(armorChestplate, base);
        RarmorUtil.reopenRarmorGui(player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawGuiContainerBackgroundLayer(Minecraft minecraft, GuiContainerBase gui, ItemStack armor, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY, int guiLeft, int guiTop) {
        minecraft.getTextureManager().bindTexture(((GuiRFArmor) gui).modulesGui);
        gui.drawTexturedModalRect(gui.getGuiLeft() - 22, gui.getGuiTop() + 7, 0, 129, 25, 71);
        ItemStack mod1 = NBTUtil.readSlots(armor, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).drawGuiContainerBackgroundLayer(minecraft, gui, armor, module, settingActivated, partialTicks, mouseX, mouseY, guiLeft, guiTop);
        }
    }

    @Override
    public void onContainerIsInit(ContainerBase container, EntityPlayer player, InventoryBase inventory, ItemStack armorChestplate, ItemStack module) {
        container.addSlot(slot1 = new SlotModuleSplitter(inventory, 31, -16, 14, armorChestplate));
        container.addSlot(slot2 = new SlotModuleSplitter(inventory, 32, -16, 34, armorChestplate));
        container.addSlot(slot3 = new SlotModuleSplitter(inventory, 33, -16, 54, armorChestplate));
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onContainerIsInit(container, player, inventory, armorChestplate, module);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onContainerIsInit(container, player, inventory, armorChestplate, module);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onContainerIsInit(container, player, inventory, armorChestplate, module);
        }
    }

    @Override
    public void drawScreen(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, float partialTicks, int mouseX, int mouseY) {
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).drawScreen(minecraft, gui, armorChestplate, module, settingActivated, partialTicks, mouseX, mouseY);
        }
    }

    @Override
    public void initGui(World world, EntityPlayer player, ItemStack armorChestplate, GuiContainerBase gui, List<GuiCheckBox> checkBoxes, ResourceLocation checkBoxResource) {
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).initGui(world, player, armorChestplate, gui, checkBoxes, checkBoxResource);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).initGui(world, player, armorChestplate, gui, checkBoxes, checkBoxResource);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).initGui(world, player, armorChestplate, gui, checkBoxes, checkBoxResource);
        }
    }

    @Override
    public void onGuiOpenEvent(World worldObj, EntityPlayer player, GuiScreen gui, ItemStack armorChestplate, ItemStack module) {
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onGuiOpenEvent(worldObj, player, gui, armorChestplate, module);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onGuiOpenEvent(worldObj, player, gui, armorChestplate, module);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onGuiOpenEvent(worldObj, player, gui, armorChestplate, module);
        }
    }

    @Override
    public void onModuleTickInArmor(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module, InventoryBase inventory) {
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onModuleTickInArmor(world, player, armorChestplate, module, inventory);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onModuleTickInArmor(world, player, armorChestplate, module, inventory);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onModuleTickInArmor(world, player, armorChestplate, module, inventory);
        }
    }

    @Override
    public void onMouseClicked(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, int type, int mouseX, int mouseY, int guiLeft, int guiTop) {
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onMouseClicked(minecraft, gui, armorChestplate, module, settingActivated, type, mouseX, mouseY, guiLeft, guiTop);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onMouseClicked(minecraft, gui, armorChestplate, module, settingActivated, type, mouseX, mouseY, guiLeft, guiTop);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onMouseClicked(minecraft, gui, armorChestplate, module, settingActivated, type, mouseX, mouseY, guiLeft, guiTop);
        }
    }

    @Override
    public void onPlayerLoginEvent(World world, EntityPlayer player, ItemStack armorChestplate, ItemStack module) {
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).onPlayerLoginEvent(world, player, armorChestplate, module);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).onPlayerLoginEvent(world, player, armorChestplate, module);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).onPlayerLoginEvent(world, player, armorChestplate, module);
        }
    }

    @Override
    public boolean onPlayerTakeDamage(World world, EntityPlayer player, ItemStack armorChestplate, DamageSource damageSource, float damage) {
        boolean[] b = new boolean[3];
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            b[0] = ((IRarmorModule) mod1.getItem()).onPlayerTakeDamage(world, player, armorChestplate, damageSource, damage);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            b[1] = ((IRarmorModule) mod2.getItem()).onPlayerTakeDamage(world, player, armorChestplate, damageSource, damage);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            b[2] = ((IRarmorModule) mod3.getItem()).onPlayerTakeDamage(world, player, armorChestplate, damageSource, damage);
        }
        return b[0] || b[1] || b[2];
    }

    @Override
    public void renderWorldScreen(Minecraft minecraft, EntityPlayer player, ScaledResolution resolution, FontRenderer fontRendererObj, RenderGameOverlayEvent.ElementType type, ItemStack playersRarmorChestplate, ItemStack module, float partialTicks) {
        ItemStack mod1 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod1.getItem()).renderWorldScreen(minecraft, player, resolution, fontRendererObj, type, playersRarmorChestplate, module, partialTicks);
        }
        ItemStack mod2 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod2.getItem()).renderWorldScreen(minecraft, player, resolution, fontRendererObj, type, playersRarmorChestplate, module, partialTicks);
        }
        ItemStack mod3 = NBTUtil.readSlots(playersRarmorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule){
            ((IRarmorModule) mod3.getItem()).renderWorldScreen(minecraft, player, resolution, fontRendererObj, type, playersRarmorChestplate, module, partialTicks);
        }
    }

    @Override
    public boolean showSlot(Minecraft minecraft, GuiContainerBase gui, ItemStack armorChestplate, ItemStack module, boolean settingActivated, Slot slot, int mouseX, int mouseY, int slotX, int slotY, boolean isMouseOverSlot) {
        ItemStack mod1 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(31);
        if(mod1 != null && mod1.getItem() instanceof IRarmorModule && GuiContainerBase.isMouseOverSlot(slot1, mouseX, mouseY, gui.getGuiLeft(), gui.getGuiTop())){
            //return ((IRarmorModule) mod1.getItem()).showSlot(minecraft, gui, armorChestplate, module, settingActivated, slot, mouseX, mouseY, slotX, slotY, isMouseOverSlot);
        }
        ItemStack mod2 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(32);
        if(mod2 != null && mod2.getItem() instanceof IRarmorModule && GuiContainerBase.isMouseOverSlot(slot2, mouseX, mouseY, gui.getGuiLeft(), gui.getGuiTop())){
           // return ((IRarmorModule) mod2.getItem()).showSlot(minecraft, gui, armorChestplate, module, settingActivated, slot, mouseX, mouseY, slotX, slotY, isMouseOverSlot);
        }
        ItemStack mod3 = NBTUtil.readSlots(armorChestplate, ItemRFArmorBody.slotAmount).getStackInSlot(33);
        if(mod3 != null && mod3.getItem() instanceof IRarmorModule && GuiContainerBase.isMouseOverSlot(slot3, mouseX, mouseY, gui.getGuiLeft(), gui.getGuiTop())){
            //return ((IRarmorModule) mod3.getItem()).showSlot(minecraft, gui, armorChestplate, module, settingActivated, slot, mouseX, mouseY, slotX, slotY, isMouseOverSlot);
        }
        return isMouseOverSlot && !(slot instanceof IModuleSlot);
    }
}
